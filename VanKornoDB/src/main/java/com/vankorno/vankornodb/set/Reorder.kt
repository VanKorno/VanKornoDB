// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.get.getCursorPro
import com.vankorno.vankornodb.get.getInt
import com.vankorno.vankornodb.get.getLastPosition
import com.vankorno.vankornodb.misc.columns
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition

fun SQLiteDatabase.reorder(                                           table: String,
                                                                         id: Int,
                                                               moveUpOrBack: Boolean,
                                                            makeFirstOrLast: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
): Boolean {
    // region LOG
    Log.d(DbTAG, "reorder(table = $table, id = $id, moveUpOrBack = $moveUpOrBack, makeFirstOrLast = $makeFirstOrLast")
    // endregion
    if (makeFirstOrLast) {
        val successful = reorderToStartEnd(table, id, moveUpOrBack)
        return successful //\/\/\/\/\/\
    }
    val position = getInt(table, cPosition) { ID = id }
    
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
        
        val neighbourID = cursor.getInt(0)
        val neighbourPosition = cursor.getInt(1)
        
        setInt(neighbourPosition, table, cPosition) { ID = id }
        setInt(position, table, cPosition) { ID = neighbourID }
    }
    return true
}


private fun SQLiteDatabase.reorderToStartEnd(                         table: String,
                                                                         id: Int,
                                                               moveUpOrBack: Boolean,
                                                                      where: WhereDsl.()->Unit = {},
): Boolean {
    val position = getInt(table, cPosition) { ID = id }
    
    if (!moveUpOrBack) {
        val last = getLastPosition(table, where)
        if (position == last) return false //\/\/\/\/\/\
        
        val newPosition = last + 1
        setInt(newPosition, table, cPosition) { ID = id }
    }
    else {
        if (position < 2) return false
        
        set(
            table,
            where = {
                cPosition less position
                andGroup(where)
            }
        ) {
            cPosition add 1
        }
        setInt(1, table, cPosition) { ID = id }
    }
    return true
}







