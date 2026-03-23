/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase

/** 
 * Retrieves a list of objects mapped from the given columns.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjectsPro(                      tableInfo: TableInfoBase<T>,
                                                                              dsl: FullDsl.()->Unit,
): List<T> = getCursorPro(tableInfo.name) {
    applyDsl(dsl)
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
fun <T : BaseEntity> SQLiteDatabase.getObjMapPro(                       tableInfo: TableInfoBase<T>,
                                                                              dsl: FullDsl.()->Unit,
): Map<Long, T> = getCursorPro(tableInfo.name) {
    applyDsl(dsl)
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












