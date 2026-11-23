/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.core.dsl.MiniDslTable
import java.io.File

fun Dbh.tableExists(                                                               table: String
): Boolean = read(false, "tableExists") { db ->
    db.tableExists(table)
}
fun SQLiteDatabase.tableExists(                                                    table: String
): Boolean = rawQuery(
    "SELECT 1 FROM $TABLE_Master WHERE type=? AND name=?",
    
    arrayOf(DbTypeTable, table)
).use { it.moveToFirst() }


fun Dbh.tableExists(                                                      table: MiniDslTable<*, *>
): Boolean = read(false, "tableExists") { db ->
    db.tableExists(table)
}
fun SQLiteDatabase.tableExists(                                           table: MiniDslTable<*, *>
): Boolean = tableExists(table.name)




fun Dbh.isTableEmpty(                                                              table: String
): Boolean = read(true, "isTableEmpty") { db ->
    db.isTableEmpty(table)
}
fun SQLiteDatabase.isTableEmpty(table: String): Boolean = !hasRows(table)


fun Dbh.isTableEmpty(                                                     table: MiniDslTable<*, *>
): Boolean = read(true, "isTableEmpty") { db ->
    db.isTableEmpty(table)
}
fun SQLiteDatabase.isTableEmpty(table: MiniDslTable<*, *>): Boolean = isTableEmpty(table.name)




fun Dbh.getDbFileName(): String = read("", "getDbFileName") { db -> db.getDbFileName() }

fun SQLiteDatabase.getDbFileName(): String = File(path).name




fun Dbh.getAppTableNames(): List<String> = read(emptyList(), "getAppTableNames") { db ->
    db.getAppTableNames()
}
fun SQLiteDatabase.getAppTableNames(): List<String> = tableNames(internalTables = false)


fun Dbh.getInternalTableNames(): List<String> = read(emptyList(), "getInternalTableNames") { db ->
    db.getInternalTableNames()
}
fun SQLiteDatabase.getInternalTableNames(): List<String> = tableNames(internalTables = true)



private fun SQLiteDatabase.tableNames(                                    internalTables: Boolean
): List<String> {
    val filter = "(name LIKE ? OR name IN (?, ?))"
    val predicate = if (internalTables) filter else "NOT $filter"
    val sql = "SELECT name FROM $TABLE_Master WHERE type=? AND $predicate ORDER BY $RowID"
    return rawQuery(sql, arrayOf(DbTypeTable, "sqlite_%", TABLE_AndroidMetadata, TABLE_EntityVersions)).use { cursor ->
        buildList {
            while (cursor.moveToNext())
                add(cursor.getString(0))
        }
    }
}

