package com.vankorno.vankornodb.getSet

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.JoinBuilder
import kotlin.reflect.KClass

/** Retrieves a single entity of type [T] by its ID. Throws if no row found.*/
inline fun <reified T : Any> SQLiteDatabase.getOneById(                            table: String,
                                                                                      id: Int
): T = getOne(table, where = {ID equal id})



/** Retrieves a single entity of type [T] by its ID, or null if not found.*/

inline fun <reified T : Any> SQLiteDatabase.getOneOrNullById(                      table: String,
                                                                                      id: Int
): T? = getOneOrNull(table, where = {ID equal id})




/**
 * Retrieves a single entity of type [T] from the given [table] selecting specified [columns].
 * Throws if no row is found.
 * Uses reified type parameter and default "*" column selection.
 */
inline fun <reified T : Any> SQLiteDatabase.getOne(         table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                   noinline joins: JoinBuilder.()->Unit = {},
                                                   noinline where: WhereBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = 1,
                                                           offset: Int? = null
): T = getCursor(
    table, columns, joins, where, groupBy, having, orderBy, limit, offset
).use { cursor ->
    if (!cursor.moveToFirst()) error("No result for getOne<>()")
    cursor.toEntity(T::class)
}


/**
 * Returns one result of type [T] from [table], selecting a single [column]. Fails if no row is found.
 */
inline fun <reified T : Any> SQLiteDatabase.getOne(                table: String,
                                                                  column: String,
                                                          noinline joins: JoinBuilder.()->Unit = {},
                                                          noinline where: WhereBuilder.()->Unit = {},
                                                                 groupBy: String = "",
                                                                  having: String = "",
                                                                 orderBy: String = "",
                                                                   limit: Int? = 1,
                                                                  offset: Int? = null
): T = getOne(
    table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset
)


/**
 * Gets one [T] from [table] with explicit type. Throws if no result.
 */
fun <T : Any> SQLiteDatabase.getOne(                        clazz: KClass<T>,
                                                            table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                            joins: JoinBuilder.()->Unit = {},
                                                            where: WhereBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = 1,
                                                           offset: Int? = null
): T = getCursor(
    table, columns, joins, where, groupBy, having, orderBy, limit, offset
).use { cursor ->
    if (!cursor.moveToFirst()) error("No result for getOne($clazz)")
    cursor.toEntity(clazz)
}


/**
 * Gets one [T] from [table], single column + explicit type. Throws if no result.
 */
fun <T : Any> SQLiteDatabase.getOne(                               clazz: KClass<T>,
                                                                   table: String,
                                                                  column: String,
                                                                   joins: JoinBuilder.()->Unit = {},
                                                                   where: WhereBuilder.()->Unit = {},
                                                                 groupBy: String = "",
                                                                  having: String = "",
                                                                 orderBy: String = "",
                                                                   limit: Int? = 1,
                                                                  offset: Int? = null
): T = getOne(
    clazz, table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset
)










/**
 * Queries [table] for a single result of type [T] using [columns]. Returns null if no result found.
 */
inline fun <reified T : Any> SQLiteDatabase.getOneOrNull(   table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                   noinline joins: JoinBuilder.()->Unit = {},
                                                   noinline where: WhereBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = 1,
                                                           offset: Int? = null,
                                                        customEnd: String = ""
): T? = getCursor(
    table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(T::class)
}


/**
 * Queries [table] for a single result of type [T] using one [column]. Returns null if no result found.
 */
inline fun <reified T : Any> SQLiteDatabase.getOneOrNull(          table: String,
                                                                  column: String,
                                                          noinline joins: JoinBuilder.()->Unit = {},
                                                          noinline where: WhereBuilder.()->Unit = {},
                                                                 groupBy: String = "",
                                                                  having: String = "",
                                                                 orderBy: String = "",
                                                                   limit: Int? = 1,
                                                                  offset: Int? = null,
                                                               customEnd: String = ""
): T? = getOneOrNull(
    table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset, customEnd
)


/**
 * Queries [table] for a single result of [clazz] using [columns]. Returns null if no result found.
 */
fun <T : Any> SQLiteDatabase.getOneOrNull(                  clazz: KClass<T>,
                                                            table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                            joins: JoinBuilder.()->Unit = {},
                                                            where: WhereBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = 1,
                                                           offset: Int? = null,
                                                        customEnd: String = ""
): T? = getCursor(
    table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(clazz)
}


/**
 * Queries [table] for a single result of [clazz] using one [column]. Returns null if no result found.
 */
fun <T : Any> SQLiteDatabase.getOneOrNull(                         clazz: KClass<T>,
                                                                   table: String,
                                                                  column: String,
                                                                   joins: JoinBuilder.()->Unit = {},
                                                                   where: WhereBuilder.()->Unit = {},
                                                                 groupBy: String = "",
                                                                  having: String = "",
                                                                 orderBy: String = "",
                                                                   limit: Int? = 1,
                                                                  offset: Int? = null,
                                                               customEnd: String = ""
): T? = getOneOrNull(
    clazz, table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset, customEnd
)




