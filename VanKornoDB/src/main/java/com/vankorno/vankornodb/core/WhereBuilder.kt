package com.vankorno.vankornodb.core

import com.vankorno.vankornodb.core.data.DbConstants.*

class WhereBuilder {
    val clauses = mutableListOf<String>()
    val args = mutableListOf<String>()
    
    private fun condition(                                                        column: String,
                                                                                operator: String,
                                                                                   value: String,
    ) {
        clauses.add(column + operator + "?")
        args.add(value)
    }
    private fun conditionRaw(                                                     column: String,
                                                                                operator: String,
                                                                             otherColumn: String,
    ) = clauses.add(column + operator + otherColumn)
    
    
    /** For comparisons to provided values. The values are automatically put into the arg array. **/
    
    infix fun <T> String.equal(value: T) = condition(this, "=", if (value is Boolean) if (value) "1" else "0" else value.toString())
    infix fun <T> String.notEqual(value: T) = condition(this, "!=", if (value is Boolean) if (value) "1" else "0" else value.toString())
    infix fun <T> String.greater(value: T) = condition(this, ">", value.toString())
    infix fun <T> String.greaterEqual(value: T) = condition(this, ">=", value.toString())
    infix fun <T> String.less(value: T) = condition(this, "<", value.toString())
    infix fun <T> String.lessEqual(value: T) = condition(this, "<=", value.toString())
    
    
    /** For comparisons to values from other columns.
     *  The provided column names aren't treated as values and are not put into the arg array, but put directly inside the query string.
    **/
    infix fun String.equalCol(other: String) = conditionRaw(this, "=", other)
    infix fun String.notEqualCol(other: String) = conditionRaw(this, "!=", other)
    infix fun String.lessCol(other: String) = conditionRaw(this, "<", other)
    infix fun String.lessEqualCol(other: String) = conditionRaw(this, "<=", other)
    infix fun String.greaterCol(other: String) = conditionRaw(this, ">", other)
    infix fun String.greaterEqualCol(other: String) = conditionRaw(this, ">=", other)
    
    
    infix fun String.dot(str2: String = "") = this + "." + str2
    
    
    
    // TODO Complex nested LIKE and IN:
    // IN (SELECT id FROM Users WHERE banned = 1)
    // LIKE (SELECT default_name FROM Defaults WHERE id = 1)
    // as well as passing raw strings, like with conditionRaw...
    
    infix fun <T> String.like(value: T) = condition(this, " LIKE ", value.toString())
    infix fun <T> String.notLike(value: T) = condition(this, " NOT LIKE ", value.toString())
    
    fun <T> String.likeAny(vararg values: T) = multCompare(this, " LIKE ", values)
    fun <T> String.likeNone(vararg values: T) = multCompare(this, " NOT LIKE ", values)
    
    fun <T> String.equalAny(vararg values: T) = multCompare(this, IN, values)
    fun <T> String.equalNone(vararg values: T) = multCompare(this, notIN, values)
    
    
    private fun <T> multCompare(                                                  column: String,
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
    
    /** To pass your own condition string. Use with caution! (SQL-injection risk)**/
    fun rawClause(str: String) = clauses.add(str)
}