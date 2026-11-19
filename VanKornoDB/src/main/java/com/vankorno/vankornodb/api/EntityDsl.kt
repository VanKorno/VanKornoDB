/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.api

import com.vankorno.vankornodb.core.data.DbConstants.IN
import com.vankorno.vankornodb.core.data.DbConstants.NOT_IN
import com.vankorno.vankornodb.core.dsl.toWhereArg
import com.vankorno.vankornodb.dbManagement.data.*

/** Generated subclasses expose their schema's columns. The current query owns [builder]. */
@DslInject
abstract class EntityDsl<E : BaseEntity, W : EntityDsl<E, W>>(
                                                              private val context: DslContext<W>,
) {
    private val builder get() = context.builder
    
    
    protected fun <C : TypedColumn<*>> column(definition: C) = EntityColumn<W, C>(definition)
    
    
    protected fun <C : TypedColumn<*>> column(                               definitions: List<C>
    ): List<EntityColumn<W, C>> =
        object : AbstractList<EntityColumn<W, C>>() {
            override val size get() = definitions.size
            override fun get(index: Int) = EntityColumn<W, C>(definitions[index])
        }
    
    
    fun group(where: W.()->Unit) = builder.group(context.table.where(context.qualifier, where))
    fun and(where: W.()->Unit) = builder.and(context.table.where(context.qualifier, where))
    fun or(where: W.()->Unit) = builder.or(context.table.where(context.qualifier, where))
    
    infix fun <T> EntityColumn<W, AscendingColumn<T>>.equal(value: T) = with(builder) { definition.name equal value }
    infix fun <T> EntityColumn<W, AscendingColumn<T>>.notEqual(value: T) = with(builder) { definition.name notEqual value }
    
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.greater(value: T) = with(builder) { definition.name greater value }
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.greaterEqual(value: T) = with(builder) { definition.name greaterEqual value }
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.less(value: T) = with(builder) { definition.name less value }
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.lessEqual(value: T) = with(builder) { definition.name lessEqual value }
    
    infix fun <T> EntityColumn<W, AscendingColumn<T>>.equalCol(other: EntityColumn<W, AscendingColumn<T>>) = with(builder) { definition.name equalCol other.definition }
    infix fun <T> EntityColumn<W, AscendingColumn<T>>.notEqualCol(other: EntityColumn<W, AscendingColumn<T>>) = with(builder) { definition.name notEqualCol other.definition }
    
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.greaterCol(other: EntityColumn<W, NumberColumn<T>>) = with(builder) { definition.name greaterCol other.definition }
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.greaterEqualCol(other: EntityColumn<W, NumberColumn<T>>) = with(builder) { definition.name greaterEqualCol other.definition }
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.lessCol(other: EntityColumn<W, NumberColumn<T>>) = with(builder) { definition.name lessCol other.definition }
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.lessEqualCol(other: EntityColumn<W, NumberColumn<T>>) = with(builder) { definition.name lessEqualCol other.definition }
    
    infix fun EntityColumn<W, StrCol>.like(value: String) = with(builder) { definition like value }
    infix fun EntityColumn<W, StrCol>.notLike(value: String) = with(builder) { definition notLike value }
    infix fun EntityColumn<W, StrCol>.likeCol(other: EntityColumn<W, StrCol>) = with(builder) { definition likeCol other.definition }
    infix fun EntityColumn<W, StrCol>.notLikeCol(other: EntityColumn<W, StrCol>) = with(builder) { definition notLikeCol other.definition }
    
    fun EntityColumn<W, StrCol>.likeAny(vararg values: String) = with(builder) { definition.likeAny(*values) }
    fun EntityColumn<W, StrCol>.notLikeAny(vararg values: String) = with(builder) { definition.notLikeAny(*values) }
    fun EntityColumn<W, StrCol>.likeAnyCol(vararg others: EntityColumn<W, StrCol>) = with(builder) { definition.likeAnyCol(*others.map { it.definition }.toTypedArray()) }
    fun EntityColumn<W, StrCol>.notLikeAnyCol(vararg others: EntityColumn<W, StrCol>) = with(builder) { definition.notLikeAnyCol(*others.map { it.definition }.toTypedArray()) }
    
    
    private fun EntityColumn<W, *>.compareAny(                           operator: String,
                                                                           values: Array<out Any?>,
    ) {
        builder.multCompare(definition.name, operator, values) { it.toWhereArg() }
    }
    
    
    infix fun <T> EntityColumn<W, AscendingColumn<T>>.equalAny(values: Array<out T>) = compareAny(IN, values)
    infix fun <T> EntityColumn<W, AscendingColumn<T>>.notEqualAny(values: Array<out T>) = compareAny(NOT_IN, values)
    infix fun <T> EntityColumn<W, AscendingColumn<T>>.equalAny(values: List<T>) = compareAny(IN, values.toTypedArray<Any?>())
    infix fun <T> EntityColumn<W, AscendingColumn<T>>.notEqualAny(values: List<T>) = compareAny(NOT_IN, values.toTypedArray<Any?>())
    fun EntityColumn<W, IntCol>.equalAnyOf(vararg values: Int) = with(builder) { definition.equalAnyOf(*values) }
    fun EntityColumn<W, LongCol>.equalAnyOf(vararg values: Long) = with(builder) { definition.equalAnyOf(*values) }
    fun EntityColumn<W, FloatCol>.equalAnyOf(vararg values: Float) = with(builder) { definition.equalAnyOf(*values) }
    fun EntityColumn<W, BoolCol>.equalAnyOf(vararg values: Boolean) = with(builder) { definition.equalAnyOf(*values) }
    fun EntityColumn<W, StrCol>.equalAnyOf(vararg values: String) = with(builder) { definition.equalAnyOf(*values) }
    
    fun EntityColumn<W, IntCol>.notEqualAnyOf(vararg values: Int) = with(builder) { definition.notEqualAnyOf(*values) }
    fun EntityColumn<W, LongCol>.notEqualAnyOf(vararg values: Long) = with(builder) { definition.notEqualAnyOf(*values) }
    fun EntityColumn<W, FloatCol>.notEqualAnyOf(vararg values: Float) = with(builder) { definition.notEqualAnyOf(*values) }
    fun EntityColumn<W, BoolCol>.notEqualAnyOf(vararg values: Boolean) = with(builder) { definition.notEqualAnyOf(*values) }
    fun EntityColumn<W, StrCol>.notEqualAnyOf(vararg values: String) = with(builder) { definition.notEqualAnyOf(*values) }
    
    infix fun EntityColumn<W, TypedColumn<*>>.equal(subquery: Subquery) = with(builder) { definition equal subquery }
    infix fun EntityColumn<W, TypedColumn<*>>.notEqual(subquery: Subquery) = with(builder) { definition notEqual subquery }
    infix fun EntityColumn<W, NumberColumn<*>>.greater(subquery: Subquery) = with(builder) { definition greater subquery }
    infix fun EntityColumn<W, NumberColumn<*>>.greaterEqual(subquery: Subquery) = with(builder) { definition greaterEqual subquery }
    infix fun EntityColumn<W, NumberColumn<*>>.less(subquery: Subquery) = with(builder) { definition less subquery }
    infix fun EntityColumn<W, NumberColumn<*>>.lessEqual(subquery: Subquery) = with(builder) { definition lessEqual subquery }
    infix fun EntityColumn<W, TypedColumn<*>>.inSubquery(subquery: Subquery) = with(builder) { definition inSubquery subquery }
    infix fun EntityColumn<W, TypedColumn<*>>.notInSubquery(subquery: Subquery) = with(builder) { definition notInSubquery subquery }
    infix fun EntityColumn<W, TypedColumn<*>>.equalAny(subquery: Subquery) = this inSubquery subquery
    infix fun EntityColumn<W, TypedColumn<*>>.notEqualAny(subquery: Subquery) = this notInSubquery subquery
    
    
    
    fun <I, T> EntityColumn<W, TypedColumn<T>>.inSubquery(
                                                  table: EntityTable<*, I>,
                                                     column: ColumnSelector<I, TypedColumn<T>>,
                                                    orderBy: @DslInject OrderDsl.()->Unit = {},
                                                      extra: @DslInject ExtraDsl.()->Unit = {},
                                                      where: I.()->Unit = {},
    ) = this inSubquery subquery(table, column, orderBy, extra, where)
    
    
    
    fun <I, T> EntityColumn<W, TypedColumn<T>>.notInSubquery(
                                                  table: EntityTable<*, I>,
                                                     column: ColumnSelector<I, TypedColumn<T>>,
                                                    orderBy: @DslInject OrderDsl.()->Unit = {},
                                                      extra: @DslInject ExtraDsl.()->Unit = {},
                                                      where: I.()->Unit = {},
    ) = this notInSubquery subquery(table, column, orderBy, extra, where)
    
    
    
    fun <I> subquery(                                 table: EntityTable<*, I>,
                                                     column: ColumnSelector<I, TypedColumn<*>>,
                                                    orderBy: @DslInject OrderDsl.()->Unit = {},
                                                      extra: @DslInject ExtraDsl.()->Unit = {},
                                                      where: I.()->Unit = {},
    ): Subquery = com.vankorno.vankornodb.api.subquery(table, column, orderBy, extra, where)
    
    
    
    fun <I> subquery(                                 table: EntityTable<*, I>,
                                             vararg columns: ColumnSelector<I, TypedColumn<*>>,
                                                    orderBy: @DslInject OrderDsl.()->Unit = {},
                                                      extra: @DslInject ExtraDsl.()->Unit = {},
                                                      where: I.()->Unit = {},
    ): Subquery = com.vankorno.vankornodb.api.subquery(table, *columns, orderBy = orderBy, extra = extra, where = where)
    
    
    
    /** Correlates the selected inner and outer columns. With no projection, selects 1 for EXISTS. */
    fun <I, T> subquery(                           table: EntityTable<*, I>,
                                             outerColumn: EntityColumn<W, TypedColumn<T>>,
                                             innerColumn: ColumnSelector<I, TypedColumn<T>>,
                                                  column: ColumnSelector<I, TypedColumn<*>>? = null,
                                                 orderBy: @DslInject OrderDsl.()->Unit = {},
                                                   extra: @DslInject ExtraDsl.()->Unit = {},
                                                   where: I.()->Unit = {},
    ): Subquery {
        val outer = context.qualifier
        val from = table.name
        val qualifier = if (from.equals(outer, ignoreCase = true)) {
            if (outer.equals("vk_inner", ignoreCase = true)) "vk_inner2" else "vk_inner"
        } else from
        val source = if (qualifier == from) from else "$from AS $qualifier"
        val innerName = table.select(innerColumn).name
        val outerName = outerColumn.definition.name
        val projection = column?.let { table.select(it).name } ?: "1"
        
        return builder.subquery(source, projection, orderBy, extra) {
            conditionRaw("$qualifier.$innerName", "=", "$outer.$outerName")
            and(table.where(qualifier, where))
        }
    }
    
    
    fun exists(subquery: Subquery) = builder.exists(subquery)
    
    fun notExists(subquery: Subquery) = builder.notExists(subquery)
    
    
    fun <I> exists(                                   table: EntityTable<*, I>,
                                                    orderBy: @DslInject OrderDsl.()->Unit = {},
                                                      extra: @DslInject ExtraDsl.()->Unit = {},
                                                      where: I.()->Unit = {},
    ) = builder.exists(table.name, orderBy = orderBy, extra = extra, where = table.where(where))
    
    
    fun <I> notExists(                                table: EntityTable<*, I>,
                                                    orderBy: @DslInject OrderDsl.()->Unit = {},
                                                      extra: @DslInject ExtraDsl.()->Unit = {},
                                                      where: I.()->Unit = {},
    ) = builder.notExists(table.name, orderBy = orderBy, extra = extra, where = table.where(where))
    
    
    
    infix fun Subquery.equal(value: String) = with(builder) { this@equal equal value }
    infix fun Subquery.equal(value: Boolean) = with(builder) { this@equal equal value }
    infix fun Subquery.equal(value: Number) = builder.subqueryCondition(this, "=", value.toString())
    
    infix fun Subquery.notEqual(value: String) = with(builder) { this@notEqual notEqual value }
    infix fun Subquery.notEqual(value: Boolean) = with(builder) { this@notEqual notEqual value }
    infix fun Subquery.notEqual(value: Number) = builder.subqueryCondition(this, "!=", value.toString())
    
    infix fun Subquery.greater(value: Number) = builder.subqueryCondition(this, ">", value.toString())
    infix fun Subquery.greaterEqual(value: Number) = builder.subqueryCondition(this, ">=", value.toString())
    infix fun Subquery.less(value: Number) = builder.subqueryCondition(this, "<", value.toString())
    infix fun Subquery.lessEqual(value: Number) = builder.subqueryCondition(this, "<=", value.toString())
    
    infix fun Subquery.equal(other: Subquery) = with(builder) { this@equal equal other }
    infix fun Subquery.notEqual(other: Subquery) = with(builder) { this@notEqual notEqual other }
    infix fun Subquery.equalCol(other: EntityColumn<W, *>) = with(builder) { this@equalCol equalCol other.definition }
    infix fun Subquery.notEqualCol(other: EntityColumn<W, *>) = with(builder) { this@notEqualCol notEqualCol other.definition }
    infix fun Subquery.greaterCol(other: EntityColumn<W, *>) = with(builder) { this@greaterCol greaterCol other.definition }
    infix fun Subquery.greaterEqualCol(other: EntityColumn<W, *>) = with(builder) { this@greaterEqualCol greaterEqualCol other.definition }
    infix fun Subquery.lessCol(other: EntityColumn<W, *>) = with(builder) { this@lessCol lessCol other.definition }
    infix fun Subquery.lessEqualCol(other: EntityColumn<W, *>) = with(builder) { this@lessEqualCol lessEqualCol other.definition }
}

/** The projection and predicate both use the inner table's receiver. */
fun <W> subquery(                                     table: EntityTable<*, W>,
                                                     column: ColumnSelector<W, TypedColumn<*>>,
                                                    orderBy: @DslInject OrderDsl.()->Unit = {},
                                                      extra: @DslInject ExtraDsl.()->Unit = {},
                                                      where: W.()->Unit = {},
): Subquery = subquery(table.name, table.select(column), orderBy, extra, table.where(where))



fun <W> subquery(                                     table: EntityTable<*, W>,
                                             vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                    orderBy: @DslInject OrderDsl.()->Unit = {},
                                                      extra: @DslInject ExtraDsl.()->Unit = {},
                                                      where: W.()->Unit = {},
): Subquery = if (columns.isEmpty())
    subquery(table.name, orderBy = orderBy, extra = extra, where = table.where(where))
else
    subquery(table.name, table.select(columns), orderBy, extra, table.where(where))
