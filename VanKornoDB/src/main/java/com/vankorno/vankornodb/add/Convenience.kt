/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.add

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.get.getNextIDs
import com.vankorno.vankornodb.get.getNextPositions

import com.vankorno.vankornodb.misc.data.DbIdAndPosition

/**
 * Inserts multiple entities while automatically assigning new IDs and positions.
 * Existing IDs and positions are always replaced.
 *
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param entities Entities to insert.
 * @param positionConditions In case not all row's positions are relevant.
 * 
 * @param setIdAndPosition Returns an entity with the specified ID and position.
 * Looks like this:
 *  { entity, id, position -> entity.copy(id = id, position = position) }
 * 
 * @return A list of [DbIdAndPosition] containing the assigned ID and position for each inserted entity.
 */
fun <T : CurrEntity> SQLiteDatabase.addWithNewIDsAndPositions(    tableInfo: TableInfoNormal<T>,
                                                                   entities: List<T>,
                                                         positionConditions: WhereDsl.()->Unit = {},
                                                           setIdAndPosition: (T, Long, Long)->T,
): List<DbIdAndPosition> {
    val newIDs = getNextIDs(tableInfo.name, entities.size)
    val newPositions = getNextPositions(tableInfo.name, entities.size, where = positionConditions)
    val result = ArrayList<DbIdAndPosition>(entities.size)
    
    entities.forEachIndexed { idx, entity ->
        val id = newIDs[idx]
        val pos = newPositions[idx]
        val newEntity = setIdAndPosition(entity, id, pos)
        addEntityWithoutIdCheck(tableInfo, newEntity)
        result.add(DbIdAndPosition(id, pos))
    }
    return result
}


/**
 * Inserts a single entity while automatically assigning a new ID and position.
 * Existing ID and position are always replaced.
 *
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param entity Entity to insert.
 * @param positionConditions In case not all row's positions are relevant.
 * 
 * @param setIdAndPosition Returns an entity with the specified ID and position.
 * Looks like this:
 *  { entity, id, position -> entity.copy(id = id, position = position) }
 * 
 * @return The [DbIdAndPosition] containing the assigned ID and position.
 */
fun <T : CurrEntity> SQLiteDatabase.addWithNewIdAndPosition(      tableInfo: TableInfoNormal<T>,
                                                                     entity: T,
                                                         positionConditions: WhereDsl.()->Unit = {},
                                                           setIdAndPosition: (T, Long, Long)->T,
): DbIdAndPosition = addWithNewIDsAndPositions(
    tableInfo = tableInfo,
    entities = listOf(entity),
    positionConditions = positionConditions,
    setIdAndPosition = setIdAndPosition,
).firstOrNull() ?: DbIdAndPosition()



