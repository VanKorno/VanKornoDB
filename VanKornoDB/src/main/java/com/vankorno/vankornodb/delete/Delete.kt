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



/** Deletes rows from the specified tables matching the condition. */
fun SQLiteDatabase.deleteRowsFromTables(                                  tables: List<String>,
                                                                           where: WhereDsl.()->Unit,
) {
    for (table in tables) {
        deleteRows(table, where)
    }
}

/** Vararg overload of [deleteRowsFromTables] */
fun SQLiteDatabase.deleteRowsFromTables(                           vararg tables: String,
                                                                           where: WhereDsl.()->Unit,
) {
    for (table in tables) {
        deleteRows(table, where)
    }
}




/** Deletes the first row from the specified table. */
fun SQLiteDatabase.deleteFirstRow(                                                 table: String,
) {
    delete(table, "ROWID = (SELECT ROWID FROM $table ORDER BY ROWID ASC LIMIT 1)", null)
}

/** Deletes the last row from the specified table. */
fun SQLiteDatabase.deleteLastRow(                                                  table: String,
) {
    delete(table, "ROWID = (SELECT ROWID FROM $table ORDER BY ROWID DESC LIMIT 1)", null)
}


/** Clears the contents of the specified table. */
fun SQLiteDatabase.clearTable(table: String) { execSQL("DELETE FROM " + table) }

/** Clears the contents of the specified tables. */
fun SQLiteDatabase.clearTables(vararg tables: String) = tables.forEach { clearTable(it) }

/** List overload of [clearTables] */
fun SQLiteDatabase.clearTables(tables: List<String>) = tables.forEach { clearTable(it) }



/** Deletes the specified table from the database. */
fun SQLiteDatabase.deleteTable(table: String) { execSQL("DROP TABLE IF EXISTS " + table) }

/** Deletes the specified tables from the database. */
fun SQLiteDatabase.deleteTables(vararg tables: String) = tables.forEach { deleteTable(it) }

/** List overload of [deleteTables] */
fun SQLiteDatabase.deleteTables(tables: List<String>) = tables.forEach { deleteTable(it) }













