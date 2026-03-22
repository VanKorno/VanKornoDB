/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
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
fun <T : BaseEntity> SQLiteDatabase.getObjPro(                          tableInfo: TableInfoBase<T>,
                                                                          default: T,
                                                                              dsl: FullDsl.()->Unit,
): T = getObjPro(tableInfo, dsl) ?: run {
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
fun <T : BaseEntity> SQLiteDatabase.getObjPro(                          tableInfo: TableInfoBase<T>,
                                                                              dsl: FullDsl.()->Unit,
): T? = getCursorPro(tableInfo.name) {
    applyDsl(dsl)
    limit = 1
}.use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(tableInfo.schema)
}






