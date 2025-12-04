package com.vankorno.vankornodb.core

import com.vankorno.vankornodb.core.data.QueryOptsHolder

class QueryOpts {
    val query = QueryOptsHolder()
    
    fun joins(builder: JoinBuilder.()->Unit) {
        query.joins = builder
    }
    
    fun where(builder: WhereBuilder.()->Unit) {
        query.where = builder
    }
    
    fun groupBy(by: String) {
        query.groupBy = by
    }
    
    fun having(having: String) {
        query.having = having
    }
    fun orderBy(by: String) {
        query.orderBy = by
    }
    fun limit(limit: Int) {
        query.limit = limit
    }
    
    fun offset(offset: Int) {
        query.offset = offset
    }
    
    fun customEnd(queryStr: String) {
        query.customEnd = queryStr
    }
    
    
    
    
    
}