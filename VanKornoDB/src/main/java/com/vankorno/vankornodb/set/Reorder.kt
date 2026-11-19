/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.EntityColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.get.getLargestLong
import com.vankorno.vankornodb.get.idColumn
import com.vankorno.vankornodb.get.useCursor

/** Moves a row one position, or to the first or last position, among rows matching [where].
 * Returns false when the row has no available move.
 */
fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.reorder(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                     id: Long,
                                                           moveUpOrBack: Boolean,
                                                        makeFirstOrLast: Boolean = false,
                                                                  where: W.()->Unit = {},
): Boolean = readWrite(false, "reorder") { db ->
    db.reorder(table, column, id, moveUpOrBack, makeFirstOrLast, where)
}

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.reorder(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                     id: Long,
                                                           moveUpOrBack: Boolean,
                                                        makeFirstOrLast: Boolean = false,
                                                                  where: W.()->Unit = {},
): Boolean {
    beginTransaction()
    try {
        val moved = reorderRow(table, column, id, moveUpOrBack, makeFirstOrLast, where)
        setTransactionSuccessful()
        return moved
    } finally {
        endTransaction()
    }
}




private fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.reorderRow(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                     id: Long,
                                                           moveUpOrBack: Boolean,
                                                        makeFirstOrLast: Boolean,
                                                                  where: W.()->Unit,
): Boolean {
    val idColumn = idColumn<W>()
    val position = useCursor(table, column, where = { idColumn() equal id; and(where) }) {
        if (it.moveToFirst()) it.getLong(0) else null
    } ?: return false //\/\/\/\/\/\
    
    if (makeFirstOrLast) {
        if (moveUpOrBack) {
            if (position < 2L) return false //\/\/\/\/\/\
            addToLong(1L, table, column) { column() less position; and(where) }
            setLong(1L, table, column) { idColumn() equal id }
        } else {
            val last = getLargestLong(table, column, where)
            if (last == position || last == Long.MAX_VALUE) return false //\/\/\/\/\/\
            setLong(last + 1L, table, column) { idColumn() equal id }
        }
        return true //\/\/\/\/\/\
    }
    
    val sortColumn = EntityColumn<W, LongCol>(table.select(column))
    val neighbour = useCursor(table, idColumn, column,
        orderBy = { sortColumn.flipIf(moveUpOrBack) },
        extra = { limit = 1 },
        where = {
            if (moveUpOrBack) column() less position else column() greater position
            and(where)
        },
    ) { cursor ->
        if (cursor.moveToFirst()) cursor.getLong(0) to cursor.getLong(1) else null
    } ?: return false //\/\/\/\/\/\
    
    setLong(neighbour.second, table, column) { idColumn() equal id }
    setLong(position, table, column) { idColumn() equal neighbour.first }
    return true
}
