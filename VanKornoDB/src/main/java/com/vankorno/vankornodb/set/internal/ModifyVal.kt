/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.internal

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.dsl.DbColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.NumberColumn
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

internal fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.baseAddTo(
                                                         addend: Number,
                                                         column: DbColumn<W, O, X, NumberColumn<*>>,
                                                          where: W.()->Unit,
) {
    if (addend.toDouble() == 0.0) return //\/\/\/\/\/\
    
    modifyColumn(column, where, listOf(addend)) { "$it + ?" }
}


internal fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.modifyColumn(
                                                          column: DbColumn<W, O, X, TypedColumn<*>>,
                                                           where: W.()->Unit,
                                                            args: List<Any>,
                                                      expression: (String)->String,
) {
    val table = column.table
    val name = table.columnSql(column.selector)
    val predicate = table.where(where)
    
    val filter = if (predicate.query.isEmpty())
                    ""
                else
                    " WHERE ${predicate.query}"
    
    execSQL(
        "UPDATE ${table.name} SET $name = ${expression(name)}$filter",
        
        (args + predicate.args).toTypedArray()
    )
}
