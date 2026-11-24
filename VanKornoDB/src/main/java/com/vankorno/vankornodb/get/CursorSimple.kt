/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh


/** Reads all rows and columns from [table] and closes the cursor after [block] finishes.
 */
fun Dbh.useCursor(                                                           table: String,
                                                                             block: (Cursor)->Unit,
) = write("useCursor") { db ->
    db.useCursor(table, block)
}

/** Reads the named table and closes the cursor after [block] finishes.
 */
inline fun <R> SQLiteDatabase.useCursor(                                        table: String,
                                                                                block: (Cursor)->R,
): R = getCursor(table).use(block)




/** Returns the result of [block], with [default] as the helper's fallback.
 */
fun <R> Dbh.getFromCursor(                                                      table: String,
                                                                              default: R,
                                                                                block: (Cursor)->R,
): R = readWrite(default, "getFromCursor") { db ->
    db.useCursor(table, block)
}

/** Reads all columns and rows from the named table. The caller owns the returned cursor.
 */
fun SQLiteDatabase.getCursor(                                                      table: String
): Cursor {
    val quotedTable = "\"" + table.replace("\"", "\"\"") + "\""
    return rawQuery("SELECT * FROM $quotedTable", null)
}
