// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.mapper.toEntity
import kotlin.reflect.KClass


/**
 * Gets one db table row as an object of type [T] using the full VanKorno DSL (but limit is always 1). Returns null if no result found.
 */
inline fun <reified T : BaseEntity> SQLiteDatabase.getObjPro(               table: String,
                                                                     noinline dsl: FullDsl.()->Unit,
): T? = getCursorPro(table) {
    applyDsl(dsl)
    limit = 1
}.use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(T::class)
}


/**
 * Gets one db table row as an object of [clazz] using using the full VanKorno DSL (but limit is always 1). Returns null if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjPro(                              table: String,
                                                                            clazz: KClass<T>,
                                                                              dsl: FullDsl.()->Unit,
): T? = getCursorPro(table) {
    applyDsl(dsl)
    limit = 1
}.use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(clazz)
}




inline fun <reified T : BaseEntity> SQLiteDatabase.getObjPro(               table: String,
                                                                          default: T,
                                                                     noinline dsl: FullDsl.()->Unit,
): T = getObjPro<T>(table, dsl) ?: run {
    // region LOG
        Log.e(DbTAG, "getObjPro(): The requested row doesn't exist in $table, returning default")
    // endregion
    default
}


fun <T : BaseEntity> SQLiteDatabase.getObjPro(                              table: String,
                                                                            clazz: KClass<T>,
                                                                          default: T,
                                                                              dsl: FullDsl.()->Unit,
): T = getObjPro(table, clazz, dsl) ?: run {
    // region LOG
        Log.e(DbTAG, "getObjPro(): The requested row doesn't exist in $table, returning default")
    // endregion
    default
}
