/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.core.dsl.EntityColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.SchemaBundle
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.mappers.toEntt
import com.vankorno.vankornodb.misc.dLog
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.suppressEntityGetterErrorLog

/**
 * Gets data from a db row as a DbEntity, selecting the columns declared by its schema.
 * Every selected column must exist with the same type in the source [table].
 *
 * Returns [default] if no result is found.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLiteEntity(
                                                           table: QueryTable<TableInfo<*>, W, O, X>,
                                                      liteSchema: SchemaBundle<T>,
                                                         default: T,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit,
): T = read(default, "getLiteEntity") { db ->
    db.getLiteEntity(table, liteSchema, default, orderBy, extra, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLiteEntity(
                                                           table: QueryTable<TableInfo<*>, W, O, X>,
                                                      liteSchema: SchemaBundle<T>,
                                                         default: T,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit,
): T {
    val definitions = checkedLiteColumns(liteSchema, table.info.schema.columns)
    val columns = table.columns { definitions.map { EntityColumn<W, TypedColumn<*>>(it) } }
    
    return useCursor(columns, orderBy = orderBy, where = where, extra = {
        extra()
        limit = 1
    }) { cursor ->
        if (!cursor.moveToFirst()) {
            // region LOG
            if (!suppressEntityGetterErrorLog)
                eLog("getLiteEntity(): The requested row doesn't exist in ${table.name}, returning default")
            // endregion
            return default
        }
        cursor.toEntt(liteSchema)
    }
}




/**
 * Retrieves entities using the columns declared by the lite schema.
 * Every selected column must exist with the same type in the source [table].
 * Returns an empty list if no result is found.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLiteEntities(
                                                           table: QueryTable<TableInfo<*>, W, O, X>,
                                                      liteSchema: SchemaBundle<T>,
                                                         orderBy: O.()->Unit = { raw("ROWID") },
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<T> = read(emptyList(), "getLiteEntities") { db ->
    db.getLiteEntities(table, liteSchema, orderBy, extra, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLiteEntities(
                                                           table: QueryTable<TableInfo<*>, W, O, X>,
                                                      liteSchema: SchemaBundle<T>,
                                                         orderBy: O.()->Unit = { raw("ROWID") },
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<T> {
    val definitions = checkedLiteColumns(liteSchema, table.info.schema.columns)
    val columns = table.columns { definitions.map { EntityColumn<W, TypedColumn<*>>(it) } }
    
    return useCursor(columns, orderBy = orderBy, extra = extra, where = where) { cursor ->
        buildList {
            if (cursor.moveToFirst()) {
                do {
                    add(cursor.toEntity(liteSchema))
                } while (cursor.moveToNext())
            }
            else {
                // region LOG
                if (!suppressEntityGetterErrorLog)
                    dLog("getLiteEntities(): Can't find rows in ${table.name}, returning an empty List.")
                // endregion
            }
        }
    }
}





internal fun <T : DbEntity> checkedLiteColumns(                   liteSchema: SchemaBundle<T>,
                                                               sourceColumns: List<TypedColumn<*>>?,
): Array<TypedColumn<*>> {
    val liteColumns = liteSchema.columns
        ?: error("Lite schema for ${liteSchema.clazz.simpleName} must declare columns")
    val sourceColumnsByName = checkNotNull(sourceColumns) { "Source schema must declare columns" }.associateBy { it.name }
    
    require(liteColumns.isNotEmpty()) {
        "Lite schema for ${liteSchema.clazz.simpleName} declares no columns"
    }
    require(liteColumns.map { it.name }.distinct().size == liteColumns.size) {
        "Lite schema for ${liteSchema.clazz.simpleName} declares duplicate column names"
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
