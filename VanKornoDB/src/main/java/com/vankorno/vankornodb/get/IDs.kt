/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.data.DbConstants._ID
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.Field
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.lCol

internal fun <W> idColumn(): W.()->Field<W, LongCol> = { Field(lCol(_ID)) }


fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLastId(             table: DslTable<*, W, O, X>,
): Long = read(0L, "getLastId") { db ->
    db.getLastId(table)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLastId(
                                                                        table: DslTable<*, W, O, X>,
): Long = getLargestLong(table.col(idColumn()))




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getId(                 table: DslTable<*, W, O, X>,
                                                                        where: W.()->Unit,
): Long = read(-1L, "getId") { db ->
    db.getId(table, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getId(      table: DslTable<*, W, O, X>,
                                                                        where: W.()->Unit,
): Long = getLong(table.col(idColumn()), where = where)




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getIDs(                table: DslTable<*, W, O, X>,
                                                                      orderBy: O.()->Unit = {},
                                                                        where: W.()->Unit,
): List<Long> = read(emptyList(), "getIDs") { db ->
    db.getIDs(table, orderBy, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getIDs(     table: DslTable<*, W, O, X>,
                                                                      orderBy: O.()->Unit = {},
                                                                        where: W.()->Unit,
): List<Long> = getColLongs(table.col(idColumn()), orderBy, where = where)




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getAllIDs(           table: DslTable<*, W, O, X>,
                                                                    orderBy: O.()->Unit = {},
): List<Long> = read(emptyList(), "getAllIDs") { db ->
    db.getAllIDs(table, orderBy)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getAllIDs(
                                                                        table: DslTable<*, W, O, X>,
                                                                      orderBy: O.()->Unit = {},
): List<Long> = getColLongs(table.col(idColumn()), orderBy)




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextId(           table: DslTable<*, W, O, X>,
                                                                reservedIDs: Set<Long> = emptySet(),
): Long = read(-1L, "getNextId") { db ->
    db.getNextId(table, reservedIDs)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextId(
                                                                      table: DslTable<*, W, O, X>,
                                                                reservedIDs: Set<Long> = emptySet(),
): Long = getNextIDs(table, 1, reservedIDs).first()




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextIDs(          table: DslTable<*, W, O, X>,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> = read(emptyList(), "getNextIDs") { db ->
    db.getNextIDs(table, count, reservedIDs)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextIDs(
                                                                      table: DslTable<*, W, O, X>,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> = getNextLongs(table.col(idColumn()), count, reserved = reservedIDs)
