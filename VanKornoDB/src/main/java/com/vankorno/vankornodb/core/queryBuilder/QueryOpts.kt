// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.core.queryBuilder

import com.vankorno.vankornodb.api.JoinBuilder
import com.vankorno.vankornodb.api.OrderByBuilder
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.core.data.QueryOptsHolder
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition

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
    
    fun orderBy(vararg columns: BaseColumn) { query.orderBy = { col(*columns) } }
    
    fun orderBy(vararg strings: String) { query.orderBy = { raw(*strings) } }
    
    
    fun orderRandomly() = orderBy(RANDOM)
    
    fun orderById() = orderBy(cID)
    fun orderByIdAnd(andOrderBy: OrderByBuilder.()->Unit) = orderBy { cID(); and(andOrderBy) }
    
    fun orderByName() = orderBy(cName)
    fun orderByNameAnd(andOrderBy: OrderByBuilder.()->Unit) = orderBy { cName(); and(andOrderBy) }
    
    fun orderByPosition() = orderBy(cPosition)
    fun orderByPositionAnd(andOrderBy: OrderByBuilder.()->Unit) = orderBy { cPosition(); and(andOrderBy) }
    
    fun orderByFlippedRows() = orderBy(RowID + DESCENDING)
    
    
    fun IntCol.flip() = DescendingIntCol(this.name)
    fun StrCol.flip() = DescendingStrCol(this.name)
    fun BoolCol.flip() = DescendingBoolCol(this.name)
    fun LongCol.flip() = DescendingLongCol(this.name)
    fun FloatCol.flip() = DescendingFloatCol(this.name)
    
    fun IntCol.flipIf(condition: Boolean) = if (condition) this.flip() else this
    fun StrCol.flipIf(condition: Boolean) = if (condition) this.flip() else this
    fun BoolCol.flipIf(condition: Boolean) = if (condition) this.flip() else this
    fun LongCol.flipIf(condition: Boolean) = if (condition) this.flip() else this
    fun FloatCol.flipIf(condition: Boolean) = if (condition) this.flip() else this
    
    
    
    
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








