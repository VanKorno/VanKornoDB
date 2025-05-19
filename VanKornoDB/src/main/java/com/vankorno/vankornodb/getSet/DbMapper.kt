package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.Cursor
import com.vankorno.vankornodb.getBool
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

/** 
 * Ignores parameters ending with "List" or "Array" 
**/

inline fun <reified T : Any> Cursor.mapToEntity(): T {
    val constructor = T::class.primaryConstructor
        ?: error("Class ${T::class.simpleName} must have a primary constructor")
    
    val args = constructor.parameters.mapNotNull  { param ->
        val colName = param.name ?: error("Constructor param must have a name")
        
        if (colName.endsWith("List") || colName.endsWith("Array")) {
            null
        } else {
            val index = getColumnIndexOrThrow(colName)
            val isNullable = param.type.isMarkedNullable
            val type = param.type.jvmErasure
            
            val value = when (type) {
                Int::class       -> if (isNullable) getNullable(index) { getInt(it) } else getInt(index)
                String::class    -> if (isNullable) getNullable(index) { getString(it) } else getString(index)
                Boolean::class   -> if (isNullable) getNullable(index) { getBool(it) } else getBool(index)
                Long::class      -> if (isNullable) getNullable(index) { getLong(it) } else getLong(index)
                Float::class     -> if (isNullable) getNullable(index) { getFloat(it) } else getFloat(index)
                ByteArray::class -> if (isNullable) getNullable(index) { getBlob(it) } else getBlob(index)
            
                else -> error("Unsupported parameter type: $type for $colName")
            }
            param to value
        }
    }.toMap()
    return constructor.callBy(args)
}


inline fun <T> Cursor.getNullable(                                                 index: Int,
                                                                                  getter: (Int)->T
): T? = if (isNull(index))
            null
        else
            getter(index)








