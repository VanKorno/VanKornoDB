package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.Cursor
import com.vankorno.vankornodb.dbManagement.data.AutoId
import com.vankorno.vankornodb.dbManagement.data.AutoIdNullable
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BlobColNullable
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.BoolColNullable
import com.vankorno.vankornodb.dbManagement.data.ColumnDef
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.FloatColNullable
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.IntColNullable
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.LongColNullable
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.dbManagement.data.StrColNullable
import com.vankorno.vankornodb.getBool
import kotlin.collections.indexOfFirst
import kotlin.reflect.full.primaryConstructor

/** 
 * Ignores parameters ending with "List" or "Array" 
**/

inline fun <reified T : Any> Cursor.mapToEntity(                        entity: List<ColumnDef>
): T {
    val constructor = T::class.primaryConstructor
        ?: error("Class ${T::class.simpleName} must have a primary constructor")

    val args = constructor.parameters.mapNotNull  { param ->
        val colName = param.name ?: error("Constructor param must have a name")
        
        if (colName.endsWith("List") || colName.endsWith("Array")) {
            null
        } else {
            val columnIndex = entity.indexOfFirst { it.name == colName }
            
            if (columnIndex == -1) error("No column found for parameter: $colName")
    
            val type = entity[columnIndex].type
            val value = when (type) {
                AutoId, IntCol       -> getInt(columnIndex)
                StrCol               -> getString(columnIndex)
                BoolCol              -> getBool(columnIndex)
                LongCol              -> getLong(columnIndex)
                FloatCol             -> getFloat(columnIndex)
                BlobCol              -> getBlob(columnIndex)
                AutoIdNullable,
                IntColNullable       -> getNullable(columnIndex) { getInt(it) }
                StrColNullable       -> getNullable(columnIndex) { getString(it) }
                BoolColNullable      -> getNullable(columnIndex) { getBool(it) }
                LongColNullable      -> getNullable(columnIndex) { getLong(it) }
                FloatColNullable     -> getNullable(columnIndex) { getFloat(it) }
                BlobColNullable      -> getNullable(columnIndex) { getBlob(it) }
                else                 -> error("Unsupported column type: $type")
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
