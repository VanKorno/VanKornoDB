package com.vankorno.vankornodb.dbManagement.migration
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.dbDrop
import com.vankorno.vankornodb.dbManagement.createTable
import com.vankorno.vankornodb.getSet.getCursor
import com.vankorno.vankornodb.getSet.insertEntity
import com.vankorno.vankornodb.getSet.toEntity
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType


typealias MigrationStepLambda = (Any) -> Any

fun SQLiteDatabase.migrateMultiStep(                  tableName: String,
                                                     oldVersion: Int,
                                                     newVersion: Int,
                                               versionedClasses: Map<Int, KClass<*>>,
                                                  renameHistory: Map<String, List<Pair<Int, String>>>,
                                                  allMilestones: List<Pair<Int, MigrationStepLambda>>,
                                                  onNewDbFilled: (List<Any>)->Unit = {}
) {
    if (newVersion <= oldVersion) return //\/\/\/\/\/\
    
    val relevantMilestones = allMilestones
        .filter { (version, _) -> version > oldVersion && version <= newVersion }
        .toMutableList()
    
    val newVerIsMilestone = relevantMilestones.any { it.first == newVersion }
    if (!newVerIsMilestone)
        relevantMilestones.add(newVersion to {})
    
    val steps = relevantMilestones.map { it.first }
    
    val lambdas = relevantMilestones.toMap()
    
    val finalClass = versionedClasses[newVersion]
        ?: error("Missing entity class for version $newVersion")
    
    val utils = MigrationUtils()
    val oldUnits = utils.getListOfOlds(this, tableName, oldVersion, versionedClasses)
    
    val migratedList = oldUnits.map { original ->
        utils.convertThroughSteps(original, oldVersion, steps, renameHistory, versionedClasses, lambdas)
    }
    this.execSQL(dbDrop + tableName)
    this.createTable(tableName, finalClass)
    migratedList.forEach { this.insertEntity(tableName, it) }
    onNewDbFilled(migratedList)
}


/**
 * Utility functions to assist with migration-related property name resolution and default value provisioning.
 */
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
                                                    renameSnapshot: Map<String, String> = emptyMap()
    ): Any {
        val fromProps = oldObject::class.memberProperties.associateBy { it.name }
        val constructor = newClass.primaryConstructor
            ?: error("Target class ${newClass.simpleName} must have a primary constructor")
        
        val args = constructor.parameters.associateWith { param ->
            val toName = param.name ?: error("Constructor parameter must have a name")
            val fromName = renameSnapshot[toName] ?: toName
            val fromProp = fromProps[fromName]
            
            val rawValue = fromProp?.getter?.call(oldObject)
            val fromType = fromProp?.returnType
            val toType = param.type
            
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
    
    
    
    fun getListOfOlds(                                                       db: SQLiteDatabase,
                                                                      tableName: String,
                                                                     oldVersion: Int,
                                                               versionedClasses: Map<Int, KClass<*>>
    ): List<Any> {
        val fromClass = versionedClasses[oldVersion]
            ?: error("Missing entity class for version $oldVersion")
        
        return db.getCursor(tableName).use { cursor ->
            buildList {
                if (cursor.moveToFirst()) {
                    do {
                        add(cursor.toEntity(fromClass))
                    } while (cursor.moveToNext())
                }
            }
        }
    }
    
    
    fun convertThroughSteps(                           original: Any,
                                                     oldVersion: Int,
                                                          steps: List<Int>,
                                                  renameHistory: Map<String, List<Pair<Int, String>>>,
                                               versionedClasses: Map<Int, KClass<*>>,
                                                        lambdas: Map<Int, MigrationStepLambda>
    ): Any {
        var current = original
        var currentVer = oldVersion
        
        for (nextVer in steps) {
            val renameSnapshot = getRenameSnapshot(currentVer, nextVer, renameHistory)
            val nextClass = versionedClasses[nextVer]
                ?: error("Missing entity class for version $nextVer")
            
            current = convertEntity(current, nextClass, renameSnapshot)
            
            current = lambdas[nextVer]?.invoke(current)
                ?: error("Missing migration lambda for version $nextVer")
            currentVer = nextVer
        }
        return current
    }
    
    /**
     * Builds a snapshot of field renames between two entity versions.
     *
     * @param fromVer The source version number.
     * @param toVer The target version number.
     * @param renameHistory A map of current field names to their rename history.
     * @return A map where each key is the current name and each value is the corresponding old name.
     */
    fun getRenameSnapshot(                              fromVer: Int,
                                                          toVer: Int,
                                                  renameHistory: Map<String, List<Pair<Int, String>>>
    ): Map<String, String> {
        val snapshot = mutableMapOf<String, String>()
        
        for ((newestName, history) in renameHistory) {
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
     * @param history A list of (version, name) pairs representing rename history.
     * @param version The version to query.
     * @return The name valid at the given version, or null if not found.
     */
    private fun getNameAtVersion(                                   history: List<Pair<Int, String>>,
                                                                    version: Int
    ): String? = history
                    .filter { (ver, _) -> ver <= version }
                    .maxByOrNull{ it.first }?.second
    
    
    fun isPrimitive(type: KType): Boolean {
        return type.classifier in setOf(
            Int::class, Long::class, Float::class, Double::class, Boolean::class, String::class
        )
    }
    
    fun isSameListType(a: KType, b: KType): Boolean {
        if (!a.isSubtypeOf(List::class.starProjectedType) || !b.isSubtypeOf(List::class.starProjectedType)) return false
        val aArg = a.arguments.firstOrNull()?.type
        val bArg = b.arguments.firstOrNull()?.type
        return aArg != null && bArg != null && aArg == bArg
    }
    
    fun tryAutoConvertValue(
        value: Any?,
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










