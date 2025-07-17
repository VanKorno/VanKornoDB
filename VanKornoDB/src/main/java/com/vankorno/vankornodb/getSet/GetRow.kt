package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.core.WhereBuilder
import kotlin.reflect.KClass

/** Retrieves a single entity of type [T] by its ID. Throws if no row found.*/

inline fun <reified T : Any> SQLiteDatabase.getRowById(                               id: Int,
                                                                                   table: String
): T = getRow(table, where = { ID equal id })



/** Retrieves a single entity of type [T] by its ID, or null if not found.*/

inline fun <reified T : Any> SQLiteDatabase.getRowOrNullById(                         id: Int,
                                                                                   table: String
): T? = getRowOrNull(table, where = { ID equal id })




/**
 * Retrieves a single entity of type [T] from the given [table] selecting specified [columns].
 * Throws if no row is found.
 * Uses reified type parameter and default "*" column selection.
 */
inline fun <reified T : Any> SQLiteDatabase.getRow(         table: String,
                                                   noinline joins: JoinBuilder.()->Unit = {},
                                                   noinline where: WhereBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                           offset: Int? = null
): T = getCursor(
    table, arrayOf("*"), joins, where, groupBy, having, orderBy, 1, offset
).use { cursor ->
    if (!cursor.moveToFirst()) error("No result for getOne<>()")
    cursor.toEntity(T::class)
}



/**
 * Gets one [T] from [table] with explicit type. Throws if no result.
 */
fun <T : Any> SQLiteDatabase.getRow(                        clazz: KClass<T>,
                                                            table: String,
                                                            joins: JoinBuilder.()->Unit = {},
                                                            where: WhereBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                           offset: Int? = null
): T = getCursor(
    table, arrayOf("*"), joins, where, groupBy, having, orderBy, 1, offset
).use { cursor ->
    if (!cursor.moveToFirst()) error("No result for getOne($clazz)")
    cursor.toEntity(clazz)
}











/**
 * Queries [table] for a single result of type [T] using [columns]. Returns null if no result found.
 */
inline fun <reified T : Any> SQLiteDatabase.getRowOrNull(
                                                                  table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                 offset: Int? = null,
                                                              customEnd: String = ""
): T? = getCursor(
    table, arrayOf("*"), joins, where, groupBy, having, orderBy, 1, offset, customEnd
).use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(T::class)
}




/**
 * Queries [table] for a single result of [clazz] using [columns]. Returns null if no result found.
 */
fun <T : Any> SQLiteDatabase.getRowOrNull(                        clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                 offset: Int? = null,
                                                              customEnd: String = ""
): T? = getCursor(
    table, arrayOf("*"), joins, where, groupBy, having, orderBy, 1, offset, customEnd
).use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(clazz)
}






