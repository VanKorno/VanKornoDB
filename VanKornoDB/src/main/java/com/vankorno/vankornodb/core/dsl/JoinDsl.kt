/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.dbManagement.data.TypedColumn

@DslInject
class JoinDsl<W> internal constructor(private val context: DslContext<W>) {
    internal val clauses = mutableListOf<String>()
    internal val args = mutableListOf<String>()
    
    fun <I, T> inner(table: DbhTable<*, I>, left: ColumnSelector<W, TypedColumn<T>>, right: ColumnSelector<I, TypedColumn<T>>) =
        inner(table, "${context.qualifier}.${context.table.columnSql(left)}=${table.name}.${table.columnSql(right)}")
    
    fun <I, T> left(table: DbhTable<*, I>, left: ColumnSelector<W, TypedColumn<T>>, right: ColumnSelector<I, TypedColumn<T>>) =
        left(table, "${context.qualifier}.${context.table.columnSql(left)}=${table.name}.${table.columnSql(right)}")
    
    fun inner(table: DbhTable<*, *>, on: String, vararg bindings: String) {
        clauses += "INNER JOIN ${table.name} ON $on"
        args += bindings
    }
    
    fun left(table: DbhTable<*, *>, on: String, vararg bindings: String) {
        clauses += "LEFT JOIN ${table.name} ON $on"
        args += bindings
    }
    
    fun cross(table: DbhTable<*, *>) { clauses += "CROSS JOIN ${table.name}" }
}
