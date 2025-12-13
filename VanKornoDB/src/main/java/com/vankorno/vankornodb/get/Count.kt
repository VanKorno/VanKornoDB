package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder

/** Returns the number of rows matching the query conditions. */

fun SQLiteDatabase.getRowCount(                                   table: String,
                                                                  where: WhereBuilder.()->Unit = {},
): Int = getCursorPro(table, "1") {
    applyOpts(where = where)
}.use { it.count }


/** Returns the number of rows matching the advanced query conditions. */

fun SQLiteDatabase.getRowCountPro(                                        table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
): Int = getCursorPro(table, "1") {
    applyOpts(queryOpts)
}.use { it.count }





/** Returns true if at least one row matches the query conditions. */

fun SQLiteDatabase.hasRows(                                       table: String,
                                                                  where: WhereBuilder.()->Unit = {},
): Boolean = getCursorPro(table, "1") {
    this.where = where
    limit = 1
}.use { it.moveToFirst() }


/** Returns true if at least one row matches the advanced query conditions. */

fun SQLiteDatabase.hasRowsPro(                                            table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
): Boolean = getCursorPro(table, "1") {
    applyOpts(queryOpts)
    limit = 1
}.use { it.moveToFirst() }

















