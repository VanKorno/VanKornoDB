/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.misc.data.SharedCol.cID

/**
 * Returns 0L if nothing was found (for "get last + 1" logic)
 */
fun SQLiteDatabase.getLastId(table: String) = getLargestLong(table, cID)

/**
 * Returns the first id that fits, -1L if nothing was found.
 */
fun SQLiteDatabase.getId(table: String, where: WhereDsl.()->Unit) = getLong(table, cID, where = where)


fun SQLiteDatabase.getIDs(                                            table: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
) = getColLongs(table, cID, orderBy, where = where)



fun SQLiteDatabase.getAllIDs(                                         table: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
) = getColLongs(table, cID, orderBy)



/** Generates [count] new IDs for the specified table. */
fun SQLiteDatabase.getNextIDs(                                        table: String,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> = getNextLongs(table, cID, count, 1L, reservedIDs)


/** Single-object wrapper for [getNextIDs] */
fun SQLiteDatabase.getNextId(                                         table: String,
                                                                reservedIDs: Set<Long> = emptySet(),
): Long = getNextLongs(table, cID, 1, 1L, reservedIDs).first()







