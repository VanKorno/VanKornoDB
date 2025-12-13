package com.vankorno.vankornodb.core.queryBuilder

import com.vankorno.vankornodb.core.data.DbConstants.comma

abstract class WhereBuilderBase() {
    val clauses = mutableListOf<String>()
    val args = mutableListOf<String>()
    
    internal fun buildStr(): String = clauses.joinToString(" ")
    
    internal fun condition(                                                       column: String,
                                                                                operator: String,
                                                                                   value: String,
    ) {
        clauses.add(column + operator + "?")
        args.add(value)
    }
    
    
    internal fun conditionRaw(                                                    column: String,
                                                                                operator: String,
                                                                             otherColumn: String,
    ) {
        clauses.add(column + operator + otherColumn)
    }
    
    
    internal fun <T> multCompare(                                                 column: String,
                                                                                operator: String,
                                                                                  values: Array<T>,
    ) {
        clauses.add(
            buildString {
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
            args.add(value.toString())
        }
    }
    
    
    internal fun multCompareRaw(                                          column: String,
                                                                        operator: String,
                                                                       otherCols: Array<out String>,
                                                                        positive: Boolean,
    ) {
        clauses.add(
            buildString {
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
    
}









