// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.mapper

import android.database.Cursor
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.mapper.EntityMapperUtils.defaultInstanceValueOf
import com.vankorno.vankornodb.mapper.EntityMapperUtils.getListFromCursor
import com.vankorno.vankornodb.mapper.EntityMapperUtils.getListSizeFromDefault
import com.vankorno.vankornodb.mapper.EntityMapperUtils.getNullable
import com.vankorno.vankornodb.misc.getBoolean
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

/*
 * ## Rules for List Parameters:
 *  1. Lists must always be declared at the END of the constructor parameter list.
 * 
 *  2. List parameters must be named with the "List" suffix (e.g., `dayList`).
 *     Corresponding DB column names must match the base name (e.g., `day`)
 *     followed by a 1-based index suffix: `day1`, `day2`, ..., etc.
 * 
 *  3. Lists must be non-nullable and must have default values with a fixed number of elements
 *     (used to infer the column count for each list).
 * 
 *  4. All list elements must be of the same type, limited to a supported primitive type
 *     (Boolean, Int, Long, Float, or String).
 * 
 *  5. Nested lists or complex types (e.g., data classes) inside lists are NOT SUPPORTED.
 */





/**
 * Maps the current row of the Cursor to an instance of the specified data class [T].
 *
 * This function assumes that column names in the Cursor match the constructor parameter names
 * of [T]. Scalar values are resolved directly by name and type; list values are resolved using
 * a naming convention and parameter ordering.
 *
 * If a scalar parameter's type is unsupported, its default value will be used.
 *
 * @return An instance of [T] constructed from the current row of the Cursor.
 * @throws IllegalArgumentException if required fields are missing or types are unsupported.
 */
fun <T : DbEntity> Cursor.toEntity(                                                clazz: KClass<T>
): T {
    require(clazz.isData) { "Entity must be a data class" }
    
    val constructor = clazz.primaryConstructor
        ?: error("Class ${clazz.simpleName} must have a primary constructor")
    
    val defaultInstance = constructor.callBy(emptyMap())
    val params = constructor.parameters
    val listStartIndex = params.indexOfFirst { it.name?.endsWith("List") == true }
    val scalarParams = if (listStartIndex == -1) params else params.subList(0, listStartIndex)
    val listParams = if (listStartIndex == -1) emptyList() else params.subList(listStartIndex, params.size)
    
    
    val scalarArgs = scalarParams.associate { param ->
        val colName = param.name ?: error("Constructor param must have a name")
        val index = getColumnIndexOrThrow(colName)
        val isNullable = param.type.isMarkedNullable
        val type = param.type.jvmErasure
        
        val value = when (type) {
            Int::class -> if (isNullable) getNullable(index) { getInt(it) } else getInt(index)
            String::class -> if (isNullable) getNullable(index) { getString(it) } else getString(index)
            
            Boolean::class -> if (isNullable) getNullable(index) { getBoolean(it) } else getBoolean(index)
            Long::class -> if (isNullable) getNullable(index) { getLong(it) } else getLong(index)
            Float::class -> if (isNullable) getNullable(index) { getFloat(it) } else getFloat(index)
            ByteArray::class -> if (isNullable) getNullable(index) { getBlob(it) } else getBlob(index)
            
            else -> return@associate param to defaultInstanceValueOf(param, defaultInstance)
        }
        param to value
    }
    
    // Find the first list column index once (assumes naming like weDay1, weDay2...)
    val firstListColumnIndex = if (listParams.isNotEmpty()) {
        val firstListParamName = listParams.first().name ?: error("List param must have a name")
        val baseName = if (firstListParamName.endsWith("List")) firstListParamName.removeSuffix("List") else firstListParamName
        getColumnIndexOrThrow("${baseName}1")
    } else -1
    
    var currentListColumnIndex = firstListColumnIndex
    
    val listArgs = listParams.associateWith { param ->
        val listSize = getListSizeFromDefault(param, defaultInstance, clazz)
        val elementType = param.type.arguments.firstOrNull()?.type?.jvmErasure
            ?: error("List param must have a generic type")
        
        // Read list from current position in cursor
        val list = getListFromCursor(currentListColumnIndex, listSize, elementType)
        
        // Move the currentListColumnIndex forward by the size of this list for the next param
        currentListColumnIndex += listSize
        
        list
    }
    val args = scalarArgs + listArgs
    return constructor.callBy(args)
}





/**
 * Internal utility container for functions supporting reflection-based entity mapping
 * from SQLite Cursor rows to Kotlin data classes. Handles default values, list parsing,
 * and type-safe construction based on constructor signatures.
 */
internal object EntityMapperUtils {
    /**
     * Returns the value of a constructor parameter [param] from a default [instance] of [T].
     */
    fun <T : Any> defaultInstanceValueOf(                                        param: KParameter,
                                                                              instance: T,
    ): Any? {
        val prop = instance::class.memberProperties
            .find { it.name == param.name }
            ?: error("Property ${param.name} not found in ${instance::class.simpleName}")
        @Suppress("UNCHECKED_CAST")
        return (prop as KProperty1<T, *>).get(instance)
    }
    
    
    /**
     * Retrieves the default value of the constructor parameter [param] from the given [defaultInstance].
     */
    fun <T : Any> getListSizeFromDefault(                                        param: KParameter,
                                                                       defaultInstance: T,
                                                                                 clazz: KClass<T>,
    ): Int {
        val defaultValue = clazz.memberProperties
            .firstOrNull { it.name == param.name }
            ?.getter
            ?.call(defaultInstance)
            ?: error("List param must have a default value")
        
        return (defaultValue as? List<*>)?.size
            ?: error("Default value is not a list for ${param.name}")
    }
    
    
    /**
     * Reads a list of [size] elements from the Cursor, starting at [firstListColumnIndex],
     * assuming all elements are of [elementType].
     */
    fun Cursor.getListFromCursor(                                   firstListColumnIndex: Int,
                                                                                    size: Int,
                                                                             elementType: KClass<*>,
    ): List<Any> {
        require(firstListColumnIndex + size <= columnCount) {
            "List param overruns Cursor column count"
        }
        val list = mutableListOf<Any>()
        
        for (i in 0 until size) {
            val idx = firstListColumnIndex + i
            val value = when (elementType) {
                Int::class -> getInt(idx)
                Boolean::class -> getBoolean(idx)
                Long::class -> getLong(idx)
                String::class -> getString(idx)
                Float::class -> getFloat(idx)
                else -> error("Unsupported list element type: $elementType at column index $idx")
            }
            list.add(value)
        }
        return list
    }
    
    
    /**
     * Returns the value at [index] using [getter], or null if the Cursor value is null.
     */
    inline fun <T> Cursor.getNullable(                                             index: Int,
                                                                                  getter: (Int)->T,
    ): T? = if (isNull(index))
                null
            else
                getter(index)
    
}