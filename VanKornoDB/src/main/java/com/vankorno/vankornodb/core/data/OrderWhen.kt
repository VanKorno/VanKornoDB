/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.data

class OrderWhen<W, O> internal constructor(
                              internal val orders: O.()->Unit,
                               internal val whens: W.()->Unit,
)
