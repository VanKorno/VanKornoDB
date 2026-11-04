/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.api.JoinDsl
import com.vankorno.vankornodb.core.data.ExtraDslHolder

/**
 *  Internal base — use [com.vankorno.vankornodb.api.ExtraDsl] from the api package instead
 */
@Suppress("unused")
open class ExtraDslInternal {
    val query = ExtraDslHolder()
    
    fun joins(joinDsl: JoinDsl.()->Unit) { query.joins = joinDsl }
    
    var joins: JoinDsl.() -> Unit
        get() = query.joins
        set(value) { query.joins = value }
    
    
    fun groupBy(by: String) { query.groupBy = by }
    
    var groupBy: String
        get() = query.groupBy
        set(value) { query.groupBy = value }
    
    
    fun having(having: String) { query.having = having }
    
    var having: String
        get() = query.having
        set(value) { query.having = value }
    
    
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
    
    
    
    fun applyDsl(                                                     joins: JoinDsl.()->Unit = {},
                                                                    groupBy: String = "",
                                                                     having: String = "",
                                                                      limit: Int? = null,
                                                                     offset: Int? = null,
                                                                  customEnd: String = "",
    ) {
        this.joins(joins)
        if (groupBy.isNotBlank()) this.groupBy(groupBy)
        if (having.isNotBlank()) this.having(having)
        if (limit != null) this.limit(limit)
        if (offset != null) this.offset(offset)
        if (customEnd.isNotBlank()) this.customEnd(customEnd)
    }
}



