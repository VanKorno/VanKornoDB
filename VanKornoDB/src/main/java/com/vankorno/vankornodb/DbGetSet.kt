package com.vankorno.vankornodb
// This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
// If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.*

private const val TAG = "DbGetSet"

open class DbGetSet(val db: SQLiteDatabase) {
    
    // ============================== GETTERS ===========================================
    
    fun <T> getInt(                                                            tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ): Int {
        val cursor = db.rawQuery(select + column + from + tableName + where + whereClause+"=?",
                                                                    arrayOf(whereArg.toString()) )
        val mySocks =   if (cursor.moveToFirst())
                            cursor.getInt(0)
                        else {
                            // region LOG
                            Log.e(TAG, "getInt() Unable to get value from DB. Returning zero. Condition: $whereClause = $whereArg")
                            // endregion
                            -1
                        }
        cursor.close()
        return mySocks
    }
    
    fun <T> getStr(                                                            tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ): String {
        val cursor = db.rawQuery(select + column + from + tableName + where + whereClause+"=?",
                                                                    arrayOf(whereArg.toString()) )
        val mySocks =   if (cursor.moveToFirst())
                            cursor.getString(0)
                        else {
                            // region LOG
                            Log.e(TAG, "getString() Unable to get value from DB. Returning an empty str. Condition: $whereClause = $whereArg")
                            // endregion
                            ""
                        }
        cursor.close()
        return mySocks
    }
    fun <T> getBool(                                                           tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ): Boolean {
        val cursor = db.rawQuery(select + column + from + tableName + where + whereClause+"=?",
                                                                    arrayOf(whereArg.toString()) )
        val mySocks =   if (cursor.moveToFirst())
                            cursor.getBool(0)
                        else {
                            // region LOG
                            Log.e(TAG, "getBool() Unable to get value from DB. Returning FALSE. Condition: $whereClause = $whereArg")
                            // endregion
                            false
                        }
        cursor.close()
        return mySocks
    }
    fun <T> getLong(                                                           tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ): Long {
        val cursor = db.rawQuery(select + column + from + tableName + where + whereClause+"=?",
                                                                   arrayOf(whereArg.toString()) )
        val mySocks =   if (cursor.moveToFirst())
                            cursor.getLong(0)
                        else {
                            // region LOG
                            Log.e(TAG, "getLong() Unable to get value from DB. Returning -1. Condition: $whereClause = $whereArg")
                            // endregion
                            -1L
                        }
        cursor.close()
        return mySocks
    }
    fun <T> getFloat(                                                          tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ): Float {
        val cursor = db.rawQuery(select + column + from + tableName + where + whereClause+"=?",
                                                                    arrayOf(whereArg.toString()) )
        val mySocks =   if (cursor.moveToFirst())
                            cursor.getFloat(0)
                        else {
                            // region LOG
                            Log.e(TAG, "getFloat() Unable to get value from DB. Returning -1. Condition: $whereClause = $whereArg")
                            // endregion
                            -1F
                        }
        cursor.close()
        return mySocks
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
    ): Int {
        val cursor = db.rawQuery(select + ID + from + tableName, null)
        
        val mySocks = if (cursor.moveToLast())
                            cursor.getInt(0)
                        else{
                            // region LOG
                            Log.e(TAG, "getLastID() Unable to get value from DB ($tableName). Returning -1")
                            // endregion
                            -1
                        }
        cursor.close()
        return mySocks
    }
    
    fun getAllIDs(                                                          tableName: String,
                                                                              orderBy: String = ""
    ): MutableList<Int> {
        val cursor = db.getCursor(tableName, ID, orderBy = orderBy)
        
        val ids = mutableListOf<Int>()
        
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0))
            } while (cursor.moveToNext())
        }
        else {
            // region LOG
                Log.d(TAG, "getAllIDs() didn't find any elements in $tableName")
            // endregion
        }
        cursor.close()
        return ids
    }
    
    fun isTableEmpty(                                                          tableName: String
    ): Boolean {
        val cursor = db.rawQuery(selectAllFrom + tableName, null)
        val emptiness = cursor.count < 1
        cursor.close()
        return emptiness
    }
    
    fun getNewPriority(                                                        tableName: String
    ): Int {
        val cursor = db.getCursor(tableName, Priority, orderBy = Priority)
        var priority =  if (cursor.moveToLast())
                            cursor.getInt(0) + 1
                        else
                            1
        cursor.close()
        return priority
    }
    
    
    
    
    
    
    
    
    
}