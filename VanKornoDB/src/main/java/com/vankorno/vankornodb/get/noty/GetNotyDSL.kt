/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.noty

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.getBoolean
import com.vankorno.vankornodb.misc.suppressValGetterErrorLog


fun <R> SQLiteDatabase.getValueNoty(                                  table: String,
                                                                     column: String,
                                                                    default: R,
                                                                    funName: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
                                                             getCursorValue: (Cursor)->R,
): R {
    return getCursorNoty(table, arrayOf(column), orderBy, where = where,
        extra = {
            applyDsl(extra)
            limit = 1
        }
    ).use { cursor ->
        if (cursor.moveToFirst())
            getCursorValue(cursor)
        else {
            // region LOG
            if (!suppressValGetterErrorLog)
                eLog("$funName() Unable to get value from $table (column: $column). Returning default.")
            // endregion
            default
        }
    }
}


fun SQLiteDatabase.getIntNoty(table: String, column: String, where: WhereDsl.()->Unit) =
    getValueNoty(table, column, -1, "getInt", where = where) { it.getInt(0) }

fun SQLiteDatabase.getStrNoty(table: String, column: String, where: WhereDsl.()->Unit): String =
    getValueNoty(table, column, "", "getStr", where = where) { it.getString(0) }

fun SQLiteDatabase.getBoolNoty(table: String, column: String, where: WhereDsl.()->Unit) =
    getValueNoty(table, column, false, "getBool", where = where) { it.getBoolean(0) }

fun SQLiteDatabase.getLongNoty(table: String, column: String, where: WhereDsl.()->Unit) =
    getValueNoty(table, column, -1L, "getLong", where = where) { it.getLong(0) }

fun SQLiteDatabase.getFloatNoty(table: String, column: String, where: WhereDsl.()->Unit) =
    getValueNoty(table, column, -1F, "getFloat", where = where) { it.getFloat(0) }

fun SQLiteDatabase.getBlobNoty(table: String, column: String, where: WhereDsl.()->Unit): ByteArray =
    getValueNoty(table, column, ByteArray(0), "getBlob", where = where) { it.getBlob(0) }




