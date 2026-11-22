/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.byId

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.get.getEntity

fun <T : DbEntity, W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getEntityById(
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                                id: Long,
): T? = getEntity(table) { Id equal id }

fun <T : DbEntity, W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getEntityById(
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                                id: Long,
): T? = getEntity(table) { Id equal id }


