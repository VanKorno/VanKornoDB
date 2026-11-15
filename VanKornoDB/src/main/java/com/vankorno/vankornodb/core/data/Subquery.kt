/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.data

class Subquery(
                                 val query: String,
                                  val args: Array<String> = emptyArray(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true //\/\/\/\/\/\
        if (other !is Subquery) return false //\/\/\/\/\/\
        return query == other.query && args.contentEquals(other.args)
    }
    
    override fun hashCode(): Int {
        var result = query.hashCode()
        result = 31 * result + args.contentHashCode()
        return result
    }
    
    override fun toString(): String = "Subquery(query='$query', args=${args.contentToString()})"
}
