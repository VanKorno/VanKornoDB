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
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, columns, orderBy, extra, where)
    return rawQuery(query, args)
}



/**
 * Executes a query and manages the lifecycle of the resulting [Cursor] within [block].
 */
inline fun <R> SQLiteDatabase.useCursorNoty(                          table: String,
                                                                    columns: Array<out String>,
                                                           noinline orderBy: OrderDsl.()->Unit = {},
                                                             noinline extra: ExtraDsl.()->Unit = {},
                                                             noinline where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->R,
): R = getCursorNoty(table, columns, orderBy, extra, where).use(block)







