/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.dbManagement.data.IntCol

/**
 * Generates [count] unique values for the specified column.
 * Uses the largest existing or reserved value + 1 when possible.
 * If there isn't enough space, searches for available values starting from [startFrom].
 *
 * @param table The database table.
 * @param column The column to generate values for.
 * @param count Number of values to generate.
 * @param startFrom Starting value for fallback allocation. Must be positive.
 * @param reserved Values that must not be assigned.
 * @return A list of available values.
 * @throws IllegalStateException If there aren't enough available values.
 */
fun SQLiteDatabase.getNextInts(                                        table: String,
                                                                      column: IntCol,
                                                                       count: Int,
                                                                   startFrom: Int = 1,
                                                                    reserved: Set<Int> = emptySet(),
): List<Int> {
    if (count <= 0) return emptyList() ///\/\/\/\/\/\
    require(startFrom > 0) { "startFrom must be positive" } //\/\/\
    
    val maxReservedValue = reserved.maxOrNull() ?: Int.MIN_VALUE
    val maxValue = maxOf(
        startFrom - 1,
        getLargestInt(table, column),
        maxReservedValue
    )
    val firstValue = if (maxValue < startFrom) startFrom else maxValue + 1
    val enoughSpace = count <= Int.MAX_VALUE - maxValue
    
    return  if (enoughSpace)
                List(count) { idx -> firstValue + idx }
            else
                getFreeIntsFromStart(table, column, count, startFrom, reserved)
}


/** Single-value wrapper for [getNextInts]. Returns -1 if no value is available. */
fun SQLiteDatabase.getNextInt(                                         table: String,
                                                                      column: IntCol,
                                                                   startFrom: Int = 1,
                                                                    reserved: Set<Int> = emptySet(),
): Int = getNextInts(table, column, 1, startFrom, reserved).firstOrNull() ?: -1


/**
 * Finds the first [count] available values, starting from [startFrom].
 * Skips existing and reserved values.
 *
 * @throws IllegalStateException If there aren't enough available values.
 */
fun SQLiteDatabase.getFreeIntsFromStart(                               table: String,
                                                                      column: IntCol,
                                                                       count: Int,
                                                                   startFrom: Int = 1,
                                                                    reserved: Set<Int> = emptySet(),
): List<Int> {
    if (count <= 0) return emptyList() ///\/\/\/\/\/\
    require(startFrom > 0) { "startFrom must be positive" } //\/\/\
    
    val unavailableValues = getColInts(table, column).toSet() + reserved
    
    val freeValues = mutableListOf<Int>()
    var candidateValue = startFrom
    
    while (freeValues.size < count) {
        if (candidateValue !in unavailableValues)
            freeValues.add(candidateValue)
        
        if (freeValues.size < count) {
            if (candidateValue == Int.MAX_VALUE)
                throw IllegalStateException("No more available Int values in column '${column.name}' of table '$table'")
            
            candidateValue++
        }
    }
    return freeValues
}





