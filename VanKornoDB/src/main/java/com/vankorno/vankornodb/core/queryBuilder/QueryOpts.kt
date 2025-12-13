package com.vankorno.vankornodb.core.queryBuilder
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import com.vankorno.vankornodb.api.JoinBuilder
import com.vankorno.vankornodb.api.OrderByBuilder
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.QueryOptsHolder
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

@Suppress("unused")
open class QueryOptsInternal {
    val query = QueryOptsHolder()
    
    
    fun joins(builder: JoinBuilder.()->Unit) { query.joins = builder }
    
    var joins: JoinBuilder.() -> Unit
        get() = query.joins
        set(value) { query.joins = value }
    
    
    
    fun where(builder: WhereBuilder.()->Unit) { query.where = builder }
    
    var where: WhereBuilder.() -> Unit
        get() = query.where
        set(value) { query.where = value }
    
    
    
    fun groupBy(by: String) { query.groupBy = by }
    
    var groupBy: String
        get() = query.groupBy
        set(value) { query.groupBy = value }
    
    
    
    fun having(having: String) { query.having = having }
    
    var having: String
        get() = query.having
        set(value) { query.having = value }
    
    
    
    
    var orderBy: OrderByBuilder.()->Unit
        get() = query.orderBy
        set(value) { query.orderBy = value }
    
    fun orderBy(builder: OrderByBuilder.()->Unit) {
        query.orderBy = builder
    }
    
    fun orderBy(vararg cols: TypedColumn<*>) {
        query.orderBy = {
            for (col in cols) {
                +col
            }
        }
    }
    fun orderBy(vararg strings: String) {
        query.orderBy = {
            for (str in strings) {
                +str
            }
        }
    }
    
    fun orderRandomly() = orderBy { +"RANDOM()" }
    
    
    
    
    
    
    
    
    fun limit(limit: Int) { query.limit = limit }
    
    var limit: Int?
        get() = query.limit
        set(value) { query.limit = value }
    
    
    
    fun offset(offset: Int) { query.offset = offset }
    
    var offset: Int?
        get() = query.offset
        set(value) { query.offset = value }
    
    
    
    fun customEnd(queryStr: String) { query.customEnd = queryStr }
    
    var customEnd: String
        get() = query.customEnd
        set(value) { query.customEnd = value }
    
    
    
    
    fun applyOpts(                                              joins: JoinBuilder.()->Unit = {},
                                                                where: WhereBuilder.()->Unit = {},
                                                              groupBy: String = "",
                                                               having: String = "",
                                                              orderBy: OrderByBuilder.()->Unit = {},
                                                                limit: Int? = null,
                                                               offset: Int? = null,
                                                            customEnd: String = "",
    ) {
        this.joins(joins)
        this.where(where)
        this.orderBy(orderBy)
        if (groupBy.isNotBlank()) this.groupBy(groupBy)
        if (having.isNotBlank()) this.having(having)
        if (limit != null) this.limit(limit)
        if (offset != null) this.offset(offset)
        if (customEnd.isNotBlank()) this.customEnd(customEnd)
    }
}








