/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.DbConstants.IN
import com.vankorno.vankornodb.core.data.DbConstants.NOT_IN
import com.vankorno.vankornodb.core.data.Subquery
import com.vankorno.vankornodb.dbManagement.data.*

@DslInject
abstract class WhereDsl<W>(private val context: DslContext<W>) {
    infix fun <C : TypedColumn<*>> SelectExpr<W, C>.then(next: SelectExpr<W, C>) = listOf(this, next)

    infix fun <C : TypedColumn<*>> List<SelectExpr<W, C>>.then(next: SelectExpr<W, C>) = this + next

    private val builder get() = context.builder
    
    private fun appendGroup(                                                  operator: String,
                                                                                 where: W.()->Unit,
    ) {
        val child = context.table.predicate(context.qualifier, where)
        if (child.clauses.isNotEmpty()) {
            if (operator.isNotEmpty() && builder.clauses.isNotEmpty())
                builder.clauses += operator
            builder.clauses += "(${child.buildStr()})"
            builder.args += child.args
        }
    }
    
    fun rawClause(                                                                   sql: String,
                                                                             vararg args: String,
    ) {
        builder.clauses += (if (builder.clauses.isEmpty()) "" else "AND ") + sql
        builder.args += args
    }
    
    private fun likeValues(                                                 name: String,
                                                                          values: Array<out String>,
                                                                        positive: Boolean,
    ) {
        builder.multCompareRaw(name, if (positive) " LIKE " else " NOT LIKE ", Array(values.size) { "?" }, positive)
        builder.args.addAll(values)
    }
    
    protected fun <C : TypedColumn<*>> column(definition: C) = Field<W, C>(definition)
    
    
    protected fun <C : TypedColumn<*>> column(                               definitions: List<C>
    ): List<Field<W, C>> =
        object : AbstractList<Field<W, C>>() {
            override val size get() = definitions.size
            override fun get(index: Int) = Field<W, C>(definitions[index])
        }
    
    
    fun group(where: W.()->Unit) = appendGroup("", where)
    fun and(where: W.()->Unit) = appendGroup("AND", where)
    fun or(where: W.()->Unit) = appendGroup("OR", where)
    
    infix fun <T> Field<W, AscendingColumn<T>>.equal(value: T) = builder.condition(definition.name, "=", value.toWhereArg())
    infix fun <T> Field<W, AscendingColumn<T>>.notEqual(value: T) = builder.condition(definition.name, "!=", value.toWhereArg())
    
    infix fun <T : Number> Field<W, NumberColumn<T>>.greater(value: T) = builder.condition(definition.name, ">", value.toWhereArg())
    infix fun <T : Number> Field<W, NumberColumn<T>>.greaterEqual(value: T) = builder.condition(definition.name, ">=", value.toWhereArg())
    infix fun <T : Number> Field<W, NumberColumn<T>>.less(value: T) = builder.condition(definition.name, "<", value.toWhereArg())
    infix fun <T : Number> Field<W, NumberColumn<T>>.lessEqual(value: T) = builder.condition(definition.name, "<=", value.toWhereArg())
    
    infix fun <T> Field<W, AscendingColumn<T>>.equalCol(other: Field<W, AscendingColumn<T>>) = builder.conditionRaw(definition.name, "=", other.definition.name)
    infix fun <T> Field<W, AscendingColumn<T>>.notEqualCol(other: Field<W, AscendingColumn<T>>) = builder.conditionRaw(definition.name, "!=", other.definition.name)
    
    infix fun <T : Number> Field<W, NumberColumn<T>>.greaterCol(other: Field<W, NumberColumn<T>>) = builder.conditionRaw(definition.name, ">", other.definition.name)
    infix fun <T : Number> Field<W, NumberColumn<T>>.greaterEqualCol(other: Field<W, NumberColumn<T>>) = builder.conditionRaw(definition.name, ">=", other.definition.name)
    infix fun <T : Number> Field<W, NumberColumn<T>>.lessCol(other: Field<W, NumberColumn<T>>) = builder.conditionRaw(definition.name, "<", other.definition.name)
    infix fun <T : Number> Field<W, NumberColumn<T>>.lessEqualCol(other: Field<W, NumberColumn<T>>) = builder.conditionRaw(definition.name, "<=", other.definition.name)
    
    infix fun Field<W, StrCol>.like(value: String) = builder.condition(definition.name, " LIKE ", value.toWhereArg())
    infix fun Field<W, StrCol>.notLike(value: String) = builder.condition(definition.name, " NOT LIKE ", value.toWhereArg())
    infix fun Field<W, StrCol>.likeCol(other: Field<W, StrCol>) = builder.conditionRaw(definition.name, " LIKE ", other.definition.name)
    infix fun Field<W, StrCol>.notLikeCol(other: Field<W, StrCol>) = builder.conditionRaw(definition.name, " NOT LIKE ", other.definition.name)
    
