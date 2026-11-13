package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.LiteEntity
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoLite
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.mappers.toEntt
import com.vankorno.vankornodb.misc.dLog
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.orderByRowId
import com.vankorno.vankornodb.misc.suppressEntityGetterErrorLog

/**
 * Gets data from a db row as a LiteEntity, selecting the columns declared by its schema.
 * Every selected column must exist with the same type in [originalColumns].
 *
 * Returns [default] if no result is found.
 */
fun <T : LiteEntity> SQLiteDatabase.getLiteEntity(                tableInfo: TableInfoLite<T>,
                                                            originalColumns: EntityColumns,
                                                                    default: T,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
): T {
    val columns = checkedLiteColumns(tableInfo, originalColumns)
    
    return useCursor(tableInfo.name, columns, orderBy, where = where, extra = {
        applyDsl(extra)
        limit = 1
    }) { cursor ->
        if (!cursor.moveToFirst()) {
            // region LOG
            if (!suppressEntityGetterErrorLog)
                eLog("getLiteEntity(): The requested row doesn't exist in ${tableInfo.name}, returning default")
            // endregion
            return default
        }
        cursor.toEntt(tableInfo.schema)
    }
}

/**
 * Retrieves entities using the columns declared by the lite schema.
 * Every selected column must exist with the same type in [originalColumns].
 * Returns an empty list if no result is found.
 */
fun <T : LiteEntity> SQLiteDatabase.getLiteEntities(
                                                      tableInfo: TableInfoLite<T>,
                                                originalColumns: EntityColumns,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
): List<T> {
    val columns = checkedLiteColumns(tableInfo, originalColumns)
    
    return useCursor(tableInfo.name, columns, orderBy, extra, where) { cursor ->
        buildList {
            if (cursor.moveToFirst()) {
                do {
                    add(cursor.toEntity(tableInfo.schema))
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

internal fun <T : LiteEntity> checkedLiteColumns(                        tableInfo: TableInfoLite<T>,
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
