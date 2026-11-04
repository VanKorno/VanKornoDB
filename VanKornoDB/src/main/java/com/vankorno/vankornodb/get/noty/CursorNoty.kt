/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.noty

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.dsl.getQuery


/**
 * Same as [com.vankorno.vankornodb.get.getCursor], but takes column names as strings
 * without checking their type (not type-safe - NoTy).
 */
fun SQLiteDatabase.getCursorNoty(                                     table: String,
                                                                    columns: Array<out String>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, columns, where, orderBy, extra)
    return rawQuery(query, args)
}

