/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase

/** Retrieves a list of entity objects mapped from the given table.
 */
fun <T : BaseEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getEntities(
                                                       table: QueryTable<TableInfoBase<T>, W, O, X>,
                                                     orderBy: O.()->Unit = { raw("ROWID") },
                                                       extra: X.()->Unit = {},
                                                       where: W.()->Unit = {},
): List<T> = read(emptyList(), "getEntities") { db ->
    db.getEntities(table, orderBy, extra, where)
}

/** Retrieves a list of entity objects mapped from the given table.
 */
fun <T : BaseEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getEntities(
                                                       table: QueryTable<TableInfoBase<T>, W, O, X>,
                                                     orderBy: O.()->Unit = { raw("ROWID") },
                                                       extra: X.()->Unit = {},
                                                       where: W.()->Unit = {},
): List<T> = useCursor(
    table, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(cursor.toEntity(table.info.schema))
            } while (cursor.moveToNext())
        }
    }
}




/** Retrieves a map of entity objects from the given table.
 */
fun <T : BaseEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getEntityMap(
                                                       table: QueryTable<TableInfoBase<T>, W, O, X>,
                                                     orderBy: O.()->Unit = { raw("ROWID") },
                                                       extra: X.()->Unit = {},
                                                       where: W.()->Unit = {},
): Map<Long, T> = read(emptyMap(), "getEntityMap") { db ->
    db.getEntityMap(table, orderBy, extra, where)
}

/** Retrieves a map of entity objects from the given table.
 */
fun <T : BaseEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getEntityMap(
                                                       table: QueryTable<TableInfoBase<T>, W, O, X>,
                                                     orderBy: O.()->Unit = { raw("ROWID") },
                                                       extra: X.()->Unit = {},
                                                       where: W.()->Unit = {},
): Map<Long, T> = useCursor(
    table, orderBy = orderBy, extra = extra, where = where
) { cursor ->
    buildMap {
        if (cursor.moveToFirst()) {
            val idColIdx = cursor.getColumnIndexOrThrow("id")
            do {
                val entity = cursor.toEntity(table.info.schema)
                put(cursor.getLong(idColIdx), entity)
            } while (cursor.moveToNext())
        }
    }
}
