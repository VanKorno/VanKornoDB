/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.fix

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.get.getNextInts
import com.vankorno.vankornodb.get.useCursor
import com.vankorno.vankornodb.misc.data.SharedCol.cRowId
import com.vankorno.vankornodb.set.setInt

/**
 * Multi-table, multi-column variant of [fixDuplicateInts]
 */
fun SQLiteDatabase.fixDuplicateIntsMult(                             tables: List<String>,
                                                                    columns: List<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
) = tables.sumOf { table ->
    columns.sumOf { column -> fixDuplicateInts(table, column, where) }
}

/**
 * Finds duplicate values in a column and assigns unique values to subsequent occurrences.
 * The first occurrence of each value is preserved.
 *
 * @return Number of rows modified.
 */
fun SQLiteDatabase.fixDuplicateInts(                                  table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
): Int {
    val rows = mutableListOf<Pair<Long, Int>>()
    
    useCursor(table, arrayOf(cRowId, column), orderBy = { cRowId }, where = where) { cursor ->
        if (cursor.moveToFirst()) {
            do {
                rows.add(cursor.getLong(0) to cursor.getInt(1))
            } while (cursor.moveToNext())
        }
    }
    val seenValues = mutableSetOf<Int>()
    val duplicateRows = rows.filter { (_, value) -> !seenValues.add(value) }
    
    if (duplicateRows.isEmpty()) return 0 //\/\/\/\/\/\
    
    val newValues = getNextInts(table, column, duplicateRows.size)
    
    duplicateRows.forEachIndexed { idx, (rowId, _) ->
        setInt(newValues[idx], table, column) {
            cRowId equal rowId
        }
    }
    return duplicateRows.size
}
