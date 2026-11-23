/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.id

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.get.getEntity

/** Gets a db table row with provided [id] as an object of an entity class.
 *  Returns null if no result found.
 */
fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.getEntityById(                                        id: Long,
                                                             table: DslTable<TableInfo<T>, W, O, X>,
): T? = getEntity(table) { Id equal id }

fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.getEntityById(                             id: Long,
                                                             table: DslTable<TableInfo<T>, W, O, X>,
): T? = getEntity(table) { Id equal id }




/** Gets a db table row with provided [id] as an object of an entity class.
 *  Returns [default] if no result found.
 */
fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.getEntityById(                                        id: Long,
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                           default: T,
): T = getEntity(table, default) { Id equal id }

fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.getEntityById(                             id: Long,
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                           default: T,
): T = getEntity(table, default) { Id equal id }

