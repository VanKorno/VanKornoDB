package com.vankorno.vankornodb
// This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
// If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.DbManager.mainDb
import androidx.core.database.sqlite.transaction

private const val TAG = "DbHelper"

open class DbHelper(            context: Context,
                                 dbName: String,
                              dbVersion: Int,
                               entities: Array<TableAndEntt> = emptyArray<TableAndEntt>(),
                          onCreateStart: (SQLiteDatabase)->Unit = {},
                         onCreateFinish: (SQLiteDatabase)->Unit = {},
                              onUpgrade: (SQLiteDatabase)->Unit = {}
) : DbMaker(context, dbName, dbVersion, entities, onCreateStart, onCreateFinish, onUpgrade) {
    
    // Better reading performance, a bit less safe writing
    inline fun <T> readDB(                                        defaultValue: T,
                                                                           run: (SQLiteDatabase)->T
    ): T {
        return try {
            synchronized(dbLock) {
                run(mainDb)
            }
        } catch (e: Exception) {
            defaultValue
        }
    }
    
    // Void (can read or write in the db, but doesn't return anything)
    inline fun writeDB(run: (SQLiteDatabase)->Unit) {
        synchronized(dbLock) {
            mainDb.transaction { run(this) }
        }
    }
    
    // All-mighty
    inline fun <T> readWriteDB(run: (SQLiteDatabase)->T): T {
        synchronized(dbLock) {
            return  mainDb.transaction { run(mainDb) }
        }
    }
    
    
    
    // ============================== GETTERS ===========================================
    
    fun getInt(                                                                tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ): Int = readDB(0) {
        DbGetSet(it).getInt(tableName, column, whereClause, whereArg)
    }
    
    fun getStr(                                                                tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ): String = readDB("") {
        DbGetSet(it).getStr(tableName, column, whereClause, whereArg)
    }
    
    fun getBool(                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ): Boolean = readDB(false) { 
        DbGetSet(it).getBool(tableName, column, whereClause, whereArg)
    }
    
    fun getLong(                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ): Long = readDB(0L) { 
        DbGetSet(it).getLong(tableName, column, whereClause, whereArg)
    }
    
    fun getFloat(                                                              tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ): Float = readDB(0F) { 
        DbGetSet(it).getFloat(tableName, column, whereClause, whereArg)
    }
    
    
    
    
    
    // ============================== SETTERS ===========================================
    
    fun setStr(                                                                    value: String,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ) {
        writeDB {
            DbGetSet(it).setStr(value, tableName, column, whereClause, whereArg)
        }
    }
    fun setInt(                                                                    value: Int,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ) {
        writeDB {
            DbGetSet(it).setInt(value, tableName, column, whereClause, whereArg)
        }
    }
    fun setBool(                                                                   value: Boolean,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ) {
        writeDB {
            DbGetSet(it).setBool(value, tableName, column, whereClause, whereArg)
        }
    }
    fun setLong(                                                                   value: Long,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ) {
        writeDB {
            DbGetSet(it).setLong(value, tableName, column, whereClause, whereArg)
        }
    }
    fun setFloat(                                                                  value: Float,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ) {
        writeDB {
            DbGetSet(it).setFloat(value, tableName, column, whereClause, whereArg)
        }
    }
    
    
    
    // ================  Some useful utility fun  ================
    
    fun checkIfEmpty(tableName: String) = readDB(true) { DbGetSet(it).isEmpty(tableName) }
    
    fun getLastID(tableName: String) = readDB(0) { DbGetSet(it).getLastID(tableName) }
    
    
    fun deleteFirstRow(tableName: String) {
        writeDB { DbMisc().deleteFirstRow(it, tableName) }
    }
    
    
    
    
    
    
    
}