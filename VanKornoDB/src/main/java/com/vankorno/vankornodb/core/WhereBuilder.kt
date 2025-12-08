package com.vankorno.vankornodb.core

import com.vankorno.vankornodb.core.data.BoolCol
import com.vankorno.vankornodb.core.data.DbConstants.IN
import com.vankorno.vankornodb.core.data.DbConstants.notIN
import com.vankorno.vankornodb.core.data.FloatCol
import com.vankorno.vankornodb.core.data.IntCol
import com.vankorno.vankornodb.core.data.LongCol
import com.vankorno.vankornodb.core.data.StrCol

class WhereBuilder() : WhereBuilderBase() {
    
    infix fun IntCol.equal(value: Int) = condition(this.name, "=", value.toString())
    infix fun StrCol.equal(value: String) = condition(this.name, "=", value)
    infix fun BoolCol.equal(value: Boolean) = condition(this.name, "=", if (value) "1" else "0")
    infix fun LongCol.equal(value: Long) = condition(this.name, "=", value.toString())
    infix fun FloatCol.equal(value: Float) = condition(this.name, "=", value.toString())
    
    infix fun IntCol.notEqual(value: Int) = condition(this.name, "!=", value.toString())
    infix fun StrCol.notEqual(value: String) = condition(this.name, "!=", value)
    infix fun BoolCol.notEqual(value: Boolean) = condition(this.name, "!=", if (value) "1" else "0")
    infix fun LongCol.notEqual(value: Long) = condition(this.name, "!=", value.toString())
    infix fun FloatCol.notEqual(value: Float) = condition(this.name, "!=", value.toString())
    
    infix fun IntCol.greater(value: Int) = condition(this.name, ">", value.toString())
    infix fun StrCol.greater(value: String) = condition(this.name, ">", value)
    infix fun BoolCol.greater(value: Boolean) = condition(this.name, ">", if (value) "1" else "0")
    infix fun LongCol.greater(value: Long) = condition(this.name, ">", value.toString())
    infix fun FloatCol.greater(value: Float) = condition(this.name, ">", value.toString())
    
    infix fun IntCol.greaterEqual(value: Int) = condition(this.name, ">=", value.toString())
    infix fun StrCol.greaterEqual(value: String) = condition(this.name, ">=", value)
    infix fun BoolCol.greaterEqual(value: Boolean) = condition(this.name, ">=", if (value) "1" else "0")
    infix fun LongCol.greaterEqual(value: Long) = condition(this.name, ">=", value.toString())
    infix fun FloatCol.greaterEqual(value: Float) = condition(this.name, ">=", value.toString())
    
    infix fun IntCol.less(value: Int) = condition(this.name, "<", value.toString())
    infix fun StrCol.less(value: String) = condition(this.name, "<", value)
    infix fun BoolCol.less(value: Boolean) = condition(this.name, "<", if (value) "1" else "0")
    infix fun LongCol.less(value: Long) = condition(this.name, "<", value.toString())
    infix fun FloatCol.less(value: Float) = condition(this.name, "<", value.toString())
    
    infix fun IntCol.lessEqual(value: Int) = condition(this.name, "<=", value.toString())
    infix fun StrCol.lessEqual(value: String) = condition(this.name, "<=", value)
    infix fun BoolCol.lessEqual(value: Boolean) = condition(this.name, "<=", if (value) "1" else "0")
    infix fun LongCol.lessEqual(value: Long) = condition(this.name, "<=", value.toString())
    infix fun FloatCol.lessEqual(value: Float) = condition(this.name, "<=", value.toString())
    
    
    
    
    
    infix fun IntCol.equalCol(otherCol: IntCol) = conditionRaw(this.name, "=", otherCol.name)
    infix fun StrCol.equalCol(otherCol: StrCol) = conditionRaw(this.name, "=", otherCol.name)
    infix fun BoolCol.equalCol(otherCol: BoolCol) = conditionRaw(this.name, "=", otherCol.name)
    infix fun LongCol.equalCol(otherCol: LongCol) = conditionRaw(this.name, "=", otherCol.name)
    infix fun FloatCol.equalCol(otherCol: FloatCol) = conditionRaw(this.name, "=", otherCol.name)
    
    
    
    
    
    
    
    
    
    
    
    
    
    
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
    infix fun String.equalCol(otherCol: String) = conditionRaw(this, "=", otherCol)
    infix fun String.notEqualCol(otherCol: String) = conditionRaw(this, "!=", otherCol)
    infix fun String.lessCol(otherCol: String) = conditionRaw(this, "<", otherCol)
    infix fun String.lessEqualCol(otherCol: String) = conditionRaw(this, "<=", otherCol)
    infix fun String.greaterCol(otherCol: String) = conditionRaw(this, ">", otherCol)
    infix fun String.greaterEqualCol(otherCol: String) = conditionRaw(this, ">=", otherCol)
    
    
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