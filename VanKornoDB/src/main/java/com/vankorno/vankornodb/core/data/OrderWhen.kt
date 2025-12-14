package com.vankorno.vankornodb.core.data

import com.vankorno.vankornodb.api.OrderByBuilder
import com.vankorno.vankornodb.api.WhereBuilder

data class OrderWhen(
                                val orders: OrderByBuilder.()->Unit,
                                 val whens: WhereBuilder.()->Unit,
)
