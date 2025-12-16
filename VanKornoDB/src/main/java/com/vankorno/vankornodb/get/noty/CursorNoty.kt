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
