package com.vankorno.vankornodb.core.data

import com.vankorno.vankornodb.JoinBuilder
import com.vankorno.vankornodb.WhereBuilder


data class QueryOptsHolder(
                                 var joins: JoinBuilder.()->Unit = {},
                                 var where: WhereBuilder.()->Unit = {},
                               var groupBy: String = "",
                                var having: String = "",
                               var orderBy: String = "",
                                 var limit: Int? = null,
                                var offset: Int? = null,
                             var customEnd: String = "", /* To pass your own string. */
)

