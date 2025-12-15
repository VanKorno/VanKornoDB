package com.vankorno.vankornodb.add
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.add.internal.getId
import com.vankorno.vankornodb.add.internal.hasIdField
import com.vankorno.vankornodb.add.internal.toContentValues
import com.vankorno.vankornodb.add.internal.withId
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.get.getLastId

// TODO Maybe: Upsert-like function — insert or update depending on whether the row exists (SQLite supports INSERT OR REPLACE, INSERT ON CONFLICT, etc.)

/**
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
 *
 * @param table The name of the table to insert into.
 * @param obj The entity object to insert.
 * @return The row ID of the newly inserted row, or -1 if an error occurred.
 */
fun <T : DbEntity> SQLiteDatabase.addObj(                                          table: String,
                                                                                     obj: T,
): Long {
    val modifiedEntity = if (obj.hasIdField() && obj.getId() < 1) {
        obj.withId(getLastId(table) + 1)
    } else obj

    val cv = toContentValues(modifiedEntity)
    if (cv.size() == 0) return -1 //\/\/\/\/\/\
    return insert(table, null, cv)
}

/**
 * Inserts multiple objects into the specified database table.
 * Converts each entity into ContentValues and performs the SQLite insert operation for each.
 *
 * @param table The name of the table to insert into.
 * @param objects The list of entity objects to insert.
 * @return The number of rows successfully inserted.
 */
fun <T : DbEntity> SQLiteDatabase.addObjects(                                      table: String,
                                                                                 objects: List<T>,
): Int {
    var count = 0
    for (obj in objects) {
        val rowId = addObj(table, obj)
        if (rowId != -1L) count++
    }
    return count
}


// TODO Check if needed
/*
inline fun <reified T : DbEntity> SQLiteDatabase.insertObjectsWithAutoIds(         table: String,
                                                                                 objects: List<T>,
): Int {
    if (objects.isEmpty()) return 0

    val kClass = T::class
    val hasId = kClass.memberProperties.any { it.name == _ID }
    val idProp = kClass.memberProperties.firstOrNull { it.name == _ID }
    val ctor = kClass.primaryConstructor!!

    var nextId = getLastId(table) + 1
    var count = 0

    for (obj in objects) {
        val currentId = idProp?.getter?.call(obj) as? Int ?: -1
        val modified = if (hasId && currentId < 1) {
            val args = ctor.parameters.associateWith { param ->
                if (param.name == _ID) nextId++
                else kClass.memberProperties.first { it.name == param.name }.getter.call(obj)
            }
            ctor.callBy(args)
        } else obj

        val cv = toContentValues(modified)
        if (cv.size() != 0 && insert(table, null, cv) != -1L) {
            count++
        }
    }
    return count
}*/








