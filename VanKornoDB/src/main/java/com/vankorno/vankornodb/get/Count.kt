/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.get.noty.getCursorNoty
import com.vankorno.vankornodb.misc.columns

/**
 * Returns the number of rows matching the query conditions.
 */
fun SQLiteDatabase.getRowCount(                                       table: String,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): Int = getCursorNoty(table, columns("1"), where, extra = extra).use { it.count }




/**
 * Returns true if at least one row matches the advanced query conditions.
 */
fun SQLiteDatabase.hasRows(                                           table: String,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): Boolean = getCursorNoty(table, columns("1"), where,
    extra = {
        applyDsl(extra)
        limit = 1
    }
).use { it.moveToFirst() }

















