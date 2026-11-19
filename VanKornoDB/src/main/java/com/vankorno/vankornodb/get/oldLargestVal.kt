/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol

/**
 * Returns 0L if nothing was found
 */
fun SQLiteDatabase.getLargestInt(                                     table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
): Int = getInt(
    table = table,
    column = iCol("IFNULL(MAX(${column.name}), 0)"),
    where = where
)

/**
 * Returns 0L if nothing was found
 */
fun SQLiteDatabase.getLargestLong(                                    table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
): Long = getLong(
    table = table,
    column = lCol("IFNULL(MAX(${column.name}), 0)"),
    where = where
)





