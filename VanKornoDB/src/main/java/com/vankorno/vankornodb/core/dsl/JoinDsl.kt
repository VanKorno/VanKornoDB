/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.dbManagement.data.TypedColumn

@DslInject
class JoinDsl<W> internal constructor(private val context: DslContext<W>) {
    internal val clauses = mutableListOf<String>()
    internal val args = mutableListOf<String>()
    
    fun <I, T> inner(table: MiniDslTable<*, I>, left: FieldSelector<W, TypedColumn<T>>, right: FieldSelector<I, TypedColumn<T>>) =
        inner(table, "${context.qualifier}.${context.table.columnSql(left)}=${table.name}.${table.columnSql(right)}")
    
    fun <I, T> left(table: MiniDslTable<*, I>, left: FieldSelector<W, TypedColumn<T>>, right: FieldSelector<I, TypedColumn<T>>) =
        left(table, "${context.qualifier}.${context.table.columnSql(left)}=${table.name}.${table.columnSql(right)}")
    
    fun inner(table: MiniDslTable<*, *>, on: String, vararg bindings: String) {
        clauses += "INNER JOIN ${table.name} ON $on"
        args += bindings
    }
    
    fun left(table: MiniDslTable<*, *>, on: String, vararg bindings: String) {
        clauses += "LEFT JOIN ${table.name} ON $on"
        args += bindings
    }
    
    fun cross(table: MiniDslTable<*, *>) { clauses += "CROSS JOIN ${table.name}" }
}
