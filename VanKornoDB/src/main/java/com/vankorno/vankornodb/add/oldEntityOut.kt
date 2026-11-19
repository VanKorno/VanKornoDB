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
private fun <T : NormalEntity> SQLiteDatabase.addEntityOut(       tableInfo: TableInfoNormal<out T>,
                                                                     entity: T,
): Long {
    val cv = toContentValues(entity, tableInfo.schema)
    if (cv.size() == 0) return -1 //\/\/\/\/\/\
    return insert(tableInfo.name, null, cv)
}

/**
 *  For migration / polymorphic use
*/
internal fun <T : NormalEntity> SQLiteDatabase.addEntitiesOut(    tableInfo: TableInfoNormal<out T>,
                                                                   entities: List<T>,
): Int {
    var count = 0
    
    for (entity in entities) {
        val rowId = addEntityOut(tableInfo, entity)
        if (rowId == -1L) {
            // region LOG
                wLog("addEntitiesOut() FAILED to insert entity: $entity")
            // endregion
        } else {
            count++
        }
    }
    return count
}



