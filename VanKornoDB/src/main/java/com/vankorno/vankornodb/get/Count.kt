package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.JoinBuilder
import com.vankorno.vankornodb.api.WhereBuilder

/** Returns the number of rows matching the query conditions. */

fun SQLiteDatabase.getRowCount(                                   table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
): Int = getCursorPro(table, arrayOf("1")) {
    applyOpts(joins = joins, where = where)
}.use { it.count }





/** Returns true if at least one row matches the query conditions. */

fun SQLiteDatabase.hasRows(                                       table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
): Boolean = getCursorPro(table, arrayOf("1")) {
    applyOpts(joins = joins, where = where, limit = 1)
}.use { it.moveToFirst() }

