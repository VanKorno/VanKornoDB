/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.core.data.QueryWithArgs
import com.vankorno.vankornodb.core.data.Subquery
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.set.dsl.SetDsl

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
annotation class DslInject


/** The owner is invariant so columns from different entity scopes cannot be interchanged.
 */
sealed class SelectExpr<W, out C : TypedColumn<*>>(internal val sql: String)


class Field<W, out C : TypedColumn<*>> internal constructor(internal val definition: C) :
    SelectExpr<W, C>(definition.name)

private class RawExpr<W>(sql: String) : SelectExpr<W, Nothing>(sql)

/** Emits the supplied SQL verbatim in a column selection. */
fun <W> raw(sql: String): SelectExpr<W, Nothing> = RawExpr(sql)


open class TableColumns<W, O : OrderDsl<W, O>, X : ExtraDsl<W>, out C : TypedColumn<*>> internal constructor(
    internal val table: DslTable<*, W, O, X>,
    internal val selectors: Array<out FieldSelector<W, C>>,
)

class TableColumn<W, O : OrderDsl<W, O>, X : ExtraDsl<W>, out C : TypedColumn<*>> internal constructor(
    table: DslTable<*, W, O, X>,
    internal val selector: FieldSelector<W, C>,
) : TableColumns<W, O, X, C>(table, arrayOf(selector))


typealias FieldSelector<W, C> = W.()->SelectExpr<W, C>



/** The library supplies one context to each generated receiver.
 */
class DslContext<W> internal constructor(                    internal val table: MiniDslTable<*, W>,
                                                           internal val builder: WhereDslBase,
                                                         internal val qualifier: String,
)


/** A physical table, its mapping, and the factory for its generated entity receiver.
 */
open class MiniDslTable<out I : TableInfo<*>, W> internal constructor(
                                                                       val info: I,
                                                           internal val factory: (DslContext<W>)->W,
) {
    val name get() = info.name
    
    
    internal fun predicate(                                                  qualifier: String,
                                                                                 block: W.()->Unit,
    ): WhereDslBase {
        val state = WhereDslBase()
        factory(DslContext(this, state, qualifier)).block()
        return state
    }
    
    fun where(                                                                     block: W.()->Unit
    ): QueryWithArgs {
        val predicate = predicate(name, block)
        return QueryWithArgs(predicate.buildStr(), predicate.args.toTypedArray())
    }
    
    fun <S : SetDsl<W>> set(                                                   factory: (W)->S,
                                                                               actions: S.()->Unit,
    ) = factory(selectionScope()).apply(actions).ops
    
    
    internal fun selectionScope() = factory(DslContext(this, WhereDslBase(), name))
    
    
    /** Selects one schema descriptor for APIs that accept a typed column directly.
     */
    fun <C : TypedColumn<*>> select(column: W.()->Field<W, C>): C =
        selectionScope().column().definition
    
    
    
    internal fun columnSql(column: FieldSelector<W, *>): String = selectionScope().column().sql

    internal fun select(                      columns: Array<out FieldSelector<W, TypedColumn<*>>>
    ): Array<String> {
        val scope = selectionScope()
        return Array(columns.size) { idx -> columns[idx](scope).sql }
    }
}


open class DslTable<out I : TableInfo<*>, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> internal constructor(
                                                                           info: I,
                                                                        factory: (DslContext<W>)->W,
                                                       private val orderFactory: (W)->O,
                                                       private val extraFactory: (W)->X,
) : MiniDslTable<I, W>(info, factory) {
    fun <C : TypedColumn<*>> col(block: FieldSelector<W, C>): TableColumn<W, O, X, C> {
        val selected = selectionScope().block()
        return TableColumn(this) { selected }
    }
    
    fun <C : TypedColumn<*>> columns(block: W.()->List<SelectExpr<W, C>>): TableColumns<W, O, X, C> {
        val selected = selectionScope().block()
        require(selected.isNotEmpty()) { "An explicit projection must contain at least one column" }
        val selectors = Array<FieldSelector<W, C>>(selected.size) { idx ->
            val column = selected[idx]
            { column }
        }
        return TableColumns(this, selectors)
    }
    
    internal fun query(        columns: Array<out FieldSelector<W, TypedColumn<*>>> = emptyArray(),
                               orderBy: O.()->Unit = {},
                                 extra: X.()->Unit = {},
                                 where: W.()->Unit = {},
    ): QueryWithArgs {
        val names = if (columns.isEmpty()) arrayOf("*") else select(columns)
        return render(name, name, names, predicate(name, where), orderBy, extra)
    }
    
    internal fun existenceQuery(                                               orderBy: O.()->Unit,
                                                                                 extra: X.()->Unit,
                                                                                 where: W.()->Unit,
    ): Subquery {
        val result = render(name, name, arrayOf("1"), predicate(name, where), orderBy, extra)
        return Subquery(result.query, result.args)
    }
    
    internal fun render(                                                     source: String,
                                                                          qualifier: String,
                                                                            columns: Array<String>,
                                                                              where: WhereDslBase,
                                                                            orderBy: O.()->Unit,
                                                                              extra: X.()->Unit,
    ): QueryWithArgs {
        val context = DslContext(this, WhereDslBase(), qualifier)
        val scope = factory(context)
        fun ordering(): O = orderFactory(scope).also {
            it.context = context
            it.nested = ::ordering
        }
        val options = extraFactory(scope)
        options.context = context
        options.extra()
        return getQuery(source, columns, ordering().apply(orderBy), options.query, where)
    }
}


class FullDslTable<out I : TableInfo<*>, W, O : OrderDsl<W, O>, X : ExtraDsl<W>, S : SetDsl<W>> internal constructor(
                                                                           info: I,
                                                                          where: (DslContext<W>)->W,
                                                                          order: (W)->O,
                                                                          extra: (W)->X,
                                                        internal val setFactory: (W)->S,
) : DslTable<I, W, O, X>(info, where, order, extra)

fun <E : DbEntity, I : TableInfo<E>, W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> I.withDsl(
                                                                          where: (DslContext<W>)->W,
                                                                          order: (W)->O,
                                                                          extra: (W)->X,
) = DslTable(this, where, order, extra)

fun <E : DbEntity, I : TableInfo<E>, W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>, S : SetDsl<W>> I.withDsl(
                                                                          where: (DslContext<W>)->W,
                                                                          order: (W)->O,
                                                                          extra: (W)->X,
                                                                            set: (W)->S,
) = FullDslTable(this, where, order, extra, set)
