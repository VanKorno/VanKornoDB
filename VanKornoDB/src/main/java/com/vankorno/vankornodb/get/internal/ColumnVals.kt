/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.internal

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.dsl.DbColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

/**
 * For internal use with the type-safe getColVals... functions.
 */
internal fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> SQLiteDatabase.getColVals(
                                                           column: DbColumn<W, O, X, TypedColumn<R>>,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
                                                  getCursorValue: (Cursor)->R,
): List<R> = baseGetVals(
    column, orderBy = orderBy, extra = extra, where = where
) { cursor, _ ->
    getCursorValue(cursor)
}.flatten()







