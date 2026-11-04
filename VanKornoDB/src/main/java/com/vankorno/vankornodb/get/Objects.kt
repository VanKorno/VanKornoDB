/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase
import com.vankorno.vankornodb.misc.data.SharedCol.cRowId

/**
 * Retrieves a list of objects mapped from the given columns.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjects(         tableInfo: TableInfoBase<T>,
                                                          orderBy: OrderDsl.()->Unit = { cRowId() },
                                                            extra: ExtraDsl.()->Unit = {},
                                                            where: WhereDsl.()->Unit = {},
): List<T> = useCursor(tableInfo.name, orderBy, extra, where) { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(cursor.toEntity(tableInfo.schema))
            } while (cursor.moveToNext())
        }
    }
}





/** 
 * Retrieves a map of objects from the given table.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjMap(          tableInfo: TableInfoBase<T>,
                                                          orderBy: OrderDsl.()->Unit = { cRowId() },
                                                            extra: ExtraDsl.()->Unit = {},
                                                            where: WhereDsl.()->Unit = {},
): Map<Long, T> = useCursor(tableInfo.name, orderBy, extra, where) { cursor ->
    buildMap {
        if (cursor.moveToFirst()) {
            val idColIdx = cursor.getColumnIndexOrThrow("id")
            do {
                val entity = cursor.toEntity(tableInfo.schema)
                put(cursor.getLong(idColIdx), entity)
            } while (cursor.moveToNext())
        }
    }
}












