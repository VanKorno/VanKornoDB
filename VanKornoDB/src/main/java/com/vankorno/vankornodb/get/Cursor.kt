/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.dsl.getQuery
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.misc.getColNames

fun SQLiteDatabase.getCursor(                                         table: String,
                                                                      where: WhereDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, arrayOf("*")) { this.where = where }
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursor(                                      table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                   where: WhereDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, getColNames(columns)) { this.where = where }
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursor(                                         table: String,
                                                                     column: TypedColumn<*>,
                                                                      where: WhereDsl.()->Unit = {},
): Cursor {
    val (query, args) = getQuery(table, arrayOf(column.name)) { this.where = where }
    return rawQuery(query, args)
}





fun SQLiteDatabase.getCursorPro(                                            table: String,
                                                                              dsl: FullDsl.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, arrayOf("*"), dsl)
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursorPro(                                   table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                     dsl: FullDsl.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, getColNames(columns), dsl)
    return rawQuery(query, args)
}


fun SQLiteDatabase.getCursorPro(                                            table: String,
                                                                           column: TypedColumn<*>,
                                                                              dsl: FullDsl.()->Unit,
): Cursor {
    val (query, args) = getQuery(table, arrayOf(column.name), dsl)
    return rawQuery(query, args)
}





inline fun <R> SQLiteDatabase.useCursor(                              table: String,
                                                             noinline where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->R,
): R = getCursor(table, where).use(block)


inline fun <R> SQLiteDatabase.useCursor(                           table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                          noinline where: WhereDsl.()->Unit = {},
                                                                   block: (Cursor)->R,
): R = getCursor(table, columns, where).use(block)


inline fun <R> SQLiteDatabase.useCursor(                              table: String,
                                                                     column: TypedColumn<*>,
                                                             noinline where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->R,
): R = getCursor(table, column, where).use(block)





inline fun <R> SQLiteDatabase.useCursorPro(                                 table: String,
                                                                     noinline dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->R,
): R = getCursorPro(table, dsl).use(block)


inline fun <R> SQLiteDatabase.useCursorPro(                        table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                            noinline dsl: FullDsl.()->Unit,
                                                                   block: (Cursor)->R,
): R = getCursorPro(table, columns, dsl).use(block)


inline fun <R> SQLiteDatabase.useCursorPro(                                 table: String,
                                                                           column: TypedColumn<*>,
                                                                     noinline dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->R,
): R = getCursorPro(table, column, dsl).use(block)








