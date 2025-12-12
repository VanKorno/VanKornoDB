package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.misc.getBoolean

//  ----------------------------------------  I N T  ----------------------------------------  \\

/**
 * Retrieves a list of Ints from a single column.
 * [table] Db table name
 * [column] IntCol object with db column name, type, etc.
 * [queryOpts] DSL with full SQLite query options
 */
fun SQLiteDatabase.getColIntsPro(                                    table: String,
                                                                    column: IntCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Int> = getColValsPro(table, column.name, queryOpts) { it.getInt(0) }

/**
 * Retrieves a list of Ints from a single column.
 */
fun SQLiteDatabase.getColInts(                                    table: String,
                                                                 column: IntCol,
                                                                  where: WhereBuilder.()->Unit = {},
): List<Int> = getColVals(table, column.name, where) { it.getInt(0) }





//  -------------------------------------  S T R I N G  -------------------------------------  \\

/**
 * Retrieves a list of Strings from a single column.
 */
fun SQLiteDatabase.getColStrings(                                 table: String,
                                                                 column: StrCol,
                                                                  where: WhereBuilder.()->Unit = {},
): List<String> = getColVals(table, column.name, where) { it.getString(0) }


/**
 * Retrieves a list of Strings from a single column.
 */
fun SQLiteDatabase.getColStringsPro(                                 table: String,
                                                                    column: StrCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<String> = getColValsPro(table, column.name, queryOpts) { it.getString(0) }




//  ------------------------------------  B O O L E A N  ------------------------------------  \\

/**
 * Retrieves a list of Booleans from a single column.
 */
fun SQLiteDatabase.getColBools(                                   table: String,
                                                                 column: BoolCol,
                                                                  where: WhereBuilder.()->Unit = {},
): List<Boolean> = getColVals(table, column.name, where) { it.getBoolean(0) }


/**
 * Retrieves a list of Booleans from a single column.
 */
fun SQLiteDatabase.getColBoolsPro(                                   table: String,
                                                                    column: BoolCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Boolean> = getColValsPro(table, column.name, queryOpts) { it.getBoolean(0) }





//  ---------------------------------------  L O N G  ---------------------------------------  \\

/**
 * Retrieves a list of Longs from a single column.
 */
fun SQLiteDatabase.getColLongs(                                   table: String,
                                                                 column: LongCol,
                                                                  where: WhereBuilder.()->Unit = {},
): List<Long> = getColVals(table, column.name, where) { it.getLong(0) }


/**
 * Retrieves a list of Longs from a single column.
 */
fun SQLiteDatabase.getColLongsPro(                                   table: String,
                                                                    column: LongCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Long> = getColValsPro(table, column.name, queryOpts) { it.getLong(0) }




//  --------------------------------------  F L O A T  --------------------------------------  \\

/**
 * Retrieves a list of Floats from a single column.
 */
fun SQLiteDatabase.getColFloats(                                  table: String,
                                                                 column: FloatCol,
                                                                  where: WhereBuilder.()->Unit = {},
): List<Float> = getColVals(table, column.name, where) { it.getFloat(0) }


/**
 * Retrieves a list of Floats from a single column.
 */
fun SQLiteDatabase.getColFloatsPro(                                  table: String,
                                                                    column: FloatCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Float> = getColValsPro(table, column.name, queryOpts) { it.getFloat(0) }





//  ---------------------------------------  B L O B  ---------------------------------------  \\

/**
 * Retrieves a list of Blobs from a single column.
 */
fun SQLiteDatabase.getColBlobs(                                   table: String,
                                                                 column: BlobCol,
                                                                  where: WhereBuilder.()->Unit = {},
): List<ByteArray> = getColVals(table, column.name, where) { it.getBlob(0) }


/**
 * Retrieves a list of Blobs from a single column.
 */
fun SQLiteDatabase.getColBlobsPro(                                   table: String,
                                                                    column: BlobCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<ByteArray> = getColValsPro(table, column.name, queryOpts) { it.getBlob(0) }






















