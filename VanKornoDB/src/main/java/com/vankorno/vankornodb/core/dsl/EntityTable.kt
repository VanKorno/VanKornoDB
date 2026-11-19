/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.QueryWithArgs
import com.vankorno.vankornodb.core.data.Subquery
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.set.dsl.SetDsl

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
annotation class DslInject


/** The owner is invariant so columns from different entity scopes cannot be interchanged.
 */
class EntityColumn<W, out C : TypedColumn<*>> internal constructor(internal val definition: C)


typealias ColumnSelector<W, C> = W.()->EntityColumn<W, C>



/** The library supplies one context to each generated receiver.
 */
class DslContext<W> internal constructor(                     internal val table: EntityTable<*, W>,
                                                            internal val builder: WhereDslBase,
                                                          internal val qualifier: String,
)



/** A physical table, its mapping, and the factory for its generated entity receiver.
 */
open class EntityTable<out I : TableInfoBase<*>, W> internal constructor(
                                                                     val info: I,
                                                          internal val factory: (DslContext<W>)->W,
) {
    val name get() = info.name
    
    
    internal fun predicate(qualifier: String, block: W.()->Unit): WhereDslBase {
        val state = WhereDslBase()
        factory(DslContext(this, state, qualifier)).block()
        return state
    }
    
    fun where(block: W.()->Unit): QueryWithArgs {
        val predicate = predicate(name, block)
        return QueryWithArgs(predicate.buildStr(), predicate.args.toTypedArray())
    }
    
    fun <S : SetDsl<W>> set(factory: (W)->S, actions: S.()->Unit) =
        factory(selectionScope()).apply(actions).ops
    
    private fun selectionScope() = factory(DslContext(this, WhereDslBase(), name))
    
    
    /** Selects one schema descriptor for APIs that accept a typed column directly.
     */
    fun <C : TypedColumn<*>> select(column: ColumnSelector<W, C>): C =
        selectionScope().column().definition
    
    
    
    internal fun select(                      columns: Array<out ColumnSelector<W, TypedColumn<*>>>
    ): Array<TypedColumn<*>> {
        val scope = selectionScope()
        return Array(columns.size) { idx -> columns[idx](scope).definition }
    }
}


open class QueryTable<out I : TableInfoBase<*>, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> internal constructor(
                                                                           info: I,
                                                                        factory: (DslContext<W>)->W,
                                                       private val orderFactory: (W)->O,
                                                       private val extraFactory: (W)->X,
) : EntityTable<I, W>(info, factory) {
    internal fun query(        columns: Array<out ColumnSelector<W, TypedColumn<*>>> = emptyArray(),
                               orderBy: O.()->Unit = {},
                                 extra: X.()->Unit = {},
                                 where: W.()->Unit = {},
    ): QueryWithArgs {
        val names = if (columns.isEmpty()) arrayOf("*") else select(columns).map { it.name }.toTypedArray()
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


class UpdateTable<out I : TableInfoBase<*>, W, O : OrderDsl<W, O>, X : ExtraDsl<W>, S : SetDsl<W>> internal constructor(
                                                                                info: I,
                                                                               where: (DslContext<W>)->W,
                                                                               order: (W)->O,
                                                                               extra: (W)->X,
                                                             internal val setFactory: (W)->S,
) : QueryTable<I, W, O, X>(info, where, order, extra)

fun <E : BaseEntity, I : TableInfoBase<E>, W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> I.withDsl(
                                                                          where: (DslContext<W>)->W,
                                                                          order: (W)->O,
                                                                          extra: (W)->X,
) = QueryTable(this, where, order, extra)

fun <E : BaseEntity, I : TableInfoBase<E>, W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>, S : SetDsl<W>> I.withDsl(
                                                                               where: (DslContext<W>)->W,
                                                                               order: (W)->O,
                                                                               extra: (W)->X,
                                                                                 set: (W)->S,
) = UpdateTable(this, where, order, extra, set)
