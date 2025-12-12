package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.core.queryBuilder.getQuery

fun SQLiteDatabase.getCursor(                                        table: String,
                                                                   columns: Array<out String>,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, columns, queryOpts)
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursor(                                        table: String,
                                                                    column: String = "*",
                                                                 queryOpts: QueryOpts.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, arrayOf(column), queryOpts)
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursor(                                        table: String,
                                                                 queryOpts: QueryOpts.()->Unit = {},
                                                            vararg columns: String,
): Cursor {
    val (query, args) = getQuery(table, columns, queryOpts)
    return rawQuery(query, args)
}
