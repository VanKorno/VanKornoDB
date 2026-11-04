/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.fix

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.get.getNextLongs
import com.vankorno.vankornodb.get.getTableLongs
import com.vankorno.vankornodb.misc.data.SharedCol.cRowID
import com.vankorno.vankornodb.set.setLong

/**
 * Multi-table, multi-column variant of [fixDuplicateLongs]
 */
fun SQLiteDatabase.fixDuplicateLongsMult(                            tables: List<String>,
                                                                    columns: List<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
) = tables.sumOf { table ->
    columns.sumOf { column -> fixDuplicateLongs(table, column, where) }
}


/**
 * Finds duplicate values in a column and assigns unique values to subsequent occurrences.
 * The first occurrence of each value is preserved.
 *
 * @return Number of rows modified.
 */
fun SQLiteDatabase.fixDuplicateLongs(                                 table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
): Int {
    val rows = getTableLongs(table, arrayOf(cRowID, column), where)
    val seenValues = mutableSetOf<Long>()
    val duplicateRows = rows.filter { (_, value) -> !seenValues.add(value) }
    
    if (duplicateRows.isEmpty()) return 0 //\/\/\/\/\/\
    
    val newValues = getNextLongs(table, column, duplicateRows.size)
    
    duplicateRows.forEachIndexed { idx, (rowId, _) ->
        setLong(newValues[idx], table, column) {
            cRowID equal rowId
        }
    }
    return duplicateRows.size
}
