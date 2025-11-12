package com.vankorno.vankornodb.dbManagement.migration
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.DbTAG
import com.vankorno.vankornodb.core.DbConstants.dbDrop
import com.vankorno.vankornodb.dbManagement.createTable
import com.vankorno.vankornodb.dbManagement.createTables
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.dbManagement.migration.data.MigrationBundle
import com.vankorno.vankornodb.dbManagement.migration.data.MilestoneLambdas
import com.vankorno.vankornodb.dbManagement.migration.data.RenameRecord
import com.vankorno.vankornodb.dbManagement.migration.dsl.TransformCol
import com.vankorno.vankornodb.getSet.getObjects
import com.vankorno.vankornodb.getSet.insertObj
import com.vankorno.vankornodb.getSet.insertObjects
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType


/**
 * Migrates the contents of a table through multiple versioned entity definitions and optional transformation lambdas.
 *
 * This function performs step-by-step data migration from [oldVersion] to [newVersion], converting each entity instance
 * according to the provided versioned classes, rename history, and transformation lambdas. The table is dropped,
 * recreated using the structure of the final version class, and repopulated with the migrated data.
 *
 * @param tableName The name of the table to migrate.
 * @param oldVersion The version of the entity currently stored in the table.
 * @param newVersion The target version of the entity to migrate to.
 * @param migrationBundle A bundle of versioned classes, rename history and milestone lambdas.
 * @param onNewDbFilled An optional callback invoked with the list of fully migrated objects after the table has been repopulated.
 *
 * @throws IllegalArgumentException if any expected entity class or migration lambda is missing.
 */
fun SQLiteDatabase.migrateMultiStep(                              tableName: String,
                                                                 oldVersion: Int,
                                                                 newVersion: Int,
                                                            migrationBundle: MigrationBundle,
                                                              onNewDbFilled: (List<Any>)->Unit = {}
) {
    this.migrateMultiStep(tableName, oldVersion, newVersion, migrationBundle.versionedClasses,
        migrationBundle.renameHistory, migrationBundle.milestones, onNewDbFilled
    )
}

/**
 * Migrates the contents of a table through multiple versioned entity definitions and optional transformation lambdas.
 *
 * This function performs step-by-step data migration from [oldVersion] to [newVersion], converting each entity instance
 * according to the provided versioned classes, rename history, and transformation lambdas. The table is dropped,
 * recreated using the structure of the final version class, and repopulated with the migrated data.
 *
 * @param tableName The name of the table to migrate.
 * @param oldVersion The version of the entity currently stored in the table.
 * @param newVersion The target version of the entity to migrate to.
 * @param versionedClasses A map of version numbers to their corresponding entity KClass definitions.
 * @param renameHistory A map of current property names to their list of historical names and versions.
 * @param milestones A list of intermediate version numbers paired with transformation lambdas to apply during migration.
 * @param onNewDbFilled An optional callback invoked with the list of fully migrated objects after the table has been repopulated.
 *
 * @throws IllegalArgumentException if any expected entity class or migration lambda is missing.
 */
