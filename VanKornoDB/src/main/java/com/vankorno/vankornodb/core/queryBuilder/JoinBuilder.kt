// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.core.queryBuilder

/**
 *  Internal base — use `JoinBuilder` from the api package instead
 */
@Suppress("unused")
open class JoinBuilderInternal {
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
