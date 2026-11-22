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
                                                                   id: Long,
                                                                value: Int,
                                                               column: TableColumn<W, O, X, IntCol>,
                                                                async: Boolean = false,
) = setInt(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setIntById(
                                                                   id: Long,
                                                                value: Int,
                                                               column: TableColumn<W, O, X, IntCol>,
) = setInt(value, column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setStrById(
                                                                   id: Long,
                                                                value: String,
                                                               column: TableColumn<W, O, X, StrCol>,
                                                                async: Boolean = false,
) = setStr(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setStrById(
                                                                   id: Long,
                                                                value: String,
                                                               column: TableColumn<W, O, X, StrCol>,
) = setStr(value, column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setBoolById(
                                                                  id: Long,
                                                               value: Boolean,
                                                              column: TableColumn<W, O, X, BoolCol>,
                                                               async: Boolean = false,
) = setBool(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setBoolById(
                                                                  id: Long,
                                                               value: Boolean,
                                                              column: TableColumn<W, O, X, BoolCol>,
) = setBool(value, column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setLongById(
                                                                  id: Long,
                                                               value: Long,
                                                              column: TableColumn<W, O, X, LongCol>,
                                                               async: Boolean = false,
) = setLong(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setLongById(
                                                                  id: Long,
                                                               value: Long,
                                                              column: TableColumn<W, O, X, LongCol>,
) = setLong(value, column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setFloatById(
                                                                 id: Long,
                                                              value: Float,
                                                             column: TableColumn<W, O, X, FloatCol>,
                                                              async: Boolean = false,
) = setFloat(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setFloatById(
                                                                 id: Long,
                                                              value: Float,
                                                             column: TableColumn<W, O, X, FloatCol>,
) = setFloat(value, column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.setBlobById(
                                                                  id: Long,
                                                               value: ByteArray,
                                                              column: TableColumn<W, O, X, BlobCol>,
                                                               async: Boolean = false,
) = setBlob(value, column, async) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.setBlobById(
                                                                  id: Long,
                                                               value: ByteArray,
                                                              column: TableColumn<W, O, X, BlobCol>,
) = setBlob(value, column) { Id equal id }
