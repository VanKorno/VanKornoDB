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
import com.vankorno.vankornodb.get.internal.baseGetVals
import com.vankorno.vankornodb.misc.getBoolean

/** Retrieves a 2D matrix of Ints from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableInts(     table: QueryTable<*, W, O, X>,
                                                          vararg columns: ColumnSelector<W, IntCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<List<Int>> = read(emptyList(), "getTableInts") { db ->
    db.getTableInts(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a 2D matrix of Ints from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableInts(
                                                                   table: QueryTable<*, W, O, X>,
                                                          vararg columns: ColumnSelector<W, IntCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<List<Int>> = baseGetVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getInt(col)
}




/** Retrieves a 2D matrix of Strings from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableStrings(
                                                                   table: QueryTable<*, W, O, X>,
                                                          vararg columns: ColumnSelector<W, StrCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<List<String>> = read(emptyList(), "getTableStrings") { db ->
    db.getTableStrings(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a 2D matrix of Strings from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableStrings(
                                                                   table: QueryTable<*, W, O, X>,
                                                          vararg columns: ColumnSelector<W, StrCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): List<List<String>> = baseGetVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getString(col)
}




/** Retrieves a 2D matrix of Booleans from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableBools(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, BoolCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<Boolean>> = read(emptyList(), "getTableBools") { db ->
    db.getTableBools(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a 2D matrix of Booleans from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableBools(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, BoolCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<Boolean>> = baseGetVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getBoolean(col)
}




/** Retrieves a 2D matrix of Longs from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableLongs(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, LongCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<Long>> = read(emptyList(), "getTableLongs") { db ->
    db.getTableLongs(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a 2D matrix of Longs from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableLongs(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, LongCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<Long>> = baseGetVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getLong(col)
}




/** Retrieves a 2D matrix of Floats from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableFloats(
                                                                 table: QueryTable<*, W, O, X>,
                                                        vararg columns: ColumnSelector<W, FloatCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<List<Float>> = read(emptyList(), "getTableFloats") { db ->
    db.getTableFloats(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a 2D matrix of Floats from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableFloats(
                                                                 table: QueryTable<*, W, O, X>,
                                                        vararg columns: ColumnSelector<W, FloatCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): List<List<Float>> = baseGetVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getFloat(col)
}




/** Retrieves a 2D matrix of Blobs (ByteArray) from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getTableBlobs(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, BlobCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<ByteArray>> = read(emptyList(), "getTableBlobs") { db ->
    db.getTableBlobs(table, *columns, orderBy = orderBy, extra = extra, where = where)
}
/** Retrieves a 2D matrix of Blobs (ByteArray) from specified columns and rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getTableBlobs(
                                                                  table: QueryTable<*, W, O, X>,
                                                         vararg columns: ColumnSelector<W, BlobCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): List<List<ByteArray>> = baseGetVals(
    table, *columns, orderBy = orderBy, extra = extra, where = where
) { cursor, col ->
    cursor.getBlob(col)
}
