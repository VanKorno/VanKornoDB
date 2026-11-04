/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.get.noty.getValueNoty
import com.vankorno.vankornodb.misc.getBoolean


fun SQLiteDatabase.getInt(                                            table: String,
                                                                     column: IntCol,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
) = getValueNoty(table, column.name, -1, "getIntPro", extra, where) {
    it.getInt(0)
}


fun SQLiteDatabase.getStr(                                            table: String,
                                                                     column: StrCol,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
) = getValueNoty(table, column.name, "", "getStrPro", extra, where) {
    it.getString(0)
}


fun SQLiteDatabase.getBool(                                           table: String,
                                                                     column: BoolCol,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
) = getValueNoty(table, column.name, false, "getBoolPro", extra, where) {
    it.getBoolean(0)
}


fun SQLiteDatabase.getLong(                                           table: String,
                                                                     column: LongCol,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
) = getValueNoty(table, column.name, -1L, "getLongPro", extra, where) {
    it.getLong(0)
}


fun SQLiteDatabase.getFloat(                                          table: String,
                                                                     column: FloatCol,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
) = getValueNoty(table, column.name, -1F, "getFloat", extra, where) {
    it.getFloat(0)
}


fun SQLiteDatabase.getBlob(                                           table: String,
                                                                     column: BlobCol,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
) = getValueNoty(table, column.name, ByteArray(0), "getBlob", extra, where) {
    it.getBlob(0)
}









