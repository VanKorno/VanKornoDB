/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.api

import com.vankorno.vankornodb.core.data.QueryWithArgs
import com.vankorno.vankornodb.core.dsl.getQuery
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

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
                                                            internal val builder: WhereDsl,
                                                          internal val qualifier: String,
)



/** A physical table, its mapping, and the factory for its generated entity receiver.
 */
open class EntityTable<out I : TableInfoBase<*>, W> internal constructor(
                                                                     val info: I,
                                                          internal val factory: (DslContext<W>)->W,
) {
    val name get() = info.name
    
    
    /** Also adapts a scoped predicate for existing operations that take a WhereDsl lambda.
     */
    fun where(block: W.()->Unit): WhereDsl.()->Unit = where(name, block)
    
    
    internal fun where(                                                      qualifier: String,
                                                                                 block: W.()->Unit,
    ): WhereDsl.()->Unit = {
        factory(DslContext(this@EntityTable, this, qualifier)).block()
    }
    
    
    
    private fun selectionScope() = factory(DslContext(this, WhereDsl(), name))
    
    
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


class QueryTable<out I : TableInfoBase<*>, W, O : OrderDsl, X : ExtraDsl> internal constructor(
                                                              info: I,
                                                           factory: (DslContext<W>)->W,
                                          private val orderFactory: (W)->O,
                                          private val extraFactory: (W)->X,
) : EntityTable<I, W>(info, factory) {
    internal fun query(                         columns: Array<out ColumnSelector<W, TypedColumn<*>>>,
                                                orderBy: O.()->Unit,
                                                  extra: X.()->Unit,
                                                  where: W.()->Unit,
    ): QueryWithArgs {
        val context = DslContext(this, WhereDsl(), name)
        val scope = factory(context)
        val options = extraFactory(scope).apply(extra)
        val ordering = orderFactory(scope).apply(orderBy)
        val names = if (columns.isEmpty()) arrayOf("*") else
            Array(columns.size) { idx -> columns[idx](scope).definition.name }
        
        scope.where()
        return getQuery(name, names, ordering, options.query, context.builder)
    }
}


fun <E : BaseEntity, I : TableInfoBase<E>, W : EntityDsl<E, W>, O : OrderDsl, X : ExtraDsl> I.withDsl(
                                                            where: (DslContext<W>)->W,
                                                            order: (W)->O,
                                                            extra: (W)->X,
) = QueryTable(this, where, order, extra)
