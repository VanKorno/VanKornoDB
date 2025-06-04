package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.database.sqlite.transaction
import com.vankorno.vankornodb.DbMisc
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.getSet.getBlob
import com.vankorno.vankornodb.getSet.getBool
import com.vankorno.vankornodb.getSet.getFloat
import com.vankorno.vankornodb.getSet.getInt
import com.vankorno.vankornodb.getSet.getLastID
import com.vankorno.vankornodb.getSet.getLong
import com.vankorno.vankornodb.getSet.getStr
import com.vankorno.vankornodb.getSet.isTableEmpty
import com.vankorno.vankornodb.getSet.set
import com.vankorno.vankornodb.getSet.tableExists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Suppress("NOTHING_TO_INLINE", "unused")
open class DbHelper(
                                context: Context,
                                 dbName: String,
                              dbVersion: Int,
                               onCreate: (SQLiteDatabase)->Unit = {},
                              onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> }
    
) : DbMaker(context, dbName, dbVersion, onCreate, onUpgrade) {
    
    /**
     * Better reading performance, optimal for reading, but writing can also be done in a less safe way.
     */
    @JvmOverloads
    fun <T> readDB(                                             defaultValue: T,
                                                                     funName: String = "readDB",
                                                                         run: (SQLiteDatabase) -> T
    ): T = runBlocking {
        withContext(Dispatchers.IO) {
            readBase(defaultValue, funName, run)
        }
    }
    
    /**
     * Same as readDB, but does not return anything (for reading db and setting some values from the inside).
     */
    @JvmOverloads
    fun voidReadDB(                                                 funName: String = "voidReadDB",
                                                                        run: (SQLiteDatabase)->Unit
    ) {
        runBlocking {
            withContext(Dispatchers.IO) {
                readBase(Unit, funName) { run(it) }
            }
        }
    }
    
    /**
     * Void (can read or write db, but doesn't return anything), with writing overhead.
     */
    @JvmOverloads
    fun writeDB(                                                    funName: String = "writeDB",
                                                                        run: (SQLiteDatabase)->Unit
    ) {
        runBlocking {
            withContext(Dispatchers.IO) {
                writeBase(funName) { run(it) }
            }
        }
    }
    
    /**
     * All-mighty, but with writing overhead.
     */
    @JvmOverloads
    fun <T> readWriteDB(                                       defaultValue: T,
                                                                    funName: String = "readWriteDB",
                                                                        run: (SQLiteDatabase)->T
    ): T = runBlocking {
        withContext(Dispatchers.IO) {
            readWriteBase(defaultValue, funName, run)
        }
    }
    
    
    
    
    /**
     * Better reading performance, optimal for reading, but writing can also be done in a less safe way.
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun <T> readAsync(                                  defaultValue: T,
                                                                     funName: String = "readAsync",
                                                                         run: (SQLiteDatabase)->T
    ): T = withContext(Dispatchers.IO) {
        readBase(defaultValue, funName, run)
    }
    
    /**
     * Same as readDB, but does not return anything (for reading db and setting some values from the inside).
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun voidReadAsync(                                    funName: String = "voidReadAsync",
                                                                      run: (SQLiteDatabase)->Unit
    ) = withContext(Dispatchers.IO) {
        readBase(Unit, funName){ run(it) }
    }
    
    /**
     * Void (can read or write db, but doesn't return anything), with writing overhead.
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun writeAsync(                                         funName: String = "writeAsync",
                                                                        run: (SQLiteDatabase)->Unit
    ) = withContext(Dispatchers.IO) {
        writeBase(funName) { run(it) }
    }
    
    /**
     * All-mighty, but with writing overhead.
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun <T> readWriteAsync(                         defaultValue: T,
                                                                 funName: String = "readWriteAsync",
                                                                     run: (SQLiteDatabase)->T
    ): T = withContext(Dispatchers.IO) {
        readWriteBase(defaultValue, funName, run)
    }
    
    
    
    
    
    inline fun <T> readBase(                                      defaultValue: T,
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
    
    inline fun voidReadBase(                                        funName: String = "voidReadDB",
                                                                        run: (SQLiteDatabase)->Unit
    ) = readBase(Unit, funName){ run(it) }
    
    
    inline fun writeBase(                                           funName: String = "writeDB",
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
    
    inline fun <T> readWriteBase(                              defaultValue: T,
                                                                    funName: String = "readWriteDB",
                                                                        run: (SQLiteDatabase)->T
    ): T {
        return try {
            synchronized(dbLock) {
                DbManager.mainDb.transaction { run(this) }
            }
        } catch (e: Exception) {
            // region LOG
            Log.e("DB", "$funName() failed. Returning the default value. Details: ${e.message}", e)
            // endregion
            defaultValue
        }
    }
    
    
    
    
    
    
    
    
    
    
    // ============================== SETTERS ===========================================
    
    inline fun setById(value: Any, id: Int, tableName: String, column: String) = set(value, tableName, column, ID, id)
    
    suspend fun setByIdAsync(value: Any, id: Int, tableName: String, column: String) = setAsync(value, tableName, column, ID, id)
    
    
    inline fun <T> set(                                                            value: Any,
                                                                               tableName: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: T
    ) = writeDB("set") { it.set(value, tableName, column, where, equals) }
    
    inline fun <T> set(                                                value: Any,
                                                                   tableName: String,
                                                                      column: String,
                                                              noinline where: WhereBuilder.()->Unit
    ) = writeDB("set") { it.set(value, tableName, column, where) }
    
    
    suspend fun <T> setAsync(                                                      value: Any,
                                                                               tableName: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: T
    ) = writeAsync("setAsync") {
        it.set(value, tableName, column, where, equals)
    }
    
    suspend fun <T> setAsync(                                          value: Any,
                                                                   tableName: String,
                                                                      column: String,
                                                                       where: WhereBuilder.()->Unit
    ) = writeAsync("setAsync") { it.set(value, tableName, column, where) }
    
    
    
    
    
    
    
    
    // ============================== GETTERS ===========================================
    
    inline fun <T> getInt(tableName: String, column: String, whereClause: String, whereArg: T) =
        readDB(0, "getInt") {
            it.getInt(tableName, column, whereClause, whereArg)
        }
    
    inline fun <T> getStr(tableName: String, column: String, whereClause: String, whereArg: T): String =
        readDB("", "getStr") {
            it.getStr(tableName, column, whereClause, whereArg)
        }
    
    inline fun <T> getBool(tableName: String, column: String, whereClause: String, whereArg: T) =
        readDB(false, "getBool") { 
            it.getBool(tableName, column, whereClause, whereArg)
        }
    
    inline fun <T> getLong(tableName: String, column: String, whereClause: String, whereArg: T) =
        readDB(0L, "getLong") { 
            it.getLong(tableName, column, whereClause, whereArg)
        }
    
    inline fun <T> getFloat(tableName: String, column: String, whereClause: String, whereArg: T) =
        readDB(0F, "getFloat") { 
            it.getFloat(tableName, column, whereClause, whereArg)
        }
    
    inline fun <T> getBlob(tableName: String, column: String, whereClause: String, whereArg: T) =
        readDB(null, "getBlob") {
            it.getBlob(tableName, column, whereClause, whereArg)
        }
    
    
    // ----------------------  SUSPEND FUN  ----------------------
    
    suspend fun <T> getIntAsync(tableName: String, column: String, whereClause: String, whereArg: T) =
        readAsync(0, "getInt") {
            it.getInt(tableName, column, whereClause, whereArg)
        }
    
    suspend fun <T> getStrAsync(tableName: String, column: String, whereClause: String, whereArg: T): String =
        readAsync("", "getStr") {
            it.getStr(tableName, column, whereClause, whereArg)
        }
    
    suspend fun <T> getBoolAsync(tableName: String, column: String, whereClause: String, whereArg: T) =
        readAsync(false, "getBool") { 
            it.getBool(tableName, column, whereClause, whereArg)
        }
    
    suspend fun <T> getLongAsync(tableName: String, column: String, whereClause: String, whereArg: T) =
        readAsync(0L, "getLong") { 
            it.getLong(tableName, column, whereClause, whereArg)
        }
    
    suspend fun <T> getFloatAsync(tableName: String, column: String, whereClause: String, whereArg: T) =
        readAsync(0F, "getFloat") { 
            it.getFloat(tableName, column, whereClause, whereArg)
        }
    
    suspend fun <T> getBlobAsync(tableName: String, column: String, whereClause: String, whereArg: T): ByteArray? =
        readAsync(null, "getBlob") {
            it.getBlob(tableName, column, whereClause, whereArg)
        }
    
    // By ID
    
    inline fun getIntById(id: Int, tableName: String, column: String) = getInt(tableName, column, ID, id)
    suspend fun getIntByIdAsync(id: Int, tableName: String, column: String) = getIntAsync(tableName, column, ID, id)
    
    inline fun getStrById(id: Int, tableName: String, column: String) = getStr(tableName, column, ID, id)
    suspend fun getStrByIdAsync(id: Int, tableName: String, column: String) = getStrAsync(tableName, column, ID, id)
    
    inline fun getBoolById(id: Int, tableName: String, column: String) = getBool(tableName, column, ID, id)
    suspend fun getBoolByIdAsync(id: Int, tableName: String, column: String) = getBoolAsync(tableName, column, ID, id)
    
    inline fun getLongById(id: Int, tableName: String, column: String) = getLong(tableName, column, ID, id)
    suspend fun getLongByIdAsync(id: Int, tableName: String, column: String) = getLongAsync(tableName, column, ID, id)
    
    inline fun getFloatById(id: Int, tableName: String, column: String) = getFloat(tableName, column, ID, id)
    suspend fun getFloatByIdAsync(id: Int, tableName: String, column: String) = getFloatAsync(tableName, column, ID, id)
    
    inline fun getBlobById(id: Int, tableName: String, column: String): ByteArray? = getBlob(tableName, column, ID, id)
    suspend fun getBlobByIdAsync(id: Int, tableName: String, column: String): ByteArray? = getBlobAsync(tableName, column, ID, id)
    
    
    
    
    // ================  Some useful utility fun  ================
    
    inline fun tableExists(tableName: String) = readDB(false, "tableExists") { it.tableExists(tableName) }
    suspend fun tableExistsAsync(tableName: String) = readAsync(false, "tableExistsAsync") { it.tableExists(tableName) }
    
    inline fun isTableEmpty(tableName: String) = readDB(true, "isTableEmpty") { it.isTableEmpty(tableName) }
    suspend fun isTableEmptyAsync(tableName: String) = readAsync(true, "isTableEmptyAsync") { it.isTableEmpty(tableName) }
    
    
    inline fun getLastID(tableName: String) = readDB(0, "getLastID") { it.getLastID(tableName) }
    suspend fun getLastIDAsync(tableName: String) = readAsync(0, "getLastIDAsync") { it.getLastID(tableName) }
    
    inline fun deleteFirstRow(tableName: String) = writeDB("deleteFirstRow") { DbMisc().deleteFirstRow(it, tableName) }
    suspend fun deleteFirstRowAsync(tableName: String) = writeAsync("deleteFirstRowAsync") { DbMisc().deleteFirstRow(it, tableName) }
    
    
    
    
    
    
    
}