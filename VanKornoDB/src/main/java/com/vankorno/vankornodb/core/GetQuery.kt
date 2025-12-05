package com.vankorno.vankornodb.core
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import com.vankorno.vankornodb.core.data.DbConstants.comma
import com.vankorno.vankornodb.core.data.QueryOptsHolder
import com.vankorno.vankornodb.core.data.QueryWithArgs

// TODO better orderBy, to avoid to cover stuff like this: orderBy = Stage+comma + Position + descending
// TODO interface for builders


internal fun getQuery(                                      table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                        queryOpts: QueryOpts.()->Unit,
) = getQuery(table, columns, QueryOpts().apply(queryOpts).query)



internal fun getQuery(                                   table: String,
                                                       columns: Array<out String> = arrayOf("*"),
                                                       sqlOpts: QueryOptsHolder = QueryOptsHolder(),
): QueryWithArgs {
    val conditions = WhereBuilder().apply(sqlOpts.where)
    val joinBuilder = JoinBuilder().apply(sqlOpts.joins)
    
    val query = buildString {
        append("SELECT ")
        append(columns.joinToString(comma))
        append(" FROM ")
        append(table)
        if (joinBuilder.joins.isNotEmpty()) {
            append(" ")
            append(joinBuilder.joins.joinToString(" "))
        }
        if (conditions.clauses.isNotEmpty()) {
            append(" WHERE ")
            append(conditions.clauses.joinToString(" "))
        }
        if (sqlOpts.groupBy.isNotBlank()) {
            append(" GROUP BY ")
            append(sqlOpts.groupBy)
        }
        if (sqlOpts.having.isNotBlank()) {
            append(" HAVING ")
            append(sqlOpts.having)
        }
        if (sqlOpts.orderBy.isNotBlank()) {
            append(" ORDER BY ")
            append(sqlOpts.orderBy)
        }
        if (sqlOpts.limit != null) {
            append(" LIMIT ")
            append(sqlOpts.limit)
        }
        if (sqlOpts.offset != null) {
            append(" OFFSET ")
            append(sqlOpts.offset)
        }
        if (sqlOpts.customEnd.isNotBlank()) {
            append(sqlOpts.customEnd)
        }
    }
    return QueryWithArgs(query, conditions.args.toTypedArray())
}















