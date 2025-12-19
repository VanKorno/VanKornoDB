// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get.internal

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder


internal inline fun <T> SQLiteDatabase.getColVals(                table: String,
                                                                 column: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
                                             crossinline getCursorValue: (Cursor)->T,
): List<T> = getColValsPro(table, column, { this.where = where }, getCursorValue)


/**
 * For internal use with the type-safe getColVals... functions.
 */
internal inline fun <T> SQLiteDatabase.getColValsPro(                table: String,
                                                                    column: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
                                                crossinline getCursorValue: (Cursor)->T,
): List<T> = baseGetValsPro(table, arrayOf(column), queryOpts) { cursor, _ ->
    getCursorValue(cursor)
}.flatten()







