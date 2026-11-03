/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.misc.data.SharedCol.cID

/**
 * Returns 0L if nothing was found (for "get last + 1" logic)
 */
fun SQLiteDatabase.getLastId(table: String) = getLargestLong(table, cID)

/**
 * Returns the first id that fits, -1L if nothing was found.
 */
fun SQLiteDatabase.getId(table: String, where: WhereDsl.()->Unit) = getLong(table, cID, where)


fun SQLiteDatabase.getIDs(                                            table: String,
                                                                      where: WhereDsl.()->Unit,
                                                                    orderBy: OrderDsl.()->Unit = {},
) = getColLongsPro(table, cID) {
    where(where)
    orderBy(orderBy)
}



fun SQLiteDatabase.getAllIDs(                                         table: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
) = getColLongsPro(table, cID) { orderBy(orderBy) }



/**
 * Generates [count] unique positive IDs for the specified table.
 * Uses the largest existing or reserved ID + 1 when possible.
 * If there isn't enough space, searches for the first available IDs starting from 1.
 *
 * @param table The database table.
 * @param count Number of IDs to generate.
 * @param reservedIDs IDs that must not be assigned.
 * @return A list of available IDs.
 * @throws IllegalStateException If there aren't enough available positive IDs.
 */
fun SQLiteDatabase.getNewIDs(                                         table: String,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> {
    if (count <= 0) return emptyList() ///\/\/\/\/\/\
    
    val maxReservedID = reservedIDs.filter { it > 0L }.maxOrNull() ?: 0L
    
    val maxID = maxOf(0L, getLastId(table), maxReservedID)
    
    val spaceLeft = Long.MAX_VALUE - maxID
    val enoughSpace = count.toLong() <= spaceLeft
    
    return  if (enoughSpace)
                List(count) { idx -> maxID + idx + 1L }
            else
                getFreeIDsFromStart(table, count, reservedIDs)
}


/** Single-object wrapper for [getNewIDs] */
fun SQLiteDatabase.getNewId(                                          table: String,
                                                                reservedIDs: Set<Long> = emptySet(),
): Long = getNewIDs(table, 1, reservedIDs).first()




/**
 * Finds the first [count] available positive IDs, starting from 1.
 * Skips existing and reserved IDs.
 *
 * @throws IllegalStateException If there aren't enough available positive IDs.
 */
private fun SQLiteDatabase.getFreeIDsFromStart(                       table: String,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> {
    if (count <= 0) return emptyList() ///\/\/\/\/\/\
    
    val unavailableIDs = getAllIDs(table).toSet() + reservedIDs.filter { it > 0L }
    
    val freeIDs = mutableListOf<Long>()
    var candidateId = 1L
    
    while (freeIDs.size < count) {
        if (candidateId !in unavailableIDs)
            freeIDs.add(candidateId)
        
        if (freeIDs.size < count) {
            if (candidateId == Long.MAX_VALUE)
                throw IllegalStateException("No more available positive IDs in table '$table'")
            
            candidateId++
        }
    }
    return freeIDs
}







