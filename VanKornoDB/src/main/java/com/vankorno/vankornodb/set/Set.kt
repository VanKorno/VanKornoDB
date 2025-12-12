package com.vankorno.vankornodb.set
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.misc.byNameAnd

fun SQLiteDatabase.setInt(value: Int, table: String, column: IntCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setStr(value: String, table: String, column: StrCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setBool(value: Boolean, table: String, column: BoolCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setLong(value: Long, table: String, column: LongCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setFloat(value: Float, table: String, column: FloatCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setBlob(value: ByteArray, table: String, column: BlobCol, where: WhereBuilder.()->Unit) = setNoty(value, table, column.name, where)




fun SQLiteDatabase.setIntById(value: Int, id: Int, table: String, column: IntCol, andWhere: WhereBuilder.()->Unit = {})
    = setByIdNoty(value, id, table, column.name, andWhere)

fun SQLiteDatabase.setStrById(value: String, id: Int, table: String, column: StrCol, andWhere: WhereBuilder.()->Unit = {})
    = setByIdNoty(value, id, table, column.name, andWhere)

fun SQLiteDatabase.setBoolById(value: Boolean, id: Int, table: String, column: BoolCol, andWhere: WhereBuilder.()->Unit = {})
    = setByIdNoty(value, id, table, column.name, andWhere)

fun SQLiteDatabase.setLongById(value: Long, id: Int, table: String, column: LongCol, andWhere: WhereBuilder.()->Unit = {})
    = setByIdNoty(value, id, table, column.name, andWhere)

fun SQLiteDatabase.setFloatById(value: Float, id: Int, table: String, column: FloatCol, andWhere: WhereBuilder.()->Unit = {})
    = setByIdNoty(value, id, table, column.name, andWhere)

fun SQLiteDatabase.setBlobById(value: ByteArray, id: Int, table: String, column: BlobCol, andWhere: WhereBuilder.()->Unit = {})
    = setByIdNoty(value, id, table, column.name, andWhere)




fun SQLiteDatabase.setIntByName(value: Int, name: String, table: String, column: IntCol, andWhere: WhereBuilder.()->Unit = {})
    = setNoty(value, name, table, column.name, byNameAnd(name, andWhere))

fun SQLiteDatabase.setStrByName(value: String, name: String, table: String, column: StrCol, andWhere: WhereBuilder.()->Unit = {})
    = setNoty(value, name, table, column.name, byNameAnd(name, andWhere))

fun SQLiteDatabase.setBoolByName(value: Boolean, name: String, table: String, column: BoolCol, andWhere: WhereBuilder.()->Unit = {})
    = setNoty(value, name, table, column.name, byNameAnd(name, andWhere))

fun SQLiteDatabase.setLongByName(value: Long, name: String, table: String, column: LongCol, andWhere: WhereBuilder.()->Unit = {})
    = setNoty(value, name, table, column.name, byNameAnd(name, andWhere))

fun SQLiteDatabase.setFloatByName(value: Float, name: String, table: String, column: FloatCol, andWhere: WhereBuilder.()->Unit = {})
    = setNoty(value, name, table, column.name, byNameAnd(name, andWhere))

fun SQLiteDatabase.setBlobByName(value: ByteArray, name: String, table: String, column: BlobCol, andWhere: WhereBuilder.()->Unit = {})
    = setNoty(value, name, table, column.name, byNameAnd(name, andWhere))




