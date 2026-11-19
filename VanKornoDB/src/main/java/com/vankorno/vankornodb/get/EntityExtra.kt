/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.suppressEntityGetterErrorLog

/** Gets the first row as an object of an entity class using WhereDsl.
 *  Returns null if no result found.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getFirstEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                       where: W.()->Unit = {},
): T? = read(null, "getFirstEntity") { db ->
    db.getFirstEntity(tableInfo, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getFirstEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                       where: W.()->Unit = {},
): T? = getEntity(tableInfo, { raw("ROWID") }, where = where)


/** Gets the first row as an object of an entity class using WhereDsl.
 *  Returns [default] and logs error if no result found.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getFirstEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                     default: T,
                                                       where: W.()->Unit = {},
): T = read(default, "getFirstEntity") { db ->
    db.getFirstEntity(tableInfo, default, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getFirstEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                     default: T,
                                                       where: W.()->Unit = {},
): T = getFirstEntity(tableInfo, where) ?: run {
    // region LOG
    if (!suppressEntityGetterErrorLog)
        eLog("getFirstEntity(): No fitting rows exist in ${tableInfo.name}, returning default")
    // endregion
    default
}




/** Gets the last row as an object of an entity class using WhereDsl.
 *  Returns null if no result found.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLastEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                       where: W.()->Unit = {},
): T? = read(null, "getLastEntity") { db ->
    db.getLastEntity(tableInfo, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLastEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                       where: W.()->Unit = {},
): T? = getEntity(tableInfo, { raw("ROWID DESC") }, where = where)


/** Gets the last row as an object of an entity class using WhereDsl.
 *  Returns [default] and logs error if no result found.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLastEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                     default: T,
                                                       where: W.()->Unit = {},
): T = read(default, "getLastEntity") { db ->
    db.getLastEntity(tableInfo, default, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLastEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                     default: T,
                                                       where: W.()->Unit = {},
): T = getLastEntity(tableInfo, where) ?: run {
    // region LOG
    if (!suppressEntityGetterErrorLog)
        eLog("getLastEntity(): No fitting rows exist in ${tableInfo.name}, returning default")
    // endregion
    default
}




/** Retrieves a single random entity object (row) from the specified table and maps it to an entity class.
 *
 * @param T The type of the object to retrieve. Must be a Kotlin class representing the table structure.
 * @param tableInfo Contains table name and a schema bundle of any entity type.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random object of type [T] from the table, or null if no rows match.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRandomEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                       where: W.()->Unit = {},
): T? = read(null, "getRandomEntity") { db ->
    db.getRandomEntity(tableInfo, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRandomEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                       where: W.()->Unit = {},
): T? = getEntity(tableInfo, { random() }, where = where)


/**
 * Retrieves a single random entity object (row) from the specified table and maps it to an entity class.
 *
 * @param T The type of the object to retrieve. Must be a Kotlin class representing the table structure.
 * @param tableInfo Contains table name and a schema bundle of any entity type.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random object of type [T] from the table, or [default] if no rows match.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getRandomEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                     default: T,
                                                       where: W.()->Unit = {},
): T = read(default, "getRandomEntity") { db ->
    db.getRandomEntity(tableInfo, default, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getRandomEntity(
                                                   tableInfo: QueryTable<TableInfo<T>, W, O, X>,
                                                     default: T,
                                                       where: W.()->Unit = {},
): T = getRandomEntity(tableInfo, where) ?: run {
    // region LOG
    if (!suppressEntityGetterErrorLog)
        eLog("getRandomEntity(): No fitting rows exist in ${tableInfo.name}, returning default")
    // endregion
    default
}


// TODO getRandomEntities maybe
