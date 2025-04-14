package com.vankorno.vankornodb
// This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
// If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.CondBuilder
import com.vankorno.vankornodb.core.DbConstants.groupBy
import com.vankorno.vankornodb.core.DbConstants.having
import com.vankorno.vankornodb.core.DbConstants.limit
import com.vankorno.vankornodb.core.DbConstants.orderBy
import com.vankorno.vankornodb.core.DbConstants.where
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.core.getQuery

fun SQLiteDatabase.query(                                          table: String,
                                                                 columns: Array<out String>,
                                                                   joins: JoinBuilder.()->Unit = {},
                                                                   where: CondBuilder.()->Unit = {},
                                                                 groupBy: String? = null,
                                                                  having: String? = null,
                                                                 orderBy: String? = null,
                                                                   limit: String? = null,
                                                                  offset: String? = null,
                                                               customEnd: String? = null
): Cursor {
    val (query, args) = getQuery(table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    return rawQuery(query, args)
}


fun SQLiteDatabase.query(                                          table: String,
                                                                  column: String = "*",
                                                                   joins: JoinBuilder.()->Unit = {},
                                                                   where: CondBuilder.()->Unit = {},
                                                                 groupBy: String? = null,
                                                                  having: String? = null,
                                                                 orderBy: String? = null,
                                                                   limit: String? = null,
                                                                  offset: String? = null,
                                                               customEnd: String? = null
): Cursor {
    val (query, args) = getQuery(table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    return rawQuery(query, args)
}