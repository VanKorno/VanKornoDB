package com.vankorno.vankornodb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.DbManager.mainDb
import androidx.core.database.sqlite.transaction

private const val TAG = "DbHelper"

open class DbHelper(            context: Context,
                                 dbName: String,
                              dbVersion: Int,
                               entities: Array<LibDbTableAndEntt> = emptyArray<LibDbTableAndEntt>(),
                          onCreateStart: (SQLiteDatabase)->Unit = {},
                         onCreateFinish: (SQLiteDatabase)->Unit = {},
                              onUpgrade: (SQLiteDatabase)->Unit = {}
) : DbMaker(context, dbName, dbVersion, entities, onCreateStart, onCreateFinish, onUpgrade) {
    
    inline fun <T> readDB(run: (SQLiteDatabase)->T): T {
        synchronized(dbLock) {
            return  mainDb.transaction { run(mainDb) }
        }
    }
    
    inline fun writeDB(run: (SQLiteDatabase)->Unit) {
        synchronized(dbLock) {
            mainDb.transaction { run(this) }
        }
    }
    
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
    ): Int = readDB {
        LibGetSetDB(it).getInt(tableName, column, whereClause, whereArg)
    }
    
    fun getStr(                                                                tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ): String = readDB {
        LibGetSetDB(it).getStr(tableName, column, whereClause, whereArg)
    }
    
    fun getBool(                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ): Boolean = readDB { 
        LibGetSetDB(it).getBool(tableName, column, whereClause, whereArg)
    }
    
    fun getLong(                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ): Long = readDB { 
        LibGetSetDB(it).getLong(tableName, column, whereClause, whereArg)
    }
    
    fun getFloat(                                                              tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ): Float = readDB { 
        LibGetSetDB(it).getFloat(tableName, column, whereClause, whereArg)
    }
    
    
    
    
    
    // ============================== SETTERS ===========================================
    
    fun setStr(                                                                    value: String,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ) {
        writeDB {
            LibGetSetDB(it).setStr(value, tableName, column, whereClause, whereArg)
        }
    }
    fun setInt(                                                                    value: Int,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ) {
        writeDB {
            LibGetSetDB(it).setInt(value, tableName, column, whereClause, whereArg)
        }
    }
    fun setBool(                                                                   value: Boolean,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ) {
        writeDB {
            LibGetSetDB(it).setBool(value, tableName, column, whereClause, whereArg)
        }
    }
    fun setLong(                                                                   value: Long,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ) {
        writeDB {
            LibGetSetDB(it).setLong(value, tableName, column, whereClause, whereArg)
        }
    }
    fun setFloat(                                                                  value: Float,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: String
    ) {
        writeDB {
            LibGetSetDB(it).setFloat(value, tableName, column, whereClause, whereArg)
        }
    }
    
    
    
    
    
    fun checkIfEmpty(tableName: String) = readDB { LibGetSetDB(it).isEmpty(tableName) }
    
    fun getLastID(tableName: String) = readDB { LibGetSetDB(it).getLastID(tableName) }
    
    
    
    fun deleteFirstRow(tableName: String) {
        writeDB { DbMisc().deleteFirstRow(it, tableName) }
    }
    
    
    
    
    
    
    
}