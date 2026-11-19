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
import com.vankorno.vankornodb.get.internal.getColVals
import com.vankorno.vankornodb.misc.getBoolean

/**
 * Retrieves a list of Ints from a single column.
 */
fun SQLiteDatabase.getColInts(                                        table: String,
                                                                     column: IntCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<Int> = getColVals(table, column.name, orderBy, extra, where) { it.getInt(0) }


/**
 * Retrieves a list of Strings from a single column.
 */
fun SQLiteDatabase.getColStrings(                                     table: String,
                                                                     column: StrCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<String> = getColVals(table, column.name, orderBy, extra, where) { it.getString(0) }


/**
 * Retrieves a list of Booleans from a single column.
 */
fun SQLiteDatabase.getColBools(                                       table: String,
                                                                     column: BoolCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<Boolean> = getColVals(table, column.name, orderBy, extra, where) { it.getBoolean(0) }


/**
 * Retrieves a list of Longs from a single column.
 */
fun SQLiteDatabase.getColLongs(                                       table: String,
                                                                     column: LongCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<Long> = getColVals(table, column.name, orderBy, extra, where) { it.getLong(0) }


/**
 * Retrieves a list of Floats from a single column.
 */
fun SQLiteDatabase.getColFloats(                                      table: String,
                                                                     column: FloatCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<Float> = getColVals(table, column.name, orderBy, extra, where) { it.getFloat(0) }


/**
 * Retrieves a list of Blobs from a single column.
 */
fun SQLiteDatabase.getColBlobs(                                       table: String,
                                                                     column: BlobCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
): List<ByteArray> = getColVals(table, column.name, orderBy, extra, where) { it.getBlob(0) }












