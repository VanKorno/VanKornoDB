/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.EntityTable
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.set.internal.baseAddTo
import com.vankorno.vankornodb.set.internal.modifyColumn

/** Adds [addend] to [column] in matching rows.
 */
fun <W> Dbh.addToInt(                                             addend: Number,
                                                                   table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   async: Boolean = false,
                                                                   where: W.()->Unit = {},
) = write("addToInt", async) { db ->
    db.addToInt(addend, table, column, where)
}

fun <W> SQLiteDatabase.addToInt(                                  addend: Number,
                                                                   table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   where: W.()->Unit = {},
) = baseAddTo(addend, table, column, where)


/** Adds [addend] to [column] in matching rows.
 */
fun <W> Dbh.addToLong(                                           addend: Number,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit = {},
) = write("addToLong", async) { db ->
    db.addToLong(addend, table, column, where)
}

fun <W> SQLiteDatabase.addToLong(                                addend: Number,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                  where: W.()->Unit = {},
) = baseAddTo(addend, table, column, where)


/** Adds [addend] to [column] in matching rows.
 */
fun <W> Dbh.addToFloat(                                         addend: Number,
                                                                 table: EntityTable<*, W>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                                 async: Boolean = false,
                                                                 where: W.()->Unit = {},
) = write("addToFloat", async) { db ->
    db.addToFloat(addend, table, column, where)
}

fun <W> SQLiteDatabase.addToFloat(                              addend: Number,
                                                                 table: EntityTable<*, W>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                                 where: W.()->Unit = {},
) = baseAddTo(addend, table, column, where)


/** Toggles [column] in matching rows.
 */
fun <W> Dbh.flipBool(                                             table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit = {},
) = write("flipBool", async) { db ->
    db.flipBool(table, column, where)
}

fun <W> SQLiteDatabase.flipBool(                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                  where: W.()->Unit = {},
) = modifyColumn(table, column, where, emptyList()) { "NOT $it" }
