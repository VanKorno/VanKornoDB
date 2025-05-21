package com.vankorno.vankornodb.dbManagement.migration
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.dbManagement.migration.MigrationUtils.defaultValueForParam
import com.vankorno.vankornodb.getCursor
import com.vankorno.vankornodb.getSet.mapToEntity
import kotlin.collections.get
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


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







