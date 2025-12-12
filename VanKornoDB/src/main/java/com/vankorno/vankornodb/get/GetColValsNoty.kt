package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.get.raw.getTypedValAt

/**
 * For internal use with the type-safe getColVals... functions.
 */
internal inline fun <R> SQLiteDatabase.getColValsPro(                table: String,
                                                                    column: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
                                                crossinline getCursorValue: (Cursor)->R,
): List<R> = getCursorPro(table, column) {
    applyOpts(queryOpts)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(getCursorValue(cursor))
            } while (cursor.moveToNext())
        }
    }
}

internal inline fun <R> SQLiteDatabase.getColVals(                table: String,
                                                                 column: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
                                             crossinline getCursorValue: (Cursor)->R,
): List<R> = getCursorPro(table, column) {
    this.where = where
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(getCursorValue(cursor))
            } while (cursor.moveToNext())
        }
    }
}


/**
 * Retrieves a list of values from a single column, cast to the specified type [V].
 */
inline fun <reified V> SQLiteDatabase.getColValsNoty(                table: String,
                                                                columnName: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
): List<V> = getCursorPro(table, columnName) {
    applyOpts(queryOpts)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(cursor.getTypedValAt<V>(0))
            } while (cursor.moveToNext())
        }
    }
}



