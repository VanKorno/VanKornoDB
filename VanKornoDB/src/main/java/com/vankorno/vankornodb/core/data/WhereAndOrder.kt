package com.vankorno.vankornodb.core.data

import com.vankorno.vankornodb.api.OrderByBuilder
import com.vankorno.vankornodb.api.WhereBuilder

data class WhereAndOrder(
                             val condition: WhereBuilder,
                                 val order: OrderByBuilder,
)
