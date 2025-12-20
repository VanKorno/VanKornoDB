// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get.noty

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.dsl.getQuery

fun SQLiteDatabase.getCursorNoty(                                 table: String,
                                                                columns: Array<out String>,
                                                                  where: WhereDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, columns) { this.where = where }
    return rawQuery(query, args)
}



fun SQLiteDatabase.getCursorProNoty(                                      table: String,
                                                                        columns: Array<out String>,
                                                                      fullDsl: FullDsl.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, columns, fullDsl)
    return rawQuery(query, args)
}
