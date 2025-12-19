// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.mapper.toEntity
import kotlin.reflect.KClass

/**
 * Gets one db table row as an object of type [T] using the WhereBuilder. Returns null if no result found.
 */
inline fun <reified T : DbEntity> SQLiteDatabase.getObj(          table: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
): T? = getObjPro(table) { this.where = where }




/**
 * Gets one db table row as an object of [clazz] using using WhereBuilder. Returns null if no result found.
 */
fun <T : DbEntity> SQLiteDatabase.getObj(                         clazz: KClass<T>,
                                                                  table: String,
                                                                  where: WhereBuilder.()->Unit = {},
): T? = getObjPro(clazz, table) { this.where = where }




/**
 * Gets one db table row as an object of type [T] using the full VanKorno DSL (but limit is always 1). Returns null if no result found.
 */
inline fun <reified T : DbEntity> SQLiteDatabase.getObjPro(               table: String,
                                                             noinline queryOpts: QueryOpts.()->Unit,
): T? = getCursorPro(table) {
    applyOpts(queryOpts)
    limit = 1
}.use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(T::class)
}


/**
 * Gets one db table row as an object of [clazz] using using the full VanKorno DSL (but limit is always 1). Returns null if no result found.
 */
fun <T : DbEntity> SQLiteDatabase.getObjPro(                              clazz: KClass<T>,
                                                                          table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
): T? = getCursorPro(table) {
    applyOpts(queryOpts)
    limit = 1
}.use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(clazz)
}






