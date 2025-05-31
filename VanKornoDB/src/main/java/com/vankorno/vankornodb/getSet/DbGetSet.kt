package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.getBool

private const val TAG = "DbGetSet"

open class DbGetSet(val db: SQLiteDatabase) {
    
    fun setById(                                                                   value: Any,
                                                                                      id: Int,
                                                                               tableName: String,
                                                                                  column: String
    ) = set(value, tableName, column, ID, id)
    
    
    fun <T> set(                                                                   value: Any,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        val cv = ContentValues()
        when (value) {
            is String -> cv.put(column, value)
            is Int -> cv.put(column, value)
            is Boolean -> cv.put(column, if (value) 1 else 0)
            is Long -> cv.put(column, value)
            is Float -> cv.put(column, value)
            is ByteArray -> cv.put(column, value)
            else -> throw IllegalArgumentException("Unsupported value type: ${value::class}")
        }
        db.update(tableName, cv, whereClause+"=?", arrayOf(whereArg.toString()))
    }
    
    
    
    // ============================== GETTERS ===========================================
    
    private inline fun <T, R> getValue(                                     tableName: String,
                                                                               column: String,
                                                                          whereClause: String,
                                                                             whereArg: T,
                                                                              default: R,
                                                                             typeName: String,
                                                           crossinline getCursorValue: (Cursor)->R
    ): R {
        return db.rawQuery("SELECT $column FROM $tableName WHERE $whereClause=? LIMIT 1",
                                                                 arrayOf(whereArg.toString() )
        ).use { cursor ->
            if (cursor.moveToFirst()) getCursorValue(cursor)
            else {
                // region LOG
                Log.e(TAG, "$typeName() Unable to get value from DB. Returning default. Condition: $whereClause = $whereArg")
                // endregion
                default
            }
        }
    }
    
    fun <T> getInt(tableName: String, column: String, whereClause: String, whereArg: T) =
        getValue(tableName, column, whereClause, whereArg, -1, "getInt") { it.getInt(0) }
    
    fun <T> getStr(tableName: String, column: String, whereClause: String, whereArg: T): String =
        getValue(tableName, column, whereClause, whereArg, "", "getStr") { it.getString(0) }
    
    fun <T> getBool(tableName: String, column: String, whereClause: String, whereArg: T) =
        getValue(tableName, column, whereClause, whereArg, false, "getBool") { it.getBool(0) }
    
    fun <T> getLong(tableName: String, column: String, whereClause: String, whereArg: T) =
        getValue(tableName, column, whereClause, whereArg, -1L, "getLong") { it.getLong(0) }
    
    fun <T> getFloat(tableName: String, column: String, whereClause: String, whereArg: T) =
        getValue(tableName, column, whereClause, whereArg, -1F, "getFloat") { it.getFloat(0) }
    
    fun <T> getBlob(tableName: String, column: String, whereClause: String, whereArg: T): ByteArray =
        getValue(tableName, column, whereClause, whereArg, ByteArray(0), "getBlob") { it.getBlob(0) }
    
    
    // By ID
    fun getIntById(id: Int, tableName: String, column: String) =
        getValue(tableName, column, ID, id, -1, "getIntById") { it.getInt(0) }
    
    fun getStrById(id: Int, tableName: String, column: String): String =
        getValue(tableName, column, ID, id, "", "getStrById") { it.getString(0) }
    
    fun getBoolById(id: Int, tableName: String, column: String) =
        getValue(tableName, column, ID, id, false, "getBoolById") { it.getBool(0) }
    
    fun getLongById(id: Int, tableName: String, column: String) =
        getValue(tableName, column, ID, id, -1L, "getLongById") { it.getLong(0) }
    
    fun getFloatById(id: Int, tableName: String, column: String) =
        getValue(tableName, column, ID, id, -1F, "getFloatById") { it.getFloat(0) }
    
    fun getBlobById(id: Int, tableName: String, column: String): ByteArray =
        getValue(tableName, column, ID, id, ByteArray(0), "getBlobById") { it.getBlob(0) }
    
    
    
    
    
    
    // --------------------------  Potentially useful fun  ----------------------------------
    
    fun getLastID(                                                             tableName: String
    ) = db.rawQuery(select + ID + from + tableName, null).use { cursor ->
        if (cursor.moveToLast())
            cursor.getInt(0)
        else {
            // region LOG
            Log.e(TAG, "getLastID() Unable to get value from DB ($tableName). Returning -1")
            // endregion
            -1
        }
    }
    
    
    fun getAllIDs(                                                          tableName: String,
                                                                              orderBy: String = ""
    ): MutableList<Int> {
        val ids = mutableListOf<Int>()
        
        db.getCursor(tableName, ID, orderBy = orderBy).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    ids.add(cursor.getInt(0))
                } while (cursor.moveToNext())
            } else {
                // region LOG
                    Log.d(TAG, "getAllIDs() didn't find any elements in $tableName")
                // endregion
            }
        }
        return ids
    }
    
    
    fun tableExists(                                                           tableName: String
    ) = db.rawQuery(
        select + Name + from + MasterTable + where + "type='table' AND name=?", arrayOf(tableName)
    ).use { it.moveToFirst() }
    
    
    fun isTableEmpty(tableName: String) = !db.hasRows(tableName)
    
    
    fun getNewPriority(                                                        tableName: String
    ) = db.getCursor(tableName, Priority, orderBy = Priority).use { cursor ->
        if (cursor.moveToLast())
            cursor.getInt(0) + 1
        else
            1
    }
    
    
    
    
    
    
    
    
    
}