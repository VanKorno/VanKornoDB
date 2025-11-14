package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.core.WhereBuilder
import kotlin.reflect.KClass


/** 
 * Retrieves a list of entities of type [T] mapped from the specified columns. 
 * Supports joins, filtering, grouping, sorting, pagination, and optional post-mapping. 
 */
inline fun <reified T : Any> SQLiteDatabase.getObjects(           table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T)->T = {it},
): List<T> = getCursor(
    table, "*", joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(mapAfter(cursor.toEntity(T::class)))
            } while (cursor.moveToNext())
        }
    }
}





/** 
 * Retrieves a list of objects of the specified [clazz] mapped from the given columns. 
 * Similar to the reified version but uses explicit KClass parameter.
 */
fun <T : Any> SQLiteDatabase.getObjects(                          clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                               mapAfter: (T)->T = {it},
): List<T> = getCursor(
    table, "*", joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(mapAfter(cursor.toEntity(clazz)))
            } while (cursor.moveToNext())
        }
    }
}


/** 
 * Retrieves a map of objects of type [T] from the specified table, 
 * using the `id` column (Int) as the key. 
 * Supports joins, filtering, grouping, sorting, pagination, and optional post-mapping. 
 */
inline fun <reified T : Any> SQLiteDatabase.getObjMap(            table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T)->T = {it},
): Map<Int, T> = getCursor(
    table, "*", joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    buildMap {
        if (cursor.moveToFirst()) {
            val idColIdx = cursor.getColumnIndexOrThrow("id")
            do {
                val entity = mapAfter(cursor.toEntity(T::class))
                put(cursor.getInt(idColIdx), entity)
            } while (cursor.moveToNext())
        }
    }
}


/** 
 * Retrieves a map of objects of the specified [clazz] from the given table, 
 * using the `id` column (Int) as the key. 
 * Similar to the reified version but uses explicit KClass parameter. 
 */
fun <T : Any> SQLiteDatabase.getObjMap(                           clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                               mapAfter: (T)->T = {it},
): Map<Int, T> = getCursor(
    table, "*", joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    buildMap {
        if (cursor.moveToFirst()) {
            val idColIdx = cursor.getColumnIndexOrThrow("id")
            do {
                val entity = mapAfter(cursor.toEntity(clazz))
                put(cursor.getInt(idColIdx), entity)
            } while (cursor.moveToNext())
        }
    }
}







