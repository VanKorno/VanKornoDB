// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import kotlin.reflect.KClass

/**
 * Gets one db table row as an object of type [T] using the WhereBuilder. Returns null if no result found.
 */
inline fun <reified T : DbEntity> SQLiteDatabase.getObj(          table: String,
                                                         noinline where: WhereDsl.()->Unit = {},
): T? = getObjPro(table) { this.where = where }




/**
 * Gets one db table row as an object of [clazz] using using WhereBuilder. Returns null if no result found.
 */
fun <T : DbEntity> SQLiteDatabase.getObj(                         table: String,
                                                                  clazz: KClass<T>,
                                                                  where: WhereDsl.()->Unit = {},
): T? = getObjPro(table, clazz) { this.where = where }




inline fun <reified T : DbEntity> SQLiteDatabase.getObj(          table: String,
                                                                default: T,
                                                         noinline where: WhereDsl.()->Unit = {},
): T = getObj<T>(table, where) ?: run {
    // region LOG
        Log.e(DbTAG, "getObj(): The requested row doesn't exist in $table, returning default")
    // endregion
    default
}


fun <T : DbEntity> SQLiteDatabase.getObj(                         table: String,
                                                                  clazz: KClass<T>,
                                                                default: T,
                                                                  where: WhereDsl.()->Unit = {},
): T = getObj(table, clazz, where) ?: run {
    // region LOG
        Log.e(DbTAG, "getObj(): The requested row doesn't exist in $table, returning default")
    // endregion
    default
}










