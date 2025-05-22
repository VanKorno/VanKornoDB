package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.database.sqlite.transaction
import com.vankorno.vankornodb.getSet.DbGetSet
import com.vankorno.vankornodb.DbMisc

@Suppress("NOTHING_TO_INLINE", "unused")
open class DbHelper(
                                context: Context,
                                 dbName: String,
                              dbVersion: Int,
                               onCreate: (SQLiteDatabase)->Unit = {},
                              onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> }
    
) : DbMaker(context, dbName, dbVersion, onCreate, onUpgrade) {
    
    /** Better reading performance, optimal for reading, but writing can also be done in a less safe way **/
    inline fun <T> readDB(                                        defaultValue: T,
                                                                       funName: String = "readDB",
                                                                           run: (SQLiteDatabase)->T
    ): T {
        return try {
            synchronized(dbLock) {
                run(DbManager.mainDb)
            }
        } catch (e: Exception) {
            // region LOG
            Log.e("DB", "$funName() failed. Returning the default value. Details: ${e.message}", e)
            // endregion
            defaultValue
        }
    }
    
    /** Same as readDB, but does not return anything (for reading db and setting some values from the inside) **/
    inline fun voidReadDB(                                          funName: String = "voidReadDB",
                                                                        run: (SQLiteDatabase)->Unit
    ) {
        readDB(Unit, funName){run(it)}
    }
    
    
    /** Void (can read or write db, but doesn't return anything), with writing overhead **/
    inline fun writeDB(                                             funName: String = "writeDB",
                                                                        run: (SQLiteDatabase)->Unit
    ) {
        try {
            synchronized(dbLock) {
                DbManager.mainDb.transaction { run(this) }
            }
        } catch (e: Exception) {
            // region LOG
                Log.e("DB", "$funName() failed. Details: ${e.message}", e)
            // endregion
        }
    }
    
    
    /** All-mighty, but with writing overhead **/
    inline fun <T> readWriteDB(                                defaultValue: T,
                                                                    funName: String = "readWriteDB",
                                                                        run: (SQLiteDatabase)->T
    ): T {
        return try {
            synchronized(dbLock) {
                DbManager.mainDb.transaction { run(DbManager.mainDb) }
            }
        } catch (e: Exception) {
            // region LOG
            Log.e("DB", "$funName() failed. Returning the default value. Details: ${e.message}", e)
            // endregion
            defaultValue
        }
    }
    
    
    
    // ============================== GETTERS ===========================================
    
    inline fun <T> getInt(                                                     tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = readDB(0, "getInt") {
        DbGetSet(it).getInt(tableName, column, whereClause, whereArg)
    }
    
    inline fun <T> getStr(                                                     tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ): String = readDB("", "getStr") {
        DbGetSet(it).getStr(tableName, column, whereClause, whereArg)
    }
    
    inline fun <T> getBool(                                                    tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = readDB(false, "getBool") { 
        DbGetSet(it).getBool(tableName, column, whereClause, whereArg)
    }
    
    inline fun <T> getLong(                                                    tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = readDB(0L, "getLong") { 
        DbGetSet(it).getLong(tableName, column, whereClause, whereArg)
    }
    
    inline fun <T> getFloat(                                                   tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) = readDB(0F, "getFloat") { 
        DbGetSet(it).getFloat(tableName, column, whereClause, whereArg)
    }
    
    
    
    
    
    // ============================== SETTERS ===========================================
    
    inline fun <T> setStr(                                                         value: String,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        writeDB("setStr") {
            DbGetSet(it).setStr(value, tableName, column, whereClause, whereArg)
        }
    }
    inline fun <T> setInt(                                                         value: Int,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        writeDB("setInt") {
            DbGetSet(it).setInt(value, tableName, column, whereClause, whereArg)
        }
    }
    inline fun <T> setBool(                                                        value: Boolean,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        writeDB("setBool") {
            DbGetSet(it).setBool(value, tableName, column, whereClause, whereArg)
        }
    }
    inline fun <T> setLong(                                                        value: Long,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        writeDB("setLong") {
            DbGetSet(it).setLong(value, tableName, column, whereClause, whereArg)
        }
    }
    inline fun <T> setFloat(                                                       value: Float,
                                                                               tableName: String,
                                                                                  column: String,
                                                                             whereClause: String,
                                                                                whereArg: T
    ) {
        writeDB("setFloat") {
            DbGetSet(it).setFloat(value, tableName, column, whereClause, whereArg)
        }
    }
    
    
    
    // ================  Some useful utility fun  ================
    
    inline fun tableExists(tableName: String) = readDB(false, "tableExists") { DbGetSet(it).tableExists(tableName) }
    
    inline fun isTableEmpty(tableName: String) = readDB(true, "isTableEmpty") { DbGetSet(it).isTableEmpty(tableName) }
    
    inline fun getLastID(tableName: String) = readDB(0, "getLastID") { DbGetSet(it).getLastID(tableName) }
    
    inline fun deleteFirstRow(tableName: String) = writeDB("deleteFirstRow") { DbMisc().deleteFirstRow(it, tableName) }
    
    
    
    
    
    
    
}