/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.LongCol

/** Returns [count] values starting at or above [startFrom], excluding [reserved] and values in rows matching [where].
 * Prefers consecutive values above the maximum. If they would overflow, scans for gaps from [startFrom].
 * [startFrom] must be positive when [count] is positive. Returns an empty list for a nonpositive count
 * or a failed read, including exhaustion. The returned values are candidates and are not reserved.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextLongs(    table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                  count: Int,
                                                              startFrom: Long = 1L,
                                                               reserved: Set<Long> = emptySet(),
                                                                  where: W.()->Unit = {},
): List<Long> = read(emptyList(), "getNextLongs") { db ->
    db.getNextLongs(table, column, count, startFrom, reserved, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextLongs(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
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
        getLargestLong(table, column, where),
        maxReservedValue
    )
    val spaceLeft = Long.MAX_VALUE - maxValue
    val enoughSpace = count.toLong() <= spaceLeft
    
    return if (enoughSpace)
        List(count) { idx -> maxValue + idx + 1L }
    else
        getFreeLongsFromStart(table, column, count, startFrom, reserved, where)
}




/** Returns one available value using the same rules as the bulk getter. The helper fallback is -1L.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextLong(     table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                              startFrom: Long = 1L,
                                                               reserved: Set<Long> = emptySet(),
                                                                  where: W.()->Unit = {},
): Long = read(-1L, "getNextLong") { db ->
    db.getNextLong(table, column, startFrom, reserved, where)
}

/** Returns one available value using the same rules as the bulk getter. The helper fallback is -1L.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextLong(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                              startFrom: Long = 1L,
                                                               reserved: Set<Long> = emptySet(),
                                                                  where: W.()->Unit = {},
): Long = getNextLongs(table, column, 1, startFrom, reserved, where)
    .firstOrNull() ?: -1L




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getFreeLongsFromStart(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                  count: Int,
                                                              startFrom: Long = 1L,
                                                               reserved: Set<Long> = emptySet(),
                                                                  where: W.()->Unit = {},
): List<Long> {
    if (count <= 0) return emptyList() //\/\/\/\/\/\
    require(startFrom > 0L) { "startFrom must be positive" } //\/\/\
    
    val unavailableValues = getColLongs(table, column, where = where).toSet() + reserved
    
    val freeValues = mutableListOf<Long>()
    var candidateValue = startFrom
    
    while (freeValues.size < count) {
        if (candidateValue !in unavailableValues)
            freeValues.add(candidateValue)
        
        if (freeValues.size < count) {
            if (candidateValue == Long.MAX_VALUE)
                throw IllegalStateException("No more available Long values in column '${table.columnSql(column)}' of table '${table.name}'") //\/\/\
            
            candidateValue++
        }
    }
    return freeValues
}
