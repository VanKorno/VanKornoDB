/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.DbConstants.DESCENDING
import com.vankorno.vankornodb.core.data.DbConstants.RANDOM
import com.vankorno.vankornodb.core.data.DbConstants.comma
import com.vankorno.vankornodb.core.data.OrderWhen
import com.vankorno.vankornodb.dbManagement.data.AscendingColumn

@DslInject
abstract class OrderDsl<W, O : OrderDsl<W, O>> {
    internal val terms = mutableListOf<String>()
    internal val args = mutableListOf<String>()
    internal lateinit var nested: ()->O
    internal lateinit var context: DslContext<W>
    
    internal fun buildStr() = terms.joinToString(comma)
    
    operator fun Field<W, AscendingColumn<*>>.invoke() = by()
    fun Field<W, AscendingColumn<*>>.by() { terms += definition.name }
    fun Field<W, AscendingColumn<*>>.flip() { terms += definition.name + DESCENDING }
    fun Field<W, AscendingColumn<*>>.flipIf(condition: Boolean) { if (condition) flip() else by() }
    fun col(vararg columns: Field<W, *>) { columns.forEach { terms += it.definition.name } }
    fun flipAll(vararg columns: Field<W, AscendingColumn<*>>) { columns.forEach { it.flip() } }
    fun random() { terms += RANDOM }
    fun orderRandomly() = random()
    fun raw(vararg expressions: String) { terms.addAll(expressions) }
    
    fun and(block: O.()->Unit) {
        val child = nested().apply(block)
        terms += child.terms
        args += child.args
    }
    
    fun group(block: O.()->Unit) {
        val child = nested().apply(block)
        if (child.terms.isNotEmpty()) {
            terms += "(${child.buildStr()})"
            args += child.args
        }
    }
    
    fun orderWhen(orders: O.()->Unit, whens: W.()->Unit) = OrderWhen(orders, whens)
    fun orderWhen(column: Field<W, AscendingColumn<*>>, whens: W.()->Unit) = orderWhen({ column.by() }, whens)
    fun orderWhen(expression: String, whens: W.()->Unit) = orderWhen({ raw(expression) }, whens)
    
    fun When(vararg branches: OrderWhen<W, O>, Else: O.()->Unit = {}) {
        val sql = StringBuilder("CASE")
        val bindings = mutableListOf<String>()
        for (branch in branches) {
            val predicate = context.table.predicate(context.qualifier, branch.whens)
            val result = nested().apply(branch.orders)
            sql.append(" WHEN ${predicate.buildStr()} THEN ${result.buildStr()}")
            bindings += predicate.args
            bindings += result.args
        }
        val fallback = nested().apply(Else)
        if (fallback.terms.isNotEmpty()) {
            sql.append(" ELSE ${fallback.buildStr()}")
            bindings += fallback.args
        }
        terms += sql.append(" END").toString()
        args += bindings
    }
}
