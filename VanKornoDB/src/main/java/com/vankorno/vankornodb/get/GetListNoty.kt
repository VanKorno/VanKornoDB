package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.get.raw.getTypedValAt

/**
 * Retrieves a list of values from a single column, cast to the specified type [V].
 * Supports filtering, sorting, grouping, and pagination.
 */
inline fun <reified V> SQLiteDatabase.getListNoty(                   table: String,
                                                                    column: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
): List<V> = getCursor(table, column) {
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