// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.get.noty.getCursorProNoty
import com.vankorno.vankornodb.misc.columns

/** Returns the number of rows matching the query conditions. */

fun SQLiteDatabase.getRowCount(                                   table: String,
                                                                  where: WhereBuilder.()->Unit = {},
): Int = getCursorProNoty(table, columns("1")) {
    applyOpts(where = where)
}.use { it.count }


/** Returns the number of rows matching the advanced query conditions. */

fun SQLiteDatabase.getRowCountPro(                                        table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
): Int = getCursorProNoty(table, columns("1")) {
    applyOpts(queryOpts)
}.use { it.count }





/** Returns true if at least one row matches the query conditions. */

fun SQLiteDatabase.hasRows(                                       table: String,
                                                                  where: WhereBuilder.()->Unit = {},
): Boolean = getCursorProNoty(table, columns("1")) {
    this.where = where
    limit = 1
}.use { it.moveToFirst() }


/** Returns true if at least one row matches the advanced query conditions. */

fun SQLiteDatabase.hasRowsPro(                                            table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
): Boolean = getCursorProNoty(table, columns("1")) {
    applyOpts(queryOpts)
    limit = 1
}.use { it.moveToFirst() }

















