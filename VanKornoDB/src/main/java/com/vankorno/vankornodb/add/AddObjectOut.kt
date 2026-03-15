// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.add

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.toContentValues
import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.misc.wLog

/**
 *  For migration / polymorphic use
*/
private fun SQLiteDatabase.addObjOut(                  tableInfo: TableInfoNormal<out NormalEntity>,
                                                             obj: NormalEntity,
): Long {
    val cv = toContentValues(obj, tableInfo.schema)
    if (cv.size() == 0) return -1 //\/\/\/\/\/\
    return insert(tableInfo.name, null, cv)
}

/**
 *  For migration / polymorphic use
*/
internal fun SQLiteDatabase.addObjectsOut(             tableInfo: TableInfoNormal<out NormalEntity>,
                                                         objects: List<NormalEntity>,
): Int {
    var count = 0
    for (obj in objects) {
        val rowId = addObjOut(tableInfo, obj)
        if (rowId == -1L) {
            // region LOG
                wLog("addObjectsOut() FAILED to insert object: $obj")
            // endregion
        } else {
            count++
        }
    }
    return count
}