package com.vankorno.vankornodb
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.*

class DbMisc() {
    
    fun deleteFirstRow(                                                         db: SQLiteDatabase,
                                                                         tableName: String
    ) {
        val whereClause = RowID + " = (" + select + RowID + from + tableName + limit + "1)"
        db.delete(tableName, whereClause, null)
    }
    
    
    
}

fun Cursor.getBool(col: Int) = this.getInt(col) == 1