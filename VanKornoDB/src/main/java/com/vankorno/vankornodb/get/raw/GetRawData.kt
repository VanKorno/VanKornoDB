// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get.raw

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.get.getCursor
import com.vankorno.vankornodb.get.raw.data.RawTableStr


fun SQLiteDatabase.getRawTableStr(                                                 table: String
): RawTableStr = getCursor(table).use { cursor ->
    val cols = cursor.columnNames.toList()
    
    val types = getTableTypesFromInitQuery(table)
    val rows = mutableListOf<List<String>>()
    
    while (cursor.moveToNext()) {
        rows += List(cols.size) { idx ->
            val type = types.getOrNull(idx)?.uppercase() ?: "TEXT"
            if (type.contains("BLOB")) {
                val blob = cursor.getBlob(idx)
                if (blob != null) "🌇" else "NULL"
            } else {
                cursor.getString(idx) ?: "NULL"
            }
        }
    }
    RawTableStr(cols, types, rows)
}








