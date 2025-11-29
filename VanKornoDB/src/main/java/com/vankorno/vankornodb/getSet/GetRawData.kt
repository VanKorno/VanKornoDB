package com.vankorno.vankornodb.getSet

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


fun SQLiteDatabase.getRawTableStr(                                                 table: String
): RawTableStr = getCursor(table).use { cursor ->
    val cols = cursor.columnNames.toList()
    val types = List(cursor.columnCount) { idx -> cursor.getType(idx) }
    val rows = mutableListOf<List<String>>()
    
    while (cursor.moveToNext()) {
        rows += List(cols.size) { idx ->
            when (types[idx]) {
                Cursor.FIELD_TYPE_BLOB -> {
                    val blob = cursor.getBlob(idx)
                    if (blob != null) "🌇" else "NULL"
                }
                else -> cursor.getString(idx) ?: "NULL"
            }
        }
    }
    
    RawTableStr(cols, types, rows)
}


data class RawTableStr(
                               val columns: List<String>,
                                 val types: List<Int>,      // Cursor.FIELD_TYPE_XXX
                                  val rows: List<List<String>>,
)
