/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.noty

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.data.DbConstants.WHERE

fun SQLiteDatabase.setNoty(                                                        value: Any,
                                                                                   table: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: Any,
) {
    val safeValue = getBoolSafeVal(value)
    
    execSQL("UPDATE $table SET $column = ?" + WHERE + where + "=?", arrayOf(safeValue, equals))
}





internal fun getBoolSafeVal(value: Any)  =  if (value is Boolean)
                                                 if (value) 1 else 0
                                             else
                                                 value
