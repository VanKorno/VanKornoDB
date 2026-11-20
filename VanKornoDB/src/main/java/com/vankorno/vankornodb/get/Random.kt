/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol

/** Returns a random matching Int value, or -1 when there are no matches.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRandomInt(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, IntCol>,
                                                                  where: W.()->Unit = {},
): Int = read(-1, "getRandomInt") { db ->
    db.getRandomInt(table, column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRandomInt(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, IntCol>,
                                                                  where: W.()->Unit = {},
): Int = getInt(table, column, orderBy = { random() }, where = where)




/** Returns a random matching String value, or an empty string when there are no matches.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRandomString(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, StrCol>,
                                                                  where: W.()->Unit = {},
): String = read("", "getRandomString") { db ->
    db.getRandomString(table, column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRandomString(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, StrCol>,
                                                                  where: W.()->Unit = {},
): String = getStr(table, column, orderBy = { random() }, where = where)




/** Returns a random matching Long value, or -1 when there are no matches.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRandomLong(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, LongCol>,
                                                                 where: W.()->Unit = {},
): Long = read(-1L, "getRandomLong") { db ->
    db.getRandomLong(table, column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRandomLong(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, LongCol>,
                                                                 where: W.()->Unit = {},
): Long = getLong(table, column, orderBy = { random() }, where = where)




/** Returns a random matching Float value, or -1 when there are no matches.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRandomFloat(
                                                                table: QueryTable<*, W, O, X>,
                                                               column: ColumnSelector<W, FloatCol>,
                                                                where: W.()->Unit = {},
): Float = read(-1f, "getRandomFloat") { db ->
    db.getRandomFloat(table, column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRandomFloat(
                                                                table: QueryTable<*, W, O, X>,
                                                               column: ColumnSelector<W, FloatCol>,
                                                                where: W.()->Unit = {},
): Float = getFloat(table, column, orderBy = { random() }, where = where)




/**
 * Retrieves a single random ID from the specified table.
 *
 * @param table The name of the table to query.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random ID from the table, or -1 if no rows match.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRandomId(
                                                                     table: QueryTable<*, W, O, X>,
                                                                     where: W.()->Unit = {},
): Long = read(-1L, "getRandomId") { db ->
    db.getRandomId(table, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRandomId(
                                                                     table: QueryTable<*, W, O, X>,
                                                                     where: W.()->Unit = {},
): Long = getRandomLong(table, idColumn(), where)
