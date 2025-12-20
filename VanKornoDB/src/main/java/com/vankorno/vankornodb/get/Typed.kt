// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.get.noty.*
import com.vankorno.vankornodb.misc.getBoolean

fun SQLiteDatabase.getInt(table: String, column: IntCol, where: WhereDsl.()->Unit) = getIntNoty(table, column.name, where)
fun SQLiteDatabase.getStr(table: String, column: StrCol, where: WhereDsl.()->Unit) = getStrNoty(table, column.name, where)
fun SQLiteDatabase.getBool(table: String, column: BoolCol, where: WhereDsl.()->Unit) = getBoolNoty(table, column.name, where)
fun SQLiteDatabase.getLong(table: String, column: LongCol, where: WhereDsl.()->Unit) = getLongNoty(table, column.name, where)
fun SQLiteDatabase.getFloat(table: String, column: FloatCol, where: WhereDsl.()->Unit) = getFloatNoty(table, column.name, where)
fun SQLiteDatabase.getBlob(table: String, column: BlobCol, where: WhereDsl.()->Unit) = getBlobNoty(table, column.name, where)



fun SQLiteDatabase.getIntPro(                                          table: String,
                                                                      column: IntCol,
                                                                         dsl: FullDsl.()->Unit = {},
) = getValueProNoty(table, column.name, -1, "getIntPro", dsl) {
    it.getInt(0)
}

fun SQLiteDatabase.getStrPro(                                          table: String,
                                                                      column: StrCol,
                                                                         dsl: FullDsl.()->Unit = {},
) = getValueProNoty(table, column.name, "", "getStrPro", dsl) {
    it.getString(0)
}

fun SQLiteDatabase.getBoolPro(                                         table: String,
                                                                      column: BoolCol,
                                                                         dsl: FullDsl.()->Unit = {},
) = getValueProNoty(table, column.name, false, "getBoolPro", dsl) {
    it.getBoolean(0)
}

fun SQLiteDatabase.getLongPro(                                         table: String,
                                                                      column: LongCol,
                                                                         dsl: FullDsl.()->Unit = {},
) = getValueProNoty(table, column.name, -1L, "getLongPro", dsl) {
    it.getLong(0)
}

fun SQLiteDatabase.getFloatPro(                                        table: String,
                                                                      column: FloatCol,
                                                                         dsl: FullDsl.()->Unit = {},
) = getValueProNoty(table, column.name, -1F, "getFloat", dsl) {
    it.getFloat(0)
}

fun SQLiteDatabase.getBlobPro(                                         table: String,
                                                                      column: BlobCol,
                                                                         dsl: FullDsl.()->Unit = {},
) = getValueProNoty(table, column.name, ByteArray(0), "getBlob", dsl) {
    it.getBlob(0)
}









