package com.vankorno.vankornodb

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.*

class DbMisc() {
    
    fun deleteFirstRow(                                                         db: SQLiteDatabase,
                                                                         tableName: String
    ) {
        val whereClause = RowID + " = (" + select + RowID + from + tableName+ limit + "1)"
        db.delete(tableName, whereClause, null)
    }
    
    
    fun buildQuery(                                              tableName: String,
                                                                    entity: ArrayList<Array<String>>
    ) = buildString {
        append(dbCreateT)
        append(tableName)
        append(" (")
        append(dbAutoID)
        append(comma)
        entity.forEachIndexed { idx, column ->
            append(column[0] + column[1])
            if (idx < entity.lastIndex)
                append(comma)
        }
        append(")")
    }
    
}

fun Cursor.getBool(col: Int) = this.getInt(col) == 1