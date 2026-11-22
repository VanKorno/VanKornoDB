/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.byId

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.FullDslTable
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.set.dsl.SetDsl
import com.vankorno.vankornodb.set.set

fun <W : WhereDsl<W>, S : SetDsl<W>> Dbh.setById(                table: FullDslTable<*, W, *, *, S>,
                                                                    id: Long,
                                                                 async: Boolean = false,
                                                               actions: S.()->Unit,
) = set(table, where = { Id equal id }, async = async, actions = actions)



fun <W : WhereDsl<W>, S : SetDsl<W>> SQLiteDatabase.setById(     table: FullDslTable<*, W, *, *, S>,
                                                                    id: Long,
                                                               actions: S.()->Unit,
) = set(table, where = { Id equal id }, actions = actions)
