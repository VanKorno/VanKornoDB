package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.ID
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

private const val TAG = "DbInsertUpdate"

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
 * Inserts the given entity into the specified database table.
 * Converts the entity into ContentValues, optionally modifies them via [modify] lambda,
 * then performs the SQLite insert operation.
 *
 * @param table The name of the table to insert into.
 * @param entity The entity object to insert.
 * @param modify Optional lambda to customize ContentValues before insertion.
 * @return The row ID of the newly inserted row, or -1 if an error occurred.
 */
inline fun <reified T : Any> SQLiteDatabase.insertEntity(    table: String,
                                                            entity: T,
                                                            modify: (ContentValues)->ContentValues = { it }
): Long {
    val baseCV = toContentValues(entity)
    val finalCV = modify(baseCV)
    if (finalCV.size() == 0) return -1
    return this.insert(table, null, finalCV)
}

/**
 * Updates the row with the specified [id] in the given table with the values from [entity].
 * Converts the entity into ContentValues, optionally customizes them via [customize] lambda,
 * then performs the SQLite update operation.
 *
 * @param tableName The name of the table to update.
 * @param id The primary key ID of the row to update.
 * @param entity The entity object with updated data.
 * @param customize Optional lambda to customize ContentValues before update.
 * @return The number of rows affected.
 */
inline fun <reified T : Any> SQLiteDatabase.updateEntity( tableName: String,
                                                                 id: Int,
                                                             entity: T,
                                                          customize: (ContentValues)->ContentValues = { it }
): Int {
    val cv = customize(toContentValues(entity))
    return update(tableName, cv, ID+"=?", arrayOf(id.toString()))
}


/**
 * Converts the given entity into ContentValues for SQLite operations.
 * Handles primitive fields first, then processes list properties ending with "List"
 * by mapping each list element to sequentially numbered columns (e.g. day1, day2, ...).
 *
 * @param entity The data object to convert.
 * @param clazz The KClass of the entity, defaults to the runtime class of the entity.
 * @return ContentValues representing the entity suitable for database insertion/update.
 * @throws IllegalArgumentException if list element types are unsupported.
 */
fun <T : Any> toContentValues(                                 entity: T,
                                                                clazz: KClass<out T> = entity::class
): ContentValues {
    val cv = ContentValues()
    
    clazz.memberProperties.forEach { prop ->
        val name = prop.name
        if (name == ID) return@forEach
        
        val value = prop.getter.call(entity)
        val returnType = prop.returnType
        val classifier = returnType.classifier
        
        if (classifier is KClass<*>
            && (classifier.java.isArray || Collection::class.java.isAssignableFrom(classifier.java))
        ) {
            return@forEach
        }
        
        if (value == null) {
            cv.putNull(name)
        } else {
            when (value) {
                is String     -> cv.put(name, value)
                is Int        -> cv.put(name, value)
                is Long       -> cv.put(name, value)
                is Float      -> cv.put(name, value)
                is Double     -> cv.put(name, value.toFloat())
                is Short      -> cv.put(name, value.toInt())
                is Boolean    -> cv.put(name, if (value) 1 else 0)
                is ByteArray  -> cv.put(name, value)
                else          -> cv.put(name, value.toString())
            }
        }
    }
    
    // 2. Handle list fields (must be at end of constructor)
    clazz.memberProperties.forEach { prop ->
        val name = prop.name
        if (!name.endsWith("List")) return@forEach
        
        val value = prop.getter.call(entity)
        if (value !is List<*>) return@forEach
        if (value.isEmpty()) return@forEach
        
        val elementType = prop.returnType.arguments.first().type?.classifier as? KClass<*>
        requireNotNull(elementType) { "Cannot determine list element type for $name" }
        
        val putValue: (colName: String, Any?) -> Unit = when (elementType) {
            String::class     -> { colName, v -> cv.put(colName, v as String) }
            Int::class        -> { colName, v -> cv.put(colName, v as Int) }
            Long::class       -> { colName, v -> cv.put(colName, v as Long) }
            Float::class      -> { colName, v -> cv.put(colName, v as Float) }
            Double::class     -> { colName, v -> cv.put(colName, (v as Double).toFloat()) }
            Short::class      -> { colName, v -> cv.put(colName, (v as Short).toInt()) }
            Boolean::class    -> { colName, v -> cv.put(colName, if (v as Boolean) 1 else 0) }
            ByteArray::class  -> { colName, v -> cv.put(colName, v as ByteArray) }
            else              -> error("Unsupported list element type: $elementType for $name")
        }
        val baseName = name.removeSuffix("List")
        
        value.forEachIndexed { index, elem ->
            val columnName = baseName + (index + 1)
            
            if (elem == null)
                cv.putNull(columnName)
            else
                putValue(columnName, elem)
        }
    }
    // region disabled LOG
        //Log.v(TAG, "toContentValues() result: $cv")
    // endregion
    return cv
}