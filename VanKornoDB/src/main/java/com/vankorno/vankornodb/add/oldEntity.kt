/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.add

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.toContentValues
import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.get.getNextIDs
import com.vankorno.vankornodb.get.getNextId
import com.vankorno.vankornodb.misc.wLog

/*
 * ## Rules for List Parameters:
 *  1. Lists must always be declared at the END of the constructor parameter list.
 * 
 *  2. List parameters must be named with the "List" suffix (e.g., `dayList`).
 *     Corresponding DB column names must match the base name (e.g., `day`)
 *     followed by a 1-based index suffix: `day1`, `day2`, ..., etc.
 * 
 *  3. Lists must be non-nullable and must have default values with a fixed number of elements
 *     (used to infer the column count for each list).
 * 
 *  4. All list elements must be of the same type, limited to a supported primitive type
 *     (Boolean, Int, Long, Float, or String).
 * 
 *  5. Nested lists or complex types (e.g., data classes) inside lists are NOT SUPPORTED.
 */


/**
 * Inserts the given entity object into the specified database table.
 * Converts the entity into ContentValues and performs the SQLite insert operation.
 * If a schema bundle of [tableInfo] doesn't have a setter - reflection-based mapping is used.
 * If object's id is less than 1, the new id is assigned automatically.
 *
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param entity The entity object to insert.
 * @return The ROW ID of the newly inserted row, or -1 if an error occurred.
 */
fun <T : NormalEntity> SQLiteDatabase.addEntity(                      tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
): Long {
    val modified =  if (entity.id < 1L)
                        tableInfo.schema.withId(entity, getNextId(tableInfo.name))
                    else
                        entity
    
    val rowId = addEntityWithoutIdCheck(tableInfo, modified)
    if (rowId == -1L) {
        // region LOG
            wLog("addEntity() FAILED to insert entity: $modified")
        // endregion
    }
    return rowId
}


/**
 * Inserts the given entity object into the specified database table without checking or modifying its ID.
 *
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param entity Entity object to insert.
 * @return The ROW ID of the inserted row, or -1 if an error occurred.
 */
fun <T : NormalEntity> SQLiteDatabase.addEntityWithoutIdCheck(        tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
): Long {
    val cv = toContentValues(entity, tableInfo.schema)
    if (cv.size() == 0) return -1L //\/\/\/\/\/\
    return insert(tableInfo.name, null, cv)
}


/**
 * Inserts multiple entity objects into the specified database table without checking or modifying their IDs.
 *
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param entities Entity objects to insert.
 * @return A list of ROW IDs of the inserted rows, with -1 indicating insertion failures.
 */
fun <T : NormalEntity> SQLiteDatabase.addEntitiesWithoutIdCheck(      tableInfo: TableInfoNormal<T>,
                                                                       entities: List<T>,
): List<Long> = entities.map {
    addEntityWithoutIdCheck(tableInfo, it)
}



/**
 * Inserts multiple entity objects into the specified database table.
 * Converts each entity into ContentValues and performs the SQLite insert operation for each.
 * If a schema bundle of [tableInfo] doesn't have a setter - reflection-based mapping is used.
 * If entity's id is less than 1, the new id is assigned automatically.
 * 
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param entities The list of entity objects to insert.
 * @return The list of ROW IDs.
 */
fun <T : NormalEntity> SQLiteDatabase.addEntities(                    tableInfo: TableInfoNormal<T>,
                                                                       entities: List<T>,
): List<Long> {
    val newIDs = getNextIDs(
        table = tableInfo.name,
        count = entities.count { it.id < 1L },
        reservedIDs = entities.filter { it.id > 0L }.map { it.id }.toSet()
    ).iterator()
    
    val rowIDs = mutableListOf<Long>()
    
    for (entity in entities) {
        val modifiedEntity = if (entity.id < 1L)
            tableInfo.schema.withId(entity, newIDs.next())
        else
            entity
        
        val rowId = addEntityWithoutIdCheck(tableInfo, modifiedEntity)
        
        rowIDs.add(rowId)
        
        if (rowId == -1L)
            // region LOG
                wLog("addEntities() FAILED to insert entity: $modifiedEntity")
            // endregion
    }
    return rowIDs
}


/**
 * Inserts multiple entity objects into the specified database table.
 * Converts each entity into ContentValues and performs the SQLite insert operation for each.
 * If a schema bundle of [tableInfo] doesn't have a setter - reflection-based mapping is used.
 * If entity's id is less than 1, the new id is assigned automatically.
 * 
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param entities Entity objects to insert as vararg.
 * @return The list of ROW IDs.
 */
fun <T : NormalEntity> SQLiteDatabase.addEntities(                    tableInfo: TableInfoNormal<T>,
                                                                vararg entities: T,
): List<Long> = addEntities(tableInfo, entities.toList())









