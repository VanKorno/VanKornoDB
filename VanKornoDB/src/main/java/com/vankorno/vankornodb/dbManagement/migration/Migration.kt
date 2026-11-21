/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.add.internal.addEntitiesOut
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.TransformColDsl
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.core.data.DbConstants.RowID
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.dbManagement.migration.data.MilestoneLambdas
import com.vankorno.vankornodb.dbManagement.migration.data.RenameRecord
import com.vankorno.vankornodb.delete.deleteTable
import com.vankorno.vankornodb.mappers.toEntt
import com.vankorno.vankornodb.misc.dLog
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.wLog
import com.vankorno.vankornodb.newTable.createTable
import com.vankorno.vankornodb.newTable.createTables
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
internal fun SQLiteDatabase.migrateMultiStepInternal(            table: String,
                                                            oldVersion: Int,
                                                            entityMeta: BaseEntityMeta,
                                                         onNewDbFilled: (List<DbEntity>)->Unit = {},
) {
    val newVersion = entityMeta.currVersion
    
    if (oldVersion < 1 || newVersion < 1) {
        // region LOG
            eLog("migrateMultiStepInternal(): Invalid version detected for table $table (oldVersion = $oldVersion, newVersion = $newVersion). Aborting migration.")
        // endregion
        return //\/\/\/\/\/\
    }
    if (newVersion <= oldVersion)  return  //\/\/\/\/\/\
    
    val migrationBundle = entityMeta.migrationBundle.value
    val schemaBundle = entityMeta.schemaBundle
    
    val versionedClasses = migrationBundle.versionedClasses
    val renameHistory = migrationBundle.renameHistory
    val milestones = migrationBundle.milestones
    
    validateMigrationBundle(versionedClasses, renameHistory)
    // region LOG
        dLog("Starting migrateMultiStep()... Table = $table, oldVer = $oldVersion, newVer = $newVersion, allMilestone size = ${milestones.size}")
    // endregion
    val relevantMilestones = milestones
        .filter { (version, _) -> version > oldVersion && version <= newVersion }
        .toMutableList()
    
    val newVerIsMilestone = relevantMilestones.any { it.first == newVersion }
    if (!newVerIsMilestone)
        relevantMilestones.add(newVersion to MilestoneLambdas())
    
    val steps = relevantMilestones.map { it.first }
    // region LOG
        dLog("migrateMultiStep() Milestone count: ${steps.size}. Milestone ver numbers: ${steps.joinToString(", ")}")
    // endregion
    
    val utils = MigrationUtils()
    
    val oldObjects = utils.getEntitiesByVersion(this, table, oldVersion, versionedClasses)
    
    val migrationSteps = relevantMilestones.mapIndexed { index, (version, actions) ->
        val previousVersion = steps.getOrNull(index - 1) ?: oldVersion
        val targetClass = versionedClasses[version] ?: error("Missing entity class for version $version")
        MigrationStep(targetClass, utils.getRenameSnapshot(previousVersion, version, renameHistory, targetClass), actions)
    }
    
    val migratedObjects = oldObjects.map { original ->
        utils.convertThroughSteps(original, migrationSteps)
    }
    this.deleteTable(table)
    // region LOG
        dLog("migrateMultiStep() $table table is dropped. Recreating...")
    // endregion
    this.createTable(table using entityMeta.schemaBundle)
    // region LOG
        dLog("migrateMultiStep() Fresh $table is supposed to be recreated at this point. Starting to insert rows...")
    // endregion
    
    addEntitiesOut(table using schemaBundle, migratedObjects)
    // region LOG
        dLog("migrateMultiStep() Done inserting rows. Starting onNewDbFilled()...")
    // endregion
    onNewDbFilled(migratedObjects)
}








internal class MigrationStep(
    val targetClass: KClass<out DbEntity>,
    val renameSnapshot: Map<String, String>,
    val actions: MilestoneLambdas,
)


