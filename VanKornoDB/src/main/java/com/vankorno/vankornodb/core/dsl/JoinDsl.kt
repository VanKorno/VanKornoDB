/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

/**
 *  Internal base — use `JoinDsl` from the api package instead
 */
@Suppress("unused")
open class JoinDslInternal {
    val joins = mutableListOf<String>()
    val args = mutableListOf<String>()
    
    internal fun buildStr(): String = joins.joinToString(" ")
    
    

    fun inner(table: String, on: String) {
        joins.add("INNER JOIN $table ON $on")
    }

    fun left(table: String, on: String) {
        joins.add("LEFT JOIN $table ON $on")
    }

    fun cross(table: String) {
        joins.add("CROSS JOIN $table")
    }
}
