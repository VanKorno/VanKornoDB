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
        eLog("getObj(): The requested row doesn't exist in ${tableInfo.name}, returning default")
    // endregion
    default
}










