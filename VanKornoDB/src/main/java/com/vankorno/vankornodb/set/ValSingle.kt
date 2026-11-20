/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DbColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol

/** Sets [column] to [value] in matching rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setInt(           value: Int,
                                                                  column: DbColumn<W, O, X, IntCol>,
                                                                   async: Boolean = false,
                                                                   where: W.()->Unit,
) = write("setInt", async) { db ->
    db.setInt(value, column, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setInt(
                                                                   value: Int,
                                                                  column: DbColumn<W, O, X, IntCol>,
                                                                   where: W.()->Unit = {},
) {
    val table = column.table
    val values = ContentValues(1).apply { put(table.columnSql(column.selector), value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}




/** Sets [column] to [value] in matching rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setStr(           value: String,
                                                                  column: DbColumn<W, O, X, StrCol>,
                                                                   async: Boolean = false,
                                                                   where: W.()->Unit,
) = write("setStr", async) { db ->
    db.setStr(value, column, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setStr(
                                                                   value: String,
                                                                  column: DbColumn<W, O, X, StrCol>,
                                                                   where: W.()->Unit = {},
) {
    val table = column.table
    val values = ContentValues(1).apply { put(table.columnSql(column.selector), value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}




/** Sets [column] to [value] in matching rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setBool(         value: Boolean,
                                                                 column: DbColumn<W, O, X, BoolCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit,
) = write("setBool", async) { db ->
    db.setBool(value, column, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setBool(
                                                                  value: Boolean,
                                                                 column: DbColumn<W, O, X, BoolCol>,
                                                                  where: W.()->Unit = {},
) {
    val table = column.table
    val values = ContentValues(1).apply { put(table.columnSql(column.selector), value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}




/** Sets [column] to [value] in matching rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setLong(         value: Long,
                                                                 column: DbColumn<W, O, X, LongCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit,
) = write("setLong", async) { db ->
    db.setLong(value, column, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setLong(
                                                                  value: Long,
                                                                 column: DbColumn<W, O, X, LongCol>,
                                                                  where: W.()->Unit = {},
) {
    val table = column.table
    val values = ContentValues(1).apply { put(table.columnSql(column.selector), value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}




/** Sets [column] to [value] in matching rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setFloat(       value: Float,
                                                                column: DbColumn<W, O, X, FloatCol>,
                                                                 async: Boolean = false,
                                                                 where: W.()->Unit,
) = write("setFloat", async) { db ->
    db.setFloat(value, column, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setFloat(
                                                                 value: Float,
                                                                column: DbColumn<W, O, X, FloatCol>,
                                                                 where: W.()->Unit = {},
) {
    val table = column.table
    val values = ContentValues(1).apply { put(table.columnSql(column.selector), value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}




/** Sets [column] to [value] in matching rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setBlob(         value: ByteArray,
                                                                 column: DbColumn<W, O, X, BlobCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit,
) = write("setBlob", async) { db ->
    db.setBlob(value, column, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setBlob(
                                                                  value: ByteArray,
                                                                 column: DbColumn<W, O, X, BlobCol>,
                                                                  where: W.()->Unit = {},
) {
    val table = column.table
    val values = ContentValues(1).apply { put(table.columnSql(column.selector), value) }
    val predicate = table.where(where)
    update(table.name, values, predicate.query, predicate.args)
}
