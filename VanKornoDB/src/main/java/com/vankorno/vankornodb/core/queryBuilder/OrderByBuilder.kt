package com.vankorno.vankornodb.core.queryBuilder

import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

open class OrderByBuilderInternal {
    val orderoids = mutableListOf<String>()
    val args = mutableListOf<String>()
    
    operator fun TypedColumn<*>.unaryPlus() { orderoids += name }
    operator fun TypedColumn<*>.unaryMinus() { orderoids += name + " DESC" }
    
    operator fun String.unaryPlus() { orderoids += this }
    
    
    operator fun WhereBuilder.unaryPlus() {
        val inner = this
        orderoids += "WHERE " + inner.buildStr()
        args += inner.args
    }
    
    operator fun (WhereBuilder.()->Unit).unaryPlus() {
        val builder = WhereBuilder().apply(this)
        +builder
    }
    
    
    
    internal fun buildStr(): String = orderoids.joinToString(", ")
}





