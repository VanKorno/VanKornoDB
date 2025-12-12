package com.vankorno.vankornodb.getSet

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.misc.getBool


inline fun <R> SQLiteDatabase.getValue(                                table: String,
                                                                      column: String,
                                                                     default: R,
                                                                    typeName: String,
                                                                       where: WhereBuilder.()->Unit,
                                                  crossinline getCursorValue: (Cursor)->R,
): R {
    val builder = WhereBuilder().apply(where)
    val whereClause = builder.clauses.joinToString(" ")
    val whereArgs = builder.args.toTypedArray()

    return rawQuery(
        "SELECT $column FROM $table WHERE $whereClause LIMIT 1",
        whereArgs
    ).use { cursor ->
        if (cursor.moveToFirst()) getCursorValue(cursor)
        else {
            // region LOG
            Log.e(DbTAG, "$typeName() Unable to get value from $table (column: $column). Returning default. Where: $whereClause Args: ${whereArgs.joinToString()}")
            // endregion
            default
        }
    }
}


fun SQLiteDatabase.getInt(table: String, column: String, where: WhereBuilder.()->Unit) =
    getValue(table, column, -1, "getInt", where) { it.getInt(0) }

fun SQLiteDatabase.getStr(table: String, column: String, where: WhereBuilder.()->Unit): String =
    getValue(table, column, "", "getStr", where) { it.getString(0) }

fun SQLiteDatabase.getBool(table: String, column: String, where: WhereBuilder.()->Unit) =
    getValue(table, column, false, "getBool", where) { it.getBool(0) }

fun SQLiteDatabase.getLong(table: String, column: String, where: WhereBuilder.()->Unit) =
    getValue(table, column, -1L, "getLong", where) { it.getLong(0) }

fun SQLiteDatabase.getFloat(table: String, column: String, where: WhereBuilder.()->Unit) =
    getValue(table, column, -1F, "getFloat", where) { it.getFloat(0) }

fun SQLiteDatabase.getBlob(table: String, column: String, where: WhereBuilder.()->Unit): ByteArray =
    getValue(table, column, ByteArray(0), "getBlob", where) { it.getBlob(0) }
