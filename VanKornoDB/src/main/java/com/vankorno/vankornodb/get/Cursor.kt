// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.dsl.getQuery
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.misc.getColNames

fun SQLiteDatabase.getCursor(                                     table: String,
                                                                  where: WhereDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, arrayOf("*")) { this.where = where }
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursor(                                     table: String,
                                                                columns: Array<out TypedColumn<*>>,
                                                                  where: WhereDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, getColNames(columns)) { this.where = where }
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursor(                                     table: String,
                                                                 column: TypedColumn<*>,
                                                                  where: WhereDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, arrayOf(column.name)) { this.where = where }
    return rawQuery(query, args)
}





fun SQLiteDatabase.getCursorPro(                                          table: String,
                                                                      fullDsl: FullDsl.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, arrayOf("*"), fullDsl)
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursorPro(                                   table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                               fullDsl: FullDsl.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, getColNames(columns), fullDsl)
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursorPro(                                          table: String,
                                                                         column: TypedColumn<*>,
                                                                      fullDsl: FullDsl.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, arrayOf(column.name), fullDsl)
    return rawQuery(query, args)
}


