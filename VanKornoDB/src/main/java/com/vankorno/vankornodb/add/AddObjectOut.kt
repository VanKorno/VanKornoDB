/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.add

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.toContentValues
import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.misc.wLog

/**
 *  For migration / polymorphic use
*/
private fun <T : NormalEntity> SQLiteDatabase.addObjOut(          tableInfo: TableInfoNormal<out T>,
                                                                        obj: T,
): Long {
    val cv = toContentValues(obj, tableInfo.schema)
    if (cv.size() == 0) return -1 //\/\/\/\/\/\
    return insert(tableInfo.name, null, cv)
}

/**
 *  For migration / polymorphic use
*/
internal fun <T : NormalEntity> SQLiteDatabase.addObjectsOut(     tableInfo: TableInfoNormal<out T>,
                                                                    objects: List<T>,
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



