package com.vankorno.vankornodb.dbManagement.migration
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.dbDrop
import com.vankorno.vankornodb.dbManagement.createTables
import com.vankorno.vankornodb.dbManagement.migration.MigrationUtils.defaultValueForParam
import com.vankorno.vankornodb.dbManagement.migration.oldToNewEntt
import com.vankorno.vankornodb.dbManagement.tableOf
import com.vankorno.vankornodb.getCursor
import com.vankorno.vankornodb.getSet.insertInto
import com.vankorno.vankornodb.getSet.mapToEntity
import kotlin.collections.get
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


/**
 * Performs a simple migration from [OLD] to [NEW] entity structures for the given [tableNames].
 * Automatically handles renaming and structural changes (column order).
 *
 * This function:
 * 1. Reads existing rows from each table using [readAsMigrated], applying rename rules and new structure.
 * 2. Drops and recreates each table using the schema of [NEW].
 * 3. Reinserts the migrated data into the new table.
 *
 * Suitable for straightforward cases where changing the migrated table is enough and no data in other
 * tables needs to be updated (like setting ID references to new IDs or similar).
 * For more complex migration scenarios, users can write their own functions
 * using the existing [readAsMigrated], [createTables], and [insertInto] directly.
 *
 * @param oldVersion The old database version used to resolve renaming rules.
 * @param renameMap A mapping from new field names to old names by version.
 * @param tableNames The names of tables to migrate.
 */

inline fun <reified OLD : Any, reified NEW : Any> SQLiteDatabase.migrateLite(
                                                            oldVersion: Int,
                                                             renameMap: Map<String, Map<Int, String>>,
                                                            tableNames: List<String>
) {
    tableNames.forEach { tableName ->
        // region LOG
            println("Migrating table: $tableName from version $oldVersion")
        // endregion
        val newList = readAsMigrated<OLD, NEW>(tableName, oldVersion, renameMap)
        // region LOG
            println("Dropping old table: $tableName")
        // endregion
        this.execSQL(dbDrop + tableName)
        // region LOG
            println("Creating new table schema for: $tableName")
        // endregion
        createTables(tableOf<NEW>(tableName))
        // region LOG
            println("Reinserting migrated data into: $tableName, total rows: ${newList.size}")
        // endregion
        newList.forEach { insertInto(tableName, it) }
        // region LOG
            println("Migration completed for table: $tableName")
        // endregion
    }
}

/**
 * Overload of [migrateLite] for a single table.
 */
inline fun <reified OLD : Any, reified NEW : Any> SQLiteDatabase.migrateLite(
                                                            oldVersion: Int,
                                                             renameMap: Map<String, Map<Int, String>>,
                                                             tableName: String
) = migrateLite<OLD, NEW>(oldVersion, renameMap, listOf(tableName))




/**
 * Reads all rows from the specified [tableName], maps each row to an instance of [OLD]
 * using [mapToEntity], then converts each [OLD] instance to [NEW] by copying properties,
 * applying renaming rules with version awareness via [renameMap].
 *
 * This is designed for database migrations, where:
 * - [OLD] is the legacy entity matching the old schema.
 * - [NEW] is the updated entity matching the new schema.
 *
 * @param tableName The name of the database table to query.
 * @param oldVersion The version number of the old schema used to resolve property renames.
 * @param renameMap A map of new property names to maps of old versions and their corresponding old names.
 * @return A list of migrated [NEW] instances created from the old data.
 */
inline fun <reified OLD : Any, reified NEW : Any> SQLiteDatabase.readAsMigrated(
                                                tableName: String,
                                               oldVersion: Int,
                                                renameMap: Map<String, Map<Int, String>> = emptyMap()
): List<NEW> {
    val oldItems = mutableListOf<NEW>()
    
    this.getCursor(tableName).use { cursor ->
        if (cursor.moveToFirst()) {
            do {
                val old = cursor.mapToEntity(OLD::class)
                val new = old.oldToNewEntt<OLD, NEW>(oldVersion, renameMap)
                oldItems += new
            } while (cursor.moveToNext())
        }
    }
    return oldItems
}





