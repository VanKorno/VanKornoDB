package com.vankorno.vankornodb.get
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
import com.vankorno.vankornodb.get.noty.getBlobNoty
import com.vankorno.vankornodb.get.noty.getBoolNoty
import com.vankorno.vankornodb.get.noty.getFloatNoty
import com.vankorno.vankornodb.get.noty.getIntNoty
import com.vankorno.vankornodb.get.noty.getLongNoty
import com.vankorno.vankornodb.get.noty.getStrNoty

fun SQLiteDatabase.getInt(table: String, column: IntCol, where: WhereBuilder.()->Unit) = getIntNoty(table, column.name, where)
fun SQLiteDatabase.getStr(table: String, column: StrCol, where: WhereBuilder.()->Unit) = getStrNoty(table, column.name, where)
fun SQLiteDatabase.getBool(table: String, column: BoolCol, where: WhereBuilder.()->Unit) = getBoolNoty(table, column.name, where)
fun SQLiteDatabase.getLong(table: String, column: LongCol, where: WhereBuilder.()->Unit) = getLongNoty(table, column.name, where)
fun SQLiteDatabase.getFloat(table: String, column: FloatCol, where: WhereBuilder.()->Unit) = getFloatNoty(table, column.name, where)
fun SQLiteDatabase.getBlob(table: String, column: BlobCol, where: WhereBuilder.()->Unit) = getBlobNoty(table, column.name, where)


// TODO Pro versions



