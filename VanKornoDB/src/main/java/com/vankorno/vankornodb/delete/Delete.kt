/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.delete

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.MiniDslTable


/** Deletes rows matching [where]. An empty predicate deletes every row.
 */
fun <W> Dbh.deleteRows(                                                   table: MiniDslTable<*, W>,
                                                                          async: Boolean = false,
                                                                          where: W.()->Unit,
) = write("deleteRows", async) { db ->
    db.deleteRows(table, where)
}

fun <W> SQLiteDatabase.deleteRows(                                        table: MiniDslTable<*, W>,
                                                                          where: W.()->Unit,
): Int {
    val predicate = table.where(where)
    return delete(table.name, predicate.query, predicate.args)
}



/** Applies [where] to each table sharing the same entity DSL.
 */
fun <W> Dbh.deleteRowsFromTables(                                  tables: List<MiniDslTable<*, W>>,
                                                                    async: Boolean = false,
                                                                    where: W.()->Unit,
) = write("deleteRowsFromTables", async) { db ->
    db.deleteRowsFromTables(tables, where)
}

fun <W> SQLiteDatabase.deleteRowsFromTables(                       tables: List<MiniDslTable<*, W>>,
                                                                    where: W.()->Unit,
) {
    tables.forEach { deleteRows(it, where) }
}



/** Applies [where] to each table sharing the same entity DSL.
 */
fun <W> Dbh.deleteRowsFromTables(                                 vararg tables: MiniDslTable<*, W>,
                                                                          where: W.()->Unit,
) = write("deleteRowsFromTables") { db ->
    db.deleteRowsFromTables(*tables, where = where)
}

fun <W> SQLiteDatabase.deleteRowsFromTables(                      vararg tables: MiniDslTable<*, W>,
                                                                          where: W.()->Unit,
) = deleteRowsFromTables(tables.asList(), where)



/** Deletes the row with the lowest ROWID.
 */
fun Dbh.deleteFirstRow(                                                   table: MiniDslTable<*, *>,
                                                                          async: Boolean = false,
) = write("deleteFirstRow", async) { db ->
    db.deleteFirstRow(table)
}

fun SQLiteDatabase.deleteFirstRow(                                         table: MiniDslTable<*, *>
) {
    delete(table.name, "ROWID = (SELECT ROWID FROM ${table.name} ORDER BY ROWID ASC LIMIT 1)", null)
}



/** Deletes the row with the highest ROWID.
 */
fun Dbh.deleteLastRow(                                                    table: MiniDslTable<*, *>,
                                                                          async: Boolean = false,
) = write("deleteLastRow", async) { db ->
    db.deleteLastRow(table)
}

fun SQLiteDatabase.deleteLastRow(                                          table: MiniDslTable<*, *>
) {
    delete(table.name, "ROWID = (SELECT ROWID FROM ${table.name} ORDER BY ROWID DESC LIMIT 1)", null)
}



/** Deletes all rows and keeps the table schema.
 */
fun Dbh.clearTable(                                                       table: MiniDslTable<*, *>,
                                                                          async: Boolean = false,
) = write("clearTable", async) { db ->
    db.clearTable(table)
}

fun SQLiteDatabase.clearTable(                                             table: MiniDslTable<*, *>
) { delete(table.name, null, null) }



/** Deletes all rows and keeps each table schema.
 */
fun Dbh.clearTables(                                               tables: List<MiniDslTable<*, *>>,
                                                                    async: Boolean = false,
) = write("clearTables", async) { db ->
    db.clearTables(tables)
}

fun SQLiteDatabase.clearTables(                                     tables: List<MiniDslTable<*, *>>
) { tables.forEach { clearTable(it) } }



/** Deletes all rows and keeps each table schema.
 */
fun Dbh.clearTables(                                              vararg tables: MiniDslTable<*, *>,
                                                                          async: Boolean = false,
) = write("clearTables", async) { db ->
    db.clearTables(*tables)
}

fun SQLiteDatabase.clearTables(vararg tables: MiniDslTable<*, *>) = clearTables(tables.asList())



/** Drops the table if it exists.
 */
fun Dbh.deleteTable(                                                      table: MiniDslTable<*, *>,
                                                                          async: Boolean = false,
) = write("deleteTable", async) { db ->
    db.deleteTable(table)
}

fun SQLiteDatabase.deleteTable(table: MiniDslTable<*, *>) = deleteTable(table.name)



/** Drops each table if it exists.
 */
fun Dbh.deleteTables(                                              tables: List<MiniDslTable<*, *>>,
                                                                    async: Boolean = false,
) = write("deleteTables", async) { db ->
    db.deleteTables(tables)
}

fun SQLiteDatabase.deleteTables(tables: List<MiniDslTable<*, *>>) { tables.forEach { deleteTable(it) } }



/** Drops each table if it exists.
 */
fun Dbh.deleteTables(                                             vararg tables: MiniDslTable<*, *>,
                                                                          async: Boolean = false,
) = write("deleteTables", async) { db ->
    db.deleteTables(*tables)
}

fun SQLiteDatabase.deleteTables(vararg tables: MiniDslTable<*, *>) = deleteTables(tables.asList())




fun Dbh.deleteTable(table: String) = write("deleteTable") { db -> db.deleteTable(table) }

fun SQLiteDatabase.deleteTable(table: String) { execSQL("DROP TABLE IF EXISTS " + table) }


fun Dbh.deleteTables(                                               vararg tables: String,
                                                                            async: Boolean = false,
) = write("deleteTables", async) { db ->
    db.deleteTables(*tables)
}
fun SQLiteDatabase.deleteTables(vararg tables: String) { tables.forEach { deleteTable(it) }}



/*
internal fun SQLiteDatabase.deleteRowById(                                            id: Long,
                                                                                   table: String,
): Int = delete(table, "$_ID = ?", arrayOf(id.toString()))


internal fun <T> SQLiteDatabase.deleteRows(                                        table: String,
                                                                                whereStr: String,
                                                                                  equals: T,
): Int = delete(table, "$whereStr = ?", arrayOf(equals.toString()))
*/
