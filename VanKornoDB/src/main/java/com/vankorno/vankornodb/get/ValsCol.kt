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
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<Int> = getColVals(table, column.name, where, orderBy, extra) { it.getInt(0) }


/**
 * Retrieves a list of Strings from a single column.
 */
fun SQLiteDatabase.getColStrings(                                     table: String,
                                                                     column: StrCol,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<String> = getColVals(table, column.name, where, orderBy, extra) { it.getString(0) }


/**
 * Retrieves a list of Booleans from a single column.
 */
fun SQLiteDatabase.getColBools(                                       table: String,
                                                                     column: BoolCol,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<Boolean> = getColVals(table, column.name, where, orderBy, extra) { it.getBoolean(0) }


/**
 * Retrieves a list of Longs from a single column.
 */
fun SQLiteDatabase.getColLongs(                                       table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<Long> = getColVals(table, column.name, where, orderBy, extra) { it.getLong(0) }


/**
 * Retrieves a list of Floats from a single column.
 */
fun SQLiteDatabase.getColFloats(                                      table: String,
                                                                     column: FloatCol,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<Float> = getColVals(table, column.name, where, orderBy, extra) { it.getFloat(0) }


/**
 * Retrieves a list of Blobs from a single column.
 */
fun SQLiteDatabase.getColBlobs(                                       table: String,
                                                                     column: BlobCol,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): List<ByteArray> = getColVals(table, column.name, where, orderBy, extra) { it.getBlob(0) }












