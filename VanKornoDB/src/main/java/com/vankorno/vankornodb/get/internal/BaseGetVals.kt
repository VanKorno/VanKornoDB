/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.internal

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.get.noty.getCursorNoty

/**
 * Base fun for getting multiple values from db
 */
internal fun <T> SQLiteDatabase.baseGetVals(                          table: String,
                                                                    columns: Array<out String>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
                                                             getCursorValue: (Cursor, Int)->T,
): List<List<T>> = getCursorNoty(table, columns, orderBy, extra, where).use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(List(columns.size) { idx -> getCursorValue(cursor, idx) })
            } while (cursor.moveToNext())
        }
    }
}
