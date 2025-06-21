package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.DbConstants.Priority


fun SQLiteDatabase.getLastId(                                                  tableName: String
) = rawQuery("SELECT MAX($ID) FROM $tableName", null).use { cursor ->
    if (cursor.moveToFirst())
        cursor.getInt(0)
    else
        0
}


fun SQLiteDatabase.getAllIDs(                                               tableName: String,
                                                                              orderBy: String = ""
) = getList<Int>(tableName, ID, orderBy = orderBy)



fun SQLiteDatabase.tableExists(                                                tableName: String
) = rawQuery(
    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
    arrayOf(tableName)
).use { it.moveToFirst() }


fun SQLiteDatabase.isTableEmpty(tableName: String) = !hasRows(tableName)


fun SQLiteDatabase.getLastPriority(                                             tableName: String
) = rawQuery("SELECT MAX($Priority) FROM $tableName", null).use { cursor ->
    if (cursor.moveToFirst())
        cursor.getInt(0)
    else
        0
}










