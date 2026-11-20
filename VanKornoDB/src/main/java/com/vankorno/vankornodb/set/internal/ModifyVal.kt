/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.internal

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.EntityTable
import com.vankorno.vankornodb.dbManagement.data.NumberColumn
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

internal fun <W> SQLiteDatabase.baseAddTo(               addend: Number,
                                                          table: EntityTable<*, W>,
                                                         column: ColumnSelector<W, NumberColumn<*>>,
                                                          where: W.()->Unit,
) {
    if (addend.toDouble() == 0.0) return //\/\/\/\/\/\
    
    modifyColumn(table, column, where, listOf(addend)) { "$it + ?" }
}


internal fun <W> SQLiteDatabase.modifyColumn(              table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, TypedColumn<*>>,
                                                           where: W.()->Unit,
                                                            args: List<Any>,
                                                      expression: (String)->String,
) {
    val name = table.columnSql(column)
    val predicate = table.where(where)
    val filter = if (predicate.query.isEmpty()) "" else " WHERE ${predicate.query}"
    execSQL("UPDATE ${table.name} SET $name = ${expression(name)}$filter", (args + predicate.args).toTypedArray())
}
