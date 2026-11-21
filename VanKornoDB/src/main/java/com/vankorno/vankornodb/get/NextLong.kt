/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.TableColumn
import com.vankorno.vankornodb.dbManagement.data.LongCol

/** Returns [count] values starting at or above [startFrom], excluding [reserved] and values in rows matching [where].
 * Prefers consecutive values above the maximum. If they would overflow, scans for gaps from [startFrom].
 * [startFrom] must be positive when [count] is positive. Returns an empty list for a nonpositive count
 * or a failed read, including exhaustion. The returned values are candidates and are not reserved.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextLongs(
                                                              column: TableColumn<W, O, X, LongCol>,
                                                               count: Int,
                                                           startFrom: Long = 1L,
                                                            reserved: Set<Long> = emptySet(),
                                                               where: W.()->Unit = {},
): List<Long> = read(emptyList(), "getNextLongs") { db ->
    db.getNextLongs(column, count, startFrom, reserved, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextLongs(
                                                              column: TableColumn<W, O, X, LongCol>,
                                                               count: Int,
                                                           startFrom: Long = 1L,
                                                            reserved: Set<Long> = emptySet(),
                                                               where: W.()->Unit = {},
): List<Long> {
    if (count <= 0) return emptyList() ///\/\/\/\/\/\
    
    require(startFrom > 0L) { "startFrom must be positive" } //\/\/\
    
    val maxReservedValue = reserved.maxOrNull() ?: Long.MIN_VALUE
    val maxValue = maxOf(
        startFrom - 1L,
        getLargestLong(column, where),
        maxReservedValue
    )
    val spaceLeft = Long.MAX_VALUE - maxValue
    val enoughSpace = count.toLong() <= spaceLeft
    
    return if (enoughSpace)
        List(count) { idx -> maxValue + idx + 1L }
    else
        getFreeLongsFromStart(column, count, startFrom, reserved, where)
}




/** Returns one available value using the same rules as the bulk getter. The helper fallback is -1L.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextLong(
                                                              column: TableColumn<W, O, X, LongCol>,
                                                           startFrom: Long = 1L,
                                                            reserved: Set<Long> = emptySet(),
                                                               where: W.()->Unit = {},
): Long = read(-1L, "getNextLong") { db ->
    db.getNextLong(column, startFrom, reserved, where)
}

/** Returns one available value using the same rules as the bulk getter. The helper fallback is -1L.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextLong(
                                                              column: TableColumn<W, O, X, LongCol>,
                                                           startFrom: Long = 1L,
                                                            reserved: Set<Long> = emptySet(),
                                                               where: W.()->Unit = {},
): Long = getNextLongs(column, 1, startFrom, reserved, where)
    .firstOrNull() ?: -1L




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getFreeLongsFromStart(
                                                              column: TableColumn<W, O, X, LongCol>,
                                                               count: Int,
                                                           startFrom: Long = 1L,
                                                            reserved: Set<Long> = emptySet(),
                                                               where: W.()->Unit = {},
): List<Long> {
    if (count <= 0) return emptyList() //\/\/\/\/\/\
    
    require(startFrom > 0L) { "startFrom must be positive" } //\/\/\
    
    val unavailableValues = getColLongs(column, where = where).toSet() + reserved
    
    val freeValues = mutableListOf<Long>()
    var candidateValue = startFrom
    
    while (freeValues.size < count) {
        if (candidateValue !in unavailableValues)
            freeValues.add(candidateValue)
        
        if (freeValues.size < count) {
            if (candidateValue == Long.MAX_VALUE)
                throw IllegalStateException("No more available Long values in column '${column.table.columnSql(column.selector)}' of table '${column.table.name}'") //\/\/\
            
            candidateValue++
        }
    }
    return freeValues
}
