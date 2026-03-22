/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.data

interface EntityColumnsInternal {
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




