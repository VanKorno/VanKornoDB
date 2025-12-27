// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.set.noty.setNoty

fun SQLiteDatabase.setInt(value: Int, table: String, column: IntCol, where: WhereDsl.()->Unit = {}) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setStr(value: String, table: String, column: StrCol, where: WhereDsl.()->Unit = {}) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setBool(value: Boolean, table: String, column: BoolCol, where: WhereDsl.()->Unit = {}) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setLong(value: Long, table: String, column: LongCol, where: WhereDsl.()->Unit = {}) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setFloat(value: Float, table: String, column: FloatCol, where: WhereDsl.()->Unit = {}) = setNoty(value, table, column.name, where)
fun SQLiteDatabase.setBlob(value: ByteArray, table: String, column: BlobCol, where: WhereDsl.()->Unit = {}) = setNoty(value, table, column.name, where)



