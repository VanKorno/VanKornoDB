/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.TableColumns
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
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getInts(
                                                         columns: TableColumns<W, O, X, IntCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<Int>> = read(emptyList(), "getInts") { db ->
    db.getInts(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Ints from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getInts(
                                                                columns: TableColumns<W, O, X, IntCol>,
                                                                orderBy: O.()->Unit = columns.defaultOrder,
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<Int>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getInt(col)
}




/** Retrieves a 2D matrix of Strings from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getStrings(
                                                         columns: TableColumns<W, O, X, StrCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<String>> = read(emptyList(), "getStrings") { db ->
    db.getStrings(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Strings from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getStrings(
                                                         columns: TableColumns<W, O, X, StrCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<String>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getString(col)
}




/** Retrieves a 2D matrix of Booleans from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getBools(
                                                         columns: TableColumns<W, O, X, BoolCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<Boolean>> = read(emptyList(), "getBools") { db ->
    db.getBools(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Booleans from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getBools(
                                                         columns: TableColumns<W, O, X, BoolCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<Boolean>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getBoolean(col)
}




/** Retrieves a 2D matrix of Longs from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLongs(
                                                         columns: TableColumns<W, O, X, LongCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<Long>> = read(emptyList(), "getLongs") { db ->
    db.getLongs(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Longs from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLongs(
                                                         columns: TableColumns<W, O, X, LongCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<Long>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getLong(col)
}




/** Retrieves a 2D matrix of Floats from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getFloats(
                                                         columns: TableColumns<W, O, X, FloatCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<Float>> = read(emptyList(), "getFloats") { db ->
    db.getFloats(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Floats from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getFloats(
                                                         columns: TableColumns<W, O, X, FloatCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<Float>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getFloat(col)
}




/** Retrieves a 2D matrix of Blobs (ByteArray) from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getBlobs(
                                                         columns: TableColumns<W, O, X, BlobCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<ByteArray>> = read(emptyList(), "getBlobs") { db ->
    db.getBlobs(columns, orderBy, extra, where)
}
/** Retrieves a 2D matrix of Blobs (ByteArray) from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getBlobs(
                                                         columns: TableColumns<W, O, X, BlobCol>,
                                                         orderBy: O.()->Unit = columns.defaultOrder,
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<List<ByteArray>> = baseGetVals(
    columns, orderBy, extra, where
) { cursor, col ->
    cursor.getBlob(col)
}
