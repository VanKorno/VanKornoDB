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
            val type = param.type.jvmErasure
            
            val value = when (type) {
                Int::class       -> getInt(index)
                String::class    -> getString(index)
                Boolean::class   -> getBool(index)
                Long::class      -> getLong(index)
                Float::class     -> getFloat(index)
                ByteArray::class -> getBlob(index)
                
                else -> when { // Nullable support
                    type == Int::class && param.type.isMarkedNullable       -> getNullable(index) { getInt(it) }
                    type == String::class && param.type.isMarkedNullable    -> getNullable(index) { getString(it) }
                    type == Boolean::class && param.type.isMarkedNullable   -> getNullable(index) { getBool(it) }
                    type == Long::class && param.type.isMarkedNullable      -> getNullable(index) { getLong(it) }
                    type == Float::class && param.type.isMarkedNullable     -> getNullable(index) { getFloat(it) }
                    type == ByteArray::class && param.type.isMarkedNullable -> getNullable(index) { getBlob(it) }
                    
                    else -> error("Unsupported parameter type: $type for $colName")
                }
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








