/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ColumnSelector
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.QueryTable
import com.vankorno.vankornodb.dbManagement.data.TypedColumn



fun <W, O : OrderDsl, X : ExtraDsl> Dbh.useCursor(         table: QueryTable<*, W, O, X>,
                                                  vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
                                                           block: (Cursor)->Unit,
) = write("useCursor") { db ->
    db.useCursor(table, *columns, orderBy = orderBy, extra = extra, where = where, block = block)
}

inline fun <W, O : OrderDsl, X : ExtraDsl, R> SQLiteDatabase.useCursor(
                                                           table: QueryTable<*, W, O, X>,
                                                  vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                noinline orderBy: O.()->Unit = {},
                                                  noinline extra: X.()->Unit = {},
                                                  noinline where: W.()->Unit = {},
                                                           block: (Cursor)->R,
): R = getCursor(
    table, *columns, orderBy = orderBy, extra = extra, where = where
).use(block)




fun <W, O : OrderDsl, X : ExtraDsl> SQLiteDatabase.getCursor(
                                                           table: QueryTable<*, W, O, X>,
                                                  vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): Cursor {
    val (query, args) = table.query(columns, orderBy, extra, where)
    return rawQuery(query, args)
}



