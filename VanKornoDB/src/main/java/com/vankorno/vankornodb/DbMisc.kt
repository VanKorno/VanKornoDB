package com.vankorno.vankornodb
// This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
// If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.*

class DbMisc() {
    
    fun deleteFirstRow(                                                         db: SQLiteDatabase,
                                                                         tableName: String
    ) {
        val whereClause = RowID + " = (" + select + RowID + from + tableName+ limit + "1)"
        db.delete(tableName, whereClause, null)
    }
    
    
    fun buildCreateTableQuery(                                              tableName: String,
                                                                    entity: ArrayList<Array<String>>
    ): String {
        val queryStr = buildString {
            append(dbCreateT)
            append(tableName)
            append(" (")
            entity.forEachIndexed { idx, column ->
                append(column[0] + column[1])
                if (idx < entity.lastIndex)
                    append(comma)
            }
            append(")")
        }
        // region LOG
            Log.d("DbMisc", "createTableString = $queryStr")
        // endregion
        return queryStr
    }
    
}

fun Cursor.getBool(col: Int) = this.getInt(col) == 1