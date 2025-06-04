package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.getBool


fun SQLiteDatabase.setById(value: Any, id: Int, tableName: String, column: String) = set(value, tableName, column, ID, id)


fun <T> SQLiteDatabase.set(                                                        value: Any,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
) {
    val cv = ContentValues()
    cv.putSmart(column, value)
    update(tableName, cv, whereClause+"=?", arrayOf(whereArg.toString()))
}


fun SQLiteDatabase.set(                                                value: Any,
                                                                   tableName: String,
                                                                      column: String,
                                                                       where: WhereBuilder.()->Unit
) {
    val cv = ContentValues()
    cv.putSmart(column, value)
    val builder = WhereBuilder().apply(where)
    update(tableName, cv, builder.clauses.joinToString(" "), builder.args.toTypedArray())
}


fun SQLiteDatabase.setValues(                                      tableName: String,
                                                                     updates: Map<String, Any?>,
                                                                       where: WhereBuilder.()->Unit
) {
    if (updates.isEmpty()) return //\/\/\/\/\/\
    
    val cv = ContentValues().apply {
        updates.forEach { (col, value) ->
            putSmart(col, value)
        }
    }
    val builder = WhereBuilder().apply(where)
    update(tableName, cv, builder.clauses.joinToString(" "), builder.args.toTypedArray())
}


private fun ContentValues.putSmart(                                              key: String,
                                                                               value: Any?
) = when (value) {
    null         -> putNull(key)
    is String    -> put(key, value)
    is Int       -> put(key, value)
    is Boolean   -> put(key, if (value) 1 else 0)
    is Long      -> put(key, value)
    is Float     -> put(key, value)
    is ByteArray -> put(key, value)
    else         -> throw IllegalArgumentException("Unsupported value type: ${value::class}")
}











