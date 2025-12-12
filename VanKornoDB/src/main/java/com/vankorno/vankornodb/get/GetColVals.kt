package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.misc.getBoolean

/**
 * Retrieves a list of Ints from a single column.
 */
fun SQLiteDatabase.getColInts(                                       table: String,
                                                                    column: IntCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Int> = getColValsNoty(table, column.name, queryOpts) { it.getInt(0) }


/**
 * Retrieves a list of Strings from a single column.
 */
fun SQLiteDatabase.getColStrings(                                    table: String,
                                                                    column: StrCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<String> = getColValsNoty(table, column.name, queryOpts) { it.getString(0) }


/**
 * Retrieves a list of Booleans from a single column.
 */
fun SQLiteDatabase.getColBools(                                      table: String,
                                                                    column: BoolCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Boolean> = getColValsNoty(table, column.name, queryOpts) { it.getBoolean(0) }


/**
 * Retrieves a list of Longs from a single column.
 */
fun SQLiteDatabase.getColLongs(                                      table: String,
                                                                    column: LongCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Long> = getColValsNoty(table, column.name, queryOpts) { it.getLong(0) }


/**
 * Retrieves a list of Floats from a single column.
 */
fun SQLiteDatabase.getColFloats(                                     table: String,
                                                                    column: FloatCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Float> = getColValsNoty(table, column.name, queryOpts) { it.getFloat(0) }


/**
 * Retrieves a list of Blobs from a single column.
 */
fun SQLiteDatabase.getColBlobs(                                      table: String,
                                                                    column: BlobCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<ByteArray> = getColValsNoty(table, column.name, queryOpts) { it.getBlob(0) }






















