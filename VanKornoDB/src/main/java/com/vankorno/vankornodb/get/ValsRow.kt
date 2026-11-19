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
import com.vankorno.vankornodb.get.internal.getRowVals
import com.vankorno.vankornodb.misc.getBoolean

/** Retrieves a list of Ints from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowInts(     table: QueryTable<*, W, O, X>,
                                                          vararg columns: ColumnSelector<W, IntCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<Int> = read(emptyList(), "getRowInts") { db ->
    db.getRowInts(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Ints from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowInts(
                                                                   table: QueryTable<*, W, O, X>,
                                                          vararg columns: ColumnSelector<W, IntCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<Int> = getRowVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getInt(col)
}




/** Retrieves a list of Strings from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowStrings(
                                                                   table: QueryTable<*, W, O, X>,
                                                          vararg columns: ColumnSelector<W, StrCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<String> = read(emptyList(), "getRowStrings") { db ->
    db.getRowStrings(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Strings from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowStrings(
                                                                   table: QueryTable<*, W, O, X>,
                                                          vararg columns: ColumnSelector<W, StrCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<String> = getRowVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getString(col)
}




/** Retrieves a list of Booleans from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowBools(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, BoolCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Boolean> = read(emptyList(), "getRowBools") { db ->
    db.getRowBools(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Booleans from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowBools(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, BoolCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Boolean> = getRowVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getBoolean(col)
}




/** Retrieves a list of Longs from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowLongs(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, LongCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Long> = read(emptyList(), "getRowLongs") { db ->
    db.getRowLongs(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Longs from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowLongs(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, LongCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<Long> = getRowVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getLong(col)
}




/** Retrieves a list of Floats from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowFloats(
                                                                 table: QueryTable<*, W, O, X>,
                                                        vararg columns: ColumnSelector<W, FloatCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<Float> = read(emptyList(), "getRowFloats") { db ->
    db.getRowFloats(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Floats from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowFloats(
                                                                 table: QueryTable<*, W, O, X>,
                                                        vararg columns: ColumnSelector<W, FloatCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<Float> = getRowVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getFloat(col)
}




/** Retrieves a list of Blobs (ByteArray) from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRowBlobs(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, BlobCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<ByteArray> = read(emptyList(), "getRowBlobs") { db ->
    db.getRowBlobs(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a list of Blobs (ByteArray) from the first matching row.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRowBlobs(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, BlobCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<ByteArray> = getRowVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getBlob(col)
}
