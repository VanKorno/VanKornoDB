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
    inline fun writeDB(                                                 run: (SQLiteDatabase)->Unit
    ) {
        synchronized(dbLock) {
            mainDb.transaction { run(this) }
        }
    }
    
    // All-mighty
    inline fun <T> readWriteDB(                                            run: (SQLiteDatabase)->T
    ): T {
        synchronized(dbLock) {
            return  mainDb.transaction { run(mainDb) }
        }
    }
    
    
    
    // ============================== GETTERS ===========================================
    
    fun <T> getInt(                                                            tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = readDB(0) {
        DbGetSet(it).getInt(tableName, column, whereClause, whereArg)
    }
    
    fun <T> getStr(                                                            tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ): String = readDB("") {
        DbGetSet(it).getStr(tableName, column, whereClause, whereArg)
    }
    
    fun <T> getBool(                                                           tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = readDB(false) { 
        DbGetSet(it).getBool(tableName, column, whereClause, whereArg)
    }
    
    fun <T> getLong(                                                           tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = readDB(0L) { 
        DbGetSet(it).getLong(tableName, column, whereClause, whereArg)
    }
    
    fun <T> getFloat(                                                          tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = readDB(0F) { 
        DbGetSet(it).getFloat(tableName, column, whereClause, whereArg)
    }
    
    
    
    
    
    // ============================== SETTERS ===========================================
    
    fun <T> setStr(                                                                value: String,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        writeDB { DbGetSet(it).setStr(value, tableName, column, whereClause, whereArg) }
    }
    fun <T> setInt(                                                                value: Int,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        writeDB { DbGetSet(it).setInt(value, tableName, column, whereClause, whereArg) }
    }
    fun <T> setBool(                                                               value: Boolean,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        writeDB { DbGetSet(it).setBool(value, tableName, column, whereClause, whereArg) }
    }
    fun <T> setLong(                                                               value: Long,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        writeDB { DbGetSet(it).setLong(value, tableName, column, whereClause, whereArg) }
    }
    fun <T> setFloat(                                                              value: Float,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        writeDB { DbGetSet(it).setFloat(value, tableName, column, whereClause, whereArg) }
    }
    
    
    
    // ================  Some useful utility fun  ================
    
    fun isTableEmpty(tableName: String) = readDB(true) { DbGetSet(it).isTableEmpty(tableName) }
    
    fun getLastID(tableName: String) = readDB(0) { DbGetSet(it).getLastID(tableName) }
    
    fun deleteFirstRow(tableName: String) = writeDB { DbMisc().deleteFirstRow(it, tableName) }
    
    
    
    
    
    
    
}