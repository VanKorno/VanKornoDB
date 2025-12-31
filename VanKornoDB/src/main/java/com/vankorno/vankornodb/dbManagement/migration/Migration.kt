// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.migration

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.add.addObjects
import com.vankorno.vankornodb.api.TransformColDsl
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.api.createTables
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.NormalSchemaBundle
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.dbManagement.migration.data.MilestoneLambdas
import com.vankorno.vankornodb.dbManagement.migration.data.RenameRecord
import com.vankorno.vankornodb.delete.deleteTable
import com.vankorno.vankornodb.get.getObjects
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType


/**
 * Migrates the contents of a table through multiple versioned entity definitions and optional transformation lambdas.
 *
 * This function performs step-by-step data migration from [oldVersion] to the current version, stated in [entityMeta],
 * converting each entity instance according to the provided versioned classes, rename history,
 * and transformation lambdas. The table is dropped, recreated using the structure
 * of the final version class, and repopulated with the migrated data.
 *
 * @param table The name of the table to migrate.
 * @param oldVersion The version of the entity currently stored in the table.
 * @param entityMeta Cross-version entity data.
 * @param onNewDbFilled An optional callback invoked with the list of fully migrated objects after the table has been repopulated.
 *
 * @throws IllegalArgumentException if any expected entity class or migration lambda is missing.
 */
internal fun SQLiteDatabase.migrateMultiStepInternal(          table: String,
                                                          oldVersion: Int,
                                                          entityMeta: BaseEntityMeta,
                                                       onNewDbFilled: (List<BaseEntity>)->Unit = {},
) {
    val newVersion = entityMeta.currVersion
    
    if (newVersion <= oldVersion) return //\/\/\/\/\/\
    
    val migrationBundle = entityMeta.migrationBundle.value
    val schemaBundle = entityMeta.schemaBundle
    
    val versionedBundles = migrationBundle.versionedSchemaBundles
    val renameHistory = migrationBundle.renameHistory
    val milestones = migrationBundle.milestones
    // region LOG
        Log.d(DbTAG, "Starting migrateMultiStep()... Table = $table, oldVer = $oldVersion, newVer = $newVersion, allMilestone size = ${milestones.size}")
    // endregion
    val relevantMilestones = milestones
        .filter { (version, _) -> version > oldVersion && version <= newVersion }
        .toMutableList()
    
    val newVerIsMilestone = relevantMilestones.any { it.first == newVersion }
    if (!newVerIsMilestone)
        relevantMilestones.add(newVersion to MilestoneLambdas())
    
    val steps = relevantMilestones.map { it.first }
    // region LOG
        Log.d(DbTAG, "migrateMultiStep() Steps (to-versions) = ${steps.joinToString(", ")}")
    // endregion
    val lambdas = relevantMilestones.toMap()
    
    val utils = MigrationUtils()
    
    val oldObjects = utils.getObjectsByVersion(this, table, oldVersion, versionedBundles)
    
    val migratedObjects = oldObjects.map { original ->
        utils.convertThroughSteps(original, oldVersion, steps, renameHistory, versionedBundles, lambdas)
    }
    this.deleteTable(table)
    // region LOG
        Log.d(DbTAG, "migrateMultiStep() $table table is dropped. Recreating...")
    // endregion
    this.createTable(table using entityMeta.schemaBundle)
    // region LOG
        Log.d(DbTAG, "migrateMultiStep() Fresh $table is supposed to be recreated at this point. Starting to insert rows...")
    // endregion
    
    addObjects(table using schemaBundle, migratedObjects)
    // region LOG
        Log.d(DbTAG, "migrateMultiStep() Done inserting rows. Starting onNewDbFilled()...")
    // endregion
    onNewDbFilled(migratedObjects)
}








