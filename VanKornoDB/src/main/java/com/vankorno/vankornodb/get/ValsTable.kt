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

/**
 * Retrieves a 2D matrix of Ints from specified columns and rows.
 */
fun SQLiteDatabase.getTableInts(                                      table: String,
                                                                    columns: Array<IntCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<List<Int>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getInt(col)
}


/**
 * Retrieves a 2D matrix of Strings from specified columns and rows.
 */
fun SQLiteDatabase.getTableStrings(                                   table: String,
                                                                    columns: Array<StrCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<List<String>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getString(col)
}


/**
 * Retrieves a 2D matrix of Booleans from specified columns and rows.
 */
fun SQLiteDatabase.getTableBools(                                     table: String,
                                                                    columns: Array<BoolCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<List<Boolean>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getBoolean(col)
}


/**
 * Retrieves a 2D matrix of Longs from specified columns and rows.
 */
fun SQLiteDatabase.getTableLongs(                                     table: String,
                                                                    columns: Array<LongCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<List<Long>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getLong(col)
}


/**
 * Retrieves a 2D matrix of Floats from specified columns and rows.
 */
fun SQLiteDatabase.getTableFloats(                                    table: String,
                                                                    columns: Array<FloatCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<List<Float>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getFloat(col)
}


/**
 * Retrieves a 2D matrix of Blobs (ByteArray) from specified columns and rows.
 */
fun SQLiteDatabase.getTableBlobs(                                     table: String,
                                                                    columns: Array<BlobCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<List<ByteArray>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getBlob(col)
}











