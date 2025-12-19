// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get.noty

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.misc.getBoolean


inline fun <R> SQLiteDatabase.getValueNoty(                            table: String,
                                                                      column: String,
                                                                     default: R,
                                                                     funName: String,
                                                                       where: WhereBuilder.()->Unit,
                                                  crossinline getCursorValue: (Cursor)->R,
): R {
    val builder = WhereBuilder().apply(where)
    val whereClause = builder.clauses.joinToString(" ")
    val whereArgs = builder.args.toTypedArray()
    
    val whereClauseStr = if (whereClause.isNotBlank()) " WHERE $whereClause" else ""
    
    val sql = "SELECT $column FROM $table $whereClauseStr LIMIT 1"
    
    return rawQuery(sql, whereArgs).use { cursor ->
        if (cursor.moveToFirst())
            getCursorValue(cursor)
        else {
            // region LOG
            Log.e(DbTAG, "$funName() Unable to get value from $table (column: $column). Returning default. Where: $whereClause Args: ${whereArgs.joinToString()}")
            // endregion
            default
        }
    }
}


fun <R> SQLiteDatabase.getValueProNoty(                              table: String,
                                                                    column: String,
                                                                   default: R,
                                                                   funName: String,
                                                                 queryOpts: QueryOpts.()->Unit = {},
                                                            getCursorValue: (Cursor)->R,
): R {
    return getCursorProNoty(table, arrayOf(column)) {
        applyOpts(queryOpts)
        limit = 1
    }.use { cursor ->
        if (cursor.moveToFirst())
            getCursorValue(cursor)
        else {
            // region LOG
            Log.e(DbTAG, "$funName() Unable to get value from $table (column: $column). Returning default.")
            // endregion
            default
        }
    }
}


fun SQLiteDatabase.getIntNoty(table: String, column: String, where: WhereBuilder.()->Unit) =
    getValueNoty(table, column, -1, "getInt", where) { it.getInt(0) }

fun SQLiteDatabase.getStrNoty(table: String, column: String, where: WhereBuilder.()->Unit): String =
    getValueNoty(table, column, "", "getStr", where) { it.getString(0) }

fun SQLiteDatabase.getBoolNoty(table: String, column: String, where: WhereBuilder.()->Unit) =
    getValueNoty(table, column, false, "getBool", where) { it.getBoolean(0) }

fun SQLiteDatabase.getLongNoty(table: String, column: String, where: WhereBuilder.()->Unit) =
    getValueNoty(table, column, -1L, "getLong", where) { it.getLong(0) }

fun SQLiteDatabase.getFloatNoty(table: String, column: String, where: WhereBuilder.()->Unit) =
    getValueNoty(table, column, -1F, "getFloat", where) { it.getFloat(0) }

fun SQLiteDatabase.getBlobNoty(table: String, column: String, where: WhereBuilder.()->Unit): ByteArray =
    getValueNoty(table, column, ByteArray(0), "getBlob", where) { it.getBlob(0) }




