/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.api

import com.vankorno.vankornodb.core.data.OrderWhen
import com.vankorno.vankornodb.core.dsl.ExtraDslInternal
import com.vankorno.vankornodb.core.dsl.JoinDslInternal
import com.vankorno.vankornodb.core.dsl.OrderDslInternal
import com.vankorno.vankornodb.dbManagement.data.AscendingColumn
import com.vankorno.vankornodb.dbManagement.data.DescendingColumn

class ExtraDsl : ExtraDslInternal() {
    fun applyDsl(opts: ExtraDsl.()->Unit) { this.opts() }
}


class JoinDsl : JoinDslInternal()




class OrderDsl : OrderDslInternal() {
    
    fun and(                                                             orderDsl: OrderDsl.()->Unit
    ) {
        val inner = OrderDsl().apply(orderDsl)
        orderoids.addAll(inner.orderoids)
        args.addAll(inner.args)
    }
    
    fun group(                                                           orderDsl: OrderDsl.()->Unit
    ) {
        val inner = OrderDsl().apply(orderDsl)
        if (inner.orderoids.isNotEmpty()) {
            orderoids += "(" + inner.buildStr() + ")"
            args.addAll(inner.args)
        }
    }
    
    fun orderWhen(orders: OrderDsl.()->Unit, whens: WhereDsl.()->Unit) = OrderWhen(orders, whens)
    
    fun orderWhen(orderBy: AscendingColumn<*>, whens: WhereDsl.()->Unit) = OrderWhen({ orderBy() }, whens)
    fun orderWhen(orderBy: DescendingColumn, whens: WhereDsl.()->Unit) = OrderWhen({ orderBy() }, whens)
    
    fun orderWhen(orderBy: String, whens: WhereDsl.()->Unit) = OrderWhen({ raw(orderBy) }, whens)
    
    
    fun When(                                              vararg orderWhen: OrderWhen,
                                                                       Else: OrderDsl.()->Unit = {},
    ) {
        val assembled = buildString {
            for (wo in orderWhen) {
                val whereDsl = WhereDsl().apply(wo.whens)
                val orderDsl = OrderDsl().apply(wo.orders)
                
                append(" WHEN ${whereDsl.buildStr()} THEN ${orderDsl.buildStr()}")
                args += whereDsl.args
                args += orderDsl.args
            }
            val elseStr = OrderDsl().apply(Else).buildStr()
            if (elseStr.isNotEmpty())
                append(" ELSE $elseStr")
        }
        orderoids += case(assembled)
    }
}












