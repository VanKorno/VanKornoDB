/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition

/**
 * Returns 0L if nothing was found
 */
fun SQLiteDatabase.getLastPosition(                                   table: String,
                                                                      where: WhereDsl.()->Unit = {},
) = getLargestLong(table, cPosition, where)



/**
 * Generates [count] new positions for the specified table.
 */
fun SQLiteDatabase.getNextPositions(                                  table: String,
                                                                      count: Int,
                                                                   reserved: Set<Long> = emptySet(),
                                                                      where: WhereDsl.()->Unit = {},
): List<Long> = getNextLongs(table, cPosition, count, 1L, reserved, where)



/**
 * Single-object wrapper for [getNextLongs].
 */
fun SQLiteDatabase.getNextPosition(                                   table: String,
                                                                   reserved: Set<Long> = emptySet(),
                                                                      where: WhereDsl.()->Unit = {},
): Long = getNextLong(table, cPosition, 1L, reserved, where)












