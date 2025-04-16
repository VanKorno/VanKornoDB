package com.vankorno.vankornodb.core
// This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
// If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

import com.vankorno.vankornodb.core.DbConstants.comma


fun getQuery(                                               table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                            joins: JoinBuilder.()->Unit = {},
                                                            where: CondBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = null,
                                                           offset: Int? = null,
                                                        customEnd: String = ""
): Pair<String, Array<String>> {
    val conditions = CondBuilder().apply(where)
    val joinBuilder = JoinBuilder().apply(joins)
    
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
        if (groupBy.isNotBlank()) {
            append(" GROUP BY ")
            append(groupBy)
        }
        if (having.isNotBlank()) {
            append(" HAVING ")
            append(having)
        }
        if (orderBy.isNotBlank()) {
            append(" ORDER BY ")
            append(orderBy)
        }
        if (limit != null) {
            append(" LIMIT ")
            append(limit)
        }
        if (offset != null) {
            append(" OFFSET ")
            append(offset)
        }
        if (customEnd.isNotBlank()) {
            append(customEnd)
        }
    }
    return query to conditions.args.toTypedArray()
}


class CondBuilder {
    val clauses = mutableListOf<String>()
    val args = mutableListOf<String>()
    
    fun condition(                                                                column: String,
                                                                                operator: String,
                                                                                   value: String
    ) {
        clauses.add(column + operator + "?")
        args.add(value)
    }
    
    infix fun <T> String.equal(value: T) =
    condition(this, "=", if (value is Boolean) if (value) "1" else "0" else value.toString())
    
    infix fun <T> String.notEqual(value: T) =
    condition(this, "!=", if (value is Boolean) if (value) "1" else "0" else value.toString())
    
    infix fun <T> String.greater(value: T) = condition(this, ">", value.toString())
    infix fun <T> String.greaterEqual(value: T) = condition(this, ">=", value.toString())
    infix fun <T> String.less(value: T) = condition(this, "<", value.toString())
    infix fun <T> String.lessEqual(value: T) = condition(this, "<=", value.toString())
    
    infix fun String.dot(str2: String = "") = this + "." + str2
    
    
    fun group(                                                   whereBuilder: CondBuilder.()->Unit
    ) {
        val innerBuilder = CondBuilder().apply(whereBuilder)
        clauses.add("(" + innerBuilder.clauses.joinToString(" ") + ")")
        args.addAll(innerBuilder.args)
    }

    
    fun and(                                                     whereBuilder: CondBuilder.()->Unit
    ) {
        clauses.add("AND")
        val innerBuilder = CondBuilder().apply(whereBuilder)
        clauses.addAll(innerBuilder.clauses)
        args.addAll(innerBuilder.args)
    }
    fun andGroup(                                                whereBuilder: CondBuilder.()->Unit
    ) {
        clauses.add("AND")
        group(whereBuilder)
    }
    
    fun or(                                                      whereBuilder: CondBuilder.()->Unit
    ) {
        clauses.add("OR")
        val innerBuilder = CondBuilder().apply(whereBuilder)
        clauses.addAll(innerBuilder.clauses)
        args.addAll(innerBuilder.args)
    }
    fun orGroup(                                                 whereBuilder: CondBuilder.()->Unit
    ) {
        clauses.add("OR")
        group(whereBuilder)
    }
    
    fun innerQuery(                                         table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                            joins: JoinBuilder.()->Unit = {},
                                                            where: CondBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = null,
                                                           offset: Int? = null,
                                                        customEnd: String = ""
    ): String {
        val innerBuilder = getQuery(table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
        
        val clause = "(${innerBuilder.first})"
        
        args.addAll(innerBuilder.second)
        return clause
    }
}


class JoinBuilder {
    val joins = mutableListOf<String>()

    fun inner(table: String, on: String) {
        joins.add("INNER JOIN $table ON $on")
    }

    fun left(table: String, on: String) {
        joins.add("LEFT JOIN $table ON $on")
    }

    fun cross(table: String) {
        joins.add("CROSS JOIN $table")
    }
}




