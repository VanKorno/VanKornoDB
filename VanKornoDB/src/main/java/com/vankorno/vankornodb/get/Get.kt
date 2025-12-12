package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.misc.byIdAnd

fun SQLiteDatabase.getInt(table: String, column: IntCol, where: WhereBuilder.()->Unit) = getIntNoty(table, column.name, where)
fun SQLiteDatabase.getStr(table: String, column: StrCol, where: WhereBuilder.()->Unit) = getStrNoty(table, column.name, where)
fun SQLiteDatabase.getBool(table: String, column: BoolCol, where: WhereBuilder.()->Unit) = getBoolNoty(table, column.name, where)
fun SQLiteDatabase.getLong(table: String, column: LongCol, where: WhereBuilder.()->Unit) = getLongNoty(table, column.name, where)
fun SQLiteDatabase.getFloat(table: String, column: FloatCol, where: WhereBuilder.()->Unit) = getFloatNoty(table, column.name, where)
fun SQLiteDatabase.getBlob(table: String, column: BlobCol, where: WhereBuilder.()->Unit) = getBlobNoty(table, column.name, where)

fun SQLiteDatabase.getIntById(id: Int, table: String, column: IntCol, andWhere: WhereBuilder.()->Unit) = getIntNoty(table, column.name, byIdAnd(id, andWhere))
fun SQLiteDatabase.getStrById(id: Int, table: String, column: StrCol, andWhere: WhereBuilder.()->Unit) = getStrNoty(table, column.name, byIdAnd(id, andWhere))
fun SQLiteDatabase.getBoolById(id: Int, table: String, column: BoolCol, andWhere: WhereBuilder.()->Unit) = getBoolNoty(table, column.name, byIdAnd(id, andWhere))
fun SQLiteDatabase.getLongById(id: Int, table: String, column: LongCol, andWhere: WhereBuilder.()->Unit) = getLongNoty(table, column.name, byIdAnd(id, andWhere))
fun SQLiteDatabase.getFloatById(id: Int, table: String, column: FloatCol, andWhere: WhereBuilder.()->Unit) = getFloatNoty(table, column.name, byIdAnd(id, andWhere))
fun SQLiteDatabase.getBlobById(id: Int, table: String, column: BlobCol, andWhere: WhereBuilder.()->Unit) = getBlobNoty(table, column.name, byIdAnd(id, andWhere))
