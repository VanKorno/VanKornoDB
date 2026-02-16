// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get.noty

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.getBoolean

inline fun <T, R> SQLiteDatabase.getValueNoty(                                  table: String,
                                                                               column: String,
                                                                          whereClause: String,
                                                                             whereArg: T,
                                                                              default: R,
                                                                              funName: String,
                                                           crossinline getCursorValue: (Cursor)->R,
): R = rawQuery(
    "SELECT $column FROM $table WHERE $whereClause=? LIMIT 1",
    arrayOf(whereArg.toString())
).use { cursor ->
    if (cursor.moveToFirst()) getCursorValue(cursor)
    else {
        // region LOG
        eLog("$funName() Unable to get value from $table (column: $column). Returning default. Condition: $whereClause = $whereArg")
        // endregion
        default
    }
}


fun <T> SQLiteDatabase.getIntNoty(table: String, column: String, whereClause: String, whereArg: T) =
    getValueNoty(table, column, whereClause, whereArg, -1, "getIntNoty") { it.getInt(0) }

fun <T> SQLiteDatabase.getStrNoty(table: String, column: String, whereClause: String, whereArg: T): String =
    getValueNoty(table, column, whereClause, whereArg, "", "getStrNoty") { it.getString(0) }

fun <T> SQLiteDatabase.getBoolNoty(table: String, column: String, whereClause: String, whereArg: T) =
    getValueNoty(table, column, whereClause, whereArg, false, "getBoolNoty") { it.getBoolean(0) }

fun <T> SQLiteDatabase.getLongNoty(table: String, column: String, whereClause: String, whereArg: T) =
    getValueNoty(table, column, whereClause, whereArg, -1L, "getLongNoty") { it.getLong(0) }

fun <T> SQLiteDatabase.getFloatNoty(table: String, column: String, whereClause: String, whereArg: T) =
    getValueNoty(table, column, whereClause, whereArg, -1F, "getFloatNoty") { it.getFloat(0) }

fun <T> SQLiteDatabase.getBlobNoty(table: String, column: String, whereClause: String, whereArg: T): ByteArray =
    getValueNoty(table, column, whereClause, whereArg, ByteArray(0), "getBlobNoty") { it.getBlob(0) }










