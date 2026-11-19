/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.Subquery

import com.vankorno.vankornodb.dbManagement.data.TypedColumn

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> subquery(     table: QueryTable<*, W, O, X>,
                                                  vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): Subquery {
    val result = table.query(columns, orderBy, extra, where)
    return Subquery(result.query, result.args)
}



fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> subquery(     table: QueryTable<*, W, O, X>,
                                                          column: ColumnSelector<W, TypedColumn<*>>,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
) = subquery(table, *arrayOf(column), orderBy = orderBy, extra = extra, where = where)
