package com.vankorno.vankornodb.getSet

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
): MutableList<Int> {
    val ids = mutableListOf<Int>()
    
    getCursor(tableName, ID, orderBy = orderBy).use { cursor ->
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0))
            } while (cursor.moveToNext())
        } else {
            // region LOG
                Log.d(DbTAG, "getAllIDs() didn't find any elements in $tableName")
            // endregion
        }
    }
    return ids
}


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