open class MigrationUtils {
    /**
     * Converts an [oldObject] to a new instance of [newClass], applying renaming rules and type matching.
     *
     * @param oldObject The object to convert.
     * @param newClass The target class to construct.
     * @param renameSnapshot Maps new properties to their old names.
     * @return A new instance of [newClass] with mapped or defaulted values.
     */
    internal fun convertEntity(                       oldObject: NormalEntity,
                                                       newClass: KClass<out NormalEntity>,
                                                 renameSnapshot: Map<String, String> = emptyMap(),
                                                 overrideColVal: (TransformColDsl.()->Unit)? = null,
    ): NormalEntity {
        val fromProps = oldObject::class.memberProperties.associateBy { it.name }
        
        val constructor = newClass.primaryConstructor
            ?: error("Target class ${newClass.simpleName} must have a primary constructor")
        
        val dsl = TransformColDsl().apply { overrideColVal?.invoke(this) }
        
        val args = constructor.parameters.associateWith { param ->
            val toName = param.name ?: error("Constructor parameter must have a name")
            val fromName = renameSnapshot[toName] ?: toName
            val fromProp = fromProps[fromName]
            
            val rawValue = fromProp?.getter?.call(oldObject)
            val fromType = fromProp?.returnType
            val toType = param.type
            
            val overriddenValue = dsl.getOverride(toName)?.apply(rawValue)
            if (overriddenValue != null) return@associateWith overriddenValue
            
            val areSameTypeLists = fromType != null && isSameListType(fromType, toType)
            
            val areDbTypes = !areSameTypeLists
                            && fromType != null
                            && isDbPrimitive(fromType)
                            && isDbPrimitive(toType)
            
            val result = when {
                areSameTypeLists -> rawValue
                
                areDbTypes -> {
                    if (fromType.isSubtypeOf(toType)) rawValue
                    else tryAutoConvertValue(rawValue, toType)
                }
                else -> null // Other types. Fallback to default
            }
            result
        }.filterValues { it != null }
        
        return constructor.callBy(args)
    }
    
    
    /** 
     * Reads all rows from the specified table and maps them to instances 
     * of the entity class corresponding to the given version.
     */
    internal fun getObjectsByVersion(            db: SQLiteDatabase,
                                              table: String,
                                            version: Int,
                                      schemaBundles: Map<Int, NormalSchemaBundle<out NormalEntity>>,
    ): List<NormalEntity> {
        // region LOG
            Log.d(DbTAG, "readEntitiesFromVersion() starts. Table = $table, version = $version")
        // endregion
        val fromBundle = schemaBundles[version]
            ?: error("Missing schemaBundle for version $version")
        
        val elements = db.getObjects(table using fromBundle)
        // region LOG
            Log.d(DbTAG, "readEntitiesFromVersion() ${elements.size} elements are read from DB and mapped to the old entity class.")
        // endregion
        return elements
    }
    
    
    /**
     * Converts an entity through a sequence of intermediate versions using
     * rename snapshots and version-specific migration lambdas.
     */
    internal fun convertThroughSteps(      original: NormalEntity,
                                         oldVersion: Int,
                                              steps: List<Int>,
                                      renameHistory: Map<String, List<RenameRecord>>,
                                      schemaBundles: Map<Int, NormalSchemaBundle<out NormalEntity>>,
                                            lambdas: Map<Int, MilestoneLambdas>,
    ): NormalEntity {
        var currentObj = original
        var currentVer = oldVersion
        
        for (nextVer in steps) {
            val renameSnapshot = getRenameSnapshot(currentVer, nextVer, renameHistory)
            
            val nextClass = schemaBundles[nextVer]?.clazz ?: error("Missing entity class for version $nextVer")
            
            val previousObj = currentObj
            currentObj = convertEntity(currentObj, nextClass, renameSnapshot, lambdas[nextVer]?.transformColVal)
            
            currentObj = lambdas[nextVer]?.processFinalObj?.invoke(previousObj, currentObj) ?: currentObj
            currentVer = nextVer
        }
        return currentObj
    }
    
    
    /**
     * Builds a snapshot of field renames between two entity versions.
     *
     * @param fromVer The source version number.
     * @param toVer The target version number.
     * @param renameHistory A map of current field names to their rename history.
     * @return A map where each key is the current name and each value is the corresponding old name.
     */
    internal fun getRenameSnapshot(                        fromVer: Int,
                                                             toVer: Int,
                                                     renameHistory: Map<String, List<RenameRecord>>,
    ): Map<String, String> {
        val snapshot = mutableMapOf<String, String>()
        
        for ((latestName, history) in renameHistory) {
            val oldName = getNameAtVersion(history, fromVer)
            val targetName = getNameAtVersion(history, toVer)
            
            if (oldName != null && targetName != null && oldName != targetName) {
                snapshot[targetName] = oldName
            }
        }
        return snapshot
    }
    