    fun Field<W, StrCol>.likeAny(vararg values: String) = likeValues(definition.name, values, true)
    fun Field<W, StrCol>.notLikeAny(vararg values: String) = likeValues(definition.name, values, false)
    fun Field<W, StrCol>.likeAnyCol(vararg others: Field<W, StrCol>) = builder.multCompareRaw(definition.name, " LIKE ", others.map { it.definition.name }.toTypedArray(), true)
    fun Field<W, StrCol>.notLikeAnyCol(vararg others: Field<W, StrCol>) = builder.multCompareRaw(definition.name, " NOT LIKE ", others.map { it.definition.name }.toTypedArray(), false)
    
    
    private fun Field<W, *>.compareAny(                           operator: String,
                                                                           values: Array<out Any?>,
    ) {
        builder.multCompare(definition.name, operator, values) { it.toWhereArg() }
    }
    
    
    infix fun <T> Field<W, AscendingColumn<T>>.equalAny(values: Array<out T>) = compareAny(IN, values)
    infix fun <T> Field<W, AscendingColumn<T>>.notEqualAny(values: Array<out T>) = compareAny(NOT_IN, values)
    infix fun <T> Field<W, AscendingColumn<T>>.equalAny(values: List<T>) = compareAny(IN, values.toTypedArray<Any?>())
    infix fun <T> Field<W, AscendingColumn<T>>.notEqualAny(values: List<T>) = compareAny(NOT_IN, values.toTypedArray<Any?>())
    fun Field<W, IntCol>.equalAnyOf(vararg values: Int) = compareAny(IN, values.toTypedArray())
    fun Field<W, LongCol>.equalAnyOf(vararg values: Long) = compareAny(IN, values.toTypedArray())
    fun Field<W, FloatCol>.equalAnyOf(vararg values: Float) = compareAny(IN, values.toTypedArray())
    fun Field<W, BoolCol>.equalAnyOf(vararg values: Boolean) = compareAny(IN, values.toTypedArray())
    fun Field<W, StrCol>.equalAnyOf(vararg values: String) = compareAny(IN, values)
    
    fun Field<W, IntCol>.notEqualAnyOf(vararg values: Int) = compareAny(NOT_IN, values.toTypedArray())
    fun Field<W, LongCol>.notEqualAnyOf(vararg values: Long) = compareAny(NOT_IN, values.toTypedArray())
    fun Field<W, FloatCol>.notEqualAnyOf(vararg values: Float) = compareAny(NOT_IN, values.toTypedArray())
    fun Field<W, BoolCol>.notEqualAnyOf(vararg values: Boolean) = compareAny(NOT_IN, values.toTypedArray())
    fun Field<W, StrCol>.notEqualAnyOf(vararg values: String) = compareAny(NOT_IN, values)
    
    infix fun Field<W, TypedColumn<*>>.equal(subquery: Subquery) = builder.conditionSubquery(definition.name, "=", subquery)
    infix fun Field<W, TypedColumn<*>>.notEqual(subquery: Subquery) = builder.conditionSubquery(definition.name, "!=", subquery)
    infix fun Field<W, NumberColumn<*>>.greater(subquery: Subquery) = builder.conditionSubquery(definition.name, ">", subquery)
    infix fun Field<W, NumberColumn<*>>.greaterEqual(subquery: Subquery) = builder.conditionSubquery(definition.name, ">=", subquery)
    infix fun Field<W, NumberColumn<*>>.less(subquery: Subquery) = builder.conditionSubquery(definition.name, "<", subquery)
    infix fun Field<W, NumberColumn<*>>.lessEqual(subquery: Subquery) = builder.conditionSubquery(definition.name, "<=", subquery)
    infix fun Field<W, TypedColumn<*>>.inSubquery(subquery: Subquery) = builder.conditionSubquery(definition.name, IN, subquery)
    infix fun Field<W, TypedColumn<*>>.notInSubquery(subquery: Subquery) = builder.conditionSubquery(definition.name, NOT_IN, subquery)
    infix fun Field<W, TypedColumn<*>>.equalAny(subquery: Subquery) = this inSubquery subquery
    infix fun Field<W, TypedColumn<*>>.notEqualAny(subquery: Subquery) = this notInSubquery subquery
    
    
    
    fun <I, T, O : OrderDsl<I, O>, X : ExtraDsl<I>> Field<W, TypedColumn<T>>.inSubquery(
                                                          column: TableColumn<I, O, X, TypedColumn<T>>,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: I.()->Unit = {},
    ) = this inSubquery subquery(column, orderBy, extra, where)
    
    
    
    fun <I, T, O : OrderDsl<I, O>, X : ExtraDsl<I>> Field<W, TypedColumn<T>>.notInSubquery(
                                                          column: TableColumn<I, O, X, TypedColumn<T>>,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: I.()->Unit = {},
    ) = this notInSubquery subquery(column, orderBy, extra, where)
    
    
    
