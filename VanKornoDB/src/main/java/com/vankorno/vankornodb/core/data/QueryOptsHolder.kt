package com.vankorno.vankornodb.core.data
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import com.vankorno.vankornodb.api.JoinBuilder
import com.vankorno.vankornodb.api.WhereBuilder


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

