package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.ID
import kotlin.reflect.KClass

/** Gets one db table row as an object of type [T] by its ID. Throws if no row found.*/

inline fun <reified T : DbEntity> SQLiteDatabase.getObjById(                          id: Int,
                                                                                   table: String,
): T = getObj(table, where = { ID equal id })



/** Gets one db table row as an object of type [T] by its ID, or null if not found.*/

inline fun <reified T : DbEntity> SQLiteDatabase.getObjOrNullById(                    id: Int,
                                                                                   table: String,
): T? = getObjOrNull(table, where = { ID equal id })




/**
 * Gets one db table row as an object of type [T] from the given [table].
 * Throws if no row is found.
 * Uses reified type parameter and default "*" column selection.
 */
inline fun <reified T : DbEntity> SQLiteDatabase.getObj(          table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                 offset: Int? = null,
): T = getCursor(
    table, "*", joins, where, groupBy, having, orderBy, 1, offset
).use { cursor ->
    if (!cursor.moveToFirst()) error("No result for getObj<>()")
    cursor.toEntity(T::class)
}



/**
 * Gets one db table row as an object of type [T] from [table] with explicit type. Throws if no result.
 */
fun <T : DbEntity> SQLiteDatabase.getObj(                         clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                 offset: Int? = null,
): T = getCursor(
    table, "*", joins, where, groupBy, having, orderBy, 1, offset
).use { cursor ->
    if (!cursor.moveToFirst()) error("No result for getObj($clazz)")
    cursor.toEntity(clazz)
}











/**
 * Gets one db table row as an object of type [T] using the usual VanKorno DSL. Returns null if no result found.
 */
inline fun <reified T : DbEntity> SQLiteDatabase.getObjOrNull(
                                                                  table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
): T? = getCursor(
    table, "*", joins, where, groupBy, having, orderBy, 1, offset, customEnd
).use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(T::class)
}




/**
 * Gets one db table row as an object of [clazz] using using the usual VanKorno DSL. Returns null if no result found.
 */
fun <T : DbEntity> SQLiteDatabase.getObjOrNull(                   clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
): T? = getCursor(
    table, "*", joins, where, groupBy, having, orderBy, 1, offset, customEnd
).use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(clazz)
}






