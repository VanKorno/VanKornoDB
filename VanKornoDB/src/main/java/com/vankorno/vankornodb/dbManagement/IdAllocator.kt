package com.vankorno.vankornodb.dbManagement

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.get.getAllIDs
import com.vankorno.vankornodb.get.getLastId

private fun SQLiteDatabase.getNewIDs(                                 table: String,
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
                getFreeIDs(table, count, reservedIDs)
}


private fun SQLiteDatabase.getFreeIDs(                                table: String,
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
