package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.DbTAG
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.WhereBuilder
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

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
 * Inserts the given entity into the specified database table.
 * Converts the entity into ContentValues and performs the SQLite insert operation.
 *
 * @param tableName The name of the table to insert into.
 * @param entity The entity object to insert.
 * @return The row ID of the newly inserted row, or -1 if an error occurred.
 */
inline fun <reified T : Any> SQLiteDatabase.insertRow(                         tableName: String,
                                                                                  entity: T
): Long {
    val cv = toContentValues(entity)
    if (cv.size() == 0) return -1 //\/\/\/\/\/\
    return insert(tableName, null, cv)
}


/**
 * Inserts multiple entities into the specified database table.
 * Converts each entity into ContentValues and performs the SQLite insert operation for each.
 *
 * @param tableName The name of the table to insert into.
 * @param entities The list of entity objects to insert.
 * @return The number of rows successfully inserted.
 */
inline fun <reified T : Any> SQLiteDatabase.insertRows(                        tableName: String,
                                                                                entities: List<T>
): Int {
    var count = 0
    entities.forEach { entity ->
        val rowId = insertRow(tableName, entity)
        if (rowId != -1L) count++
    }
    return count
}




/**
 * Updates the row with the specified [id] in the given table with the values from [entity].
 * Converts the entity into ContentValues and performs the SQLite update operation.
 *
 * @param tableName The name of the table to update.
 * @param id The primary key ID of the row to update.
 * @param entity The entity object with updated data.
 * @return The number of rows affected.
 */
inline fun <reified T : Any> SQLiteDatabase.updateRowById(                            id: Int,
                                                                               tableName: String,
                                                                                  entity: T
): Int {
    val cv = toContentValues(entity)
    return update(tableName, cv, ID+"=?", arrayOf(id.toString()))
}


inline fun <reified T : Any> SQLiteDatabase.updateRow(             tableName: String,
                                                                      entity: T,
                                                                       where: WhereBuilder.()->Unit
): Int {
    val cv = toContentValues(entity)
    val whereBuilder = WhereBuilder().apply(where)
    val whereClause = whereBuilder.clauses.joinToString(" ")
    val whereArgs = whereBuilder.args.toTypedArray()
    
    val affected = update(tableName, cv, whereClause, whereArgs)
    
    if (affected > 1) {
        // region LOG
        Log.w(DbTAG, "updateRow: $affected rows updated in '$tableName'. You may want to set more specific conditions if you want to update a single row.")
        // endregion
    }
    return affected
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
    
    // Handle list fields (must be at the end of the constructor)
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