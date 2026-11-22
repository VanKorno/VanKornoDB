/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.DbConstants.comma
import com.vankorno.vankornodb.core.data.ExtraDslHolder

@DslInject
abstract class ExtraDsl<W> : DslWithIdAndRowId<W> {
    internal val query = ExtraDslHolder()
    internal lateinit var context: DslContext<W>
    
    fun joins(block: JoinDsl<W>.()->Unit) {
        val join = JoinDsl(context).apply(block)
        query.joins += join.clauses
        query.joinArgs += join.args
    }
    
    fun groupBy(vararg columns: Field<W, *>) { query.groupBy = columns.joinToString(comma) { it.definition.name } }
    fun having(block: W.()->Unit) {
        val predicate = context.table.predicate(context.qualifier, block)
        query.having = predicate.buildStr()
        query.havingArgs = predicate.args.toTypedArray()
    }
    
    fun groupByRaw(expression: String) { query.groupBy = expression }
    fun havingRaw(expression: String, vararg args: String) {
        query.having = expression
        query.havingArgs = arrayOf(*args)
    }
    
    var limit by query::limit
    var offset by query::offset
    var customEnd by query::customEnd
    fun limit(value: Int) { limit = value }
    fun offset(value: Int) { offset = value }
    fun customEnd(sql: String) { customEnd = sql }
}
