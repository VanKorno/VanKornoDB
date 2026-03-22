/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.data

import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl

data class OrderWhen(
                                val orders: OrderDsl.()->Unit,
                                 val whens: WhereDsl.()->Unit,
)
