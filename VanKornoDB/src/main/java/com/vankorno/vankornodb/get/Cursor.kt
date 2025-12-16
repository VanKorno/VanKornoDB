package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.queryBuilder.getQuery
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.misc.getColNames

fun SQLiteDatabase.getCursor(                                     table: String,
                                                                  where: WhereBuilder.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, arrayOf("*")) { this.where = where }
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursor(                                     table: String,
                                                                columns: Array<TypedColumn<*>>,
                                                                  where: WhereBuilder.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, getColNames(columns)) { this.where = where }
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursor(                                     table: String,
                                                                 column: TypedColumn<*>,
                                                                  where: WhereBuilder.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, arrayOf(column.name)) { this.where = where }
    return rawQuery(query, args)
}





fun SQLiteDatabase.getCursorPro(                                          table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, arrayOf("*"), queryOpts)
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursorPro(                                       table: String,
                                                                     columns: Array<TypedColumn<*>>,
                                                                   queryOpts: QueryOpts.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, getColNames(columns), queryOpts)
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursorPro(                                          table: String,
                                                                         column: TypedColumn<*>,
                                                                      queryOpts: QueryOpts.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, arrayOf(column.name), queryOpts)
    return rawQuery(query, args)
}


