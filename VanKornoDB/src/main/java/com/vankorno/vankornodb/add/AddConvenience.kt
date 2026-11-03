/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.add

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.get.getNextIDs
import com.vankorno.vankornodb.get.getNextPositions

/**
 * Inserts multiple entities while automatically assigning new IDs and positions.
 * Existing IDs and positions are always replaced.
 *
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param entities Entities to insert.
 * @param setIdAndPosition Returns an entity with the specified ID and position.
 */
fun <T : CurrEntity> SQLiteDatabase.addWithNewIdAndPosition(          tableInfo: TableInfoNormal<T>,
                                                                       entities: List<T>,
                                                               setIdAndPosition: (T, Long, Long)->T,
) {
    val newIDs = getNextIDs(tableInfo.name, entities.size)
    val newPositions = getNextPositions(tableInfo.name, entities.size)

    entities.forEachIndexed { idx, entity ->
        val newEntity = setIdAndPosition(entity, newIDs[idx], newPositions[idx])
        addObjWithoutIdCheck(tableInfo, newEntity)
    }
}


/**
 *  Single-object overload of [addWithNewIdAndPosition]
 */
fun <T : CurrEntity> SQLiteDatabase.addWithNewIdAndPosition(          tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
                                                               setIdAndPosition: (T, Long, Long)->T,
) {
    addWithNewIdAndPosition(
        tableInfo = tableInfo,
        entities = listOf(entity),
        setIdAndPosition = setIdAndPosition,
    )
}



