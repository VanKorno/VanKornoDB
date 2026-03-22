/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase

/** 
 * Retrieves a map of objects from the given table.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjects(                  tableInfo: TableInfoBase<out T>,
                                                                      where: WhereDsl.()->Unit = {},
): List<T> = getObjectsPro(tableInfo) {
    this.where = where
}





/** 
 * Retrieves a map of objects from the given table.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjMap(                    tableInfo: TableInfoBase<out T>,
                                                                      where: WhereDsl.()->Unit = {},
): Map<Int, T> = getObjMapPro(tableInfo) {
    this.where = where
}












