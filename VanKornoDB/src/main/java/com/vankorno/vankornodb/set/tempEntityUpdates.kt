/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ColumnSelector
import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.core.dsl.EntityTable
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.set.dsl.SetDsl


fun <W> SQLiteDatabase.setInt(                                     value: Int,
                                                                   table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   where: W.()->Unit = {},
) = setInt(value, table.name, table.select(column), table.where(where))

fun <W> SQLiteDatabase.setStr(                                     value: String,
                                                                   table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, StrCol>,
                                                                   where: W.()->Unit = {},
) = setStr(value, table.name, table.select(column), table.where(where))

fun <W> SQLiteDatabase.setBool(                                   value: Boolean,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                  where: W.()->Unit = {},
) = setBool(value, table.name, table.select(column), table.where(where))

fun <W> SQLiteDatabase.setLong(                                   value: Long,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                  where: W.()->Unit = {},
) = setLong(value, table.name, table.select(column), table.where(where))

fun <W> SQLiteDatabase.setFloat(                                 value: Float,
                                                                 table: EntityTable<*, W>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                                 where: W.()->Unit = {},
) = setFloat(value, table.name, table.select(column), table.where(where))

fun <W> SQLiteDatabase.setBlob(                                   value: ByteArray,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BlobCol>,
                                                                  where: W.()->Unit = {},
) = setBlob(value, table.name, table.select(column), table.where(where))

fun <W> SQLiteDatabase.set(                                                table: EntityTable<*, W>,
                                                                           where: W.()->Unit = {},
                                                                         actions: SetDsl.()->Unit,
) = set(table.name, table.where(where), actions)

fun <T : CurrEntity, W> SQLiteDatabase.setEntity(     tableInfo: EntityTable<TableInfoNormal<T>, W>,
                                                         entity: T,
                                                          where: W.()->Unit,
): Int = setEntity(tableInfo.info, entity, tableInfo.where(where))
