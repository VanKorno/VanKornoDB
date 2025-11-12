package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.DbTAG
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.getBool

// =======================   S I M P L I F I E D   C O N D I T I O N S   ======================= \\

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
        Log.e(DbTAG, "$typeName() Unable to get value from $tableName (column: $column). Returning default. Condition: $whereClause = $whereArg")
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






// =============================   D S L   C O N D I T I O N S   ============================= \\

inline fun <R> SQLiteDatabase.getValue(                            tableName: String,
                                                                      column: String,
                                                                     default: R,
                                                                    typeName: String,
                                                                       where: WhereBuilder.()->Unit,
                                                  crossinline getCursorValue: (Cursor)->R
): R {
    val builder = WhereBuilder().apply(where)
    val whereClause = builder.clauses.joinToString(" ")
    val whereArgs = builder.args.toTypedArray()

    return rawQuery(
        "SELECT $column FROM $tableName WHERE $whereClause LIMIT 1",
        whereArgs
    ).use { cursor ->
        if (cursor.moveToFirst()) getCursorValue(cursor)
        else {
            // region LOG
            Log.e(DbTAG, "$typeName() Unable to get value from $tableName (column: $column). Returning default. Where: $whereClause Args: ${whereArgs.joinToString()}")
            // endregion
            default
        }
    }
}


fun SQLiteDatabase.getInt(tableName: String, column: String, where: WhereBuilder.()->Unit) =
    getValue(tableName, column, -1, "getInt", where) { it.getInt(0) }

fun SQLiteDatabase.getStr(tableName: String, column: String, where: WhereBuilder.()->Unit): String =
    getValue(tableName, column, "", "getStr", where) { it.getString(0) }

fun SQLiteDatabase.getBool(tableName: String, column: String, where: WhereBuilder.()->Unit) =
    getValue(tableName, column, false, "getBool", where) { it.getBool(0) }

fun SQLiteDatabase.getLong(tableName: String, column: String, where: WhereBuilder.()->Unit) =
    getValue(tableName, column, -1L, "getLong", where) { it.getLong(0) }

fun SQLiteDatabase.getFloat(tableName: String, column: String, where: WhereBuilder.()->Unit) =
    getValue(tableName, column, -1F, "getFloat", where) { it.getFloat(0) }

fun SQLiteDatabase.getBlob(tableName: String, column: String, where: WhereBuilder.()->Unit): ByteArray =
    getValue(tableName, column, ByteArray(0), "getBlob", where) { it.getBlob(0) }







// =============================   M U L T I P L E   V A L U E S   ============================= \\















