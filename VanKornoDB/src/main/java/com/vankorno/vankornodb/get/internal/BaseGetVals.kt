/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.internal

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.get.noty.getCursorProNoty


internal fun <T> SQLiteDatabase.baseGetVals(                          table: String,
                                                                    columns: Array<out String>,
                                                                      where: WhereDsl.()->Unit = {},
                                                             getCursorValue: (Cursor, Int)->T,
): List<List<T>> = baseGetValsPro(table, columns, dsl = { this.where = where }, getCursorValue)



internal fun <T> SQLiteDatabase.baseGetValsPro(                        table: String,
                                                                     columns: Array<out String>,
                                                                         dsl: FullDsl.()->Unit = {},
                                                              getCursorValue: (Cursor, Int)->T,
): List<List<T>> = getCursorProNoty(table, columns) {
    applyDsl(dsl)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(List(columns.size) { idx -> getCursorValue(cursor, idx) })
            } while (cursor.moveToNext())
        }
    }
}