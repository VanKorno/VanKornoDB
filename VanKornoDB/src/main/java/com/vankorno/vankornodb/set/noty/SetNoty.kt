// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set.noty

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.data.DbConstants.WHERE

fun SQLiteDatabase.setNoty(                                                        value: Any,
                                                                                   table: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: Any,
) {
    val safeValue = getBoolSafeVal(value)
    
    execSQL("UPDATE $table SET $column = ?" + WHERE + where + "=?", arrayOf(safeValue, equals))
}



fun SQLiteDatabase.setNoty(                                           value: Any,
                                                                      table: String,
                                                                     column: String,
                                                                      where: WhereDsl.()->Unit = {},
) {
    val builder = WhereDsl().apply(where)
    val safeValue = getBoolSafeVal(value)
    
    val wherePart: String
    val args: Array<Any>
    
    if (builder.clauses.isNotEmpty()) {
        wherePart = WHERE + builder.buildStr()
        args = arrayOf(safeValue, *builder.args.toTypedArray())
    } else {
        wherePart = ""
        args = arrayOf(safeValue)
    }
    execSQL("UPDATE $table SET $column = ?" + wherePart, args)
}



internal fun getBoolSafeVal(value: Any) = if (value is Boolean)
                                             if (value) 1 else 0
                                         else
                                             value




// ==============================  M U L T I - S E T T E R S  ============================== \\

fun SQLiteDatabase.setRowValsNoty(                                         table: String,
                                                                              cv: ContentValues,
                                                                           where: WhereDsl.()->Unit,
) {
    val builder = WhereDsl().apply(where)
    update(table, cv, builder.buildStr(), builder.args.toTypedArray())
}


fun SQLiteDatabase.setRowValsNoty(                                        table: String,
                                                                          where: WhereDsl.()->Unit,
                                                                  vararg values: Pair<String, Any?>,
) {
    if (values.isEmpty()) return //\/\/\/\/\/\

    val cv = ContentValues().apply {
        for ((col, value) in values) {
            putSmart(col, value)
        }
    }
    val builder = WhereDsl().apply(where)
    update(table, cv, builder.buildStr(), builder.args.toTypedArray())
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











