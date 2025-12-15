package com.vankorno.vankornodb.delete
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants._ID

fun SQLiteDatabase.deleteRowById(                                                     id: Int,
                                                                                   table: String,
): Int = delete(table, "$_ID = ?", arrayOf(id.toString()))


fun <T> SQLiteDatabase.deleteRow(                                                  table: String,
                                                                                   where: String,
                                                                                  equals: T,
): Int = delete(table, "$where = ?", arrayOf(equals.toString()))


fun SQLiteDatabase.deleteRow(                                          table: String,
                                                                       where: WhereBuilder.()->Unit,
): Int {
    val builder = WhereBuilder().apply(where)
    return delete(table, builder.buildStr(), builder.args.toTypedArray())
}


fun SQLiteDatabase.deleteFirstRow(                                                 table: String,
) {
    delete(table, "ROWID = (SELECT ROWID FROM $table LIMIT 1)", null)
}


fun SQLiteDatabase.deleteLastRow(                                                  table: String,
) {
    delete(table, "ROWID = (SELECT ROWID FROM $table ORDER BY ROWID DESC LIMIT 1)", null)
}


fun SQLiteDatabase.clearTable(                                                     table: String,
                                                                       //resetAutoID: Boolean = true
) {
    execSQL("DELETE FROM " + table)
    //if (resetAutoID)
    //    execSQL("DELETE FROM sqlite_sequence WHERE name = '$tableName'")
}



fun SQLiteDatabase.deleteTable(table: String) { execSQL("DROP TABLE IF EXISTS " + table) }
