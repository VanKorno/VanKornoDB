/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase

/**
 * Retrieves a list of objects mapped from the given columns.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjects(                   tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<T> = getCursor(tableInfo.name, where, orderBy) {
    applyDsl(extra)
}.use { cursor ->
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
fun <T : BaseEntity> SQLiteDatabase.getObjMap(                    tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): Map<Long, T> = getCursor(tableInfo.name, where, orderBy) {
    applyDsl(extra)
}.use { cursor ->
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












