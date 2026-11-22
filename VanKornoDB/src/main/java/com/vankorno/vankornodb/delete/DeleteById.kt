/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.delete

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.MiniDslTable
import com.vankorno.vankornodb.core.dsl.WhereDsl

fun <W : WhereDsl<W>> Dbh.deleteRowById(                                     id: Long,
                                                                          table: MiniDslTable<*, W>,
                                                                          async: Boolean = false,
) = deleteRows(table, async) { Id equal id }


fun <W : WhereDsl<W>> SQLiteDatabase.deleteRowById(                          id: Long,
                                                                          table: MiniDslTable<*, W>,
): Int = deleteRows(table) { Id equal id }
