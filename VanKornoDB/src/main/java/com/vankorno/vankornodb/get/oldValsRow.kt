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
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<Int> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getInt(col)
}


/**
 * Retrieves a list of Strings from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowStrings(                                     table: String,
                                                                    columns: Array<StrCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<String> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getString(col)
}


/**
 * Retrieves a list of Booleans from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowBools(                                       table: String,
                                                                    columns: Array<BoolCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<Boolean> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getBoolean(col)
}


/**
 * Retrieves a list of Longs from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowLongs(                                       table: String,
                                                                    columns: Array<LongCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<Long> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getLong(col)
}


/**
 * Retrieves a list of Floats from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowFloats(                                      table: String,
                                                                    columns: Array<FloatCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<Float> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getFloat(col)
}


/**
 * Retrieves a list of Blobs (ByteArray) from multiple columns of a single row.
 */
fun SQLiteDatabase.getRowBlobs(                                       table: String,
                                                                    columns: Array<BlobCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<ByteArray> = getRowVals(
    table, columns.map{ it.name }.toTypedArray(), orderBy, extra, where
) { cursor, col ->
    cursor.getBlob(col)
}






