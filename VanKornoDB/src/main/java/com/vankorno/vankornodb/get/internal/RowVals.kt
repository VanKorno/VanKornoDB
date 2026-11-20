/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.internal

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.dsl.DbhColumns
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.get.getCursor

/**
 * Reads the first matching row in projection order.
 */
internal fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> SQLiteDatabase.getRowVals(
                                                        columns: DbhColumns<W, O, X, TypedColumn<R>>,
                                                        orderBy: O.()->Unit = {},
                                                          extra: X.()->Unit = {},
                                                          where: W.()->Unit = {},
                                                 getCursorValue: (Cursor, Int)->R,
): List<R> = getCursor(
    columns, orderBy = orderBy, where = where,
    extra = {
        extra()
        limit = 1
    },
).use { cursor ->
    if (cursor.moveToFirst())
        List(cursor.columnCount) { idx -> getCursorValue(cursor, idx) }
    else
        emptyList()
}
