package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.get.internal.getRowVals
import com.vankorno.vankornodb.get.internal.getRowValsPro
import com.vankorno.vankornodb.misc.getBoolean


//  ----------------------------------------  I N T  ----------------------------------------  \\

fun SQLiteDatabase.getRowInts(                                    table: String,
                                                                columns: Array<IntCol>,
                                                                  where: WhereBuilder.()->Unit = {},
): List<Int> = getRowVals(table, columns.map{ it.name }.toTypedArray(), where) { cursor, col ->
    cursor.getInt(col)
}


fun SQLiteDatabase.getRowIntsPro(                                    table: String,
                                                                   columns: Array<IntCol>,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Int> = getRowValsPro(table, columns.map{ it.name }.toTypedArray(), queryOpts) { cursor, col ->
    cursor.getInt(col)
}






//  -------------------------------------  S T R I N G  -------------------------------------  \\

fun SQLiteDatabase.getRowStrings(                                 table: String,
                                                                columns: Array<StrCol>,
                                                                  where: WhereBuilder.()->Unit = {},
): List<String> = getRowVals(table, columns.map{ it.name }.toTypedArray(), where) { cursor, col ->
    cursor.getString(col)
}


fun SQLiteDatabase.getRowStringsPro(                                 table: String,
                                                                   columns: Array<StrCol>,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<String> = getRowValsPro(table, columns.map{ it.name }.toTypedArray(), queryOpts) { cursor, col ->
    cursor.getString(col)
}




//  ------------------------------------  B O O L E A N  ------------------------------------  \\

fun SQLiteDatabase.getRowBools(                                   table: String,
                                                                columns: Array<BoolCol>,
                                                                  where: WhereBuilder.()->Unit = {},
): List<Boolean> = getRowVals(table, columns.map{ it.name }.toTypedArray(), where) { cursor, col ->
    cursor.getBoolean(col)
}


fun SQLiteDatabase.getRowBoolsPro(                                   table: String,
                                                                   columns: Array<BoolCol>,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Boolean> = getRowValsPro(table, columns.map{ it.name }.toTypedArray(), queryOpts) { cursor, col ->
    cursor.getBoolean(col)
}





//  ---------------------------------------  L O N G  ---------------------------------------  \\

fun SQLiteDatabase.getRowLongs(                                   table: String,
                                                                columns: Array<LongCol>,
                                                                  where: WhereBuilder.()->Unit = {},
): List<Long> = getRowVals(table, columns.map{ it.name }.toTypedArray(), where) { cursor, col ->
    cursor.getLong(col)
}


fun SQLiteDatabase.getRowLongsPro(                                   table: String,
                                                                   columns: Array<LongCol>,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Long> = getRowValsPro(table, columns.map{ it.name }.toTypedArray(), queryOpts) { cursor, col ->
    cursor.getLong(col)
}




//  --------------------------------------  F L O A T  --------------------------------------  \\

fun SQLiteDatabase.getRowFloats(                                  table: String,
                                                                columns: Array<FloatCol>,
                                                                  where: WhereBuilder.()->Unit = {},
): List<Float> = getRowVals(table, columns.map{ it.name }.toTypedArray(), where) { cursor, col ->
    cursor.getFloat(col)
}


fun SQLiteDatabase.getRowFloatsPro(                                  table: String,
                                                                   columns: Array<FloatCol>,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<Float> = getRowValsPro(table, columns.map{ it.name }.toTypedArray(), queryOpts) { cursor, col ->
    cursor.getFloat(col)
}





//  ---------------------------------------  B L O B  ---------------------------------------  \\

fun SQLiteDatabase.getRowBlobs(                                   table: String,
                                                                columns: Array<BlobCol>,
                                                                  where: WhereBuilder.()->Unit = {},
): List<ByteArray> = getRowVals(table, columns.map{ it.name }.toTypedArray(), where) { cursor, col ->
    cursor.getBlob(col)
}


fun SQLiteDatabase.getRowBlobsPro(                                   table: String,
                                                                   columns: Array<BlobCol>,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<ByteArray> = getRowValsPro(table, columns.map{ it.name }.toTypedArray(), queryOpts) { cursor, col ->
    cursor.getBlob(col)
}






















