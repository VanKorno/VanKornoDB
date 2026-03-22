/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.add

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.toContentValues
import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.get.getLastId
import com.vankorno.vankornodb.misc.wLog
import kotlin.math.max

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
 * Inserts the given object into the specified database table.
 * Converts the entity into ContentValues and performs the SQLite insert operation.
 * If a schema bundle of [tableInfo] doesn't have a setter - reflection-based mapping is used.
 * If object's id is less than 1, the new id is assigned automatically.
 *
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param obj The entity object to insert.
 * @return The ID of the newly inserted row, or -1 if an error occurred.
 */
fun <T : NormalEntity> SQLiteDatabase.addObj(                         tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
): Long {
    val modified =  if (obj.id < 1L)
                        tableInfo.schema.withId(obj, getLastId(tableInfo.name) + 1L)
                    else
                        obj
    
    val rowId = addObjWithoutIdCheck(tableInfo, modified)
    if (rowId == -1L) {
        // region LOG
            wLog("addObj() FAILED to insert object: $modified")
        // endregion
        return -1L
    }
    return modified.id
}



private fun <T : NormalEntity> SQLiteDatabase.addObjWithoutIdCheck(
                                                                      tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
): Long {
    val cv = toContentValues(obj, tableInfo.schema)
    if (cv.size() == 0) return -1L //\/\/\/\/\/\
    return insert(tableInfo.name, null, cv)
}


/**
 * Inserts multiple objects into the specified database table.
 * Converts each entity into ContentValues and performs the SQLite insert operation for each.
 * If a schema bundle of [tableInfo] doesn't have a setter - reflection-based mapping is used.
 * If object's id is less than 1, the new id is assigned automatically.
 * 
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param objects The list of entity objects to insert.
 * @return The number of rows successfully inserted.
 */
fun <T : NormalEntity> SQLiteDatabase.addObjects(                     tableInfo: TableInfoNormal<T>,
                                                                        objects: List<T>,
): Int {
    val maxObjId = objects.maxOfOrNull { it.id } ?: 0L
    var count = 0
    var newId = -1L
    
    for (obj in objects) {
        val needsNewId = obj.id < 1L
        
        if (needsNewId && newId == -1L)
            newId = max(maxObjId, getLastId(tableInfo.name) + 1L)
        
        val modifiedObj =   if (needsNewId)
                                tableInfo.schema.withId(obj, newId)
                            else
                                obj
        
        val rowId = addObjWithoutIdCheck(tableInfo, modifiedObj)
        
        if (rowId == -1L) {
            // region LOG
                wLog("addObjects() FAILED to insert object: $modifiedObj")
            // endregion
        } else {
            count++
            if (needsNewId)
                newId++
        }
    }
    return count
}









