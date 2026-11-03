/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.dbManagement.data.LongCol

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
fun SQLiteDatabase.getNextLongs(                                      table: String,
                                                                     column: LongCol,
                                                                      count: Int,
                                                                  startFrom: Long = 1L,
                                                                   reserved: Set<Long> = emptySet(),
): List<Long> {
    if (count <= 0) return emptyList() ///\/\/\/\/\/\
    require(startFrom > 0L) { "startFrom must be positive" } //\/\/\
    
    val maxReservedValue = reserved.maxOrNull() ?: Long.MIN_VALUE
    val maxValue = maxOf(
        startFrom - 1L,
        getLargestLong(table, column),
        maxReservedValue
    )
    val spaceLeft = Long.MAX_VALUE - maxValue
    val enoughSpace = count.toLong() <= spaceLeft
    
    /* In case I switch to handling negatives
    val enoughSpace = maxValue >= 0L && count.toLong() <= spaceLeft ||
                    maxValue < 0L && count.toLong() <= Long.MAX_VALUE - maxValue*/
    
    return if (enoughSpace)
        List(count) { idx -> maxValue + idx + 1L }
    else
        getFreeLongsFromStart(table, column, count, startFrom, reserved)
}


/**
 * Single-value wrapper for [getNextLongs]. Returns -1L if no value is available.
 */
fun SQLiteDatabase.getNextLong(                                       table: String,
                                                                     column: LongCol,
                                                                  startFrom: Long = 1L,
                                                                   reserved: Set<Long> = emptySet(),
): Long = getNextLongs(table, column, 1, startFrom, reserved)
    .firstOrNull() ?: -1L



/**
 * Finds the first [count] available values, starting from [startFrom].
 * Skips existing and reserved values.
 *
 * @throws IllegalStateException If there aren't enough available values.
 */
fun SQLiteDatabase.getFreeLongsFromStart(                             table: String,
                                                                     column: LongCol,
                                                                      count: Int,
                                                                  startFrom: Long = 1L,
                                                                   reserved: Set<Long> = emptySet(),
): List<Long> {
    if (count <= 0) return emptyList() //\/\/\/\/\/\
    require(startFrom > 0L) { "startFrom must be positive" } //\/\/\
    
    val unavailableValues = getColLongsPro(table, column).toSet() + reserved
    
    val freeValues = mutableListOf<Long>()
    var candidateValue = startFrom
    
    while (freeValues.size < count) {
        if (candidateValue !in unavailableValues)
            freeValues.add(candidateValue)
        
        if (freeValues.size < count) {
            if (candidateValue == Long.MAX_VALUE)
                throw IllegalStateException("No more available Long values in column '${column.name}' of table '$table'") //\/\/\
            
            candidateValue++
        }
    }
    return freeValues
}



