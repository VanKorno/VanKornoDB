/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.fix

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.data.DbConstants.RowID
import com.vankorno.vankornodb.core.dsl.DbColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.raw
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.get.getNextLongs
import com.vankorno.vankornodb.get.useCursor

/** Repairs each bound column and returns the total number of modified rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.fixDuplicateLongsMult(
                                                           columns: List<DbColumn<W, O, X, LongCol>>,
                                                             where: W.()->Unit = {},
): Int = readWrite(0, "fixDuplicateLongsMult") { db ->
    db.fixDuplicateLongsMult(columns, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.fixDuplicateLongsMult(
                                                           columns: List<DbColumn<W, O, X, LongCol>>,
                                                             where: W.()->Unit = {},
): Int = columns.sumOf { fixDuplicateLongs(it, where) }




/** Preserves the first matching row per value in ROWID order. Assigns subsequent matches
 * values available across the whole table and returns the number of rows updated.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.fixDuplicateLongs(
                                                                  column: DbColumn<W, O, X, LongCol>,
                                                                   where: W.()->Unit = {},
): Int = readWrite(0, "fixDuplicateLongs") { db -> db.fixDuplicateLongs(column, where) }


fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.fixDuplicateLongs(
                                                                  column: DbColumn<W, O, X, LongCol>,
                                                                   where: W.()->Unit = {},
): Int {
    val table = column.table
    val name = table.columnSql(column.selector)
    val projection = table.columns { listOf(raw(RowID), column.selector(this)) }
    val duplicates = useCursor(projection, orderBy = { raw(RowID) }, where = where) { cursor ->
        val seen = mutableSetOf<Long>()
        buildList {
            while (cursor.moveToNext()) {
                if (!seen.add(cursor.getLong(1)))
                    add(cursor.getLong(0))
            }
        }
    }
    val replacements = getNextLongs(column, duplicates.size)
    val values = ContentValues(1)
    return duplicates.indices.sumOf { idx ->
        values.put(name, replacements[idx])
        update(table.name, values, "$RowID = ?", arrayOf(duplicates[idx].toString()))
    }
}
