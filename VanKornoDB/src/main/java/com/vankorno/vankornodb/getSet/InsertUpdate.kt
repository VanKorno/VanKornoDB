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
import kotlin.reflect.full.primaryConstructor

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
fun <T : Any> SQLiteDatabase.insertObj(                                            table: String,
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
fun <T : Any> SQLiteDatabase.insertObjects(                                        table: String,
                                                                                 objects: List<T>,
): Int {
    var count = 0
    for (obj in objects) {
        val rowId = insertObj(table, obj)
        if (rowId != -1L) count++
    }
    return count
}


// TODO Check if needed

inline fun <reified T : Any> SQLiteDatabase.insertObjectsWithAutoIds(              table: String,
                                                                                 objects: List<T>,
): Int {
    if (objects.isEmpty()) return 0

    val kClass = T::class
    val hasId = kClass.memberProperties.any { it.name == ID }
    val idProp = kClass.memberProperties.firstOrNull { it.name == ID }
    val ctor = kClass.primaryConstructor!!

    var nextId = getLastId(table) + 1
    var count = 0

    for (obj in objects) {
        val currentId = idProp?.getter?.call(obj) as? Int ?: -1
        val modified = if (hasId && currentId < 1) {
            val args = ctor.parameters.associateWith { param ->
                if (param.name == ID) nextId++
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
}












/**
 * Updates the row with the specified [id] in the given table with the values from [obj].
 * Converts the entity into ContentValues and performs the SQLite update operation.
 *
 * @param table The name of the table to update.
 * @param id The primary key ID of the row to update.
 * @param obj The entity object with updated data.
 * @return The number of rows affected.
 */
fun <T : Any> SQLiteDatabase.updateObjById(                                           id: Int,
                                                                                   table: String,
                                                                                     obj: T,
): Int {
    val cv = toContentValues(obj)
    return update(table, cv, ID+"=?", arrayOf(id.toString()))
}


inline fun <T : Any> SQLiteDatabase.updateObj(                         table: String,
                                                                         obj: T,
                                                                       where: WhereBuilder.()->Unit,
): Int {
    val cv = toContentValues(obj)
    val whereBuilder = WhereBuilder().apply(where)
    val whereClause = whereBuilder.clauses.joinToString(" ")
    val whereArgs = whereBuilder.args.toTypedArray()
    
    val affected = update(table, cv, whereClause, whereArgs)
    if (affected > 1) {
        // region LOG
        Log.w(DbTAG, "updateRow: $affected rows updated in '$table'. You may want to set more specific conditions if you want to update a single row.")
        // endregion
    }
    return affected
}





@PublishedApi
internal fun Any.hasIdField(): Boolean = this::class.memberProperties.any { it.name == "id" }

@PublishedApi
internal fun Any.getId(): Int = this::class.memberProperties
    .firstOrNull { it.name == "id" }
    ?.getter?.call(this) as? Int ?: -1

@PublishedApi
internal fun <T : Any> T.withId(                                                   newId: Int
): T {
    val kClass = this::class
    val constructor = kClass.primaryConstructor!!
    val args = constructor.parameters.associateWith { param ->
        if (param.name == "id") newId
        else kClass.memberProperties.first { it.name == param.name }.getter.call(this)
    }
    return constructor.callBy(args)
}



/**
 * Converts the given entity into ContentValues for SQLite operations.
 * Handles primitive fields first, then processes list properties ending with "List"
 * by mapping each list element to sequentially numbered columns (e.g. day1, day2, ...).
 *
 * @param obj The data object to convert.
 * @param clazz The KClass of the entity, defaults to the runtime class of the entity.
 * @return ContentValues representing the entity suitable for database insertion/update.
 * @throws IllegalArgumentException if list element types are unsupported.
 */
fun <T : Any> toContentValues(                                   obj: T,
                                                               clazz: KClass<out T> = obj::class,
): ContentValues {
    val cv = ContentValues()
    
    for (prop in clazz.memberProperties) {
        val name = prop.name
        val value = prop.getter.call(obj)
        val returnType = prop.returnType
        val classifier = returnType.classifier
        
        if (classifier is KClass<*>) {
            val isCollection = Collection::class.java.isAssignableFrom(classifier.java)
            val isArrButNotByteArr = classifier.java.isArray && classifier != ByteArray::class
            if (isCollection || isArrButNotByteArr) continue //\/\/\
        }
        
        if (value == null) {
            cv.putNull(name)
        } else {
            when (value) {
                is String     -> cv.put(name, value)
                is Int        -> cv.put(name, value)
                is Long       -> cv.put(name, value)
                is Float      -> cv.put(name, value)
                is Double     -> cv.put(name, value)
                is Short      -> cv.put(name, value.toInt())
                is Boolean    -> cv.put(name, if (value) 1 else 0)
                is ByteArray  -> cv.put(name, value)
                else          -> cv.put(name, value.toString())
            }
        }
    }
    
    // Handle list fields (must be at the end of the constructor)
    for (prop in clazz.memberProperties) {
        val name = prop.name
        if (!name.endsWith("List")) continue //\/\/\
        
        val value = prop.getter.call(obj)
        if (value !is List<*>) continue //\/\/\
        if (value.isEmpty()) continue //\/\/\
        
        val elementType = prop.returnType.arguments.first().type?.classifier as? KClass<*>
        requireNotNull(elementType) { "Cannot determine list element type for $name" }
        
        val putValue: (colName: String, Any?) -> Unit = when (elementType) {
            String::class     -> { colName, v -> cv.put(colName, v as String) }
            Int::class        -> { colName, v -> cv.put(colName, v as Int) }
            Long::class       -> { colName, v -> cv.put(colName, v as Long) }
            Float::class      -> { colName, v -> cv.put(colName, v as Float) }
            Double::class     -> { colName, v -> cv.put(colName, v as Double) }
            Short::class      -> { colName, v -> cv.put(colName, (v as Short).toInt()) }
            Boolean::class    -> { colName, v -> cv.put(colName, if (v as Boolean) 1 else 0) }
            ByteArray::class  -> { colName, v -> cv.put(colName, v as ByteArray) }
            else              -> error("Unsupported list element type: $elementType for $name")
        }
        val baseName = name.removeSuffix("List")
        
        for (idx in value.indices) {
            val elem = value[idx]
            val columnName = baseName + (idx + 1)
            
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