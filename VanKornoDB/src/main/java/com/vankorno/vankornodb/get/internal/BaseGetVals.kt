// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get.internal

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.get.noty.getCursorProNoty


internal fun <T> SQLiteDatabase.baseGetVals(                      table: String,
                                                                columns: Array<out String>,
                                                                  where: WhereDsl.()->Unit = {},
                                                         getCursorValue: (Cursor, Int)->T,
): List<List<T>> = baseGetValsPro(table, columns, fullDsl = { this.where = where }, getCursorValue)



internal fun <T> SQLiteDatabase.baseGetValsPro(                      table: String,
                                                                   columns: Array<out String>,
                                                                 fullDsl: FullDsl.()->Unit = {},
                                                            getCursorValue: (Cursor, Int)->T,
): List<List<T>> = getCursorProNoty(table, columns) {
    applyDsl(fullDsl)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(List(columns.size) { idx -> getCursorValue(cursor, idx) })
            } while (cursor.moveToNext())
        }
    }
}