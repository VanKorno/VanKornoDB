/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.id

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
import com.vankorno.vankornodb.get.getBlob
import com.vankorno.vankornodb.get.getBool
import com.vankorno.vankornodb.get.getFloat
import com.vankorno.vankornodb.get.getInt
import com.vankorno.vankornodb.get.getLong
import com.vankorno.vankornodb.get.getStr

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getIntById(
                                                                   id: Long,
                                                               column: TableColumn<W, O, X, IntCol>,
): Int = getInt(column) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getIntById(
                                                                   id: Long,
                                                               column: TableColumn<W, O, X, IntCol>,
): Int = getInt(column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getStrById(
                                                                   id: Long,
                                                               column: TableColumn<W, O, X, StrCol>,
): String = getStr(column) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getStrById(
                                                                   id: Long,
                                                               column: TableColumn<W, O, X, StrCol>,
): String = getStr(column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getBoolById(
                                                                  id: Long,
                                                              column: TableColumn<W, O, X, BoolCol>,
): Boolean = getBool(column) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getBoolById(
                                                                  id: Long,
                                                              column: TableColumn<W, O, X, BoolCol>,
): Boolean = getBool(column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLongById(
                                                                  id: Long,
                                                              column: TableColumn<W, O, X, LongCol>,
): Long = getLong(column) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLongById(
                                                                  id: Long,
                                                              column: TableColumn<W, O, X, LongCol>,
): Long = getLong(column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getFloatById(
                                                                 id: Long,
                                                             column: TableColumn<W, O, X, FloatCol>,
): Float = getFloat(column) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getFloatById(
                                                                 id: Long,
                                                             column: TableColumn<W, O, X, FloatCol>,
): Float = getFloat(column) { Id equal id }




fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getBlobById(
                                                                  id: Long,
                                                              column: TableColumn<W, O, X, BlobCol>,
): ByteArray = getBlob(column) { Id equal id }

fun <W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getBlobById(
                                                                  id: Long,
                                                              column: TableColumn<W, O, X, BlobCol>,
): ByteArray = getBlob(column) { Id equal id }
