// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.mapper.toEntity
import kotlin.reflect.KClass


/** 
 * Retrieves a list of entities of type [T] mapped from the specified columns. 
 * Supports joins, filtering, grouping, sorting, pagination, and optional post-mapping. 
 */
inline fun <reified T : DbEntity> SQLiteDatabase.getObjects(              table: String,
                                                             noinline queryOpts: QueryOpts.()->Unit,
                                                              noinline mapAfter: (T)->T,
): List<T> = getCursorPro(table) {
    applyOpts(queryOpts)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(mapAfter(cursor.toEntity(T::class)))
            } while (cursor.moveToNext())
        }
    }
}

// TODO Descr
inline fun <reified T : DbEntity> SQLiteDatabase.getObjects(         table: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
): List<T> = getObjects(table, queryOpts) { it }




/** 
 * Retrieves a list of objects of the specified [clazz] mapped from the given columns. 
 * Similar to the reified version but uses explicit KClass parameter.
 */
fun <T : DbEntity> SQLiteDatabase.getObjects(                             clazz: KClass<T>,
                                                                          table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
                                                                       mapAfter: (T)->T,
): List<T> = getCursorPro(table) {
    applyOpts(queryOpts)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(mapAfter(cursor.toEntity(clazz)))
            } while (cursor.moveToNext())
        }
    }
}

// TODO Descr
fun <T : DbEntity> SQLiteDatabase.getObjects(                        clazz: KClass<T>,
                                                                     table: String,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): List<T> = getObjects(clazz, table, queryOpts) { it }





/** 
 * Retrieves a map of objects of type [T] from the specified table, 
 * using the `id` column (Int) as the key. 
 * Supports joins, filtering, grouping, sorting, pagination, and optional post-mapping. 
 */
inline fun <reified T : DbEntity> SQLiteDatabase.getObjMap(               table: String,
                                                             noinline queryOpts: QueryOpts.()->Unit,
                                                              noinline mapAfter: (T)->T,
): Map<Int, T> = getCursorPro(table) {
    applyOpts(queryOpts)
}.use { cursor ->
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


// TODO Descr
inline fun <reified T : DbEntity> SQLiteDatabase.getObjMap(          table: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
): Map<Int, T> = getObjMap(table, queryOpts) { it }





/** 
 * Retrieves a map of objects of the specified [clazz] from the given table, 
 * using the `id` column (Int) as the key. 
 * Similar to the reified version but uses explicit KClass parameter. 
 */
fun <T : DbEntity> SQLiteDatabase.getObjMap(                              clazz: KClass<T>,
                                                                          table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
                                                                       mapAfter: (T)->T,
): Map<Int, T> = getCursorPro(table) {
    applyOpts(queryOpts)
}.use { cursor ->
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


// TODO Descr
fun <T : DbEntity> SQLiteDatabase.getObjMap(                         clazz: KClass<T>,
                                                                     table: String,
                                                                 queryOpts: QueryOpts.()->Unit = {},
): Map<Int, T> = getObjMap(clazz, table, queryOpts) { it }




