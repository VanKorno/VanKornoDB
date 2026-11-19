/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.EntityTable
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol

/** Sets [column] to [value] in matching rows.
 */
fun <W> Dbh.setInt(                                                value: Int,
                                                                   table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   async: Boolean = false,
                                                                   where: W.()->Unit,
) = write("setInt", async) { db ->
    db.setInt(value, table, column, where)
}
fun <W> SQLiteDatabase.setInt(                                     value: Int,
                                                                   table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   where: W.()->Unit = {},
) {
    val values = ContentValues(1).apply { put(table.select(column).name, value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}




/** Sets [column] to [value] in matching rows.
 */
fun <W> Dbh.setStr(                                                value: String,
                                                                   table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, StrCol>,
                                                                   async: Boolean = false,
                                                                   where: W.()->Unit,
) = write("setStr", async) { db ->
    db.setStr(value, table, column, where)
}
fun <W> SQLiteDatabase.setStr(                                     value: String,
                                                                   table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, StrCol>,
                                                                   where: W.()->Unit = {},
) {
    val values = ContentValues(1).apply { put(table.select(column).name, value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}




/** Sets [column] to [value] in matching rows.
 */
fun <W> Dbh.setBool(                                              value: Boolean,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit,
) = write("setBool", async) { db ->
    db.setBool(value, table, column, where)
}
fun <W> SQLiteDatabase.setBool(                                   value: Boolean,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                  where: W.()->Unit = {},
) {
    val values = ContentValues(1).apply { put(table.select(column).name, value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}




/** Sets [column] to [value] in matching rows.
 */
fun <W> Dbh.setLong(                                              value: Long,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit,
) = write("setLong", async) { db ->
    db.setLong(value, table, column, where)
}
fun <W> SQLiteDatabase.setLong(                                   value: Long,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                  where: W.()->Unit = {},
) {
    val values = ContentValues(1).apply { put(table.select(column).name, value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}




/** Sets [column] to [value] in matching rows.
 */
fun <W> Dbh.setFloat(                                            value: Float,
                                                                 table: EntityTable<*, W>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                                 async: Boolean = false,
                                                                 where: W.()->Unit,
) = write("setFloat", async) { db ->
    db.setFloat(value, table, column, where)
}
fun <W> SQLiteDatabase.setFloat(                                 value: Float,
                                                                 table: EntityTable<*, W>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                                 where: W.()->Unit = {},
) {
    val values = ContentValues(1).apply { put(table.select(column).name, value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}




/** Sets [column] to [value] in matching rows.
 */
fun <W> Dbh.setBlob(                                              value: ByteArray,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BlobCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit,
) = write("setBlob", async) { db ->
    db.setBlob(value, table, column, where)
}
fun <W> SQLiteDatabase.setBlob(                                   value: ByteArray,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BlobCol>,
                                                                  where: W.()->Unit = {},
) {
    val values = ContentValues(1).apply { put(table.select(column).name, value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}
