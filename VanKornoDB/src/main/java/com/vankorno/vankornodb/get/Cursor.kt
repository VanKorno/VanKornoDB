/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

/**
 * Queries the database, lets you use [Cursor].
 * Selects all columns if [columns] not passed.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.useCursor(
                                                           table: QueryTable<*, W, O, X>,
                                                  vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
                                                           block: (Cursor)->Unit,
) = write("useCursor") { db ->
    db.useCursor(table, *columns, orderBy = orderBy, extra = extra, where = where, block = block)
}
/**
 * Queries the database, lets you use [Cursor].
 * Selects all columns if [columns] not passed.
 */
inline fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> SQLiteDatabase.useCursor(
                                                           table: QueryTable<*, W, O, X>,
                                                  vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                noinline orderBy: O.()->Unit = {},
                                                  noinline extra: X.()->Unit = {},
                                                  noinline where: W.()->Unit = {},
                                                           block: (Cursor)->R,
): R = getCursor(
    table, *columns, orderBy = orderBy, extra = extra, where = where
).use(block)



/**
 * Queries the database, lets you use [Cursor], and returns the result of that cursor usage.
 * Selects all columns if [columns] not passed.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> Dbh.getFromCursor(
                                                           table: QueryTable<*, W, O, X>,
                                                         default: R,
                                                  vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
                                                           block: (Cursor)->R,
): R = readWrite(default, "getFromCursor") { db ->
    db.useCursor(table, *columns, orderBy = orderBy, extra = extra, where = where, block = block)
}
/**
 * Queries the database and returns a [Cursor] over the result set.
 * Selects all columns if [columns] not passed.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getCursor(
                                                           table: QueryTable<*, W, O, X>,
                                                  vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): Cursor {
    val (query, args) = table.query(columns, orderBy, extra, where)
    return rawQuery(query, args)
}
