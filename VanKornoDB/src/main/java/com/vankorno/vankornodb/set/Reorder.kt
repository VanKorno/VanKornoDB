// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.get.getCursorPro
import com.vankorno.vankornodb.get.getInt
import com.vankorno.vankornodb.get.getLastPosition
import com.vankorno.vankornodb.misc.columns
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition

fun SQLiteDatabase.reorder(                                                 table: String,
                                                                               id: Int,
                                                                     moveUpOrBack: Boolean,
                                                                  makeFirstOrLast: Boolean = false,
) {
    // region LOG
    Log.d(DbTAG, "reorder(table = $table, id = $id, moveUpOrBack = $moveUpOrBack, makeFirstOrLast = $makeFirstOrLast")
    // endregion
    if (makeFirstOrLast) {
        reorderToStartEnd(table, id, moveUpOrBack)
        return //\/\/\/\/\/\
    }
    val position = getInt(table, cPosition) { ID = id }
    
    getCursorPro(table, columns(cID, cPosition)) {
        where {
            if (moveUpOrBack)
                cPosition less position
            else
                cPosition greater position
        }
        orderByPosition()
        limit = 1
    }.use { cursor ->
        if (cursor.count < 1)  return  //\/\/\/\/\/\
        
        if (moveUpOrBack)
            cursor.moveToLast()
        else
            cursor.moveToFirst()
        
        val neighbourID = cursor.getInt(0)
        val neighbourPosition = cursor.getInt(1)
        
        setInt(neighbourPosition, table, cPosition) { ID = id }
        setInt(position, table, cPosition) { ID = neighbourID }
    }
}


private fun SQLiteDatabase.reorderToStartEnd(                                      table: String,
                                                                                      id: Int,
                                                                            moveUpOrBack: Boolean,
) {
    if (!moveUpOrBack) {
        val newPosition = getLastPosition(table) + 1
        setInt(newPosition, table, cPosition) { ID = id }
    }
    else {
        val position = getInt(table, cPosition) { ID = id }
        
        set(table, where = { cPosition less position }) {
            cPosition add 1
        }
        setInt(1, table, cPosition) { ID = id }
    }
}







