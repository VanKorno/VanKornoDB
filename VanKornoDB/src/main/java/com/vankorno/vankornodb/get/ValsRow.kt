/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.TableColumns
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.get.internal.getRowVals
import com.vankorno.vankornodb.misc.getBoolean

/** Retrieves a list of Ints from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowInts(    columns: TableColumns<W, O, X, IntCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Int> = read(emptyList(), "getRowInts") { db ->
    db.getRowInts(columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Ints from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowInts(
                                                                columns: TableColumns<W, O, X, IntCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Int> = getRowVals(
    columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getInt(col)
}




/** Retrieves a list of Strings from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowStrings(
                                                                columns: TableColumns<W, O, X, StrCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<String> = read(emptyList(), "getRowStrings") { db ->
    db.getRowStrings(columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Strings from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowStrings(
                                                                columns: TableColumns<W, O, X, StrCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<String> = getRowVals(
    columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getString(col)
}




/** Retrieves a list of Booleans from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowBools(
                                                               columns: TableColumns<W, O, X, BoolCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<Boolean> = read(emptyList(), "getRowBools") { db ->
    db.getRowBools(columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Booleans from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowBools(
                                                               columns: TableColumns<W, O, X, BoolCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<Boolean> = getRowVals(
    columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getBoolean(col)
}




/** Retrieves a list of Longs from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowLongs(
                                                               columns: TableColumns<W, O, X, LongCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<Long> = read(emptyList(), "getRowLongs") { db ->
    db.getRowLongs(columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Longs from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowLongs(
                                                               columns: TableColumns<W, O, X, LongCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<Long> = getRowVals(
    columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getLong(col)
}




/** Retrieves a list of Floats from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowFloats(
                                                              columns: TableColumns<W, O, X, FloatCol>,
                                                              orderBy: O.()->Unit = {},
                                                                extra: X.()->Unit = {},
                                                                where: W.()->Unit = {},
): List<Float> = read(emptyList(), "getRowFloats") { db ->
    db.getRowFloats(columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Floats from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowFloats(
                                                              columns: TableColumns<W, O, X, FloatCol>,
                                                              orderBy: O.()->Unit = {},
                                                                extra: X.()->Unit = {},
                                                                where: W.()->Unit = {},
): List<Float> = getRowVals(
    columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getFloat(col)
}




/** Retrieves a list of Blobs (ByteArray) from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowBlobs(
                                                               columns: TableColumns<W, O, X, BlobCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<ByteArray> = read(emptyList(), "getRowBlobs") { db ->
    db.getRowBlobs(columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Blobs (ByteArray) from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowBlobs(
                                                               columns: TableColumns<W, O, X, BlobCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<ByteArray> = getRowVals(
    columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getBlob(col)
}
