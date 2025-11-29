package com.vankorno.vankornodb.core.data

/**
 * Holds the query string and an array of String args that you need for functions like db.getQuery()
 */
data class QueryWithArgs(
                                 val query: String,
                                  val args: Array<String>,
)
