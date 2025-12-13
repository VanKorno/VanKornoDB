package com.vankorno.vankornodb.core.queryBuilder

import com.vankorno.vankornodb.dbManagement.data.TypedColumn

open class OrderByBuilderInternal {
    val items = mutableListOf<String>()
    val args = mutableListOf<String>()
    
    operator fun TypedColumn<*>.unaryPlus() { items += name }
    operator fun TypedColumn<*>.unaryMinus() { items += name + " DESC" }
    
    operator fun String.unaryPlus() { items += this }
    
    
    
    
    
    internal fun build(): String = items.joinToString(", ")
}





