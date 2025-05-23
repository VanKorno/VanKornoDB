package com.vankorno.vankornodb.dbManagement.migration
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.dbDrop
import com.vankorno.vankornodb.dbManagement.createTableOf
import com.vankorno.vankornodb.dbManagement.migration.MigrationUtils.defaultValueForParam
import com.vankorno.vankornodb.getCursor
import com.vankorno.vankornodb.getSet.insertInto
import com.vankorno.vankornodb.getSet.mapToEntity
import kotlin.collections.get
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor



/**
 * Migrates one or more tables from [oldClass] to [newClass], adapting data to the new structure.
 *
 * For each [tableName], this function:
 * 1. Reads existing rows using [readAsMigrated], applying rename rules and constructor mapping.
 * 2. Drops the old table and recreates it using the schema from [newClass].
 * 3. Reinserts the migrated data into the new table.
 *
 * Suitable for simple migrations where only the migrated table is affected.
 * Use manual migration for complex cases involving external references or logic.
 *
 * @param oldClass The class representing the old entity structure.
 * @param newClass The class representing the new entity structure.
 * @param renameSnapshot Maps new property names to old ones.
 * @param tableNames A list of tables to migrate.
 */
fun SQLiteDatabase.migrateLite(                           oldClass: KClass<*>,
                                                          newClass: KClass<*>,
                                                        tableNames: List<String>,
                                                    renameSnapshot: Map<String, String> = emptyMap()
) {
    tableNames.forEach { tableName ->
        // region LOG
            println("Migrating table: $tableName")
        // endregion
        val newList = readAsMigrated(oldClass, newClass, tableName, renameSnapshot)
        // region LOG
            println("Dropping old table: $tableName")
        // endregion
        this.execSQL(dbDrop + tableName)
        // region LOG
            println("Creating new table schema for: $tableName")
        // endregion
        this.createTableOf(tableName, newClass)
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
fun SQLiteDatabase.migrateLite(                          tableName: String,
                                                          oldClass: KClass<*>,
                                                          newClass: KClass<*>,
                                                    renameSnapshot: Map<String, String> = emptyMap()
) = migrateLite(oldClass, newClass, listOf(tableName), renameSnapshot)




/**
 * Reads all rows from [tableName] and converts them from [oldClass] to [newClass].
 *
 * Uses [convertEntity] to apply renaming rules and provide fallback values
 * for missing or mismatched fields during migration.
 *
 * @param oldClass The old entity class used to parse existing data.
 * @param newClass The new entity class to convert into.
 * @param tableName The table to read data from.
 * @param renameSnapshot Maps new property names to old ones.
 * @return A list of [newClass] instances created from the old data.
 */
fun SQLiteDatabase.readAsMigrated(                        oldClass: KClass<*>,
                                                          newClass: KClass<*>,
                                                         tableName: String,
                                                    renameSnapshot: Map<String, String> = emptyMap()
): List<Any> {
    val oldItems = mutableListOf<Any>()
    
    this.getCursor(tableName).use { cursor ->
        if (cursor.moveToFirst()) {
            do {
                val old = cursor.mapToEntity(oldClass)
                val new = convertEntity(old, newClass, renameSnapshot)
                oldItems += new
            } while (cursor.moveToNext())
        }
    }
    return oldItems
}





/**
 * Converts an [oldObject] to a new instance of [newClass], applying renaming rules and type matching.
 *
 * @param oldObject The object to convert.
 * @param newClass The target class to construct.
 * @param renameSnapshot Maps new properties to their old names.
 * @return A new instance of [newClass] with mapped or defaulted values.
 */
fun convertEntity(                                       oldObject: Any,
                                                          newClass: KClass<*>,
                                                    renameSnapshot: Map<String, String> = emptyMap()
): Any {
    val fromProps = oldObject::class.memberProperties.associateBy { it.name }
    val constructor = newClass.primaryConstructor
        ?: error("Target class ${newClass.simpleName} must have a primary constructor")
    
    val args = constructor.parameters.associateWith { param ->
        val toName = param.name ?: error("Constructor parameter must have a name")
        
        val fromName = renameSnapshot[toName] ?: toName
        val fromProp = fromProps[fromName]
        
        if (fromProp != null && fromProp.returnType.isSubtypeOf(param.type)) {
            @Suppress("UNCHECKED_CAST")
            (fromProp as KProperty1<Any, *>).get(oldObject)
        } else {
            defaultValueForParam(param)
        }
    }
    return constructor.callBy(args)
}

/**
 * Utility functions to assist with migration-related property name resolution and default value provisioning.
 */
object MigrationUtils {
    /**
     * Provides a default value for a constructor [param] of common types,
     * used when no value can be mapped from the source object.
     *
     * Supports: Int, Long, Boolean, String, Double, Float.
     * Returns null for other or unknown types.
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
    
    
    
    /**
     * Generates a list of intermediate version steps needed to migrate from an older version to a target version.
     *
     * The migration follows a predictable and tiered strategy:
     * 1. If the starting version is not a "tenner" (i.e. ends in a zero), the first step is the next higher tenner.
     * 2. Then, the function ascends through progressively larger tier milestones (100, 1000, 10000, etc.)
     *    as long as each next tier is less than the target version.
     * 3. If the gap between the last milestone and the target version is more than 10,
     *    the closest tenner before the target is added as the next step.
     * 4. The final step is the target version itself.
     *
     * @param oldVer The starting version number.
     * @param targetVer The version to migrate to.
     * @return A list of version numbers representing each step in the migration path.
     */
    fun getMigrationSteps(                                                        oldVer: Int,
                                                                               targetVer: Int
    ): List<Int> {
        val steps = mutableListOf<Int>()
        var current = oldVer
        
        // Step 1: if not a tenner, move to next tenner
        if (!isTenner(current)) {
            val nextTenner = ((current / 10) + 1) * 10
            if (nextTenner <= targetVer) {
                steps += nextTenner
                current = nextTenner
            }
        }
        
        // Step 2: go up by hardcoded tiers as long as next tier > current and < targetVer
        val tiers = listOf(100, 1000, 10_000, 100_000)
        tiers.forEach { tier ->
            if (tier > current && tier < targetVer) {
                steps += tier
                current = tier
            }
        }
        
        // Step 3: if current is still far from target, add closest tenner before targetVer
        val targetTenner = (targetVer / 10) * 10
        if (targetTenner > current && targetTenner != targetVer) {
            steps += targetTenner
        }
        
        // Final step: only go to targetVer if not already there
        if (current != targetVer)
            steps += targetVer
        
        return steps
    }
    private fun isTenner(v: Int): Boolean = v % 10 == 0
}






























// Probably not needed anymore

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