open class MigrationUtils {
    /**
     * Converts an [oldEntity] to a new instance of [newClass], applying renaming rules and type matching.
     *
     * @param oldEntity The entity object to convert.
     * @param newClass The target class to construct.
     * @param renameSnapshot Maps new properties to their old names.
     * @return A new instance of [newClass] with mapped or defaulted values.
     */
    internal fun convertEntity(                       oldEntity: DbEntity,
                                                       newClass: KClass<out DbEntity>,
                                                 renameSnapshot: Map<String, String> = emptyMap(),
                                                transformColVal: (TransformColDsl.()->Unit)? = null,
    ): DbEntity {
        val fromProps = oldEntity::class.memberProperties.associateBy { it.name }
        
        val constructor = newClass.primaryConstructor
            ?: error("Target class ${newClass.simpleName} must have a primary constructor")
        
        val dsl = TransformColDsl().apply { transformColVal?.invoke(this) }
        
        val args = constructor.parameters.associateWith { param ->
            val toName = param.name ?: error("Constructor parameter must have a name")
            val fromName = renameSnapshot[toName] ?: toName
            val fromProp = fromProps[fromName]
            
            val rawValue = fromProp?.getter?.call(oldEntity)
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
    internal fun getEntitiesByVersion(                           db: SQLiteDatabase,
                                                              table: String,
                                                            version: Int,
                                                   versionedClasses: Map<Int, KClass<out DbEntity>>,
    ): List<DbEntity> {
        // region LOG
            dLog("getEntitiesByVersion() starts. Table = $table, version = $version")
        // endregion
        val fromClass = versionedClasses[version]
            ?: error("Missing entity class for version $version")
        
        val elements = db.readMigrationRows(table) { it.toEntt(fromClass) }
        // region LOG
            dLog("getEntitiesByVersion() ${elements.size} elements are read from DB and mapped to the old entity class.")
        // endregion
        return elements
    }
    
    
    /**
     * Converts an entity through a sequence of intermediate versions using
     * rename snapshots and version-specific migration lambdas.
     */
    internal fun convertThroughSteps(                                 original: DbEntity,
                                                                         steps: List<MigrationStep>,
    ): DbEntity {
        var currentEntity = original
        
        for (step in steps) {
            val previousEntity = currentEntity
            currentEntity = convertEntity(previousEntity, step.targetClass, step.renameSnapshot, step.actions.transformColVal)
            currentEntity = step.actions.processFinalEntity?.invoke(previousEntity, currentEntity) ?: currentEntity
        }
        return currentEntity
    }
    
    
    /**
     * Builds a snapshot of field renames between two entity versions by tracing target columns
     * backwards through historical rename records.
     *
     * @param fromVer The source version number.
     * @param toVer The target version number.
     * @param renameHistory Chronological list of all registered rename records.
     * @param targetClass Entity class of the target version.
     * @return A map where each key is the target property name and value is its corresponding source property name.
     */
    internal fun getRenameSnapshot(                           fromVer: Int,
                                                                toVer: Int,
                                                        renameHistory: List<RenameRecord>,
                                                          targetClass: KClass<out DbEntity>? = null,
    ): Map<String, String> {
        val snapshot = mutableMapOf<String, String>()
        val relevantRecords = renameHistory
            .filter { it.version > fromVer && it.version <= toVer }
            .sortedByDescending { it.version }
        
        if (relevantRecords.isEmpty()) return snapshot //\/\/\/\/\/\
        
        val targetColNames = targetClass?.primaryConstructor?.parameters?.mapNotNull { it.name }
            ?: return snapshot //\/\/\/\/\/\
        
        for (targetName in targetColNames) {
            var currName = targetName
            for (record in relevantRecords) {
                if (record.to == currName) {
                    currName = record.from
                }
            }
            if (currName != targetName) {
                snapshot[targetName] = currName
            }
        }
        return snapshot
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



private fun <T : DbEntity> SQLiteDatabase.readMigrationRows(                    table: String,
                                                                              readRow: (Cursor)->T,
): List<T> {
    val quotedName = "\"" + table.replace("\"", "\"\"") + "\""
    return rawQuery("SELECT * FROM $quotedName ORDER BY $RowID", null).use { cursor ->
        buildList {
            while (cursor.moveToNext())
                add(readRow(cursor))
        }
    }
}



internal fun SQLiteDatabase.dropAndCreateEmptyTablesInternal(
                                                              vararg tables: TableInfo<out DbEntity>
) {
    val size = tables.size
    // region LOG
        dLog("dropDroppables(): Dropping $size table(s)...")
    // endregion
    for (table in tables) {
        deleteTable(table.name)
    }
    // region LOG
        dLog("dropDroppables(): Creating $size table(s)...")
    // endregion
    this@dropAndCreateEmptyTablesInternal.createTables(*tables)
}



internal fun SQLiteDatabase.migrateWithoutChangeInternal(     vararg tables: TableInfo<out DbEntity>
) {
    // region LOG
        dLog("migrateWithoutChange(): Migrating ${tables.size} table(s) without schema changes...")
    // endregion
    for (table in tables) {
        val rows = readMigrationRows(table.name) { it.toEntity(table.schema) }
        dropAndCreateEmptyTablesInternal(table)
        addEntitiesOut(table, rows)
    }
    // region LOG
        dLog("migrateWithoutChange(): Migration complete.")
    // endregion
}



internal fun validateMigrationBundle(              versionedClasses: Map<Int, KClass<out DbEntity>>,
                                                      renameHistory: List<RenameRecord>,
) {
    for (record in renameHistory) {
        val ver = record.version
        val prevClass = versionedClasses[ver - 1]
        val currClass = versionedClasses[ver]
        
        if (prevClass != null) {
            val prevCols = prevClass.primaryConstructor?.parameters?.mapNotNull { it.name }.orEmpty()
            if (prevCols.isNotEmpty() && record.from !in prevCols) {
                val closest = findClosestMatch(record.from, prevCols)
                val hint = if (closest != null) " Did you mean '$closest'?" else ""
                // region LOG
                    wLog("Migration validation warning at version $ver: Source column '${record.from}' in rename rule ('${record.from}' -> '${record.to}') was not found in version ${ver - 1} schema ($prevCols).$hint")
                // endregion
            }
        }
        
        if (currClass != null) {
            val currCols = currClass.primaryConstructor?.parameters?.mapNotNull { it.name }.orEmpty()
            if (currCols.isNotEmpty() && record.to !in currCols) {
                val closest = findClosestMatch(record.to, currCols)
                val hint = if (closest != null) " Did you mean '$closest'?" else ""
                // region LOG
                    wLog("Migration validation warning at version $ver: Target column '${record.to}' in rename rule ('${record.from}' -> '${record.to}') was not found in version $ver schema ($currCols).$hint")
                // endregion
            }
        }
    }
}



internal fun findClosestMatch(                                                target: String,
                                                                          candidates: List<String>,
): String? {
    if (candidates.isEmpty()) return null //\/\/\/\/\/\
    
    fun levenshtein(s1: String, s2: String): Int {
        val m = s1.length
        val n = s2.length
        val dp = Array(m + 1) { IntArray(n + 1) }
        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j
        
        for (i in 1..m) {
            for (j in 1..n) {
                val cost = if (s1[i - 1].equals(s2[j - 1], ignoreCase = true)) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost,
                )
            }
        }
        return dp[m][n]
    }
    
    var bestMatch: String? = null
    var minDistance = Int.MAX_VALUE
    
    for (candidate in candidates) {
        val dist = levenshtein(target, candidate)
        if (dist < minDistance && dist <= maxOf(2, target.length / 2)) {
            minDistance = dist
            bestMatch = candidate
        }
    }
    return bestMatch
}
