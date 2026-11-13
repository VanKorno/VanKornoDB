package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
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
 * Gets data from specific columns of a db row and returns it as a LiteEntity object.
 * Columns must go in the same order and have the same names as the lite entity fields.
 * 
 * Returns [default] if no result found.
 */
fun <T : LiteEntity> SQLiteDatabase.getLiteEntity(             tableInfo: TableInfoLite<T>,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                 default: T,
                                                                 orderBy: OrderDsl.()->Unit = {},
                                                                   extra: ExtraDsl.()->Unit = {},
                                                                   where: WhereDsl.()->Unit,
): T = useCursor(tableInfo.name, columns, orderBy, where = where,
    extra = {
        applyDsl(extra)
        limit = 1
    }
) { cursor ->
    if (!cursor.moveToFirst()) {
        // region LOG
        if (!suppressEntityGetterErrorLog)
            eLog("getLiteEntity(): The requested row doesn't exist in ${tableInfo.name}, returning default")
        // endregion
        return default //\/\/\/\/\/\ 
    }
    cursor.toEntt(tableInfo.schema)
}


/**
 * Retrieves a list of entity objects mapped from the given columns.
 * Returns an empty List if no result found.
 */
fun <T : LiteEntity> SQLiteDatabase.getLiteEntities(
                                                      tableInfo: TableInfoLite<T>,
                                                        columns: Array<out TypedColumn<*>>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
): List<T> = useCursor(tableInfo.name, columns, orderBy, extra, where) { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(cursor.toEntity(tableInfo.schema))
            } while (cursor.moveToNext())
        } else {
            // region LOG
            if (!suppressEntityGetterErrorLog)
                dLog("getLiteEntity(): Can't find rows in ${tableInfo.name}, returning an empty List.")
            // endregion
        }
    }
}
