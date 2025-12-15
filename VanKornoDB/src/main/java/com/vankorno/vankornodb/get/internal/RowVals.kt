package com.vankorno.vankornodb.get.internal
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder

/**
 * For internal use with the type-safe getRowVals... functions.
 */
internal inline fun <T> SQLiteDatabase.getRowVals(                table: String,
                                                                columns: Array<out String>,
                                                         noinline where: WhereBuilder.()->Unit = {},
                                             crossinline getCursorValue: (Cursor, Int)->T,
): List<T> = getRowValsPro(table, columns, { this.where = where }, getCursorValue)


internal inline fun <T> SQLiteDatabase.getRowValsPro(                table: String,
                                                                   columns: Array<out String>,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
                                                crossinline getCursorValue: (Cursor, Int)->T,
): List<T> = baseGetValsPro(table, columns, queryOpts) { cursor, col ->
    getCursorValue(cursor, col)
}.flatten()
