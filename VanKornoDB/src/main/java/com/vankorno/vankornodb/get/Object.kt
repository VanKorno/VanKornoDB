// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.BaseOrmBundle


/**
 * Gets one db table row as an object of [clazz] using using WhereDsl. Returns null if no result found.
 */
fun <T : BaseEntity> SQLiteDatabase.getObj(                           table: String,
                                                                  ormBundle: BaseOrmBundle<T>,
                                                                      where: WhereDsl.()->Unit = {},
): T? = getObjPro(table, ormBundle) { this.where = where }



fun <T : BaseEntity> SQLiteDatabase.getObj(                           table: String,
                                                                  ormBundle: BaseOrmBundle<T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
): T = getObj(table, ormBundle, where) ?: run {
    // region LOG
        Log.e(DbTAG, "getObj(): The requested row doesn't exist in $table, returning default")
    // endregion
    default
}










