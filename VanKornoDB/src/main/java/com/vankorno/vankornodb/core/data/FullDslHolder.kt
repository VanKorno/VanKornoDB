/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.data

import com.vankorno.vankornodb.api.JoinDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl


data class FullDslHolder(
                                 var joins: JoinDsl.()->Unit = {},
                                 var where: WhereDsl.()->Unit = {},
                               var groupBy: String = "",
                                var having: String = "",
                               var orderBy: OrderDsl.()->Unit = {},
                                 var limit: Int? = null,
                                var offset: Int? = null,
                             var customEnd: String = "", /* To pass your own string. */
)

