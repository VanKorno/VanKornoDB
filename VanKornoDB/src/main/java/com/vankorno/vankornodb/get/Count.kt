/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.get.noty.useCursorNoty
import com.vankorno.vankornodb.misc.columns

/**
 * Returns the number of rows matching the query conditions.
 */
fun SQLiteDatabase.getRowCount(                                       table: String,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
): Int = useCursorNoty(table, columns("1"), extra = extra, where = where) { it.count }

/**
 * Returns the number of rows in a table.
 */
fun SQLiteDatabase.getRowCount(                                                    table: String
): Int = query(
    table, arrayOf("1"), null, null, null, null, null, null
).use { it.count }




/**
 * Returns true if at least one row matches the advanced query conditions.
 */
fun SQLiteDatabase.hasRows(                                           table: String,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
): Boolean = useCursorNoty(table, columns("1"), where = where,
    extra = {
        applyDsl(extra)
        limit = 1
    }
) { it.moveToFirst() }



/**
 * Returns true if [table] has any rows.
 */
fun SQLiteDatabase.hasRows(                                                        table: String,
): Boolean = query(
    table, arrayOf("1"), null, null, null, null, null, "1"
).use { it.moveToFirst() }

















