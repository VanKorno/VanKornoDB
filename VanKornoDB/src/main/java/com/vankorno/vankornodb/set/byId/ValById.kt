/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.byId

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.TableColumn
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.set.setBlob
import com.vankorno.vankornodb.set.setBool
import com.vankorno.vankornodb.set.setFloat
import com.vankorno.vankornodb.set.setInt
import com.vankorno.vankornodb.set.setLong
import com.vankorno.vankornodb.set.setStr

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setIntById(
                                                                value: Int,
                                                               column: TableColumn<W, O, X, IntCol>,
                                                                   id: Long,
                                                                async: Boolean = false,
) = setInt(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setIntById(
                                                                value: Int,
                                                               column: TableColumn<W, O, X, IntCol>,
                                                                   id: Long,
) = setInt(value, column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setStrById(
                                                                value: String,
                                                               column: TableColumn<W, O, X, StrCol>,
                                                                   id: Long,
                                                                async: Boolean = false,
) = setStr(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setStrById(
                                                                value: String,
                                                               column: TableColumn<W, O, X, StrCol>,
                                                                   id: Long,
) = setStr(value, column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setBoolById(
                                                                value: Boolean,
                                                               column: TableColumn<W, O, X, BoolCol>,
                                                                   id: Long,
                                                                async: Boolean = false,
) = setBool(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setBoolById(
                                                                value: Boolean,
                                                               column: TableColumn<W, O, X, BoolCol>,
                                                                   id: Long,
) = setBool(value, column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setLongById(
                                                                value: Long,
                                                               column: TableColumn<W, O, X, LongCol>,
                                                                   id: Long,
                                                                async: Boolean = false,
) = setLong(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setLongById(
                                                                value: Long,
                                                               column: TableColumn<W, O, X, LongCol>,
                                                                   id: Long,
) = setLong(value, column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setFloatById(
                                                                value: Float,
                                                               column: TableColumn<W, O, X, FloatCol>,
                                                                   id: Long,
                                                                async: Boolean = false,
) = setFloat(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setFloatById(
                                                                value: Float,
                                                               column: TableColumn<W, O, X, FloatCol>,
                                                                   id: Long,
) = setFloat(value, column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setBlobById(
                                                                value: ByteArray,
                                                               column: TableColumn<W, O, X, BlobCol>,
                                                                   id: Long,
                                                                async: Boolean = false,
) = setBlob(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setBlobById(
                                                                value: ByteArray,
                                                               column: TableColumn<W, O, X, BlobCol>,
                                                                   id: Long,
) = setBlob(value, column) { Id equal id }
