package com.vankorno.vankornodb.add.internal
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.content.ContentValues
import com.vankorno.vankornodb.api.DbEntity
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@PublishedApi
internal fun DbEntity.hasIdField(): Boolean = this::class.memberProperties.any { it.name == "id" }

@PublishedApi
internal fun DbEntity.getId(): Int = this::class.memberProperties
    .firstOrNull { it.name == "id" }
    ?.getter?.call(this) as? Int ?: -1

@PublishedApi
internal fun <T : DbEntity> T.withId(                                              newId: Int
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
@PublishedApi
internal fun <T : DbEntity> toContentValues(                        obj: T,
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
