/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.data

import com.vankorno.vankornodb.api.JoinDsl


data class ExtraDslHolder(
                                 var joins: JoinDsl.()->Unit = {},
                               var groupBy: String = "",
                                var having: String = "",
                                 var limit: Int? = null,
                                var offset: Int? = null,
                             var customEnd: String = "", /* To pass your own string. */
)

