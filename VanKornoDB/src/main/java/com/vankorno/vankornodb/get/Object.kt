/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.suppressObjGetterErrorLog

/**
 * Gets one db table row as an object of an entity class using VanKorno DSL parameters.
 * Returns [default] if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getObj(                       tableInfo: TableInfoBase<T>,
                                                                    default: T,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
): T = getObj(tableInfo, orderBy, extra, where) ?: run {
    // region LOG
    if (!suppressObjGetterErrorLog)
        eLog("getObj(): The requested row doesn't exist in ${tableInfo.name}, returning default")
    // endregion
    default
}


/**
 * Gets one db table row as an object of an entity class using VanKorno DSL parameters.
 * Returns null if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getObj(                       tableInfo: TableInfoBase<T>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
): T? = getCursor(tableInfo.name, orderBy, where = where,
    extra = {
        applyDsl(extra)
        limit = 1
    }
).use { cursor ->
    if (!cursor.moveToFirst()) return null //\/\/\/\/\/\
    
    cursor.toEntity(tableInfo.schema)
}






