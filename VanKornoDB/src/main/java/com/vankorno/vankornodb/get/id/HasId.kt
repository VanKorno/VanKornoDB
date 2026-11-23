/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.id

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.get.hasRows

/** Returns true if a db table has a row with provided id
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.hasId(                                                           id: Long,
                                                                        table: DslTable<*, W, O, X>,
): Boolean = read(false, "hasRows") { db ->
    db.hasRows(table) { Id equal id }
}

/** Returns true if a db table has a row with provided id
 */
fun <
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.hasId(                                                id: Long,
                                                                        table: DslTable<*, W, O, X>,
): Boolean = hasRows(table) { Id equal id }
