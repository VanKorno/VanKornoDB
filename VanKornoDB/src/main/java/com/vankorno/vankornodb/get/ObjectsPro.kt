// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.toEntity
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.BaseSchemaBundle

/** 
 * Retrieves a list of objects mapped from the given columns. 
 * Similar to the reified version but uses explicit KClass parameter.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjectsPro(                       table: String,
                                                                  schemaBundle: BaseSchemaBundle<T>,
                                                                           dsl: FullDsl.()->Unit,
): List<T> = getCursorPro(table) {
    applyDsl(dsl)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(cursor.toEntity(schemaBundle))
            } while (cursor.moveToNext())
        }
    }
}





/** 
 * Retrieves a map of objects from the given table.
 * Similar to the reified version but uses explicit KClass parameter. 
 */
fun <T : BaseEntity> SQLiteDatabase.getObjMapPro(                        table: String,
                                                                  schemaBundle: BaseSchemaBundle<T>,
                                                                           dsl: FullDsl.()->Unit,
): Map<Int, T> = getCursorPro(table) {
    applyDsl(dsl)
}.use { cursor ->
    buildMap {
        if (cursor.moveToFirst()) {
            val idColIdx = cursor.getColumnIndexOrThrow("id")
            do {
                val entity = cursor.toEntity(schemaBundle)
                put(cursor.getInt(idColIdx), entity)
            } while (cursor.moveToNext())
        }
    }
}












