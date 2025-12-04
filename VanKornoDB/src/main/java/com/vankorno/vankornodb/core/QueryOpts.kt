package com.vankorno.vankornodb.core

import com.vankorno.vankornodb.core.data.QueryOptsHolder

class QueryOpts {
    val query = QueryOptsHolder()
    
    
    fun joins(builder: JoinBuilder.()->Unit) { query.joins = builder }
    
    fun where(builder: WhereBuilder.()->Unit) { query.where = builder }
    
    fun groupBy(by: String) { query.groupBy = by }
    
    fun having(having: String) { query.having = having }
    
    fun orderBy(by: String) { query.orderBy = by }
    
    fun limit(limit: Int) { query.limit = limit }
    
    fun offset(offset: Int) { query.offset = offset }
    
    fun customEnd(queryStr: String) { query.customEnd = queryStr }
    
    
    
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





