/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.TableColumns

/**
 * Queries the database, lets you use [Cursor].
 * Selects all columns.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.useCursor(           table: DslTable<*, W, O, X>,
                                                                    orderBy: O.()->Unit = {},
                                                                      extra: X.()->Unit = {},
                                                                      where: W.()->Unit = {},
                                                                      block: (Cursor)->Unit,
) = write("useCursor") { db ->
    db.useCursor(table, orderBy = orderBy, extra = extra, where = where, block = block)
}
/**
 * Queries the database, lets you use [Cursor].
 * Selects all columns.
 */
inline fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> SQLiteDatabase.useCursor(
                                                                      table: DslTable<*, W, O, X>,
                                                           noinline orderBy: O.()->Unit = {},
                                                             noinline extra: X.()->Unit = {},
                                                             noinline where: W.()->Unit = {},
                                                                      block: (Cursor)->R,
): R = getCursor(
    table, orderBy = orderBy, extra = extra, where = where
).use(block)


fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.useCursor(         columns: TableColumns<W, O, X, *>,
                                                                    orderBy: O.()->Unit = {},
                                                                      extra: X.()->Unit = {},
                                                                      where: W.()->Unit = {},
                                                                      block: (Cursor)->Unit,
) = write("useCursor") { db ->
    db.useCursor(columns, orderBy, extra, where, block)
}

inline fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> SQLiteDatabase.useCursor(
                                                                    columns: TableColumns<W, O, X, *>,
                                                           noinline orderBy: O.()->Unit = {},
                                                             noinline extra: X.()->Unit = {},
                                                             noinline where: W.()->Unit = {},
                                                                      block: (Cursor)->R,
): R = getCursor(columns, orderBy, extra, where).use(block)




/**
 * Queries the database, lets you use [Cursor], and returns the result of that cursor usage.
 * Selects all columns.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> Dbh.getFromCursor(    table: DslTable<*, W, O, X>,
                                                                    default: R,
                                                                    orderBy: O.()->Unit = {},
                                                                      extra: X.()->Unit = {},
                                                                      where: W.()->Unit = {},
                                                                      block: (Cursor)->R,
): R = readWrite(default, "getFromCursor") { db ->
    db.useCursor(table, orderBy = orderBy, extra = extra, where = where, block = block)
}
/**
 * Queries the database and returns a [Cursor] over the result set.
 * Selects all columns.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getCursor(
                                                                      table: DslTable<*, W, O, X>,
                                                                    orderBy: O.()->Unit = {},
                                                                      extra: X.()->Unit = {},
                                                                      where: W.()->Unit = {},
): Cursor {
    val (query, args) = table.query(orderBy = orderBy, extra = extra, where = where)
    return rawQuery(query, args)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> Dbh.getFromCursor(
                                                                    columns: TableColumns<W, O, X, *>,
                                                                    default: R,
                                                                    orderBy: O.()->Unit = {},
                                                                      extra: X.()->Unit = {},
                                                                      where: W.()->Unit = {},
                                                                      block: (Cursor)->R,
): R = readWrite(default, "getFromCursor") { db ->
    db.useCursor(columns, orderBy, extra, where, block)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getCursor(
                                                                    columns: TableColumns<W, O, X, *>,
                                                                    orderBy: O.()->Unit = {},
                                                                      extra: X.()->Unit = {},
                                                                      where: W.()->Unit = {},
): Cursor {
    val (query, args) = columns.table.query(columns.selectors, orderBy, extra, where)
    return rawQuery(query, args)
}
