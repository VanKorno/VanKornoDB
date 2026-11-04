/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.internal

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl

/**
 * For internal use with the type-safe getColVals... functions.
 */
internal inline fun <T> SQLiteDatabase.getColVals(                    table: String,
                                                                     column: String,
                                                             noinline where: WhereDsl.()->Unit = {},
                                                           noinline orderBy: OrderDsl.()->Unit = {},
                                                             noinline extra: ExtraDsl.()->Unit = {},
                                                 crossinline getCursorValue: (Cursor)->T,
): List<T> = baseGetVals(table, arrayOf(column), where, orderBy, extra) { cursor, _ ->
    getCursorValue(cursor)
}.flatten()







