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
 * Gets one db table row as an object of an entity class,
 * using the full VanKorno DSL (but limit is always 1).
 * Returns [default] if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getObj(                       tableInfo: TableInfoBase<T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): T = getObj(tableInfo, where, orderBy, extra) ?: run {
    // region LOG
    if (!suppressObjGetterErrorLog)
        eLog("getObjPro(): The requested row doesn't exist in ${tableInfo.name}, returning default")
    // endregion
    default
}


/**
 * Gets one db table row as an object of an entity class,
 * using the full VanKorno DSL (but limit is always 1).
 * Returns null if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getObj(                       tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
): T? = getCursor(tableInfo.name, where, orderBy) {
    applyDsl(extra)
    limit = 1
}.use { cursor ->
    if (!cursor.moveToFirst()) return null //\/\/\/\/\/\
    
    cursor.toEntity(tableInfo.schema)
}






