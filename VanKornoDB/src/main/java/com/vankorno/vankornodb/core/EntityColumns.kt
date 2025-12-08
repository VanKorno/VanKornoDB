package com.vankorno.vankornodb.core

import com.vankorno.vankornodb.core.data.TypedColumn

interface EntityColumns {
    val columns: List<TypedColumn<*>>
    
    fun buildColList(builder: ColumnsBuilder.()->Unit): List<TypedColumn<*>> = ColumnsBuilder().apply(builder).build()
    
    class ColumnsBuilder {
        private val list = mutableListOf<TypedColumn<*>>()
        
        operator fun TypedColumn<*>.unaryPlus() {
            list += this
        }
        operator fun Iterable<TypedColumn<*>>.unaryPlus() {
            list += this
        }
        fun build() = list
    }
}




