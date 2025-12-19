// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get.noty

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.queryBuilder.getQuery

fun SQLiteDatabase.getCursorNoty(                                 table: String,
                                                                columns: Array<out String>,
                                                                  where: WhereBuilder.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, columns) { this.where = where }
    return rawQuery(query, args)
}



fun SQLiteDatabase.getCursorProNoty(                                      table: String,
                                                                        columns: Array<out String>,
                                                                      queryOpts: QueryOpts.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, columns, queryOpts)
    return rawQuery(query, args)
}
