package com.vankorno.vankornodb.getSet

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.RowID
import com.vankorno.vankornodb.core.DbConstants.groupBy
import com.vankorno.vankornodb.core.DbConstants.having
import com.vankorno.vankornodb.core.DbConstants.limit
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.core.WhereBuilder

/** Returns the number of rows matching the query conditions. */

fun SQLiteDatabase.getRowCount(                                    table: String,
                                                                   joins: JoinBuilder.()->Unit = {},
                                                                   where: WhereBuilder.()->Unit = {},
                                                                 groupBy: String = ""

): Int = getCursor(table, arrayOf("1"), joins, where, groupBy).use { it.count }



/** Returns true if at least one row matches the query conditions. */

fun SQLiteDatabase.hasRows(                                        table: String,
                                                                   joins: JoinBuilder.()->Unit = {},
                                                                   where: WhereBuilder.()->Unit = {}

): Boolean = getCursor(table, arrayOf("1"), joins, where, limit = 1).use { it.moveToFirst() }

