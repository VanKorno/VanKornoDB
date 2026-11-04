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
import com.vankorno.vankornodb.get.internal.getRowVals
import com.vankorno.vankornodb.misc.getBoolean

/**
 * Retrieves a list of Ints from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowInts(                                        table: String,
                                                                    columns: Array<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<Int> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getInt(col)
}


/**
 * Retrieves a list of Strings from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowStrings(                                     table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<String> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getString(col)
}


/**
 * Retrieves a list of Booleans from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowBools(                                       table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<Boolean> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getBoolean(col)
}


/**
 * Retrieves a list of Longs from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowLongs(                                       table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<Long> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getLong(col)
}


/**
 * Retrieves a list of Floats from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowFloats(                                      table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<Float> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getFloat(col)
}


/**
 * Retrieves a list of Blobs (ByteArray) from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowBlobs(                                       table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<ByteArray> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), where, orderBy, extra
) { cursor, col ->
    cursor.getBlob(col)
}






