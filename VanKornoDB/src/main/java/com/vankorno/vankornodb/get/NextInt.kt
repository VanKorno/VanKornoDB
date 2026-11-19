/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.IntCol

/** Returns [count] values starting at or above [startFrom], excluding [reserved] and values in rows matching [where].
 * Prefers consecutive values above the maximum. If they would overflow, scans for gaps from [startFrom].
 * [startFrom] must be positive when [count] is positive. Returns an empty list for a nonpositive count
 * or a failed read, including exhaustion. The returned values are candidates and are not reserved.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextInts(      table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   count: Int,
                                                               startFrom: Int = 1,
                                                                reserved: Set<Int> = emptySet(),
                                                                   where: W.()->Unit = {},
): List<Int> = read(emptyList(), "getNextInts") { db ->
    db.getNextInts(table, column, count, startFrom, reserved, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextInts(
                                                                   table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   count: Int,
                                                               startFrom: Int = 1,
                                                                reserved: Set<Int> = emptySet(),
                                                                   where: W.()->Unit = {},
): List<Int> {
    if (count <= 0) return emptyList() ///\/\/\/\/\/\
    require(startFrom > 0) { "startFrom must be positive" } //\/\/\
    
    val maxReservedValue = reserved.maxOrNull() ?: Int.MIN_VALUE
    val maxValue = maxOf(
        startFrom - 1,
        getLargestInt(table, column, where),
        maxReservedValue
    )
    val firstValue = if (maxValue < startFrom) startFrom else maxValue + 1
    val enoughSpace = count <= Int.MAX_VALUE - maxValue
    
    return  if (enoughSpace)
                List(count) { idx -> firstValue + idx }
            else
                getFreeIntsFromStart(table, column, count, startFrom, reserved, where)
}


/** Returns one available value using the same rules as the bulk getter. The helper fallback is -1.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getNextInt(       table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                               startFrom: Int = 1,
                                                                reserved: Set<Int> = emptySet(),
                                                                   where: W.()->Unit = {},
): Int = read(-1, "getNextInt") { db ->
    db.getNextInt(table, column, startFrom, reserved, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getNextInt(
                                                                   table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                               startFrom: Int = 1,
                                                                reserved: Set<Int> = emptySet(),
                                                                   where: W.()->Unit = {},
): Int = getNextInts(table, column, 1, startFrom, reserved, where)
    .firstOrNull() ?: -1



fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getFreeIntsFromStart(
                                                                   table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   count: Int,
                                                               startFrom: Int = 1,
                                                                reserved: Set<Int> = emptySet(),
                                                                   where: W.()->Unit = {},
): List<Int> {
    if (count <= 0) return emptyList() ///\/\/\/\/\/\
    require(startFrom > 0) { "startFrom must be positive" } //\/\/\
    
    val unavailableValues = getColInts(table, column, where = where).toSet() + reserved
    
    val freeValues = mutableListOf<Int>()
    var candidateValue = startFrom
    
    while (freeValues.size < count) {
        if (candidateValue !in unavailableValues)
            freeValues.add(candidateValue)
        
        if (freeValues.size < count) {
            if (candidateValue == Int.MAX_VALUE)
                throw IllegalStateException(
                    "No more available Int values in column '${table.select(column).name}' of table '${table.name}'"
                )
            
            candidateValue++
        }
    }
    return freeValues
}
