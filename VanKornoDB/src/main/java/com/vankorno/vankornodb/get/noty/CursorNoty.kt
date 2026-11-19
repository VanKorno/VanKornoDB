/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.noty

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.dsl.getQuery


/**
 * Same as [com.vankorno.vankornodb.get.getCursor], but takes column names as strings
 * without checking their type (not type-safe - NoTy).
 */
fun SQLiteDatabase.getCursorNoty(                                     table: String,
                                                                    columns: Array<out String>,
                                                                    orderBy: O.()->Unit = {},
                                                                      extra: X.()->Unit = {},
                                                                      where: W.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, columns, orderBy, extra, where)
    return rawQuery(query, args)
}



/**
 * Executes a query and manages the lifecycle of the resulting [Cursor] within [block].
 */
inline fun <R> SQLiteDatabase.useCursorNoty(                          table: String,
                                                                    columns: Array<out String>,
                                                           noinline orderBy: O.()->Unit = {},
                                                             noinline extra: X.()->Unit = {},
                                                             noinline where: W.()->Unit = {},
                                                                      block: (Cursor)->R,
): R = getCursorNoty(table, columns, orderBy, extra, where).use(block)







