/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DbhColumn
import com.vankorno.vankornodb.core.dsl.EntityColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLargestInt(
                                                                  column: DbhColumn<W, O, X, IntCol>,
                                                                   where: W.()->Unit = {},
): Int = read(0, "getLargestInt") { db ->
    db.getLargestInt(column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLargestInt(
                                                                  column: DbhColumn<W, O, X, IntCol>,
                                                                   where: W.()->Unit = {},
): Int = getLargestValue(column, where) { it.getInt(0) }




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLargestLong(
                                                                 column: DbhColumn<W, O, X, LongCol>,
                                                                  where: W.()->Unit = {},
): Long = read(0L, "getLargestLong") { db ->
    db.getLargestLong(column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLargestLong(
                                                                 column: DbhColumn<W, O, X, LongCol>,
                                                                  where: W.()->Unit = {},
): Long = getLargestValue(column, where) { it.getLong(0) }




private inline fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> SQLiteDatabase.getLargestValue(
                                                                       column: DbhColumn<W, O, X, *>,
                                                               noinline where: W.()->Unit,
                                                                    readValue: (Cursor)->R,
): R {
    val table = column.table
    val selected = column.selector(table.selectionScope())
    
    val name =  if (selected is EntityColumn<*, *>)
                    "\"" + selected.sql.replace("\"", "\"\"") + "\""
                else
                    selected.sql
    
    val predicate = table.predicate(table.name, where)
    
    val result = table.render(
        source = table.name,
        qualifier = table.name,
        columns = arrayOf("IFNULL(MAX($name), 0)"),
        where = predicate,
        orderBy = {},
        extra = {}
    )
    return rawQuery(result.query, result.args).use { cursor ->
        cursor.moveToFirst()
        readValue(cursor)
    }
}
