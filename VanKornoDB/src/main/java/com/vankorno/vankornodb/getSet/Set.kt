package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.WhereBuilder


fun SQLiteDatabase.setById(value: Any, id: Int, table: String, column: String) = set(value, table, column, ID, id)


fun <T> SQLiteDatabase.set(                                                        value: Any,
                                                                                   table: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: T,
) {
    val cv = ContentValues()
    cv.putSmart(column, value)
    update(table, cv, where+"=?", arrayOf(equals.toString()))
}


fun SQLiteDatabase.set(                                                value: Any,
                                                                       table: String,
                                                                      column: String,
                                                                       where: WhereBuilder.()->Unit,
) {
    val cv = ContentValues()
    cv.putSmart(column, value)
    val builder = WhereBuilder().apply(where)
    update(table, cv, builder.clauses.joinToString(" "), builder.args.toTypedArray())
}



// ==============================  M U L T I - S E T T E R S  ============================== \\

fun SQLiteDatabase.setRowVals(                                         table: String,
                                                                          cv: ContentValues,
                                                                       where: WhereBuilder.()->Unit,
) {
    val builder = WhereBuilder().apply(where)
    update(table, cv, builder.clauses.joinToString(" "), builder.args.toTypedArray())
}


fun SQLiteDatabase.setRowVals(                                         table: String,
                                                                       where: WhereBuilder.()->Unit,
                                                               vararg values: Pair<String, Any?>,
) {
    if (values.isEmpty()) return //\/\/\/\/\/\

    val cv = ContentValues().apply {
        for ((col, value) in values) {
            putSmart(col, value)
        }
    }
    val builder = WhereBuilder().apply(where)
    update(table, cv, builder.clauses.joinToString(" "), builder.args.toTypedArray())
}




fun SQLiteDatabase.setRowValsById(                                               id: Int,
                                                                              table: String,
                                                                                 cv: ContentValues,
) = update(table, cv, ID+"=?", arrayOf(id.toString()))


fun SQLiteDatabase.setRowValsById(                                           id: Int,
                                                                          table: String,
                                                                  vararg values: Pair<String, Any?>,
) {
    if (values.isEmpty()) return //\/\/\/\/\/\

    val cv = ContentValues().apply {
        for ((col, value) in values) {
            putSmart(col, value)
        }
    }
    update(table, cv, ID + "=?", arrayOf(id.toString()))
}


fun SQLiteDatabase.setInAllRows(                                                   value: Any,
                                                                                   table: String,
                                                                                  column: String,
) {
    val cv = ContentValues()
    cv.putSmart(column, value)
    update(table, cv, null, null)
}


fun SQLiteDatabase.setRowValsInAllRows(                                       table: String,
                                                                                 cv: ContentValues,
) = update(table, cv, null, null)


fun SQLiteDatabase.setRowValsInAllRows(                                   table: String,
                                                                  vararg values: Pair<String, Any?>,
) {
    if (values.isEmpty()) return //\/\/\/\/\/\

    val cv = ContentValues().apply {
        for ((col, value) in values) {
            putSmart(col, value)
        }
    }
    update(table, cv, null, null)
}






private fun ContentValues.putSmart(                                                  key: String,
                                                                                   value: Any?,
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











