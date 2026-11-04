/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.JoinDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.data.DbConstants.comma
import com.vankorno.vankornodb.core.data.QueryWithArgs


internal fun getQuery(                                      table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                          orderBy: OrderDsl.()->Unit = {},
                                                            extra: ExtraDsl.()->Unit = {},
                                                            where: WhereDsl.()->Unit = {},
): QueryWithArgs {
    val extraDsl = ExtraDsl().apply(extra).query
    val whereDsl = WhereDsl().apply(where)
    val joinDsl = JoinDsl().apply(extraDsl.joins)
    val orderDsl = OrderDsl().apply(orderBy)
    
    val query = buildString {
        append("SELECT ")
        append(columns.joinToString(comma))
        append(" FROM ")
        append(table)
        if (joinDsl.joins.isNotEmpty()) {
            append(" ")
            append(joinDsl.buildStr())
        }
        if (whereDsl.clauses.isNotEmpty()) {
            append(" WHERE ")
            append(whereDsl.buildStr())
        }
        if (extraDsl.groupBy.isNotBlank()) {
            append(" GROUP BY ")
            append(extraDsl.groupBy)
        }
        if (extraDsl.having.isNotBlank()) {
            append(" HAVING ")
            append(extraDsl.having)
        }
        if (orderDsl.orderoids.isNotEmpty()) {
            append(" ORDER BY ")
            append(orderDsl.buildStr())
        }
        if (extraDsl.limit != null) {
            append(" LIMIT ")
            append(extraDsl.limit)
        }
        if (extraDsl.offset != null) {
            append(" OFFSET ")
            append(extraDsl.offset)
        }
        if (extraDsl.customEnd.isNotBlank()) {
            append(extraDsl.customEnd)
        }
    }
    
    val args =  joinDsl.args +
                whereDsl.args +
                orderDsl.args
                // + havingBuilder.args, etc.
    
    return QueryWithArgs(query, args.toTypedArray())
}















