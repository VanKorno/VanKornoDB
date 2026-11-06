/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.fix

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition

/**
 * Fixes duplicate positions in the specified table by assigning new unique positions.
 * @return The number of rows modified.
 */
fun SQLiteDatabase.fixDuplicatePosition(                              table: String,
                                                                      where: WhereDsl.()->Unit = {},
) = fixDuplicateLongs(table, cPosition, where)


/**
 * Multi-table variant of [fixDuplicatePosition]
 */
fun SQLiteDatabase.fixDuplicatePositionMult(                  vararg tables: String,
                                                                      where: WhereDsl.()->Unit = {},
) = tables.sumOf { table -> fixDuplicatePosition(table, where) }

