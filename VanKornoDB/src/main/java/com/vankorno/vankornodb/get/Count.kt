/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowCount(           table: DslTable<*, W, O, X>,
                                                                        extra: X.()->Unit = {},
                                                                        where: W.()->Unit = {},
): Int = read(0, "getRowCount") { db ->
    db.getRowCount(table, extra, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowCount(
                                                                        table: DslTable<*, W, O, X>,
                                                                        extra: X.()->Unit = {},
                                                                        where: W.()->Unit = {},
): Int {
    val query = table.existenceQuery(extra, where)
    return rawQuery(query.query, query.args).use { it.count }
}




fun Dbh.getRowCount(table: String): Int = read(0, "getRowCount") { db -> db.getRowCount(table) }

fun SQLiteDatabase.getRowCount(                                                    table: String
): Int = query(
    table, arrayOf("1"), null, null, null, null, null, null
).use { it.count }




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.hasRows(               table: DslTable<*, W, O, X>,
                                                                        extra: X.()->Unit = {},
                                                                        where: W.()->Unit = {},
): Boolean = read(false, "hasRows") { db ->
    db.hasRows(table, extra, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.hasRows(    table: DslTable<*, W, O, X>,
                                                                        extra: X.()->Unit = {},
                                                                        where: W.()->Unit = {},
): Boolean {
    val query = table.existenceQuery(
        extra = {
            extra()
            limit = 1
        },
        where = where
    )
    return rawQuery(query.query, query.args).use { it.moveToFirst() }
}




fun Dbh.hasRows(                                                                   table: String
): Boolean = read(false, "hasRows") { db ->
    db.hasRows(table)
}

fun SQLiteDatabase.hasRows(                                                        table: String
): Boolean = query(
    table, arrayOf("1"), null, null, null, null, null, "1"
).use { it.moveToFirst() }
