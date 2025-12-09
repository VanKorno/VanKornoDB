package com.vankorno.vankornodb.dbManagement.data
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

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




