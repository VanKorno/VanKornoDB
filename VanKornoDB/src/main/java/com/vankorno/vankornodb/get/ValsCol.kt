/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.TableColumn
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.get.internal.getColVals
import com.vankorno.vankornodb.misc.getBoolean

/** Retrieves a list of Ints from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColInts(      column: TableColumn<W, O, X, IntCol>,
                                                                 orderBy: O.()->Unit = column.defaultOrder,
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<Int> = read(emptyList(), "getColInts") { db ->
    db.getColInts(column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Ints from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColInts(
                                                                  column: TableColumn<W, O, X, IntCol>,
                                                                 orderBy: O.()->Unit = column.defaultOrder,
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<Int> = getColVals(
    column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getInt(0)
}




/** Retrieves a list of Strings from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColStrings(
                                                                  column: TableColumn<W, O, X, StrCol>,
                                                                 orderBy: O.()->Unit = column.defaultOrder,
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<String> = read(emptyList(), "getColStrings") { db ->
    db.getColStrings(column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Strings from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColStrings(
                                                                  column: TableColumn<W, O, X, StrCol>,
                                                                 orderBy: O.()->Unit = column.defaultOrder,
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<String> = getColVals(
    column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getString(0)
}




/** Retrieves a list of Booleans from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColBools(    column: TableColumn<W, O, X, BoolCol>,
                                                                orderBy: O.()->Unit = column.defaultOrder,
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Boolean> = read(emptyList(), "getColBools") { db ->
    db.getColBools(column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Booleans from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColBools(
                                                                 column: TableColumn<W, O, X, BoolCol>,
                                                                orderBy: O.()->Unit = column.defaultOrder,
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Boolean> = getColVals(
    column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getBoolean(0)
}




/** Retrieves a list of Longs from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColLongs(    column: TableColumn<W, O, X, LongCol>,
                                                                orderBy: O.()->Unit = column.defaultOrder,
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Long> = read(emptyList(), "getColLongs") { db ->
    db.getColLongs(column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Longs from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColLongs(
                                                                 column: TableColumn<W, O, X, LongCol>,
                                                                orderBy: O.()->Unit = column.defaultOrder,
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Long> = getColVals(
    column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getLong(0)
}




/** Retrieves a list of Floats from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColFloats(
                                                                column: TableColumn<W, O, X, FloatCol>,
                                                               orderBy: O.()->Unit = column.defaultOrder,
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<Float> = read(emptyList(), "getColFloats") { db ->
    db.getColFloats(column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Floats from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColFloats(
                                                                column: TableColumn<W, O, X, FloatCol>,
                                                               orderBy: O.()->Unit = column.defaultOrder,
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<Float> = getColVals(
    column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getFloat(0)
}




/** Retrieves a list of Blobs (ByteArray) from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColBlobs(    column: TableColumn<W, O, X, BlobCol>,
                                                                orderBy: O.()->Unit = column.defaultOrder,
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<ByteArray> = read(emptyList(), "getColBlobs") { db ->
    db.getColBlobs(column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Blobs (ByteArray) from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColBlobs(
                                                                 column: TableColumn<W, O, X, BlobCol>,
                                                                orderBy: O.()->Unit = column.defaultOrder,
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<ByteArray> = getColVals(
    column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getBlob(0)
}
