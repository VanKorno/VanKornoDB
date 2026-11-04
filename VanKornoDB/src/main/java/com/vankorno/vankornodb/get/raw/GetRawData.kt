/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.raw

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.get.raw.data.RawTableStr
import com.vankorno.vankornodb.get.useCursor


fun SQLiteDatabase.getRawTableStr(                                                 table: String
): RawTableStr = useCursor(table) { cursor ->
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








