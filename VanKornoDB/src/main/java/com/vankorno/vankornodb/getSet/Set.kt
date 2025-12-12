package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.ID
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol

fun SQLiteDatabase.setInt(value: Int, table: String, column: IntCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setStr(value: String, table: String, column: StrCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setBool(value: Boolean, table: String, column: BoolCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setLong(value: Long, table: String, column: LongCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setFloat(value: Float, table: String, column: FloatCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setBlob(value: ByteArray, table: String, column: BlobCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)

fun SQLiteDatabase.setIntById(value: Int, id: Int, table: String, column: IntCol, andWhere: WhereBuilder.()->Unit) = setNoty(value, table, column.name, andWhere)
fun SQLiteDatabase.setStrById(value: String, id: Int, table: String, column: StrCol, andWhere: WhereBuilder.()->Unit) = setNoty(value, table, column.name, andWhere)
fun SQLiteDatabase.setBoolById(value: Boolean, id: Int, table: String, column: BoolCol, andWhere: WhereBuilder.()->Unit) = setNoty(value, table, column.name, andWhere)
fun SQLiteDatabase.setLongById(value: Long, id: Int, table: String, column: LongCol, andWhere: WhereBuilder.()->Unit) = setNoty(value, table, column.name, andWhere)
fun SQLiteDatabase.setFloatById(value: Float, id: Int, table: String, column: FloatCol, andWhere: WhereBuilder.()->Unit) = setNoty(value, table, column.name, andWhere)
fun SQLiteDatabase.setBlobById(value: ByteArray, id: Int, table: String, column: BlobCol, andWhere: WhereBuilder.()->Unit) = setNoty(value, table, column.name, andWhere)






fun SQLiteDatabase.setByIdNoty(                                   value: Any,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: String,
                                                               andWhere: WhereBuilder.()->Unit = {},
) = setNoty(value, table, column) {
    ID equal id
    andGroup(andWhere)
}


fun <T> SQLiteDatabase.setNoty(                                                    value: Any,
                                                                                   table: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: T,
) {
    val cv = ContentValues()
    cv.putSmart(column, value)
    update(table, cv, where+"=?", arrayOf(equals.toString()))
}


fun SQLiteDatabase.setNoty(                                            value: Any,
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