/**
 * Creates a new instance of [TO] by mapping properties from the current [FROM] instance,
 * applying a nested renaming map with version awareness.
 *
 * For each constructor parameter of [TO], this function:
 * - Finds the corresponding property name in [FROM] by checking [renameMap] based on [oldVersion].
 * - Uses the matching property value from [FROM] if types align.
 * - Otherwise, provides a default value for common types (Int, String, Boolean, etc.).
 *
 * @param oldVersion The old schema version to select the correct property rename.
 * @param renameMap Maps new property names to old property names by version.
 *                  Format: renameMap[newProperty][oldVersion] = oldProperty
 * @param overrides Optional lambda to customize or modify the created [TO] instance.
 * @return A new [TO] instance populated with mapped or default values.
 */
inline fun <reified FROM : Any, reified TO : Any> FROM.oldToNewEntt(
                                               oldVersion: Int,
                                                renameMap: Map<String, Map<Int, String>> = emptyMap(),
                                                overrides: TO.(FROM) -> TO = { this }
): TO {
    val fromProps = FROM::class.memberProperties.associateBy { it.name }
    val constructor = TO::class.primaryConstructor ?: error("TO class must have a primary constructor")
    
    val args = constructor.parameters.associateWith { param ->
        val toName = param.name ?: error("Constructor parameter must have a name")
    
        // Determine which FROM property to use via nested rename map or direct match
        val fromName = MigrationUtils.findOldName(toName, oldVersion, renameMap) ?: toName
        val fromProp = fromProps[fromName]
        
        if (fromProp != null && param.type.classifier == fromProp.returnType.classifier) {
            fromProp.get(this)
        } else {
            defaultValueForParam(param)
        }
    }.filterValues { it != null }

    val instance = constructor.callBy(args)
    return overrides(instance, this)
}

/**
 * Utility functions to assist with migration-related property name resolution and default value provisioning.
 */
object MigrationUtils {
    /**
     * Finds the old property name corresponding to a given new property [newName],
     * based on the [version] and [renameMap].
     *
     * The [renameMap] is structured as:
     * newPropertyName -> (oldVersion -> oldPropertyName)
     *
     * This function returns the old property name for the greatest version <= [version].
     * Returns null if no mapping is found.
     */
    fun findOldName(                                           newName: String,
                                                               version: Int,
                                                             renameMap: Map<String, Map<Int, String>>
    ): String? {
        val versionsMap = renameMap[newName] ?: return null
        // Find greatest version <= oldVersion
        val candidateVersion = versionsMap.keys.filter { it <= version }.maxOrNull()
        return if (candidateVersion != null) versionsMap[candidateVersion] else null
    }
    
    /**
     * Provides default values for common Kotlin types for use when no matching property
     * value can be found during migration.
     * Returns null for unsupported or unknown types.
     */
    fun defaultValueForParam(                                      param: kotlin.reflect.KParameter
    ): Any? = when (param.type.classifier) {
        Int::class -> 0
        Long::class -> 0L
        Boolean::class -> false
        String::class -> ""
        Double::class -> 0.0
        Float::class -> 0f
        else -> null
    }
}



/**
 * Maps the current instance of [FROM] to a new instance of [TO] by copying all properties
 * that have identical names and types in both classes.
 *
 * Optionally, allows customizing or overriding the result via the [overrides] lambda.
 *
 * @param overrides A lambda to apply additional transformation after initial mapping.
 * @return A new [TO] instance with matched property values from [FROM].
 */

inline fun <reified FROM : Any, reified TO : Any> FROM.mapIdenticals(
                                                                  overrides: TO.(FROM)->TO = {this}
): TO {
    val fromProps = FROM::class.memberProperties.associateBy { it.name }
    val constructor = TO::class.primaryConstructor!!
    
    val args = constructor.parameters.associateWith { param ->
        val fromProp = fromProps[param.name]
        if (fromProp != null && param.type.classifier == fromProp.returnType.classifier) {
            fromProp.get(this)
        } else null
    }.filterValues { it != null }
    
    val instance = constructor.callBy(args)
    return overrides(instance, this@mapIdenticals)
}







