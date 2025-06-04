package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.DbConstants.deleteFrom
import com.vankorno.vankornodb.core.WhereBuilder

fun SQLiteDatabase.deleteRowById(                                                     id: Int,
                                                                               tableName: String
): Int = delete(tableName, "$ID = ?", arrayOf(id.toString()))


fun <T> SQLiteDatabase.deleteRow(                                              tableName: String,
                                                                                   where: String,
                                                                                  equals: T
): Int = delete(tableName, "$where = ?", arrayOf(equals.toString()))


fun SQLiteDatabase.deleteRow(                                      tableName: String,
                                                                       where: WhereBuilder.()->Unit
): Int {
    val builder = WhereBuilder().apply(where)
    return delete(tableName, builder.clauses.joinToString(" "), builder.args.toTypedArray())
}


fun SQLiteDatabase.deleteFirstRow(                                             tableName: String
) {
    delete(tableName, "ROWID = (SELECT ROWID FROM $tableName LIMIT 1)", null)
}


fun SQLiteDatabase.deleteLastRow(                                             tableName: String
) {
    delete(tableName, "ROWID = (SELECT ROWID FROM $tableName ORDER BY ROWID DESC LIMIT 1)", null)
}


fun SQLiteDatabase.clearTable(                                           tableName: String,
                                                                       resetAutoID: Boolean = true
) {
    execSQL(deleteFrom + tableName)
    if (resetAutoID)
        execSQL("DELETE FROM sqlite_sequence WHERE name = '$tableName'")
}



