package com.vankorno.vankornodb.core.queryBuilder

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
    
    
    fun case(encased: String) = "CASE$encased END" // TODO Find where to put
    
    
}





