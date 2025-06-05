package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.getBool

inline fun <T, R> SQLiteDatabase.getValue(                                  tableName: String,
                                                                               column: String,
                                                                          whereClause: String,
                                                                             whereArg: T,
                                                                              default: R,
                                                                             typeName: String,
                                                           crossinline getCursorValue: (Cursor)->R
): R = rawQuery(
    "SELECT $column FROM $tableName WHERE $whereClause=? LIMIT 1",
    arrayOf(whereArg.toString())
).use { cursor ->
    if (cursor.moveToFirst()) getCursorValue(cursor)
    else {
        // region LOG
        Log.e(DbTAG, "$typeName() Unable to get value from DB. Returning default. Condition: $whereClause = $whereArg")
        // endregion
        default
    }
}


fun <T> SQLiteDatabase.getInt(tableName: String, column: String, whereClause: String, whereArg: T) =
    getValue(tableName, column, whereClause, whereArg, -1, "getInt") { it.getInt(0) }

fun <T> SQLiteDatabase.getStr(tableName: String, column: String, whereClause: String, whereArg: T): String =
    getValue(tableName, column, whereClause, whereArg, "", "getStr") { it.getString(0) }

fun <T> SQLiteDatabase.getBool(tableName: String, column: String, whereClause: String, whereArg: T) =
    getValue(tableName, column, whereClause, whereArg, false, "getBool") { it.getBool(0) }

fun <T> SQLiteDatabase.getLong(tableName: String, column: String, whereClause: String, whereArg: T) =
    getValue(tableName, column, whereClause, whereArg, -1L, "getLong") { it.getLong(0) }

fun <T> SQLiteDatabase.getFloat(tableName: String, column: String, whereClause: String, whereArg: T) =
    getValue(tableName, column, whereClause, whereArg, -1F, "getFloat") { it.getFloat(0) }

fun <T> SQLiteDatabase.getBlob(tableName: String, column: String, whereClause: String, whereArg: T): ByteArray =
    getValue(tableName, column, whereClause, whereArg, ByteArray(0), "getBlob") { it.getBlob(0) }


// By ID
fun SQLiteDatabase.getIntById(id: Int, tableName: String, column: String) =
    getValue(tableName, column, ID, id, -1, "getIntById") { it.getInt(0) }

fun SQLiteDatabase.getStrById(id: Int, tableName: String, column: String): String =
    getValue(tableName, column, ID, id, "", "getStrById") { it.getString(0) }

fun SQLiteDatabase.getBoolById(id: Int, tableName: String, column: String) =
    getValue(tableName, column, ID, id, false, "getBoolById") { it.getBool(0) }

fun SQLiteDatabase.getLongById(id: Int, tableName: String, column: String) =
    getValue(tableName, column, ID, id, -1L, "getLongById") { it.getLong(0) }

fun SQLiteDatabase.getFloatById(id: Int, tableName: String, column: String) =
    getValue(tableName, column, ID, id, -1F, "getFloatById") { it.getFloat(0) }

fun SQLiteDatabase.getBlobById(id: Int, tableName: String, column: String): ByteArray =
    getValue(tableName, column, ID, id, ByteArray(0), "getBlobById") { it.getBlob(0) }
