/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.data.DbConstants._ID
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.EntityColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.lCol

internal fun <W> idColumn(): ColumnSelector<W, LongCol> = { EntityColumn(lCol(_ID)) } // TODO


fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLastId(           table: QueryTable<*, W, O, X>,
): Long = read(0L, "getLastId") { db ->
    db.getLastId(table)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLastId(
                                                                      table: QueryTable<*, W, O, X>,
): Long = getLargestLong(table, idColumn())




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getId(               table: QueryTable<*, W, O, X>,
                                                                      where: W.()->Unit,
): Long = read(-1L, "getId") { db ->
    db.getId(table, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getId(    table: QueryTable<*, W, O, X>,
                                                                      where: W.()->Unit,
): Long = getLong(table, idColumn(), where = where)




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getIDs(              table: QueryTable<*, W, O, X>,
                                                                    orderBy: O.()->Unit = {},
                                                                      where: W.()->Unit,
): List<Long> = read(emptyList(), "getIDs") { db ->
    db.getIDs(table, orderBy, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getIDs(
                                                                      table: QueryTable<*, W, O, X>,
                                                                    orderBy: O.()->Unit = {},
                                                                      where: W.()->Unit,
): List<Long> = getColLongs(table, idColumn(), orderBy, where = where)




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getAllIDs(           table: QueryTable<*, W, O, X>,
                                                                    orderBy: O.()->Unit = {},
): List<Long> = read(emptyList(), "getAllIDs") { db ->
    db.getAllIDs(table, orderBy)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getAllIDs(
                                                                      table: QueryTable<*, W, O, X>,
                                                                    orderBy: O.()->Unit = {},
): List<Long> = getColLongs(table, idColumn(), orderBy)




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextId(           table: QueryTable<*, W, O, X>,
                                                                reservedIDs: Set<Long> = emptySet(),
): Long = read(-1L, "getNextId") { db ->
    db.getNextId(table, reservedIDs)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextId(
                                                                      table: QueryTable<*, W, O, X>,
                                                                reservedIDs: Set<Long> = emptySet(),
): Long = getNextIDs(table, 1, reservedIDs).first()




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextIDs(          table: QueryTable<*, W, O, X>,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> = read(emptyList(), "getNextIDs") { db ->
    db.getNextIDs(table, count, reservedIDs)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextIDs(
                                                                      table: QueryTable<*, W, O, X>,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> = getNextLongs(table, idColumn(), count, reserved = reservedIDs)
