/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.noty

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.suppressValGetterErrorLog

// TODO Make sure not used

fun <R> SQLiteDatabase.getValueNoty(                                  table: String,
                                                                     column: String,
                                                                    default: R,
                                                                    funName: String,
                                                                    orderBy: O.()->Unit = {},
                                                                      extra: X.()->Unit = {},
                                                                      where: W.()->Unit = {},
                                                             getCursorValue: (Cursor)->R,
): R {
    return getCursorNoty(table, arrayOf(column), orderBy, where = where,
        extra = {
            applyDsl(extra)
            limit = 1
        }
    ).use { cursor ->
        if (cursor.moveToFirst())
            getCursorValue(cursor)
        else {
            // region LOG
            if (!suppressValGetterErrorLog)
                eLog("$funName() Unable to get value from $table (column: $column). Returning default.")
            // endregion
            default
        }
    }
}




