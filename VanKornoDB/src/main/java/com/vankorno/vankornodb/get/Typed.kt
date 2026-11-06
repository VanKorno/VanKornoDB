/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
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
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): Int = getValueNoty(table, column.name, -1, "getIntPro", orderBy, extra, where) {
    it.getInt(0)
}


fun SQLiteDatabase.getStr(                                            table: String,
                                                                     column: StrCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): String = getValueNoty(table, column.name, "", "getStrPro", orderBy, extra, where) {
    it.getString(0)
}


fun SQLiteDatabase.getBool(                                           table: String,
                                                                     column: BoolCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): Boolean = getValueNoty(table, column.name, false, "getBoolPro", orderBy, extra, where) {
    it.getBoolean(0)
}


fun SQLiteDatabase.getLong(                                           table: String,
                                                                     column: LongCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): Long = getValueNoty(table, column.name, -1L, "getLongPro", orderBy, extra, where) {
    it.getLong(0)
}


fun SQLiteDatabase.getFloat(                                          table: String,
                                                                     column: FloatCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): Float = getValueNoty(table, column.name, -1F, "getFloat", orderBy, extra, where) {
    it.getFloat(0)
}


fun SQLiteDatabase.getBlob(                                           table: String,
                                                                     column: BlobCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): ByteArray = getValueNoty(table, column.name, ByteArray(0), "getBlob", orderBy, extra, where) {
    it.getBlob(0)
}









