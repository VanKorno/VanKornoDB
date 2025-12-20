// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.get.internal.baseGetVals
import com.vankorno.vankornodb.get.internal.baseGetValsPro
import com.vankorno.vankornodb.misc.getBoolean

//  ----------------------------------------  I N T  ----------------------------------------  \\

fun SQLiteDatabase.getTableInts(                                  table: String,
                                                                columns: Array<IntCol>,
                                                                  where: WhereDsl.()->Unit = {},
): List<List<Int>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where
) { cursor, col ->
    cursor.getInt(col)
}


fun SQLiteDatabase.getTableIntsPro(                                  table: String,
                                                                   columns: Array<IntCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
): List<List<Int>> = baseGetValsPro(
    table, columns.map{ it.name }.toTypedArray(), fullDsl
) { cursor, col ->
    cursor.getInt(col)
}






//  -------------------------------------  S T R I N G  -------------------------------------  \\

fun SQLiteDatabase.getTableStrings(                               table: String,
                                                                columns: Array<StrCol>,
                                                                  where: WhereDsl.()->Unit = {},
): List<List<String>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where
) { cursor, col ->
    cursor.getString(col)
}


fun SQLiteDatabase.getTableStringsPro(                               table: String,
                                                                   columns: Array<StrCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
): List<List<String>> = baseGetValsPro(
    table, columns.map{ it.name }.toTypedArray(), fullDsl
) { cursor, col ->
    cursor.getString(col)
}




//  ------------------------------------  B O O L E A N  ------------------------------------  \\

fun SQLiteDatabase.getTableBools(                                 table: String,
                                                                columns: Array<BoolCol>,
                                                                  where: WhereDsl.()->Unit = {},
): List<List<Boolean>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where
) { cursor, col ->
    cursor.getBoolean(col)
}


fun SQLiteDatabase.getTableBoolsPro(                                 table: String,
                                                                   columns: Array<BoolCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
): List<List<Boolean>> = baseGetValsPro(
    table, columns.map{ it.name }.toTypedArray(), fullDsl
) { cursor, col ->
    cursor.getBoolean(col)
}





//  ---------------------------------------  L O N G  ---------------------------------------  \\

fun SQLiteDatabase.getTableLongs(                                 table: String,
                                                                columns: Array<LongCol>,
                                                                  where: WhereDsl.()->Unit = {},
): List<List<Long>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where
) { cursor, col ->
    cursor.getLong(col)
}


fun SQLiteDatabase.getTableLongsPro(                                 table: String,
                                                                   columns: Array<LongCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
): List<List<Long>> = baseGetValsPro(
    table, columns.map{ it.name }.toTypedArray(), fullDsl
) { cursor, col ->
    cursor.getLong(col)
}




//  --------------------------------------  F L O A T  --------------------------------------  \\

fun SQLiteDatabase.getTableFloats(                                table: String,
                                                                columns: Array<FloatCol>,
                                                                  where: WhereDsl.()->Unit = {},
): List<List<Float>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where
) { cursor, col ->
    cursor.getFloat(col)
}


fun SQLiteDatabase.getTableFloatsPro(                                table: String,
                                                                   columns: Array<FloatCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
): List<List<Float>> = baseGetValsPro(
    table, columns.map{ it.name }.toTypedArray(), fullDsl
) { cursor, col ->
    cursor.getFloat(col)
}





//  ---------------------------------------  B L O B  ---------------------------------------  \\

fun SQLiteDatabase.getTableBlobs(                                 table: String,
                                                                columns: Array<BlobCol>,
                                                                  where: WhereDsl.()->Unit = {},
): List<List<ByteArray>> = baseGetVals(
    table, columns.map{ it.name }.toTypedArray(), where
) { cursor, col ->
    cursor.getBlob(col)
}


fun SQLiteDatabase.getTableBlobsPro(                                 table: String,
                                                                   columns: Array<BlobCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
): List<List<ByteArray>> = baseGetValsPro(
    table, columns.map{ it.name }.toTypedArray(), fullDsl
) { cursor, col ->
    cursor.getBlob(col)
}











