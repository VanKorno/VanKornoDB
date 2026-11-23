/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.id

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.TableColumns
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.get.getRowBlobs
import com.vankorno.vankornodb.get.getRowBools
import com.vankorno.vankornodb.get.getRowFloats
import com.vankorno.vankornodb.get.getRowInts
import com.vankorno.vankornodb.get.getRowLongs
import com.vankorno.vankornodb.get.getRowStrings
import com.vankorno.vankornodb.get.internal.getRowVals
import com.vankorno.vankornodb.misc.getBoolean

/** Retrieves a list of Ints from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.getRowIntsById(                                         id: Long,
                                                             columns: TableColumns<W, O, X, IntCol>,
): List<Int> = read(emptyList(), "getRowInts") { db ->
    db.getRowInts(columns) { Id equal id }
}
/** Retrieves a list of Ints from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.getRowIntsById(                              id: Long,
                                                             columns: TableColumns<W, O, X, IntCol>,
): List<Int> = getRowVals(
    columns, where = { Id equal id }
) { cursor, col ->
    cursor.getInt(col)
}




/** Retrieves a list of Strings from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.getRowStringsById(                                      id: Long,
                                                             columns: TableColumns<W, O, X, StrCol>,
): List<String> = read(emptyList(), "getRowStrings") { db ->
    db.getRowStrings(columns) { Id equal id }
}
/** Retrieves a list of Strings from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.getRowStringsById(                           id: Long,
                                                             columns: TableColumns<W, O, X, StrCol>,
): List<String> = getRowVals(
    columns, where = { Id equal id }
) { cursor, col ->
    cursor.getString(col)
}




/** Retrieves a list of Booleans from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.getRowBoolsById(                                       id: Long,
                                                            columns: TableColumns<W, O, X, BoolCol>,
): List<Boolean> = read(emptyList(), "getRowBools") { db ->
    db.getRowBools(columns) { Id equal id }
}
/** Retrieves a list of Booleans from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.getRowBoolsById(                            id: Long,
                                                            columns: TableColumns<W, O, X, BoolCol>,
): List<Boolean> = getRowVals(
    columns, where = { Id equal id }
) { cursor, col ->
    cursor.getBoolean(col)
}




/** Retrieves a list of Longs from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.getRowLongsById(                                       id: Long,
                                                            columns: TableColumns<W, O, X, LongCol>,
): List<Long> = read(emptyList(), "getRowLongs") { db ->
    db.getRowLongs(columns) { Id equal id }
}
/** Retrieves a list of Longs from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.getRowLongsById(                            id: Long,
                                                            columns: TableColumns<W, O, X, LongCol>,
): List<Long> = getRowVals(
    columns, where = { Id equal id }
) { cursor, col ->
    cursor.getLong(col)
}




/** Retrieves a list of Floats from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.getRowFloatsById(                                     id: Long,
                                                           columns: TableColumns<W, O, X, FloatCol>,
): List<Float> = read(emptyList(), "getRowFloats") { db ->
    db.getRowFloats(columns) { Id equal id }
}
/** Retrieves a list of Floats from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.getRowFloatsById(                          id: Long,
                                                           columns: TableColumns<W, O, X, FloatCol>,
): List<Float> = getRowVals(
    columns, where = { Id equal id }
) { cursor, col ->
    cursor.getFloat(col)
}




/** Retrieves a list of Blobs (ByteArray) from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.getRowBlobsById(                                       id: Long,
                                                            columns: TableColumns<W, O, X, BlobCol>,
): List<ByteArray> = read(emptyList(), "getRowBlobs") { db ->
    db.getRowBlobs(columns) { Id equal id }
}
/** Retrieves a list of Blobs (ByteArray) from the row with passed [id].
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.getRowBlobsById(                            id: Long,
                                                            columns: TableColumns<W, O, X, BlobCol>,
): List<ByteArray> = getRowVals(
    columns, where = { Id equal id }
) { cursor, col ->
    cursor.getBlob(col)
}
