/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase

/** 
 * Retrieves a list of objects mapped from the given columns. 
 * Similar to the reified version but uses explicit KClass parameter.
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
 * Similar to the reified version but uses explicit KClass parameter. 
 */
fun <T : BaseEntity> SQLiteDatabase.getObjMapPro(                       tableInfo: TableInfoBase<T>,
                                                                              dsl: FullDsl.()->Unit,
): Map<Int, T> = getCursorPro(tableInfo.name) {
    applyDsl(dsl)
}.use { cursor ->
    buildMap {
        if (cursor.moveToFirst()) {
            val idColIdx = cursor.getColumnIndexOrThrow("id")
            do {
                val entity = cursor.toEntity(tableInfo.schema)
                put(cursor.getInt(idColIdx), entity)
            } while (cursor.moveToNext())
        }
    }
}












