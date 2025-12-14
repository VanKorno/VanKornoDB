package com.vankorno.vankornodb.api
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import com.vankorno.vankornodb.core.data.OrderWhen
import com.vankorno.vankornodb.core.queryBuilder.JoinBuilderInternal
import com.vankorno.vankornodb.core.queryBuilder.OrderByBuilderInternal
import com.vankorno.vankornodb.core.queryBuilder.QueryOptsInternal
import com.vankorno.vankornodb.core.queryBuilder.WhereBuilderInternal
import com.vankorno.vankornodb.core.queryBuilder.getQuery
import com.vankorno.vankornodb.dbManagement.data.EntityColumnsInternal
import com.vankorno.vankornodb.dbManagement.data.TypedColumn


class QueryOpts : QueryOptsInternal() {
    fun applyOpts(opts: QueryOpts.()->Unit) { this.opts() }
}


class JoinBuilder : JoinBuilderInternal()


interface EntityColumns : EntityColumnsInternal



class OrderByBuilder : OrderByBuilderInternal() {
    
    fun and(                                                        builder: OrderByBuilder.()->Unit
    ) {
        val inner = OrderByBuilder().apply(builder)
        orderoids.addAll(inner.orderoids)
        args.addAll(inner.args)
    }
    
    fun group(                                                      builder: OrderByBuilder.()->Unit
    ) {
        val inner = OrderByBuilder().apply(builder)
        if (inner.orderoids.isNotEmpty()) {
            orderoids += "(" + inner.orderoids.joinToString(", ") + ")"
            args.addAll(inner.args)
        }
    }
    
    
    fun orderWhen(orders: OrderByBuilder.()->Unit, whens: WhereBuilder.()->Unit) = OrderWhen(orders, whens)
    
    fun orderWhen(orderBy: TypedColumn<*>, whens: WhereBuilder.()->Unit) = OrderWhen({ orderBy() }, whens)
    
    fun orderWhen(orderBy: String, whens: WhereBuilder.()->Unit) = OrderWhen({ raw(orderBy) }, whens)
    
    
    fun where(                                       vararg orderWhen: OrderWhen,
                                                                else_: OrderByBuilder.()->Unit = {},
    ) {
        val assembled = buildString {
            for (wo in orderWhen) {
                val whereBuilder = WhereBuilder().apply(wo.whens)
                val orderByBuilder = OrderByBuilder().apply(wo.orders)
                
                append(" WHEN ${whereBuilder.buildStr()} THEN ${orderByBuilder.buildStr()}")
                args += whereBuilder.args
                args += orderByBuilder.args
            }
            val elseStr = OrderByBuilder().apply(else_).buildStr()
            if (elseStr.isNotEmpty())
                append(" ELSE $elseStr")
        }
        orderoids += case(assembled)
    }
    
}



class WhereBuilder : WhereBuilderInternal() {
    fun group(                                                   whereBuilder: WhereBuilder.()->Unit
    ) {
        val innerBuilder = WhereBuilder().apply(whereBuilder)
        clauses.add("(" + innerBuilder.clauses.joinToString(" ") + ")")
        args.addAll(innerBuilder.args)
    }
    
    fun and(                                                     whereBuilder: WhereBuilder.()->Unit
    ) {
        clauses.add("AND")
        val innerBuilder = WhereBuilder().apply(whereBuilder)
        clauses.addAll(innerBuilder.clauses)
        args.addAll(innerBuilder.args)
    }
    fun andGroup(                                                whereBuilder: WhereBuilder.()->Unit
    ) {
        clauses.add("AND")
        group(whereBuilder)
    }
    
    fun or(                                                      whereBuilder: WhereBuilder.()->Unit
    ) {
        clauses.add("OR")
        val innerBuilder = WhereBuilder().apply(whereBuilder)
        clauses.addAll(innerBuilder.clauses)
        args.addAll(innerBuilder.args)
    }
    fun orGroup(                                                 whereBuilder: WhereBuilder.()->Unit
    ) {
        clauses.add("OR")
        group(whereBuilder)
    }
    
    fun subquery(                                           table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                        queryOpts: QueryOpts.()->Unit = {},
    ): String {
        val innerBuilder = getQuery(table, columns, queryOpts)
        
        val clause = "(${innerBuilder.query})"
        
        args.addAll(innerBuilder.args)
        return clause
    }
}










