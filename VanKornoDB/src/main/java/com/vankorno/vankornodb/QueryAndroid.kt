package com.vankorno.vankornodb

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.CondBuilder
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.core.getQuery

fun SQLiteDatabase.query(                                   table: String,
                                                          columns: Array<out String> = arrayOf("*"),
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