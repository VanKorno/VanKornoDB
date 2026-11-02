package com.vankorno.vankornodb.dbManagement

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.get.getAllIDs

private fun planB() {
    
}


private fun SQLiteDatabase.getFreeIDs(                                table: String,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> {
    if (count <= 0) return emptyList() ///\/\/\/\/\/\
    
    val existingIDs = getAllIDs(table).toSet()
    val allReservedIDs = reservedIDs.filter { it > 0L }.toSet()
    
    val freeIDs = mutableListOf<Long>()
    var candidateId = 1L
    
    while (freeIDs.size < count) {
        if (candidateId !in existingIDs && candidateId !in allReservedIDs)
            freeIDs.add(candidateId)
        
        if (freeIDs.size < count) {
            if (candidateId == Long.MAX_VALUE)
                throw IllegalStateException("No more available positive IDs in table '$table'")
            
            candidateId++
        }
    }
    return freeIDs
}