fun SQLiteDatabase.migrateMultiStep(                  tableName: String,
                                                     oldVersion: Int,
                                                     newVersion: Int,
                                               versionedClasses: Map<Int, KClass<*>>,
                                                  renameHistory: Map<String, List<RenameRecord>>,
                                                     milestones: List<Pair<Int, MilestoneLambdas>>,
                                                  onNewDbFilled: (List<Any>)->Unit = {}
) {
    if (newVersion <= oldVersion) return //\/\/\/\/\/\
    // region LOG
        Log.d(DbTAG, "Starting migrateMultiStep()... Table = $tableName, oldVer = $oldVersion, newVer = $newVersion, allMilestone size = ${milestones.size}")
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
    
    val finalClass = versionedClasses[newVersion]
        ?: error("Missing entity class for version $newVersion")
    
    val utils = MigrationUtils()
    val oldUnits = utils.readEntitiesFromVersion(this, tableName, oldVersion, versionedClasses)
    
    val migratedEntities = oldUnits.map { original ->
        utils.convertThroughSteps(original, oldVersion, steps, renameHistory, versionedClasses, lambdas)
    }
    this.execSQL(dbDrop + tableName)
    // region LOG
        Log.d(DbTAG, "migrateMultiStep() $tableName table is dropped. Recreating...")
    // endregion
    this.createTable(tableName, finalClass)
    // region LOG
        Log.d(DbTAG, "migrateMultiStep() Fresh $tableName is supposed to be recreated at this point. Starting to insert rows...")
    // endregion
    for (entity in migratedEntities) {
        val result = this.insertObj(tableName, entity)
        // region LOG
            if (result == -1L)
                Log.w(DbTAG, "migrateMultiStep() FAILED to insert row: $entity")
        // endregion
    }
    // region LOG
        Log.d(DbTAG, "migrateMultiStep() Done inserting rows. Starting onNewDbFilled()...")
    // endregion
    onNewDbFilled(migratedEntities)
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
    fun convertEntity(                                   oldObject: Any,
                                                          newClass: KClass<*>,
                                                    renameSnapshot: Map<String, String> = emptyMap(),
                                                    overrideColVal: (TransformCol.()->Unit)? = null
    ): Any {
        val fromProps = oldObject::class.memberProperties.associateBy { it.name }
        
        val constructor = newClass.primaryConstructor
            ?: error("Target class ${newClass.simpleName} must have a primary constructor")
        
        val dsl = TransformCol().apply { overrideColVal?.invoke(this) }
        
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
            
            val areDbPrimitives = !areSameTypeLists
                                    && fromType != null
                                    && isPrimitive(fromType)
                                    && isPrimitive(toType)
            
            val result = when {
                areSameTypeLists -> rawValue
                
                areDbPrimitives -> {
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
    fun readEntitiesFromVersion(                                             db: SQLiteDatabase,
                                                                      tableName: String,
                                                                        version: Int,
                                                               versionedClasses: Map<Int, KClass<*>>
    ): List<Any> {
        // region LOG
            Log.d(DbTAG, "readEntitiesFromVersion() starts. Table = $tableName, version = $version")
        // endregion
        val fromClass = versionedClasses[version]
            ?: error("Missing entity class for version $version")
        
        val elements = db.getObjects(fromClass, tableName)
        // region LOG
            Log.d(DbTAG, "readEntitiesFromVersion() ${elements.size} elements are read from DB and mapped to the old entity class.")
        // endregion
        return elements
    }
    
    
    /**
     * Converts an entity through a sequence of intermediate versions using
     * rename snapshots and version-specific migration lambdas.
     */
    fun convertThroughSteps(                           original: Any,
                                                     oldVersion: Int,
                                                          steps: List<Int>,
                                                  renameHistory: Map<String, List<RenameRecord>>,
                                               versionedClasses: Map<Int, KClass<*>>,
                                                        lambdas: Map<Int, MilestoneLambdas>
    ): Any {
        var currentObj = original
        var currentVer = oldVersion
        
        for (nextVer in steps) {
            val renameSnapshot = getRenameSnapshot(currentVer, nextVer, renameHistory)
            
            val nextClass = versionedClasses[nextVer] ?: error("Missing entity class for version $nextVer")
            
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
    fun getRenameSnapshot(                                  fromVer: Int,
                                                              toVer: Int,
                                                      renameHistory: Map<String, List<RenameRecord>>
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
                                                                        version: Int
    ): String? {
        var current: String? = colHistory.lastOrNull()?.to ?: return null
        
        for (record in colHistory.sortedByDescending { it.version }) {
            if (record.version <= version) break
            if (record.to == current) current = record.from
        }
        return current
    }
    
    
    fun isPrimitive(                                                                type: KType
    ): Boolean = type.classifier in setOf(
        Int::class, Long::class, Float::class, Double::class, Boolean::class, String::class
    )
    
    fun isSameListType(                                                                a: KType,
                                                                                       b: KType
    ): Boolean {
        if (!a.isSubtypeOf(List::class.starProjectedType) || !b.isSubtypeOf(List::class.starProjectedType)) return false
        val aArg = a.arguments.firstOrNull()?.type
        val bArg = b.arguments.firstOrNull()?.type
        return aArg != null && bArg != null && aArg == bArg
    }
    
    fun tryAutoConvertValue(                                                       value: Any?,
                                                                              targetType: KType
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


/**
 * Drops and recreates tables that don't need to be migrated.
 * Table content gets deleted.
 * 
 * @param tables A list of table names and entity data classes
 */
fun SQLiteDatabase.dropAndCreateEmptyTables(                               tables: List<TableInfo>
) {
    val size = tables.size
    // region LOG
        Log.d(DbTAG, "dropDroppables(): Dropping $size table(s)...")
    // endregion
    for (table in tables) {
        execSQL(dbDrop + table.name)
    }
    // region LOG
        Log.d(DbTAG, "dropDroppables(): Creating $size table(s)...")
    // endregion
    createTables(tables)
}

fun SQLiteDatabase.dropAndCreateEmptyTables(vararg tables: TableInfo) = dropAndCreateEmptyTables(tables.toList())



/**
 * Drops and recreates tables and their content without doing any real migrations.
 * Could be useful for things like switching from auto-incremented IDs to non-auto-incremented IDs, etc.
 * 
 * @param tables A list of table names and entity data classes
 */
fun SQLiteDatabase.migrateWithoutChange(                                  vararg tables: TableInfo
) {
    // region LOG
        Log.d(DbTAG, "migrateWithoutChange(): Migrating ${tables.size} table(s) without schema changes...")
    // endregion
    for (table in tables) {
        val rows = getObjects(table.entityClass, table.name)
        dropAndCreateEmptyTables(listOf(table))
        insertObjects(table.name, rows)
    }
    // region LOG
        Log.d(DbTAG, "migrateWithoutChange(): Migration complete.")
    // endregion
}








