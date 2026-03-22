/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.internal

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.WhereDsl


internal inline fun <T> SQLiteDatabase.getColVals(                    table: String,
                                                                     column: String,
                                                             noinline where: WhereDsl.()->Unit = {},
                                                 crossinline getCursorValue: (Cursor)->T,
): List<T> = getColValsPro(table, column, { this.where = where }, getCursorValue)


/**
 * For internal use with the type-safe getColVals... functions.
 */
internal inline fun <T> SQLiteDatabase.getColValsPro(                  table: String,
                                                                      column: String,
                                                                noinline dsl: FullDsl.()->Unit = {},
                                                  crossinline getCursorValue: (Cursor)->T,
): List<T> = baseGetValsPro(table, arrayOf(column), dsl) { cursor, _ ->
    getCursorValue(cursor)
}.flatten()







