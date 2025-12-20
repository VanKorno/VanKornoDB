// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.JoinDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.data.DbConstants.comma
import com.vankorno.vankornodb.core.data.FullDslHolder
import com.vankorno.vankornodb.core.data.QueryWithArgs

internal fun getQuery(                                      table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                              dsl: FullDsl.()->Unit,
) = getQuery(table, columns, FullDsl().apply(dsl).query)



internal fun getQuery(                                      table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                        dslHolder: FullDslHolder = FullDslHolder(),
): QueryWithArgs {
    val whereDsl = WhereDsl().apply(dslHolder.where)
    val joinDsl = JoinDsl().apply(dslHolder.joins)
    val orderDsl = OrderDsl().apply(dslHolder.orderBy)
    
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
        if (dslHolder.groupBy.isNotBlank()) {
            append(" GROUP BY ")
            append(dslHolder.groupBy)
        }
        if (dslHolder.having.isNotBlank()) {
            append(" HAVING ")
            append(dslHolder.having)
        }
        if (orderDsl.orderoids.isNotEmpty()) {
            append(" ORDER BY ")
            append(orderDsl.buildStr())
        }
        if (dslHolder.limit != null) {
            append(" LIMIT ")
            append(dslHolder.limit)
        }
        if (dslHolder.offset != null) {
            append(" OFFSET ")
            append(dslHolder.offset)
        }
        if (dslHolder.customEnd.isNotBlank()) {
            append(dslHolder.customEnd)
        }
    }
    
    val args =  joinDsl.args +
                whereDsl.args +
                orderDsl.args
                // + havingBuilder.args, etc.
    
    return QueryWithArgs(query, args.toTypedArray())
}















