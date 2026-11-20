/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.EntityColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLargestInt(    table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   where: W.()->Unit = {},
): Int = read(0, "getLargestInt") { db ->
    db.getLargestInt(table, column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLargestInt(
                                                                   table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   where: W.()->Unit = {},
): Int {
    val selected = table.selectionScope().column()
    val name = if (selected is EntityColumn<*, *>) "\"" + selected.sql.replace("\"", "\"\"") + "\"" else selected.sql
    val predicate = table.predicate(table.name, where)
    val result = table.render(table.name, table.name, arrayOf("IFNULL(MAX($name), 0)"), predicate, {}, {})
    return rawQuery(result.query, result.args).use { cursor ->
        cursor.moveToFirst()
        cursor.getInt(0)
    }
}




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLargestLong(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                  where: W.()->Unit = {},
): Long = read(0L, "getLargestLong") { db ->
    db.getLargestLong(table, column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLargestLong(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                  where: W.()->Unit = {},
): Long {
    val selected = table.selectionScope().column()
    val name = if (selected is EntityColumn<*, *>) "\"" + selected.sql.replace("\"", "\"\"") + "\"" else selected.sql
    val predicate = table.predicate(table.name, where)
    val result = table.render(table.name, table.name, arrayOf("IFNULL(MAX($name), 0)"), predicate, {}, {})
    return rawQuery(result.query, result.args).use { cursor ->
        cursor.moveToFirst()
        cursor.getLong(0)
    }
}
