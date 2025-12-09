package com.vankorno.vankornodb.api
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import com.vankorno.vankornodb.core.queryBuilder.JoinBuilderInternal
import com.vankorno.vankornodb.core.queryBuilder.QueryOptsInternal
import com.vankorno.vankornodb.core.queryBuilder.WhereBuilderInternal
import com.vankorno.vankornodb.core.queryBuilder.getQuery
import com.vankorno.vankornodb.dbManagement.data.EntityColumnsInternal


class QueryOpts : QueryOptsInternal() {
    fun applyOpts(opts: QueryOpts.()->Unit) { this.opts() }
}



class JoinBuilder : JoinBuilderInternal()


interface EntityColumns : EntityColumnsInternal




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



