/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.dsl.getQuery
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.misc.getColNames

/**
 * Queries the database and returns a [Cursor] over the result set.
 * Selects all columns (`*`).
 */
fun SQLiteDatabase.getCursor(                                         table: String,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, arrayOf("*"), where, orderBy, extra)
    return rawQuery(query, args)
}

/**
 * Queries the database and returns a [Cursor] over the result set.
 * Selects specified [columns].
 */
fun SQLiteDatabase.getCursor(                                      table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                   where: WhereDsl.()->Unit = {},
                                                                 orderBy: OrderDsl.()->Unit = {},
                                                                   extra: ExtraDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, getColNames(columns), where, orderBy, extra)
    return rawQuery(query, args)
}

/**
 * Queries the database and returns a [Cursor] over the result set.
 * Selects a single specified [column].
 */
fun SQLiteDatabase.getCursor(                                         table: String,
                                                                     column: TypedColumn<*>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, arrayOf(column.name), where, orderBy, extra)
    return rawQuery(query, args)
}





/**
 * Executes a query and manages the lifecycle of the resulting [Cursor] within [block].
 * Selects all columns (`*`).
 */
inline fun <R> SQLiteDatabase.useCursor(                              table: String,
                                                             noinline where: WhereDsl.()->Unit = {},
                                                           noinline orderBy: OrderDsl.()->Unit = {},
                                                             noinline extra: ExtraDsl.()->Unit = {},
                                                                      block: (Cursor)->R,
): R = getCursor(table, where, orderBy, extra).use(block)

/**
 * Executes a query and manages the lifecycle of the resulting [Cursor] within [block].
 * Selects specified [columns].
 */
inline fun <R> SQLiteDatabase.useCursor(                           table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                          noinline where: WhereDsl.()->Unit = {},
                                                        noinline orderBy: OrderDsl.()->Unit = {},
                                                          noinline extra: ExtraDsl.()->Unit = {},
                                                                   block: (Cursor)->R,
): R = getCursor(table, columns, where, orderBy, extra).use(block)

/**
 * Executes a query and manages the lifecycle of the resulting [Cursor] within [block].
 * Selects a single specified [column].
 */
inline fun <R> SQLiteDatabase.useCursor(                              table: String,
                                                                     column: TypedColumn<*>,
                                                             noinline where: WhereDsl.()->Unit = {},
                                                           noinline orderBy: OrderDsl.()->Unit = {},
                                                             noinline extra: ExtraDsl.()->Unit = {},
                                                                      block: (Cursor)->R,
): R = getCursor(table, column, where, orderBy, extra).use(block)







