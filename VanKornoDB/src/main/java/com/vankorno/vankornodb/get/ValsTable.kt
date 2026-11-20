/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DbColumns
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.get.internal.baseGetVals
import com.vankorno.vankornodb.misc.getBoolean

/** Retrieves a 2D matrix of Ints from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableInts(
                                                                columns: DbColumns<W, O, X, IntCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<Int>> = read(emptyList(), "getTableInts") { db ->
    db.getTableInts(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Ints from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableInts(
                                                                columns: DbColumns<W, O, X, IntCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<Int>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getInt(col)
}




/** Retrieves a 2D matrix of Strings from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableStrings(
                                                                columns: DbColumns<W, O, X, StrCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<String>> = read(emptyList(), "getTableStrings") { db ->
    db.getTableStrings(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Strings from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableStrings(
                                                                columns: DbColumns<W, O, X, StrCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<String>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getString(col)
}




/** Retrieves a 2D matrix of Booleans from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableBools(
                                                               columns: DbColumns<W, O, X, BoolCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<List<Boolean>> = read(emptyList(), "getTableBools") { db ->
    db.getTableBools(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Booleans from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableBools(
                                                               columns: DbColumns<W, O, X, BoolCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<List<Boolean>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getBoolean(col)
}




/** Retrieves a 2D matrix of Longs from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableLongs(
                                                               columns: DbColumns<W, O, X, LongCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<List<Long>> = read(emptyList(), "getTableLongs") { db ->
    db.getTableLongs(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Longs from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableLongs(
                                                               columns: DbColumns<W, O, X, LongCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<List<Long>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getLong(col)
}




/** Retrieves a 2D matrix of Floats from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableFloats(
                                                              columns: DbColumns<W, O, X, FloatCol>,
                                                              orderBy: O.()->Unit = {},
                                                                extra: X.()->Unit = {},
                                                                where: W.()->Unit = {},
): List<List<Float>> = read(emptyList(), "getTableFloats") { db ->
    db.getTableFloats(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Floats from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableFloats(
                                                              columns: DbColumns<W, O, X, FloatCol>,
                                                              orderBy: O.()->Unit = {},
                                                                extra: X.()->Unit = {},
                                                                where: W.()->Unit = {},
): List<List<Float>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getFloat(col)
}




/** Retrieves a 2D matrix of Blobs (ByteArray) from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableBlobs(
                                                               columns: DbColumns<W, O, X, BlobCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<List<ByteArray>> = read(emptyList(), "getTableBlobs") { db ->
    db.getTableBlobs(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Blobs (ByteArray) from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableBlobs(
                                                               columns: DbColumns<W, O, X, BlobCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<List<ByteArray>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getBlob(col)
}
