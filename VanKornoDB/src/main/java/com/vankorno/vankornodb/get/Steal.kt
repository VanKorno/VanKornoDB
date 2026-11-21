/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.core.dsl.Field
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.dbManagement.data.SchemaBundle
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.mappers.toEntt
import com.vankorno.vankornodb.misc.dLog
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.suppressEntityGetterErrorLog

/**
 * Steal one entity table's [ownerTable] data, and return it as another entity's [thiefSchema] object.
 * Checks if all columns of [thiefSchema] have the same name and type.
 * 
 * Returns [default] if no result is found.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.steal(
                                                      ownerTable: DslTable<TableInfo<*>, W, O, X>,
                                                     thiefSchema: SchemaBundle<T>,
                                                         default: T,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit,
): T = read(default, "stealFrom") { db ->
    db.steal(ownerTable, thiefSchema, default, orderBy, extra, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.steal(
                                                      ownerTable: DslTable<TableInfo<*>, W, O, X>,
                                                     thiefSchema: SchemaBundle<T>,
                                                         default: T,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit,
): T {
    val definitions = checkedThiefColumns(thiefSchema, ownerTable.info.schema.columns)
    val columns = ownerTable.columns { definitions.map { Field(it) } }
    
    return useCursor(columns, orderBy, where = where,
        extra = {
            extra()
            limit = 1
        }
    ) { cursor ->
        if (!cursor.moveToFirst()) {
            // region LOG
            if (!suppressEntityGetterErrorLog)
                eLog("stealFrom(): The requested row doesn't exist in ${ownerTable.name}, returning default")
            // endregion
            return default
        }
        cursor.toEntt(thiefSchema)
    }
}




/**
 * Steal one entity table's [ownerTable] data, and return it as another entity's [thiefSchema] objects.
 * Checks if all columns of [thiefSchema] have the same name and type.
 * 
 * Returns an empty list if no result is found.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.stealALot(
                                                      ownerTable: DslTable<TableInfo<*>, W, O, X>,
                                                     thiefSchema: SchemaBundle<T>,
                                                         orderBy: O.()->Unit = { raw("ROWID") },
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<T> = read(emptyList(), "stealALot") { db ->
    db.stealALot(ownerTable, thiefSchema, orderBy, extra, where)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.stealALot(
                                                      ownerTable: DslTable<TableInfo<*>, W, O, X>,
                                                     thiefSchema: SchemaBundle<T>,
                                                         orderBy: O.()->Unit = { raw("ROWID") },
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<T> {
    val definitions = checkedThiefColumns(thiefSchema, ownerTable.info.schema.columns)
    val columns = ownerTable.columns { definitions.map { Field(it) } }
    
    return useCursor(columns, orderBy, extra, where) { cursor ->
        buildList {
            if (cursor.moveToFirst()) {
                do {
                    add(cursor.toEntity(thiefSchema))
                } while (cursor.moveToNext())
            }
            else {
                // region LOG
                if (!suppressEntityGetterErrorLog)
                    dLog("stealALot(): Can't find rows in ${ownerTable.name}, returning an empty List.")
                // endregion
            }
        }
    }
}





internal fun <T : DbEntity> checkedThiefColumns(                 thiefSchema: SchemaBundle<T>,
                                                                ownerColumns: List<TypedColumn<*>>?,
): Array<TypedColumn<*>> {
    val thiefColumns = thiefSchema.columns
        ?: error("Thief schema for ${thiefSchema.clazz.simpleName} must declare columns")
    val ownerColumnsByName = checkNotNull(ownerColumns) { "Owner schema must declare columns" }.associateBy { it.name }
    
    require(thiefColumns.isNotEmpty()) {
        "Thief schema for ${thiefSchema.clazz.simpleName} declares no columns"
    }
    require(thiefColumns.map { it.name }.distinct().size == thiefColumns.size) {
        "Thief schema for ${thiefSchema.clazz.simpleName} declares duplicate column names"
    }
    
    for (thiefCol in thiefColumns) {
        val sourceCol = ownerColumnsByName[thiefCol.name]
            ?: error("The thief schema column '${thiefCol.name}' is not present in the original entity columns")
        require(thiefCol::class == sourceCol::class) {
            "Thief entity column '${thiefCol.name}' has type ${thiefCol::class.simpleName}, " +
                "but the owner entity column has type ${sourceCol::class.simpleName}"
        }
    }
    return thiefColumns.toTypedArray()
}
