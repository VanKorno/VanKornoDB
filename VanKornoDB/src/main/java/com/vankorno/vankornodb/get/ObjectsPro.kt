// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.EntitySpec
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.mapper.toEntity

/** 
 * Retrieves a list of entities of type [T] mapped from the specified columns.
 */
inline fun <reified T : BaseEntity> SQLiteDatabase.getObjectsPro(           table: String,
                                                                     noinline dsl: FullDsl.()->Unit,
): List<T> = getCursorPro(table) {
    applyDsl(dsl)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(cursor.toEntity(T::class))
            } while (cursor.moveToNext())
        }
    }
}



/** 
 * Retrieves a list of objects mapped from the given columns. 
 * Similar to the reified version but uses explicit KClass parameter.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjectsPro(                          table: String,
                                                                             spec: EntitySpec<T>,
                                                                              dsl: FullDsl.()->Unit,
): List<T> = getCursorPro(table) {
    applyDsl(dsl)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(cursor.toEntity(spec))
            } while (cursor.moveToNext())
        }
    }
}





/** 
 * Retrieves a map of objects of type [T] from the specified table.
 */
inline fun <reified T : BaseEntity> SQLiteDatabase.getObjMapPro(            table: String,
                                                                     noinline dsl: FullDsl.()->Unit,
): Map<Int, T> = getCursorPro(table) {
    applyDsl(dsl)
}.use { cursor ->
    buildMap {
        if (cursor.moveToFirst()) {
            val idColIdx = cursor.getColumnIndexOrThrow("id")
            do {
                val entity = cursor.toEntity(T::class)
                put(cursor.getInt(idColIdx), entity)
            } while (cursor.moveToNext())
        }
    }
}



/** 
 * Retrieves a map of objects from the given table.
 * Similar to the reified version but uses explicit KClass parameter. 
 */
fun <T : BaseEntity> SQLiteDatabase.getObjMapPro(                           table: String,
                                                                             spec: EntitySpec<T>,
                                                                              dsl: FullDsl.()->Unit,
): Map<Int, T> = getCursorPro(table) {
    applyDsl(dsl)
}.use { cursor ->
    buildMap {
        if (cursor.moveToFirst()) {
            val idColIdx = cursor.getColumnIndexOrThrow("id")
            do {
                val entity = cursor.toEntity(spec)
                put(cursor.getInt(idColIdx), entity)
            } while (cursor.moveToNext())
        }
    }
}












