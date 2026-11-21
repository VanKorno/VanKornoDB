/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.mappers.toEntt
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.suppressEntityGetterErrorLog

/** Gets one db table row as an object of an entity class using VanKorno DSL parameters.
 *  Returns [default] if no result found.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getEntity(
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                           default: T,
                                                           orderBy: O.()->Unit = {},
                                                             extra: X.()->Unit = {},
                                                             where: W.()->Unit,
): T = read(default, "getEntity") { db ->
    db.getEntity(table, default, orderBy, extra, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getEntity(
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                           default: T,
                                                           orderBy: O.()->Unit = {},
                                                             extra: X.()->Unit = {},
                                                             where: W.()->Unit,
): T = getEntity(table, orderBy, extra, where) ?: run {
    // region LOG
    if (!suppressEntityGetterErrorLog)
        eLog("getEntity(): The requested row doesn't exist in ${table.name}, returning default")
    // endregion
    default
}


/** Gets one db table row as an object of an entity class using VanKorno DSL parameters.
 * Returns null if no result found.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getEntity(
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                           orderBy: O.()->Unit = {},
                                                             extra: X.()->Unit = {},
                                                             where: W.()->Unit,
): T? = read(null, "getEntity") { db ->
    db.getEntity(table, orderBy, extra, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getEntity(
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                           orderBy: O.()->Unit = {},
                                                             extra: X.()->Unit = {},
                                                             where: W.()->Unit,
): T? = useCursor(table, orderBy = orderBy, where = where,
    extra = {
        extra()
        limit = 1
    }
) { cursor ->
    if (!cursor.moveToFirst()) return null //\/\/\/\/\/\
    
    cursor.toEntt(table.info.schema)
}
