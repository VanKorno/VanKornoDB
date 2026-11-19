/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
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
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColInts(     table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<Int> = read(emptyList(), "getColInts") { db ->
    db.getColInts(table, column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Ints from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColInts(
                                                                   table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<Int> = getColVals(
    table, column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getInt(0)
}




/** Retrieves a list of Strings from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColStrings(
                                                                   table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, StrCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<String> = read(emptyList(), "getColStrings") { db ->
    db.getColStrings(table, column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Strings from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColStrings(
                                                                   table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, StrCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<String> = getColVals(
    table, column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getString(0)
}




/** Retrieves a list of Booleans from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColBools(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Boolean> = read(emptyList(), "getColBools") { db ->
    db.getColBools(table, column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Booleans from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColBools(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Boolean> = getColVals(
    table, column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getBoolean(0)
}




/** Retrieves a list of Longs from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColLongs(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Long> = read(emptyList(), "getColLongs") { db ->
    db.getColLongs(table, column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Longs from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColLongs(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Long> = getColVals(
    table, column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getLong(0)
}




/** Retrieves a list of Floats from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColFloats(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<Float> = read(emptyList(), "getColFloats") { db ->
    db.getColFloats(table, column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Floats from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColFloats(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<Float> = getColVals(
    table, column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getFloat(0)
}




/** Retrieves a list of Blobs (ByteArray) from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getColBlobs(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, BlobCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<ByteArray> = read(emptyList(), "getColBlobs") { db ->
    db.getColBlobs(table, column, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Blobs (ByteArray) from one column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getColBlobs(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, BlobCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<ByteArray> = getColVals(
    table, column, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    cursor.getBlob(0)
}
