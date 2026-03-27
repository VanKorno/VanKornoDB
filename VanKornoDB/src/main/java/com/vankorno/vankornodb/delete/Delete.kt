/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.delete

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.data.DbConstants._ID

fun SQLiteDatabase.deleteRowById(                                                     id: Long,
                                                                                   table: String,
): Int = delete(table, "$_ID = ?", arrayOf(id.toString()))


fun <T> SQLiteDatabase.deleteRows(                                                 table: String,
                                                                                   where: String,
                                                                                  equals: T,
): Int = delete(table, "$where = ?", arrayOf(equals.toString()))


fun SQLiteDatabase.deleteRows(                                             table: String,
                                                                           where: WhereDsl.()->Unit,
): Int {
    val builder = WhereDsl().apply(where)
    return delete(table, builder.buildStr(), builder.args.toTypedArray())
}



fun SQLiteDatabase.deleteRowsFromTables(                                  tables: List<String>,
                                                                           where: WhereDsl.()->Unit,
) {
    for (table in tables) {
        deleteRows(table, where)
    }
}

fun SQLiteDatabase.deleteRowsFromTables(                           vararg tables: String,
                                                                           where: WhereDsl.()->Unit,
) {
    for (table in tables) {
        deleteRows(table, where)
    }
}




fun SQLiteDatabase.deleteFirstRow(                                                 table: String,
) {
    delete(table, "ROWID = (SELECT ROWID FROM $table LIMIT 1)", null)
}


fun SQLiteDatabase.deleteLastRow(                                                  table: String,
) {
    delete(table, "ROWID = (SELECT ROWID FROM $table ORDER BY ROWID DESC LIMIT 1)", null)
}


fun SQLiteDatabase.clearTable(table: String) { execSQL("DELETE FROM " + table) }

fun SQLiteDatabase.clearTables(vararg tables: String) = tables.forEach { clearTable(it) }

fun SQLiteDatabase.clearTables(tables: List<String>) = tables.forEach { clearTable(it) }



fun SQLiteDatabase.deleteTable(table: String) { execSQL("DROP TABLE IF EXISTS " + table) }

fun SQLiteDatabase.deleteTables(vararg tables: String) = tables.forEach { deleteTable(it) }

fun SQLiteDatabase.deleteTables(tables: List<String>) = tables.forEach { deleteTable(it) }













