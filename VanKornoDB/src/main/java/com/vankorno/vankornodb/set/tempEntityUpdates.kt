/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.core.dsl.EntityTable
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal


fun <T : CurrEntity, W> SQLiteDatabase.setEntity(     tableInfo: EntityTable<TableInfoNormal<T>, W>,
                                                         entity: T,
                                                          where: W.()->Unit,
): Int = setEntity(tableInfo.info, entity, tableInfo.where(where))
