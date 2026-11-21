/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.fix

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.data.DbConstants._Position
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.raw
import com.vankorno.vankornodb.dbManagement.data.requirePositionColumn

/** Repairs duplicate positions among matching rows and returns the number updated.
 * When the schema declares columns, it must declare a Long position column.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.fixDuplicatePosition(
                                                                      table: DslTable<*, W, O, X>,
                                                                      where: W.()->Unit = {},
): Int = readWrite(0, "fixDuplicatePosition") { db ->
    db.fixDuplicatePosition(table, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.fixDuplicatePosition(
                                                                      table: DslTable<*, W, O, X>,
                                                                      where: W.()->Unit = {},
): Int {
    table.info.requirePositionColumn()
    return fixDuplicateLongs(table.col { raw(_Position) }, where)
}


/** Repairs duplicate positions in each table and returns the total number of rows updated.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.fixDuplicatePositionMult(
                                                              vararg tables: DslTable<*, W, O, X>,
                                                                      where: W.()->Unit = {},
): Int = readWrite(0, "fixDuplicatePositionMult") { db ->
    db.fixDuplicatePositionMult(*tables, where = where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.fixDuplicatePositionMult(
                                                              vararg tables: DslTable<*, W, O, X>,
                                                                      where: W.()->Unit = {},
): Int = tables.sumOf { fixDuplicatePosition(it, where) }
