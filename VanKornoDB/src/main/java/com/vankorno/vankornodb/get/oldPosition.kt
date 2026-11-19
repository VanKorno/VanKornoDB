/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.LongCol

// TODO Must not require a column!

/**
 * Returns 0L if nothing was found
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLastPosition(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, LongCol>,
                                                                 where: W.()->Unit = {},
): Long = read(0L, "getLastPosition") { db ->
    db.getLastPosition(table, column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLastPosition(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, LongCol>,
                                                                 where: W.()->Unit = {},
): Long = getLargestLong(table, column, where)



/**
 * Generates [count] new positions for the specified table.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextPositions(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, LongCol>,
                                                                 count: Int,
                                                              reserved: Set<Long> = emptySet(),
                                                                 where: W.()->Unit = {},
): List<Long> = read(emptyList(), "getNextPositions") { db ->
    db.getNextPositions(table, column, count, reserved, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextPositions(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, LongCol>,
                                                                 count: Int,
                                                              reserved: Set<Long> = emptySet(),
                                                                 where: W.()->Unit = {},
): List<Long> = getNextLongs(table, column, count, 1L, reserved, where)



/**
 * Single-object wrapper for [getNextLongs].
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextPosition(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, LongCol>,
                                                              reserved: Set<Long> = emptySet(),
                                                                 where: W.()->Unit = {},
): Long = read(-1L, "getNextPosition") { db ->
    db.getNextPosition(table, column, reserved, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextPosition(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, LongCol>,
                                                              reserved: Set<Long> = emptySet(),
                                                                 where: W.()->Unit = {},
): Long = getNextLong(table, column, 1L, reserved, where)
