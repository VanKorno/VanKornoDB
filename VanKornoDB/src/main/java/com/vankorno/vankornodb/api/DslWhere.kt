/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.api

import com.vankorno.vankornodb.core.dsl.WhereDslInternal
import com.vankorno.vankornodb.core.dsl.getQuery


class WhereDsl : WhereDslInternal() {
    fun group(                                                              where: WhereDsl.()->Unit
    ) {
        val innerBuilder = WhereDsl().apply(where)
        appendGroup(innerBuilder)
    }
    private fun appendGroup(                                                innerBuilder: WhereDsl
    ) {
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
        
        if (clauses.isNotEmpty()) // Check if the parent block already has conditions
            clauses.add("AND")
        
        appendGroup(innerBuilder)
    }
    
    fun or(                                                                 where: WhereDsl.()->Unit
    ) {
        val innerBuilder = WhereDsl().apply(where)
        if (innerBuilder.clauses.isEmpty())
            return //\/\/\/\/\/\
        
        if (clauses.isNotEmpty()) // Check if the parent block already has conditions
            clauses.add("OR")
        
        appendGroup(innerBuilder)
    }
    
    fun subquery(                                           table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                            where: WhereDsl.()->Unit = {},
                                                          orderBy: OrderDsl.()->Unit = {},
                                                            extra: ExtraDsl.()->Unit = {},
    ): String {
        val innerBuilder = getQuery(table, columns, orderBy, extra, where)
        
        val clause = "(${innerBuilder.query})"
        
        args.addAll(innerBuilder.args)
        return clause
    }
}