    /**
     * Finds the field name used at or before a specific version in the rename history.
     *
     * @param colHistory A list of RenameRecord representing rename history for a single column.
     * @param version The version to query.
     * @return The name valid at the given version, or null if not found.
     */
    private fun getNameAtVersion(                                    colHistory: List<RenameRecord>,
                                                                        version: Int,
    ): String? {
        var current: String? = colHistory.lastOrNull()?.to ?: return null
        
        for (record in colHistory.sortedByDescending { it.version }) {
            if (record.version <= version) break
            if (record.to == current) current = record.from
        }
        return current
    }
    
    
    internal fun isDbPrimitive(                                                     type: KType
    ): Boolean = type.classifier in setOf(
        Int::class, Long::class, Float::class, Double::class, Boolean::class, String::class
    )
    
    internal fun isSameListType(                                                       a: KType,
                                                                                       b: KType,
    ): Boolean {
        if (!a.isSubtypeOf(List::class.starProjectedType) || !b.isSubtypeOf(List::class.starProjectedType)) return false
        val aArg = a.arguments.firstOrNull()?.type
        val bArg = b.arguments.firstOrNull()?.type
        return aArg != null && bArg != null && aArg == bArg
    }
    
    internal fun tryAutoConvertValue(                                              value: Any?,
                                                                              targetType: KType,
    ): Any? {
        if (value == null) return null
    
        val classifier = targetType.classifier
        return when (classifier) {
            Int::class -> when (value) {
                is Int -> value
                is Long -> value.toInt()
                is Float -> value.toInt()
                is Double -> value.toInt()
                is Boolean -> if (value) 1 else 0
                else -> null
            }
            Long::class -> when (value) {
                is Int -> value.toLong()
                is Long -> value
                is Float -> value.toLong()
                is Double -> value.toLong()
                is Boolean -> if (value) 1L else 0L
                else -> null
            }
            Float::class -> when (value) {
                is Int -> value.toFloat()
                is Long -> value.toFloat()
                is Float -> value
                is Double -> value.toFloat()
                is Boolean -> if (value) 1f else 0f
                else -> null
            }
            Double::class -> when (value) {
                is Int -> value.toDouble()
                is Long -> value.toDouble()
                is Float -> value.toDouble()
                is Double -> value
                is Boolean -> if (value) 1.0 else 0.0
                else -> null
            }
            Boolean::class -> when (value) {
                is Boolean -> value
                is Int -> value != 0
                is Long -> value != 0L
                is Float -> value != 0f
                is Double -> value != 0.0
                else -> null
            }
            String::class -> when (value) {
                is String -> value
                else -> null  // no auto-convert to String
            }
            else -> null
        }
    }
}



internal fun SQLiteDatabase.dropAndCreateEmptyTablesInternal(
                                                    vararg tables: TableInfoNormal<out NormalEntity>
) {
    val size = tables.size
    // region LOG
        Log.d(DbTAG, "dropDroppables(): Dropping $size table(s)...")
    // endregion
    for (table in tables) {
        deleteTable(table.name)
    }
    // region LOG
        Log.d(DbTAG, "dropDroppables(): Creating $size table(s)...")
    // endregion
    createTables(*tables)
}



internal fun SQLiteDatabase.migrateWithoutChangeInternal(
                                                    vararg tables: TableInfoNormal<out NormalEntity>
) {
    // region LOG
        Log.d(DbTAG, "migrateWithoutChange(): Migrating ${tables.size} table(s) without schema changes...")
    // endregion
    for (table in tables) {
        val rows = getObjects(table)
        dropAndCreateEmptyTablesInternal(table)
        addObjects(table, rows)
    }
    // region LOG
        Log.d(DbTAG, "migrateWithoutChange(): Migration complete.")
    // endregion
}








