package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.DbConstants.Order


fun SQLiteDatabase.getLastId(table: String) = getLargestInt(table, ID, null, null)


fun SQLiteDatabase.getAllIDs(                                                   table: String,
                                                                              orderBy: String = "",
) = getList<Int>(table, ID, orderBy = orderBy)



fun SQLiteDatabase.tableExists(                                                    table: String,
) = rawQuery(
    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
    arrayOf(table)
).use { it.moveToFirst() }


fun SQLiteDatabase.isTableEmpty(table: String) = !hasRows(table)


fun SQLiteDatabase.getLastOrder(table: String) = getLargestInt(table, Order, null, null)


fun <T> SQLiteDatabase.getLastOrderBy(                                             table: String,
                                                                             whereColumn: String,
                                                                                  equals: T,
) = getLargestInt(table, Order, whereColumn, equals)



fun <T> SQLiteDatabase.getLargestInt(                                        table: String,
                                                                      targetColumn: String,
                                                                       whereColumn: String? = null,
                                                                            equals: T? = null,
): Int {
    val hasConditions = whereColumn != null && equals != null
    
    val queryEnd = if (hasConditions) " WHERE $whereColumn=?" else ""
    val selectionArgs = if (hasConditions) arrayOf(equals.toString()) else null
    
    return rawQuery(
        "SELECT MAX($targetColumn) FROM $table" + queryEnd, selectionArgs
    ).use { cursor ->
        if (cursor.moveToFirst())
            cursor.getInt(0)
        else
            0
    }
}






