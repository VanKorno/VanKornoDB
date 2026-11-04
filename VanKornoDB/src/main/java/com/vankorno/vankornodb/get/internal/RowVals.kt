/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.internal

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl

/**
 * For internal use with the type-safe getRowVals... functions.
 */
internal inline fun <T> SQLiteDatabase.getRowVals(                    table: String,
                                                                    columns: Array<out String>,
                                                           noinline orderBy: OrderDsl.()->Unit = {},
                                                             noinline extra: ExtraDsl.()->Unit = {},
                                                             noinline where: WhereDsl.()->Unit = {},
                                                 crossinline getCursorValue: (Cursor, Int)->T,
): List<T> = baseGetVals(table, columns, orderBy, where = where, 
    extra = {
        applyDsl(extra)
        limit = 1
    }
) { cursor, col ->
    getCursorValue(cursor, col)
}.flatten()







