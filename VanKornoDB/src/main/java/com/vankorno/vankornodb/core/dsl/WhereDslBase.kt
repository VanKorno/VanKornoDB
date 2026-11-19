/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.DbConstants.comma

internal fun Any?.toWhereArg(): String = if (this is Boolean) (if (this) "1" else "0") else toString()

internal class WhereDslBase {
    val clauses = mutableListOf<String>()
    val args = mutableListOf<String>()
    
    internal fun buildStr(): String = clauses.joinToString(" ")
    
    internal fun condition(                                                       column: String,
                                                                                operator: String,
                                                                                   value: String,
    ) {
        val missingAnd = if (clauses.isNotEmpty()) "AND " else ""
        
        clauses.add(missingAnd + column + operator + "?")
        args.add(value)
    }
    
    
    internal fun conditionRaw(                                                    column: String,
                                                                                operator: String,
                                                                             otherColumn: String,
    ) {
        val missingAnd = if (clauses.isNotEmpty()) "AND " else ""
        
        clauses.add(missingAnd + column + operator + otherColumn)
    }
    
    
    internal fun <T> multCompare(                           column: String,
                                                          operator: String,
                                                            values: Array<T>,
                                                            encode: (T)->String = { it.toString() },
    ) {
        clauses.add(
            buildString {
                if (clauses.isNotEmpty())
                    append("AND ")
                
                append(column)
                append(operator)
                append("(")
                for (idx in values.indices) {
                    append("?")
                    if (idx != values.lastIndex)
                        append(comma)
                }
                append(")")
            }
        )
        for (value in values) {
            args.add(encode(value))
        }
    }
    
    
    internal fun multCompareRaw(                                          column: String,
                                                                        operator: String,
                                                                       otherCols: Array<out String>,
                                                                        positive: Boolean,
    ) {
        clauses.add(
            buildString {
                if (clauses.isNotEmpty())
                    append("AND ")
                
                append("(")
                for (idx in otherCols.indices) {
                    append(column)
                    append(operator)
                    append(otherCols[idx])
                    if (idx != otherCols.lastIndex)
                        append(if (positive) " OR " else " AND ")
                }
                append(")")
            }
        )
    }
    
    
    internal fun conditionSubquery(                                               column: String,
                                                                                operator: String,
                                                                                subquery: com.vankorno.vankornodb.core.data.Subquery,
    ) {
        val missingAnd = if (clauses.isNotEmpty()) "AND " else ""
        
        clauses.add(missingAnd + column + operator + "(" + subquery.query + ")")
        args.addAll(subquery.args)
    }
    
    
    internal fun subqueryCondition(                                             subquery: com.vankorno.vankornodb.core.data.Subquery,
                                                                                operator: String,
                                                                                   value: String,
    ) {
        val missingAnd = if (clauses.isNotEmpty()) "AND " else ""
        
        clauses.add(missingAnd + "(" + subquery.query + ")" + operator + "?")
        args.addAll(subquery.args)
        args.add(value)
    }
    
    
    internal fun subqueryConditionRaw(                                          subquery: com.vankorno.vankornodb.core.data.Subquery,
                                                                                operator: String,
                                                                             otherColumn: String,
    ) {
        val missingAnd = if (clauses.isNotEmpty()) "AND " else ""
        
        clauses.add(missingAnd + "(" + subquery.query + ")" + operator + otherColumn)
        args.addAll(subquery.args)
    }
    
    
    internal fun subqueryConditionSubquery(                                     subquery: com.vankorno.vankornodb.core.data.Subquery,
                                                                                operator: String,
                                                                           otherSubquery: com.vankorno.vankornodb.core.data.Subquery,
    ) {
        val missingAnd = if (clauses.isNotEmpty()) "AND " else ""
        
        clauses.add(missingAnd + "(" + subquery.query + ")" + operator + "(" + otherSubquery.query + ")")
        args.addAll(subquery.args)
        args.addAll(otherSubquery.args)
    }
    
    
    internal fun subqueryIn(                                                      column: String,
                                                                                operator: String,
                                                                                subquery: com.vankorno.vankornodb.core.data.Subquery,
    ) {
        val missingAnd = if (clauses.isNotEmpty()) "AND " else ""
        
        clauses.add(missingAnd + column + operator + "(" + subquery.query + ")")
        args.addAll(subquery.args)
    }
    
}
