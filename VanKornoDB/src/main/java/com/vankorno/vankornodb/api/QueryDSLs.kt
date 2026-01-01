// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.api

import com.vankorno.vankornodb.core.data.OrderWhen
import com.vankorno.vankornodb.core.dsl.FullDslInternal
import com.vankorno.vankornodb.core.dsl.JoinDslInternal
import com.vankorno.vankornodb.core.dsl.OrderDslInternal
import com.vankorno.vankornodb.core.dsl.WhereDslInternal
import com.vankorno.vankornodb.core.dsl.getQuery
import com.vankorno.vankornodb.dbManagement.data.AscendingColumn
import com.vankorno.vankornodb.dbManagement.data.DescendingColumn

class FullDsl : FullDslInternal() {
    fun applyDsl(opts: FullDsl.()->Unit) { this.opts() }
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



class WhereDsl : WhereDslInternal() {
    fun group(                                                              where: WhereDsl.()->Unit
    ) {
        val innerBuilder = WhereDsl().apply(where)
        if (innerBuilder.clauses.isEmpty())
            return //\/\/\/\/\/\
        
        clauses.add("(" + innerBuilder.buildStr() + ")")
        args.addAll(innerBuilder.args)
    }
    
    fun and(                                                                where: WhereDsl.()->Unit
    ) {
        val innerBuilder = WhereDsl().apply(where)
        if (innerBuilder.clauses.isEmpty())
            return //\/\/\/\/\/\
        
        clauses.add("AND")
        clauses.addAll(innerBuilder.clauses)
        args.addAll(innerBuilder.args)
    }
    fun andGroup(                                                           where: WhereDsl.()->Unit
    ) {
        val innerBuilder = WhereDsl().apply(where)
        if (innerBuilder.clauses.isEmpty())
            return //\/\/\/\/\/\
        
        clauses.add("AND")
        group(where)
    }
    
    fun or(                                                                 where: WhereDsl.()->Unit
    ) {
        val innerBuilder = WhereDsl().apply(where)
        if (innerBuilder.clauses.isEmpty())
            return //\/\/\/\/\/\
        
        clauses.add("OR")
        clauses.addAll(innerBuilder.clauses)
        args.addAll(innerBuilder.args)
    }
    
    fun orGroup(                                                            where: WhereDsl.()->Unit
    ) {
        val innerBuilder = WhereDsl().apply(where)
        if (innerBuilder.clauses.isEmpty())
            return //\/\/\/\/\/\
        
        clauses.add("OR")
        group(where)
    }
    
    fun subquery(                                           table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                              dsl: FullDsl.()->Unit = {},
    ): String {
        val innerBuilder = getQuery(table, columns, dsl)
        
        val clause = "(${innerBuilder.query})"
        
        args.addAll(innerBuilder.args)
        return clause
    }
}










