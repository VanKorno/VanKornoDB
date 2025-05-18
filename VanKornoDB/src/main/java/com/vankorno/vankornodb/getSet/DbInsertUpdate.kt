package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.ID
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties


inline fun <reified T : Any> SQLiteDatabase.insertInto(      table: String,
                                                            entity: T,
                                                            modify: (ContentValues)->ContentValues = { it }
): Long {
    val baseCV = toContentValues(entity)
    val finalCV = modify(baseCV)
    return this.insert(table, null, finalCV)
}


inline fun <reified T : Any> SQLiteDatabase.updateIn(    tableName: String,
                                                                id: Int,
                                                            entity: T,
                                                         customize: (ContentValues)->ContentValues = { it }
): Int {
    val cv = customize(toContentValues(entity))
    return update(tableName, cv, ID+"=?", arrayOf(id.toString()))
}



inline fun <reified T : Any> toContentValues(                                     entity: T
): ContentValues {
    val cv = ContentValues()
    
    T::class.memberProperties.forEach { prop ->
        val name = prop.name
        if (name == ID) return@forEach
        
        val value = prop.getter.call(entity)
        val returnType = prop.returnType
        
        // Skips arrays and collections
        val classifier = returnType.classifier
        if (classifier is KClass<*> && (classifier.java.isArray || Collection::class.java.isAssignableFrom(classifier.java))) {
            return@forEach
        }
        
        if (value == null) {
            cv.putNull(name)
        } else {
            when (value) {
                is String       -> cv.put(name, value)
                is Int          -> cv.put(name, value)
                is Long         -> cv.put(name, value)
                is Float        -> cv.put(name, value)
                is Double       -> cv.put(name, value)
                is Short        -> cv.put(name, value.toInt())
                is Boolean      -> cv.put(name, if (value) 1 else 0)
                is ByteArray    -> cv.put(name, value)
                else            -> cv.put(name, value.toString()) // fallback
            }
        }
    }
    return cv
}