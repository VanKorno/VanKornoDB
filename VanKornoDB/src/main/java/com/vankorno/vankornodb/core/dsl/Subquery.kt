/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.Subquery


fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> subquery(               columns: TableColumns<W, O, X, *>,
                                                                     orderBy: O.()->Unit = {},
                                                                       extra: X.()->Unit = {},
                                                                       where: W.()->Unit = {},
): Subquery {
    val result = columns.table.query(columns.selectors, orderBy, extra, where)
    return Subquery(result.query, result.args)
}
