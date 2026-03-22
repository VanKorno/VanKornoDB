/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.get.getCursorPro
import com.vankorno.vankornodb.get.getLastPosition
import com.vankorno.vankornodb.get.getLong
import com.vankorno.vankornodb.misc.columns
import com.vankorno.vankornodb.misc.dLog
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition

fun SQLiteDatabase.reorder(                                           table: String,
                                                                         id: Long,
                                                               moveUpOrBack: Boolean,
                                                            makeFirstOrLast: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
): Boolean {
    // region LOG
    dLog("reorder(table = $table, id = $id, moveUpOrBack = $moveUpOrBack, makeFirstOrLast = $makeFirstOrLast")
    // endregion
    if (makeFirstOrLast) {
        val successful = reorderToStartEnd(table, id, moveUpOrBack)
        return successful //\/\/\/\/\/\
    }
    val position = getLong(table, cPosition) { ID = id }
    
    getCursorPro(table, columns(cID, cPosition)) {
        where {
            if (moveUpOrBack)
                cPosition less position
            else
                cPosition greater position
            
            andGroup(where)
        }
        orderBy(cPosition.flipIf(moveUpOrBack))
        limit = 1
    }.use { cursor ->
        if (!cursor.moveToFirst())
            return false //\/\/\/\/\/\
        
        val neighbourID = cursor.getLong(0)
        val neighbourPosition = cursor.getLong(1)
        
        setLong(neighbourPosition, table, cPosition) { ID = id }
        setLong(position, table, cPosition) { ID = neighbourID }
    }
    return true
}


private fun SQLiteDatabase.reorderToStartEnd(                         table: String,
                                                                         id: Long,
                                                               moveUpOrBack: Boolean,
                                                                      where: WhereDsl.()->Unit = {},
): Boolean {
    val position = getLong(table, cPosition) { ID = id }
    
    if (!moveUpOrBack) {
        val last = getLastPosition(table, where)
        if (position == last) return false //\/\/\/\/\/\
        
        val newPosition = last + 1L
        setLong(newPosition, table, cPosition) { ID = id }
    }
    else {
        if (position < 2L) return false
        
        set(
            table,
            where = {
                cPosition less position
                andGroup(where)
            }
        ) {
            cPosition add 1L
        }
        setLong(1L, table, cPosition) { ID = id }
    }
    return true
}







