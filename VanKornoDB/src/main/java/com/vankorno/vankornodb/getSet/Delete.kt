package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.DbConstants.RowID
import com.vankorno.vankornodb.core.DbConstants.from
import com.vankorno.vankornodb.core.DbConstants.limit
import com.vankorno.vankornodb.core.DbConstants.select
import com.vankorno.vankornodb.core.WhereBuilder

fun SQLiteDatabase.deleteRowById(                                              tableName: String,
                                                                                      id: Int
): Int = delete(tableName, "$ID = ?", arrayOf(id.toString()))


fun SQLiteDatabase.deleteRow(                                                  tableName: String,
                                                                             whereClause: String,
                                                                                whereArg: String
): Int = delete(tableName, "$whereClause = ?", arrayOf(whereArg))


fun SQLiteDatabase.deleteRow(                                      tableName: String,
                                                                       where: WhereBuilder.()->Unit
): Int {
    val builder = WhereBuilder().apply(where)
    return delete(tableName, builder.clauses.joinToString(" "), builder.args.toTypedArray())
}


fun SQLiteDatabase.deleteFirstRow(                                             tableName: String
) {
    val whereClause = RowID + " = (" + select + RowID + from + tableName + limit + "1)"
    delete(tableName, whereClause, null)
}


fun SQLiteDatabase.deleteLastRow(                                             tableName: String
) {
    val whereClause = RowID + " = (" + select + RowID + from + tableName + limit + "1)"
    delete(tableName, whereClause, null)
}






