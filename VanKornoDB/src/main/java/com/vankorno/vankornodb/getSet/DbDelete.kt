package com.vankorno.vankornodb.getSet

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.ID
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








