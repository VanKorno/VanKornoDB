package com.vankorno.vankornodb.core.queryBuilder

import com.vankorno.vankornodb.api.OrderByBuilder
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.WhereAndOrder
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

open class OrderByBuilderInternal {
    val orderoids = mutableListOf<String>()
    val args = mutableListOf<String>()
    
    internal fun buildStr(): String = orderoids.joinToString(", ")
    
    
    operator fun TypedColumn<*>.invoke() {
        orderoids += this.name
    }
    fun TypedColumn<*>.desc() {
        orderoids += this.name + " DESC"
    }
    
    fun asc(vararg columns: TypedColumn<*>) = columns.forEach { orderoids += it.name }
    
    fun desc(vararg columns: TypedColumn<*>) = columns.forEach { orderoids += it.name + " DESC" }
    
    fun raw(vararg strings: String) = strings.forEach { orderoids += it }
    
    
    /*operator fun WhereBuilder.unaryPlus() {
        val inner = this
        orderoids += "WHERE " + inner.buildStr()
        args += inner.args
    }
    
    operator fun (WhereBuilder.()->Unit).unaryPlus() {
        val builder = WhereBuilder().apply(this)
        +builder
    }*/
    
    infix fun (WhereBuilder.()->Unit).then(orderBy: OrderByBuilder.()->Unit): WhereAndOrder {
        val condBuilder = WhereBuilder().apply(this)
        val orderBuilder = OrderByBuilder().apply(orderBy)
        return WhereAndOrder(condBuilder, orderBuilder)
    }
    
    
    
    
    
    
    
    
    
    
}





