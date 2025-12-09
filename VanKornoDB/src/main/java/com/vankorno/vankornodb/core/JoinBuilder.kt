package com.vankorno.vankornodb.core

@Suppress("unused")
open class JoinBuilderInternal {
    val joins = mutableListOf<String>()

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
