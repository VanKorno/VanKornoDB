package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.core.WhereBuilder


fun SQLiteDatabase.setById(value: Any, id: Int, tableName: String, column: String) = set(value, tableName, column, ID, id)


fun <T> SQLiteDatabase.set(                                                        value: Any,
                                                                               tableName: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: T
) {
    val cv = ContentValues()
    cv.putSmart(column, value)
    update(tableName, cv, where+"=?", arrayOf(equals.toString()))
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


fun SQLiteDatabase.setMult(                                        tableName: String,
                                                                      values: Map<String, Any?>,
                                                                       where: WhereBuilder.()->Unit
) {
    if (values.isEmpty()) return //\/\/\/\/\/\
    
    val cv = ContentValues().apply {
        values.forEach { (col, value) ->
            putSmart(col, value)
        }
    }
    val builder = WhereBuilder().apply(where)
    update(tableName, cv, builder.clauses.joinToString(" "), builder.args.toTypedArray())
}


// TODO wrappers in DbHelper

fun SQLiteDatabase.setMultById(                                                id: Int,
                                                                        tableName: String,
                                                                           values: Map<String, Any?>
) {
    if (values.isEmpty()) return //\/\/\/\/\/\
    
    val cv = ContentValues().apply {
        values.forEach { (col, value) ->
            putSmart(col, value)
        }
    }
    update(tableName, cv, ID+"=?", arrayOf(id.toString()))
}


fun <T> SQLiteDatabase.setInAll(                                                   value: Any,
                                                                               tableName: String,
                                                                                  column: String
) {
    val cv = ContentValues()
    cv.putSmart(column, value)
    update(tableName, cv, null, null)
}

fun SQLiteDatabase.setMultInAll(                                        tableName: String,
                                                                           values: Map<String, Any?>
) {
    if (values.isEmpty()) return //\/\/\/\/\/\
    
    val cv = ContentValues().apply {
        values.forEach { (col, value) ->
            putSmart(col, value)
        }
    }
    update(tableName, cv, null, null)
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











