package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.core.WhereBuilder

/** Returns the number of rows matching the query conditions. */

fun SQLiteDatabase.getRowCount(                               tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {}
    
): Int = getCursor(tableName, arrayOf("1"), joins, where).use { it.count }





/** Returns true if at least one row matches the query conditions. */

fun SQLiteDatabase.hasRows(                                   tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {}
    
): Boolean = getCursor(tableName, arrayOf("1"), joins, where, limit = 1).use { it.moveToFirst() }

