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
fun <T : CurrEntity> SQLiteDatabase.addWithNewIDsAndPositions(        tableInfo: TableInfoNormal<T>,
                                                                       entities: List<T>,
                                                               setIdAndPosition: (T, Long, Long)->T,
): List<List<Long>> {
    val newIDs = getNextIDs(tableInfo.name, entities.size)
    val newPositions = getNextPositions(tableInfo.name, entities.size)
    
    entities.forEachIndexed { idx, entity ->
        val newEntity = setIdAndPosition(entity, newIDs[idx], newPositions[idx])
        addObjWithoutIdCheck(tableInfo, newEntity)
    }
    return listOf(newIDs, newPositions)
}


/**
 * Inserts a single entity while automatically assigning a new ID and position.
 * Existing ID and position are always replaced.
 *
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param entity Entity to insert.
 * @param setIdAndPosition Returns an entity with the specified ID and position.
 */
fun <T : CurrEntity> SQLiteDatabase.addWithNewIdAndPosition(          tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
                                                               setIdAndPosition: (T, Long, Long)->T,
): Pair<Long, Long> {
    val idsPositions = addWithNewIDsAndPositions(tableInfo, listOf(entity), setIdAndPosition)
    
    val newIDs = idsPositions.getOrNull(0)
    val newPositions = idsPositions.getOrNull(1)
    
    return  if (newIDs.isNullOrEmpty() || newPositions.isNullOrEmpty())
                -1L to -1L
            else
                newIDs[0] to newPositions[0]
}



