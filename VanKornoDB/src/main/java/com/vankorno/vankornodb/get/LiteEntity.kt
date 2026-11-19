/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.api.LiteEntity
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.EntityColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.TableInfoLite
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.mappers.toEntt
import com.vankorno.vankornodb.misc.dLog
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.suppressEntityGetterErrorLog

/**
 * Gets data from a db row as a LiteEntity, selecting the columns declared by its schema.
 * Every selected column must exist with the same type in [originalColumns].
 *
 * Returns [default] if no result is found.
 */
fun <T : LiteEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLiteEntity(
                                                   tableInfo: QueryTable<TableInfoLite<T>, W, O, X>,
                                             originalColumns: EntityColumns,
                                                     default: T,
                                                     orderBy: O.()->Unit = {},
                                                       extra: X.()->Unit = {},
                                                       where: W.()->Unit,
): T = read(default, "getLiteEntity") { db ->
    db.getLiteEntity(tableInfo, originalColumns, default, orderBy, extra, where)
}

fun <T : LiteEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLiteEntity(
                                                   tableInfo: QueryTable<TableInfoLite<T>, W, O, X>,
                                             originalColumns: EntityColumns,
                                                     default: T,
                                                     orderBy: O.()->Unit = {},
                                                       extra: X.()->Unit = {},
                                                       where: W.()->Unit,
): T {
    val columns = checkedLiteColumns(tableInfo.info, originalColumns).map { definition ->
        val select: ColumnSelector<W, TypedColumn<*>> = { EntityColumn(definition) }
        select
    }.toTypedArray()
    
    return useCursor(tableInfo, *columns, orderBy = orderBy, where = where, extra = {
        extra()
        limit = 1
    }) { cursor ->
        if (!cursor.moveToFirst()) {
            // region LOG
            if (!suppressEntityGetterErrorLog)
                eLog("getLiteEntity(): The requested row doesn't exist in ${tableInfo.name}, returning default")
            // endregion
            return default
        }
        cursor.toEntt(tableInfo.info.schema)
    }
}




/**
 * Retrieves entities using the columns declared by the lite schema.
 * Every selected column must exist with the same type in [originalColumns].
 * Returns an empty list if no result is found.
 */
fun <T : LiteEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLiteEntities(
                                                   tableInfo: QueryTable<TableInfoLite<T>, W, O, X>,
                                             originalColumns: EntityColumns,
                                                     orderBy: O.()->Unit = { raw("ROWID") },
                                                       extra: X.()->Unit = {},
                                                       where: W.()->Unit = {},
): List<T> = read(emptyList(), "getLiteEntities") { db ->
    db.getLiteEntities(tableInfo, originalColumns, orderBy, extra, where)
}

fun <T : LiteEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLiteEntities(
                                                   tableInfo: QueryTable<TableInfoLite<T>, W, O, X>,
                                             originalColumns: EntityColumns,
                                                     orderBy: O.()->Unit = { raw("ROWID") },
                                                       extra: X.()->Unit = {},
                                                       where: W.()->Unit = {},
): List<T> {
    val columns = checkedLiteColumns(tableInfo.info, originalColumns).map { definition ->
        val select: ColumnSelector<W, TypedColumn<*>> = { EntityColumn(definition) }
        select
    }.toTypedArray()
    
    return useCursor(tableInfo, *columns, orderBy = orderBy, extra = extra, where = where) { cursor ->
        buildList {
            if (cursor.moveToFirst()) {
                do {
                    add(cursor.toEntity(tableInfo.info.schema))
                } while (cursor.moveToNext())
            }
            else {
                // region LOG
                if (!suppressEntityGetterErrorLog)
                    dLog("getLiteEntities(): Can't find rows in ${tableInfo.name}, returning an empty List.")
                // endregion
            }
        }
    }
}





internal fun <T : LiteEntity> checkedLiteColumns(                       tableInfo: TableInfoLite<T>,
                                                                  originalColumns: EntityColumns,
): Array<TypedColumn<*>> {
    val liteColumns = tableInfo.schema.columns?.columns
        ?: error("Lite schema for ${tableInfo.schema.clazz.simpleName} must declare columns")
    val sourceColumnsByName = originalColumns.columns.associateBy { it.name }
    
    require(liteColumns.isNotEmpty()) {
        "Lite schema for ${tableInfo.schema.clazz.simpleName} declares no columns"
    }
    require(liteColumns.map { it.name }.distinct().size == liteColumns.size) {
        "Lite schema for ${tableInfo.schema.clazz.simpleName} declares duplicate column names"
    }
    
    for (liteColumn in liteColumns) {
        val sourceColumn = sourceColumnsByName[liteColumn.name]
            ?: error("Lite column '${liteColumn.name}' is not present in the original entity columns")
        require(liteColumn::class == sourceColumn::class) {
            "Lite column '${liteColumn.name}' has type ${liteColumn::class.simpleName}, " +
                "but the original entity column has type ${sourceColumn::class.simpleName}"
        }
    }
    return liteColumns.toTypedArray()
}
