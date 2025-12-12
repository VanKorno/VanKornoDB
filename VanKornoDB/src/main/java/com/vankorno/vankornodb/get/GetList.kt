package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.dbManagement.data.IntCol

/**
 * Retrieves a list of Ints from a single column.
 */
fun SQLiteDatabase.getColInts(                                       table: String,
                                                                    column: IntCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Int> = getCursor(table, column.name) {
    applyOpts(queryOpts)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(cursor.getInt(0))
            } while (cursor.moveToNext())
        }
    }
}