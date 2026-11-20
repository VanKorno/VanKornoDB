/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.data.DbConstants._Position
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.core.dsl.raw
import com.vankorno.vankornodb.dbManagement.data.requirePositionColumn

/**
 * Returns 0L if nothing was found
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLastPosition(     table: QueryTable<*, W, O, X>,
                                                                      where: W.()->Unit = {},
): Long = read(0L, "getLastPosition") { db ->
    db.getLastPosition(table, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLastPosition(
                                                                      table: QueryTable<*, W, O, X>,
                                                                      where: W.()->Unit = {},
): Long {
    table.info.requirePositionColumn()
    return getLargestLong(table.col { raw(_Position) }, where)
}



/**
 * Generates [count] new positions for the specified table.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextPositions(    table: QueryTable<*, W, O, X>,
                                                                      count: Int,
                                                                   reserved: Set<Long> = emptySet(),
                                                                      where: W.()->Unit = {},
): List<Long> = read(emptyList(), "getNextPositions") { db ->
    db.getNextPositions(table, count, reserved, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextPositions(
                                                                      table: QueryTable<*, W, O, X>,
                                                                      count: Int,
                                                                   reserved: Set<Long> = emptySet(),
                                                                      where: W.()->Unit = {},
): List<Long> {
    table.info.requirePositionColumn()
    return getNextLongs(table.col { raw(_Position) }, count, 1L, reserved, where)
}



/**
 * Single-object wrapper for [getNextLongs].
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextPosition(     table: QueryTable<*, W, O, X>,
                                                                   reserved: Set<Long> = emptySet(),
                                                                      where: W.()->Unit = {},
): Long = read(-1L, "getNextPosition") { db ->
    db.getNextPosition(table, reserved, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextPosition(
                                                                      table: QueryTable<*, W, O, X>,
                                                                   reserved: Set<Long> = emptySet(),
                                                                      where: W.()->Unit = {},
): Long {
    table.info.requirePositionColumn()
    return getNextLong(table.col { raw(_Position) }, 1L, reserved, where)
}
