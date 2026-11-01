/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.add

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.get.getLastId
import com.vankorno.vankornodb.get.getLastPosition
import kotlin.math.max

/**
 * Defines how existing entity IDs and positions are handled during insertion.
 */
enum class InsertMode { New, Copy, Move }



/**
 * Inserts entities while automatically assigning IDs and positions when necessary.
 * 
 * Unlike [addObj], this function also generates new IDs when copying or moving
 * entities that already have valid IDs.
 * 
 * This fun optimizes the whole thing performance-wise and handles New-Copy-Move logic.
 *
 * Existing IDs and positions are preserved unless they are invalid or the insertion
 * mode is [InsertMode.Copy] or [InsertMode.Move].
 * 
 * In [InsertMode.Copy] and [InsertMode.Move], both IDs and positions are automatically
 * assigned, regardless of their existing values.
 *
 * @param tableInfo Target table name and schema.
 * @param entities Entities to insert.
 * @param insertMode Determines how existing IDs and positions are handled.
 * @param getPosition Retrieves an entity's position.
 * @param setIdAndPosition Returns an entity with the specified ID and position.
 */
fun <T : CurrEntity> SQLiteDatabase.addWithAutoIdAndPosition(         tableInfo: TableInfoNormal<T>,
                                                                       entities: List<T>,
                                                                     insertMode: InsertMode,
                                                                    getPosition: (T)->Long,
                                                               setIdAndPosition: (T, Long, Long)->T,
) {
    var nextId = getLastId(tableInfo.name) + 1
    var nextPosition = getLastPosition(tableInfo.name) + 1
    
    for (entity in entities) {
        val id = if (entity.id < 1L || insertMode == InsertMode.Copy || insertMode == InsertMode.Move)
                    nextId
                else
                    entity.id
        
        val entityPosition = getPosition(entity)
        
        val position = if (entityPosition < 1L || insertMode == InsertMode.Copy || insertMode == InsertMode.Move)
                            nextPosition
                        else
                            entityPosition
        
        val newEntity = setIdAndPosition(entity, id, position)
        
        addObjWithoutIdCheck(tableInfo, newEntity)
        
        nextId = max(nextId, id) + 1
        nextPosition = max(nextPosition, position) + 1
    }
}


/**
 * Single-object overload of [addWithAutoIdAndPosition].
 */
fun <T : CurrEntity> SQLiteDatabase.addWithAutoIdAndPosition(         tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
                                                                     insertMode: InsertMode,
                                                                    getPosition: (T)->Long,
                                                               setIdAndPosition: (T, Long, Long)->T,
) {
    addWithAutoIdAndPosition(
        tableInfo = tableInfo,
        entities = listOf(entity),
        insertMode = insertMode,
        getPosition = getPosition,
        setIdAndPosition = setIdAndPosition,
    )
}



