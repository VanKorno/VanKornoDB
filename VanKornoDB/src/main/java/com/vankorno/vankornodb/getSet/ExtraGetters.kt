package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.*


fun SQLiteDatabase.getLastID(                                                  tableName: String
) = rawQuery(
    select + ID + from + tableName + orderBy + RowID + descending + limit + "1",
    null
).use { cursor ->
    if (cursor.moveToFirst())
        cursor.getInt(0)
    else {
        // region LOG
        Log.e(DbTAG, "getLastID() Unable to get value from DB ($tableName). Returning -1")
        // endregion
        -1
    }
}


fun SQLiteDatabase.getAllIDs(                                               tableName: String,
                                                                              orderBy: String = ""
) = getList<Int>(tableName, ID, orderBy = orderBy)



fun SQLiteDatabase.tableExists(                                                tableName: String
) = rawQuery(
    select + Name + from + MasterTable + where + "type='table' AND name=?", arrayOf(tableName)
).use { it.moveToFirst() }


fun SQLiteDatabase.isTableEmpty(tableName: String) = !hasRows(tableName)


fun SQLiteDatabase.getNewPriority(                                             tableName: String
) = rawQuery(
    select + Priority + from + tableName + orderBy + Priority + descending + limit + "1",
    null
).use { cursor ->
    if (cursor.moveToFirst())
        cursor.getInt(0) + 1
    else
        1
}










