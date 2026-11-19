/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.DbConstants.comma
import com.vankorno.vankornodb.core.data.ExtraDslHolder
import com.vankorno.vankornodb.core.data.QueryWithArgs


internal fun getQuery(                                                     table: String,
                                                                         columns: Array<out String>,
                                                                        orderDsl: OrderDsl<*, *>,
                                                                        extraDsl: ExtraDslHolder,
                                                                        whereDsl: WhereDslBase,
): QueryWithArgs {
    
    val query = buildString {
        append("SELECT ")
        append(columns.joinToString(comma))
        append(" FROM ")
        append(table)
        if (extraDsl.joins.isNotEmpty()) {
            append(" ")
            append(extraDsl.joins.joinToString(" "))
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
        if (orderDsl.terms.isNotEmpty()) {
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
    
    val args = extraDsl.joinArgs + whereDsl.args + extraDsl.havingArgs + orderDsl.args
    return QueryWithArgs(query, args.toTypedArray())
}
