/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.data.DbConstants._ID
import com.vankorno.vankornodb.core.data.DbConstants._Position
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.requirePositionColumn

/** Moves a row one position, or to the first or last position, among rows matching [where].
 * Returns false when the row has no available move.
 */
fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.reorder(
                                                                      table: QueryTable<*, W, O, X>,
                                                                         id: Long,
                                                               moveUpOrBack: Boolean,
                                                            makeFirstOrLast: Boolean = false,
                                                                      where: W.()->Unit = {},
): Boolean = readWrite(false, "reorder") { db ->
    db.reorder(table, id, moveUpOrBack, makeFirstOrLast, where)
}

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.reorder(
                                                                      table: QueryTable<*, W, O, X>,
                                                                         id: Long,
                                                               moveUpOrBack: Boolean,
                                                            makeFirstOrLast: Boolean = false,
                                                                      where: W.()->Unit = {},
): Boolean {
    table.info.requirePositionColumn()
    beginTransaction()
    try {
        val moved = reorderRow(table, id, moveUpOrBack, makeFirstOrLast, where)
        setTransactionSuccessful()
        return moved
    } finally {
        endTransaction()
    }
}




private fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.reorderRow(
                                                                      table: QueryTable<*, W, O, X>,
                                                                         id: Long,
                                                               moveUpOrBack: Boolean,
                                                            makeFirstOrLast: Boolean,
                                                                      where: W.()->Unit,
): Boolean {
    fun identifier(name: String) = "\"" + name.replace("\"", "\"\"") + "\""
    
    val source = identifier(table.name)
    val pos = _Position
    val key = identifier(_ID)
    val predicate = table.where(where)
    val filter = if (predicate.query.isBlank()) "" else " AND (${predicate.query})"
    
    val position = rawQuery(
        "SELECT $pos FROM $source WHERE $key = ?$filter",
        arrayOf(id.toString(), *predicate.args),
    ).use { if (it.moveToFirst()) it.getLong(0) else null } ?: return false //\/\/\/\/\/\
    
    fun setPosition(rowId: Long, value: Long) {
        execSQL("UPDATE $source SET $pos = ? WHERE $key = ?", arrayOf(value, rowId))
    }
    
    if (makeFirstOrLast) {
        if (moveUpOrBack) {
            if (position < 2L) return false //\/\/\/\/\/\
            execSQL(
                "UPDATE $source SET $pos = $pos + 1 WHERE $pos < ?$filter",
                arrayOf<Any>(position, *predicate.args),
            )
            setPosition(id, 1L)
        } else {
            val last = rawQuery(
                "SELECT MAX($pos) FROM $source WHERE 1=1$filter", predicate.args
            ).use { cursor ->
                cursor.moveToFirst()
                cursor.getLong(0)
            }
            if (last == position || last == Long.MAX_VALUE) return false //\/\/\/\/\/\
            setPosition(id, last + 1L)
        }
        return true //\/\/\/\/\/\
    }
    
    val comparison = if (moveUpOrBack) "<" else ">"
    val direction = if (moveUpOrBack) "DESC" else "ASC"
    val neighbour = rawQuery(
        "SELECT $key, $pos FROM $source WHERE $pos $comparison ?$filter ORDER BY $pos $direction LIMIT 1",
        arrayOf(position.toString(), *predicate.args),
    ).use { cursor ->
        if (cursor.moveToFirst()) cursor.getLong(0) to cursor.getLong(1) else null
    } ?: return false //\/\/\/\/\/\
    
    setPosition(id, neighbour.second)
    setPosition(neighbour.first, position)
    return true
}
