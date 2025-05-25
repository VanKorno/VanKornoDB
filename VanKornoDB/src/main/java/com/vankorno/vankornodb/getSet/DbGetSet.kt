package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.getBool

private const val TAG = "DbGetSet"

open class DbGetSet(val db: SQLiteDatabase) {
    
    // ============================== GETTERS ===========================================
    
    fun <T> getInt(                                                            tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = db.rawQuery(
        select + column + from + tableName + where + whereClause+"=?",
        arrayOf(whereArg.toString())
    ).use { cursor ->
        if (cursor.moveToFirst())
            cursor.getInt(0)
        else {
            // region LOG
            Log.e(TAG, "getInt() Unable to get value from DB. Returning zero. Condition: $whereClause = $whereArg")
            // endregion
            -1
        }
    }
    
    fun <T> getStr(                                                            tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = db.rawQuery(
        select + column + from + tableName + where + whereClause+"=?",
        arrayOf(whereArg.toString())
    ).use { cursor ->
        if (cursor.moveToFirst())
            cursor.getString(0)
        else {
            // region LOG
            Log.e(TAG, "getString() Unable to get value from DB. Returning an empty str. Condition: $whereClause = $whereArg")
            // endregion
            ""
        }
    }
    
    fun <T> getBool(                                                           tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = db.rawQuery(
        select + column + from + tableName + where + whereClause+"=?",
        arrayOf(whereArg.toString())
    ).use { cursor ->
        if (cursor.moveToFirst())
            cursor.getBool(0)
        else {
            // region LOG
            Log.e(TAG, "getBool() Unable to get value from DB. Returning FALSE. Condition: $whereClause = $whereArg")
            // endregion
            false
        }
    }
    
    fun <T> getLong(                                                           tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = db.rawQuery(
        select + column + from + tableName + where + whereClause+"=?",
        arrayOf(whereArg.toString())
    ).use { cursor ->
        if (cursor.moveToFirst())
            cursor.getLong(0)
        else {
            // region LOG
            Log.e(TAG, "getLong() Unable to get value from DB. Returning -1. Condition: $whereClause = $whereArg")
            // endregion
            -1L
        }
    }
    
    fun <T> getFloat(                                                          tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = db.rawQuery(
        select + column + from + tableName + where + whereClause+"=?",
        arrayOf(whereArg.toString())
    ).use { cursor ->
        if (cursor.moveToFirst())
            cursor.getFloat(0)
        else {
            // region LOG
            Log.e(TAG, "getFloat() Unable to get value from DB. Returning -1. Condition: $whereClause = $whereArg")
            // endregion
            -1F
        }
    }
    
    
    // ============================== SETTERS ===========================================
    
    fun <T> setStr(                                                                value: String,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        val cv = ContentValues()
        cv.put(column, value)
        db.update(tableName, cv, whereClause+"=?", arrayOf(whereArg.toString()))
    }
    fun <T> setInt(                                                                value: Int,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        val cv = ContentValues()
        cv.put(column, value)
        db.update(tableName, cv, whereClause+"=?", arrayOf(whereArg.toString()))
    }
    fun <T> setBool(                                                               value: Boolean,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        val cv = ContentValues()
        cv.put(column, value)
        db.update(tableName, cv, whereClause+"=?", arrayOf(whereArg.toString()))
    }
    fun <T> setLong(                                                               value: Long,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        val cv = ContentValues()
        cv.put(column, value)
        db.update(tableName, cv, whereClause+"=?", arrayOf(whereArg.toString()))
    }
    fun <T> setFloat(                                                              value: Float,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        val cv = ContentValues()
        cv.put(column, value)
        db.update(tableName, cv, whereClause+"=?", arrayOf(whereArg.toString()))
    }
    
    
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