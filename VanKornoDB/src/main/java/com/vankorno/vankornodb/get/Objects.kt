// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.BaseSchemaBundle

/** 
 * Retrieves a map of objects from the given table.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjects(                       table: String,
                                                               schemaBundle: BaseSchemaBundle<T>,
                                                                      where: WhereDsl.()->Unit = {},
): List<T> = getObjectsPro(table, schemaBundle) {
    this.where = where
}





/** 
 * Retrieves a map of objects from the given table.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjMap(                        table: String,
                                                               schemaBundle: BaseSchemaBundle<T>,
                                                                      where: WhereDsl.()->Unit = {},
): Map<Int, T> = getObjMapPro(table, schemaBundle) {
    this.where = where
}












