// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.core.data

import com.vankorno.vankornodb.api.OrderByBuilder
import com.vankorno.vankornodb.api.WhereBuilder

data class OrderWhen(
                                val orders: OrderByBuilder.()->Unit,
                                 val whens: WhereBuilder.()->Unit,
)
