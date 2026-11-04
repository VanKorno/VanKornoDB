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
import com.vankorno.vankornodb.get.internal.baseGetVals
import com.vankorno.vankornodb.misc.getBoolean

// TODO doc

fun SQLiteDatabase.getTableInts(                                      table: String,
                                                                    columns: Array<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<List<Int>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getInt(col)
}


fun SQLiteDatabase.getTableStrings(                                   table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<List<String>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getString(col)
}


fun SQLiteDatabase.getTableBools(                                     table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<List<Boolean>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getBoolean(col)
}


fun SQLiteDatabase.getTableLongs(                                     table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<List<Long>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getLong(col)
}


fun SQLiteDatabase.getTableFloats(                                    table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<List<Float>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getFloat(col)
}


fun SQLiteDatabase.getTableBlobs(                                     table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<List<ByteArray>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getBlob(col)
}











