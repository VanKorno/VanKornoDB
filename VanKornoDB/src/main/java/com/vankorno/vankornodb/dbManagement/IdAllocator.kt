package com.vankorno.vankornodb.dbManagement

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.get.getAllIDs

fun planB() {
    
}


fun SQLiteDatabase.getFreeIDs(                                        table: String,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
): List<Long> {
    if (count <= 0) return emptyList() ///\/\/\/\/\/\
    
    val existingIDs = getAllIDs(table) { byId() }
    val allReservedIDs = reservedIDs.filter { it > 0L }.toSet()

    val freeIDs = mutableListOf<Long>()
    var candidate = 1L
    var idx = 0

    while (freeIDs.size < count) {
        if (idx < existingIDs.size && existingIDs[idx] == candidate || candidate in allReservedIDs) {
            if (idx < existingIDs.size && existingIDs[idx] == candidate)
                idx++
        } else {
            freeIDs.add(candidate)
        }
        candidate++
    }
    return freeIDs
}
