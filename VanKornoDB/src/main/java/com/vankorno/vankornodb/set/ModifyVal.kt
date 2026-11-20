/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DbColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.set.internal.baseAddTo
import com.vankorno.vankornodb.set.internal.modifyColumn

/** Adds [addend] to [column] in matching rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.addToInt(        addend: Number,
                                                                  column: DbColumn<W, O, X, IntCol>,
                                                                   async: Boolean = false,
                                                                   where: W.()->Unit = {},
) = write("addToInt", async) { db ->
    db.addToInt(addend, column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.addToInt(
                                                                  addend: Number,
                                                                  column: DbColumn<W, O, X, IntCol>,
                                                                   where: W.()->Unit = {},
) = baseAddTo(addend, column, where)




/** Adds [addend] to [column] in matching rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.addToLong(      addend: Number,
                                                                 column: DbColumn<W, O, X, LongCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit = {},
) = write("addToLong", async) { db ->
    db.addToLong(addend, column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.addToLong(
                                                                  addend: Number,
                                                                 column: DbColumn<W, O, X, LongCol>,
                                                                  where: W.()->Unit = {},
) = baseAddTo(addend, column, where)




/** Adds [addend] to [column] in matching rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.addToFloat(    addend: Number,
                                                                column: DbColumn<W, O, X, FloatCol>,
                                                                 async: Boolean = false,
                                                                 where: W.()->Unit = {},
) = write("addToFloat", async) { db ->
    db.addToFloat(addend, column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.addToFloat(
                                                                  addend: Number,
                                                                column: DbColumn<W, O, X, FloatCol>,
                                                                 where: W.()->Unit = {},
) = baseAddTo(addend, column, where)




/** Toggles [column] in matching rows.
 */
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.flipBool(       column: DbColumn<W, O, X, BoolCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit = {},
) = write("flipBool", async) { db ->
    db.flipBool(column, where)
}

fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.flipBool(
                                                                  column: DbColumn<W, O, X, BoolCol>,
                                                                  where: W.()->Unit = {},
) = modifyColumn(column, where, emptyList()) { "NOT $it" }
