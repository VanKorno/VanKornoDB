/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.id

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.get.getColLongs
import com.vankorno.vankornodb.get.getLargestLong
import com.vankorno.vankornodb.get.getLong
import com.vankorno.vankornodb.get.getNextLongs


fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLastId(
                                                                        table: DslTable<*, W, O, X>,
): Long = read(0L, "getLastId") { db ->
    db.getLastId(table)
}

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLastId(
                                                                        table: DslTable<*, W, O, X>,
): Long = getLargestLong(table.col{ Id })




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getId(
                                                                        table: DslTable<*, W, O, X>,
                                                                        where: W.()->Unit,
): Long = read(-1L, "getId") { db ->
    db.getId(table, where)
}

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getId(
                                                                        table: DslTable<*, W, O, X>,
                                                                        where: W.()->Unit,
): Long = getLong(table.col{ Id }, where = where)




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getIDs(
                                                                        table: DslTable<*, W, O, X>,
                                                                      orderBy: O.()->Unit = {},
                                                                        where: W.()->Unit,
): List<Long> = read(emptyList(), "getIDs") { db ->
    db.getIDs(table, orderBy, where)
}

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getIDs(
                                                                        table: DslTable<*, W, O, X>,
                                                                      orderBy: O.()->Unit = {},
                                                                        where: W.()->Unit,
): List<Long> = getColLongs(table.col{ Id }, orderBy, where = where)




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getAllIDs(
                                                                        table: DslTable<*, W, O, X>,
                                                                      orderBy: O.()->Unit = {},
): List<Long> = read(emptyList(), "getAllIDs") { db ->
    db.getAllIDs(table, orderBy)
}

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getAllIDs(
                                                                        table: DslTable<*, W, O, X>,
                                                                      orderBy: O.()->Unit = {},
): List<Long> = getColLongs(table.col{ Id }, orderBy)




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextId(
                                                                      table: DslTable<*, W, O, X>,
                                                                reservedIDs: Set<Long> = emptySet(),
): Long = read(-1L, "getNextId") { db ->
    db.getNextId(table, reservedIDs)
}

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextId(
                                                                      table: DslTable<*, W, O, X>,
                                                                reservedIDs: Set<Long> = emptySet(),
): Long = getNextIDs(table, 1, reservedIDs).first()




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextIDs(
                                                                      table: DslTable<*, W, O, X>,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> = read(emptyList(), "getNextIDs") { db ->
    db.getNextIDs(table, count, reservedIDs)
}

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextIDs(
                                                                      table: DslTable<*, W, O, X>,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> = getNextLongs(table.col{ Id }, count, reserved = reservedIDs)
