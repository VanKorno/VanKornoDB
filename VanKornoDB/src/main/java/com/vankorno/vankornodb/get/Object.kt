// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.suppressObjGetterErrorLog

/**
 * Gets one db table row as an object of an entity class using WhereDsl.
 * Returns null if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getObj(                       tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
): T? = getObjPro(tableInfo) { this.where = where }


/**
 * Gets one db table row as an object of an entity class using WhereDsl.
 * Returns [default] if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getObj(                       tableInfo: TableInfoBase<T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
): T = getObj(tableInfo, where) ?: run {
    // region LOG
    if (!suppressObjGetterErrorLog)
        eLog("getObj(): The requested row doesn't exist in ${tableInfo.name}, returning default")
    // endregion
    default
}



fun <T : BaseEntity> SQLiteDatabase.getLastObj(                   tableInfo: TableInfoBase<out T>,
                                                                      where: WhereDsl.()->Unit = {},
): T? = getObjPro(tableInfo) {
    this.where = where
    orderBy { flipRows() }
}

fun <T : BaseEntity> SQLiteDatabase.getLastObj(                   tableInfo: TableInfoBase<out T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
): T = getLastObj(tableInfo, where) ?: run {
    // region LOG
    if (!suppressObjGetterErrorLog)
        eLog("getLastObj(): No fitting rows exist in ${tableInfo.name}, returning default")
    // endregion
    default
}




/**
 * Retrieves a single random object (row) from the specified table and maps it to an entity class.
 *
 * @param T The type of the object to retrieve. Must be a Kotlin class representing the table structure.
 * @param tableInfo Contains table name and a schema bundle of any entity type.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random object of type [T] from the table, or null if no rows match.
 */
inline fun <reified T : BaseEntity> SQLiteDatabase.getRandomObj(
                                                                  tableInfo: TableInfoBase<out T>,
                                                             noinline where: WhereDsl.()->Unit = {},
): T? = getObjPro(tableInfo) {
    this.where = where
    orderRandomly()
}


inline fun <reified T : BaseEntity> SQLiteDatabase.getRandomObj(
                                                                  tableInfo: TableInfoBase<out T>,
                                                                    default: T,
                                                             noinline where: WhereDsl.()->Unit = {},
): T = getRandomObj(tableInfo, where) ?: run {
    // region LOG
    if (!suppressObjGetterErrorLog)
        eLog("getRandomObj(): No fitting rows exist in ${tableInfo.name}, returning default")
    // endregion
    default
}





