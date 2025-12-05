package com.vankorno.vankornodb.core

import com.vankorno.vankornodb.core.data.QueryOptsHolder

class QueryOpts {
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
    
    
    
    fun orderBy(by: String) { query.orderBy = by }
    
    var orderBy: String
        get() = query.orderBy
        set(value) { query.orderBy = value }
    
    
    
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
    
    
    
    fun applyOpts(opts: QueryOpts.()->Unit) { this.opts() }
    
    
    fun applyOpts(                                                joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
    ) {
        this.joins(joins)
        this.where(where)
        if (groupBy.isNotBlank()) this.groupBy(groupBy)
        if (having.isNotBlank()) this.having(having)
        if (orderBy.isNotBlank()) this.orderBy(orderBy)
        if (limit != null) this.limit(limit)
        if (offset != null) this.offset(offset)
        if (customEnd.isNotBlank()) this.customEnd(customEnd)
    }
}








