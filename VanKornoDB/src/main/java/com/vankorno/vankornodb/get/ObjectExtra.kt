/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase
import com.vankorno.vankornodb.misc.data.SharedCol.cRowId
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.flipped
import com.vankorno.vankornodb.misc.limit
import com.vankorno.vankornodb.misc.orderBy
import com.vankorno.vankornodb.misc.suppressObjGetterErrorLog

/**
 * Gets the first row as an object of an entity class using WhereDsl.
 * Returns null if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getFirstObj(                  tableInfo: TableInfoBase<out T>,
                                                                      where: WhereDsl.()->Unit = {},
): T? = getObjects(tableInfo, orderBy(cRowId), limit(1), where)
    .firstOrNull()


/**
 * Gets the first row as an object of an entity class using WhereDsl.
 * Returns [default] and logs error if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getFirstObj(                  tableInfo: TableInfoBase<out T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
): T = getFirstObj(tableInfo, where) ?: run {
    // region LOG
    if (!suppressObjGetterErrorLog)
        eLog("getFirstObj(): No fitting rows exist in ${tableInfo.name}, returning default")
    // endregion
    default
}



/**
 * Gets the last row as an object of an entity class using WhereDsl.
 * Returns null if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getLastObj(                   tableInfo: TableInfoBase<out T>,
                                                                      where: WhereDsl.()->Unit = {},
): T? = getObjects(tableInfo, orderBy(cRowId.flipped()), limit(1), where)
    .firstOrNull()


/**
 * Gets the last row as an object of an entity class using WhereDsl.
 * Returns [default] and logs error if no result found.
 */
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
): T? = getObjects(tableInfo, { random() }, limit(1), where)
    .firstOrNull()


/**
 * Retrieves a single random object (row) from the specified table and maps it to an entity class.
 *
 * @param T The type of the object to retrieve. Must be a Kotlin class representing the table structure.
 * @param tableInfo Contains table name and a schema bundle of any entity type.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random object of type [T] from the table, or [default] if no rows match.
 */
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


// TODO getRandomObjects maybe