    /** Correlates the selected inner and outer columns. With no projection, selects 1 for EXISTS. */
    fun <I, T, O : OrderDsl<I, O>, X : ExtraDsl<I>> subquery(
                                         innerColumn: TableColumn<I, O, X, TypedColumn<T>>,
                                             outerColumn: Field<W, TypedColumn<T>>,
                                                  column: FieldSelector<I, TypedColumn<*>>? = null,
                                                 orderBy: O.()->Unit = {},
                                                   extra: X.()->Unit = {},
                                                   where: I.()->Unit = {},
    ): Subquery {
        val table = innerColumn.table
        val outer = context.qualifier
        val from = table.name
        val qualifier = if (from.equals(outer, ignoreCase = true)) {
            if (outer.equals("vk_inner", ignoreCase = true)) "vk_inner2" else "vk_inner"
        } else from
        val source = if (qualifier == from) from else "$from AS $qualifier"
        val innerName = table.columnSql(innerColumn.selector)
        val outerName = outerColumn.definition.name
        val projection = column?.let { table.columnSql(it) } ?: "1"
        
        val predicate = table.predicate(qualifier, where)
        val correlation = WhereDslBase()
        correlation.conditionRaw("$qualifier.$innerName", "=", "$outer.$outerName")
        if (predicate.clauses.isNotEmpty()) {
            correlation.clauses += "AND (${predicate.buildStr()})"
            correlation.args += predicate.args
        }
        val result = table.render(source, qualifier, arrayOf(projection), correlation, orderBy, extra)
        return Subquery(result.query, result.args)
    }
    
    
    fun exists(subquery: Subquery) = rawClause("EXISTS (${subquery.query})", *subquery.args)
    fun notExists(subquery: Subquery) = rawClause("NOT EXISTS (${subquery.query})", *subquery.args)
    
    fun <I, O : OrderDsl<I, O>, X : ExtraDsl<I>> exists(              table: DslTable<*, I, O, X>,
                                                                    orderBy: O.()->Unit = {},
                                                                      extra: X.()->Unit = {},
                                                                      where: I.()->Unit = {},
    ) = exists(table.existenceQuery(orderBy, extra, where))

    fun <I, O : OrderDsl<I, O>, X : ExtraDsl<I>> notExists(           table: DslTable<*, I, O, X>,
                                                                    orderBy: O.()->Unit = {},
                                                                      extra: X.()->Unit = {},
                                                                      where: I.()->Unit = {},
    ) = notExists(table.existenceQuery(orderBy, extra, where))

    infix fun Subquery.equal(value: String) = builder.subqueryCondition(this, "=", value.toWhereArg())
    infix fun Subquery.equal(value: Boolean) = builder.subqueryCondition(this, "=", value.toWhereArg())
    infix fun Subquery.equal(value: Number) = builder.subqueryCondition(this, "=", value.toString())
    
    infix fun Subquery.notEqual(value: String) = builder.subqueryCondition(this, "!=", value.toWhereArg())
    infix fun Subquery.notEqual(value: Boolean) = builder.subqueryCondition(this, "!=", value.toWhereArg())
    infix fun Subquery.notEqual(value: Number) = builder.subqueryCondition(this, "!=", value.toString())
    
    infix fun Subquery.greater(value: Number) = builder.subqueryCondition(this, ">", value.toString())
    infix fun Subquery.greaterEqual(value: Number) = builder.subqueryCondition(this, ">=", value.toString())
    infix fun Subquery.less(value: Number) = builder.subqueryCondition(this, "<", value.toString())
    infix fun Subquery.lessEqual(value: Number) = builder.subqueryCondition(this, "<=", value.toString())
    
    infix fun Subquery.equal(other: Subquery) = builder.subqueryConditionSubquery(this, "=", other)
    infix fun Subquery.notEqual(other: Subquery) = builder.subqueryConditionSubquery(this, "!=", other)
    infix fun Subquery.equalCol(other: Field<W, *>) = builder.subqueryConditionRaw(this, "=", other.definition.name)
    infix fun Subquery.notEqualCol(other: Field<W, *>) = builder.subqueryConditionRaw(this, "!=", other.definition.name)
    infix fun Subquery.greaterCol(other: Field<W, *>) = builder.subqueryConditionRaw(this, ">", other.definition.name)
    infix fun Subquery.greaterEqualCol(other: Field<W, *>) = builder.subqueryConditionRaw(this, ">=", other.definition.name)
    infix fun Subquery.lessCol(other: Field<W, *>) = builder.subqueryConditionRaw(this, "<", other.definition.name)
    infix fun Subquery.lessEqualCol(other: Field<W, *>) = builder.subqueryConditionRaw(this, "<=", other.definition.name)
}
