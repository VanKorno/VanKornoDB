/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.byId

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.MiniDslTable
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.set.setEntity

/** Writes [entity] to rows matching [id] and returns the number updated.
 */
fun <T : DbEntity, W : WhereDsl<W>> Dbh.setEntityById(            id: Long = entity.id,
                                                               table: MiniDslTable<TableInfo<T>, W>,
                                                              entity: T,
): Int = setEntity(table, entity) { Id equal id }

fun <T : DbEntity, W : WhereDsl<W>> SQLiteDatabase.setEntityById(
                                                                  id: Long = entity.id,
                                                               table: MiniDslTable<TableInfo<T>, W>,
                                                              entity: T,
): Int = setEntity(table, entity) { Id equal id }
