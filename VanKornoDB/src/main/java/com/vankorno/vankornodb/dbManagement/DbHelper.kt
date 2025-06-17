package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.database.sqlite.transaction
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.getSet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

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
    
    
    
    // ====================================  A S Y N C  ==================================== \\
    
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
    
    
    
    // ====================================  B A S E D  ==================================== \\
    
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
    
    
    
    
    
    
    
    
    
    
    // ===================================  S E T T E R S  =================================== \\
    
    inline fun setById(value: Any, id: Int, tableName: String, column: String) = set(value, tableName, column, ID, id)
    
    suspend fun setByIdAsync(value: Any, id: Int, tableName: String, column: String) = setAsync(value, tableName, column, ID, id)
    
    
    inline fun <T> set(                                                            value: Any,
                                                                               tableName: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: T
    ) = writeDB("set") { it.set(value, tableName, column, where, equals) }
    
    fun <T> set(                                                       value: Any,
                                                                   tableName: String,
                                                                      column: String,
                                                                       where: WhereBuilder.()->Unit
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
    
    
    
    // =============================  M U L T I - S E T T E R S  ============================= \\
    
    fun setMult(                                                   tableName: String,
                                                                          cv: ContentValues,
                                                                       where: WhereBuilder.()->Unit
    ) = writeDB("setMult") { it.setMult(tableName, cv, where) }
    
    suspend fun setMultAsync(                                      tableName: String,
                                                                          cv: ContentValues,
                                                                       where: WhereBuilder.()->Unit
    ) = writeAsync("setMultAsync") { it.setMult(tableName, cv, where) }
    
    
    fun setMult(                                                   tableName: String,
                                                                      values: Map<String, Any?>,
                                                                       where: WhereBuilder.()->Unit
    ) = writeDB("setMult") { it.setMult(tableName, values, where) }
    
    suspend fun setMultAsync(                                      tableName: String,
                                                                      values: Map<String, Any?>,
                                                                       where: WhereBuilder.()->Unit
    ) = writeAsync("setMultAsync") { it.setMult(tableName, values, where) }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun setMultById(                                                      id: Int,
                                                                          tableName: String,
                                                                                 cv: ContentValues
    ) = writeDB("setMultById") { it.setMultById(id, tableName, cv) }
    
    suspend fun setMultByIdAsync(                                                id: Int,
                                                                          tableName: String,
                                                                                 cv: ContentValues
    ) = writeAsync("setMultByIdAsync") { it.setMultById(id, tableName, cv) }
    
    
    inline fun setMultById(                                                    id: Int,
                                                                        tableName: String,
                                                                           values: Map<String, Any?>
    ) = writeDB("setMultById") {
        it.setMultById(id, tableName, values)
    }
    
    suspend fun setMultByIdAsync(                                              id: Int,
                                                                        tableName: String,
                                                                           values: Map<String, Any?>
    ) = writeAsync("setMultByIdAsync") {
        it.setMultById(id, tableName, values)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun setInAll(                                                           value: Any,
                                                                               tableName: String,
                                                                                  column: String
    ) = writeDB("setInAll") { it.setInAll(value, tableName, column) }
    
    suspend fun setInAllAsync(                                                     value: Any,
                                                                               tableName: String,
                                                                                  column: String
    ) = writeAsync("setInAllAsync") { it.setInAll(value, tableName, column) }
    
    
    inline fun setMultInAll(                                            tableName: String,
                                                                           values: Map<String, Any?>
    ) = writeDB("setMultInAll") { it.setMultInAll(tableName, values) }
    
    suspend fun setMultInAllAsync(                                      tableName: String,
                                                                           values: Map<String, Any?>
    ) = writeAsync("setMultInAllAsync") { it.setMultInAll(tableName, values) }
    
    
    
    // ---------------------------------  S E T   R O W S  --------------------------------- \\
    
    inline fun <reified T : Any> insertRow(                                    tableName: String,
                                                                                  entity: T
    ): Long = readWriteDB(-1L, "insertRow") {
        it.insertRow(tableName, entity)
    }
    
    suspend inline fun <reified T : Any> insertRowAsync(                       tableName: String,
                                                                                  entity: T
    ): Long = readWriteAsync(-1L, "insertRowAsync") {
        it.insertRow(tableName, entity)
    }
    
    
    inline fun <reified T : Any> insertRows(                                   tableName: String,
                                                                                entities: List<T>
    ): Int = readWriteDB(0, "insertRows") {
        it.insertRows(tableName, entities)
    }
    
    suspend inline fun <reified T : Any> insertRowsAsync(                      tableName: String,
                                                                                entities: List<T>
    ): Int = readWriteAsync(0, "insertRowsAsync") {
        it.insertRows(tableName, entities)
    }
    
    
    
    inline fun <reified T : Any> updateRow(                        tableName: String,
                                                                      entity: T,
                                                              noinline where: WhereBuilder.()->Unit
    ): Int = readWriteDB(0, "updateRow") {
        it.updateRow(tableName, entity, where)
    }
    
    suspend inline fun <reified T : Any> updateRowAsync(           tableName: String,
                                                                      entity: T,
                                                              noinline where: WhereBuilder.()->Unit
    ): Int = readWriteAsync(0, "updateRowAsync") {
        it.updateRow(tableName, entity, where)
    }
    
    
    inline fun <reified T : Any> updateRowById(                                       id: Int,
                                                                               tableName: String,
                                                                                  entity: T
    ): Int = readWriteDB(0, "updateRowById") {
        it.updateRowById(id, tableName, entity)
    }
    
    suspend inline fun <reified T : Any> updateRowByIdAsync(                          id: Int,
                                                                               tableName: String,
                                                                                  entity: T
    ): Int = readWriteAsync(0, "updateRowByIdAsync") {
        it.updateRowById(id, tableName, entity)
    }
    
    
    
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
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
    
    
    // -----------------------------------  SUSPEND FUN  ----------------------------------- \\
    
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
    
    
    
    
    // ---------------------------------  G E T   R O W S  --------------------------------- \\
    
    inline fun <reified T : Any> getRowOrNullById(                                    id: Int,
                                                                               tableName: String
    ): T? = readDB(null, "getRowOrNullById") {
        it.getRowOrNullById<T>(id, tableName)
    }
    
    suspend inline fun <reified T : Any> getRowOrNullByIdAsync(                       id: Int,
                                                                               tableName: String
    ): T? = readAsync(null, "getRowOrNullByIdAsync") {
        it.getRowOrNullById<T>(id, tableName)
    }
    
    
    inline fun <reified T : Any> getRowOrNull(                    table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = 1,
                                                                 offset: Int? = null,
                                                              customEnd: String = ""
    ): T? = readDB(null, "getRowOrNull") {
        it.getRowOrNull<T>(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    }
    
    
    suspend inline fun <reified T : Any> getRowOrNullAsync(       table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = 1,
                                                                 offset: Int? = null,
                                                              customEnd: String = ""
    ): T? = readAsync(null, "getRowOrNullAsync") {
        it.getRowOrNull<T>(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    }
    
    
    fun <T : Any> getRowOrNull(                                   clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = 1,
                                                                 offset: Int? = null,
                                                              customEnd: String = ""
    ): T? = readDB(null, "getRowOrNull") {
        it.getRowOrNull(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    }
    
    
    suspend fun <T : Any> getRowOrNullAsync(                      clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = 1,
                                                                 offset: Int? = null,
                                                              customEnd: String = ""
    ): T? = readAsync(null, "getRowOrNullAsync") {
        it.getRowOrNull(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    }
    
    
    
    inline fun <reified T : Any> getRows(                         table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T) -> T = { it }
    ): List<T> = readDB(emptyList(), "getRows") {
        it.getRows(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    suspend inline fun <reified T : Any> getRowsAsync(            table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T) -> T = { it }
    ): List<T> = readAsync(emptyList(), "getRowsAsync") {
        it.getRows(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    fun <T : Any> getRows(                                        clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                               mapAfter: (T) -> T = { it }
    ): List<T> = readDB(emptyList(), "getRows") {
        it.getRows(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    suspend fun <T : Any> getRowsAsync(                           clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                               mapAfter: (T) -> T = { it }
    ): List<T> = readAsync(emptyList(), "getRowsAsync") {
        it.getRows(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    
    
    
    
    
    
    
    // ============================   D E L E T E,  C L E A R   ============================ \\
    
    inline fun <T> deleteRow(tableName: String, where: String, equals: T) =
        writeDB("deleteRow") { it.deleteRow(tableName, where, equals) }
    
    suspend fun <T> deleteRowAsync(tableName: String, where: String, equals: T) =
        writeAsync("deleteRowAsync") { it.deleteRow(tableName, where, equals) }
    
    
    fun deleteRow(tableName: String, where: WhereBuilder.()->Unit) =
        writeDB("deleteRow") { it.deleteRow(tableName, where) }
    
    suspend fun deleteRowAsync(tableName: String, where: WhereBuilder.()->Unit) =
        writeAsync("deleteRowAsync") { it.deleteRow(tableName, where) }
    
    
    inline fun deleteRowById(id: Int, tableName: String) =
        writeDB("deleteRowById") { it.deleteRowById(id, tableName) }
    
    suspend fun deleteRowByIdAsync(id: Int, tableName: String) =
        writeAsync("deleteRowByIdAsync") { it.deleteRowById(id, tableName) }
    
    
    inline fun deleteFirstRow(tableName: String) = writeDB("deleteFirstRow") { it.deleteFirstRow(tableName) }
    suspend fun deleteFirstRowAsync(tableName: String) = writeAsync("deleteFirstRowAsync") { it.deleteFirstRow(tableName) }
    
    
    inline fun deleteLastRow(tableName: String) = writeDB("deleteLastRow") { it.deleteLastRow(tableName) }
    suspend fun deleteLastRowAsync(tableName: String) = writeAsync("deleteLastRowAsync") { it.deleteLastRow(tableName) }
    
    
    inline fun clearTable(tableName: String, resetAutoID: Boolean = true) =
        writeDB("clearTable") { it.clearTable(tableName, resetAutoID) }
    
    suspend fun clearTableAsync(tableName: String, resetAutoID: Boolean = true) =
        writeAsync("clearTableAsync") { it.clearTable(tableName, resetAutoID) }
    
    
    
    
    // ====================================  C O U N T  ==================================== \\
    
    /** Returns the number of rows matching the query conditions. */
    fun getRowCount(                                          tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {}
    ) = readDB(0, "getRowCount") {
        it.getRowCount(tableName, joins, where)
    }
    
    /** Returns the number of rows matching the query conditions. */
    suspend fun getRowCountAsync(                             tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {}
    ) = readAsync(0, "getRowCountAsync") {
        it.getRowCount(tableName, joins, where)
    }
    
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRows(                                              tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {}
    ) = readDB(0, "hasRows") {
        it.hasRows(tableName, joins, where)
    }
    
    /** Returns true if at least one row matches the query conditions. */
    suspend fun hasRowsAsync(                                 tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {}
    ) = readAsync(0, "hasRowsAsync") {
        it.hasRows(tableName, joins, where)
    }
    
    
    
    
    
    
    
    // ====================================  O T H E R  ==================================== \\
    
    inline fun getLastID(tableName: String) = readDB(0, "getLastID") { it.getLastID(tableName) }
    suspend fun getLastIDAsync(tableName: String) = readAsync(0, "getLastIDAsync") { it.getLastID(tableName) }
    
    
    inline fun getAllIDs(tableName: String) = readDB(emptyList(), "getAllIDs") { it.getAllIDs(tableName) }
    suspend fun getAllIDsAsync(tableName: String) = readAsync(emptyList(), "getAllIDsAsync") { it.getAllIDs(tableName) }
    
    
    inline fun tableExists(tableName: String) = readDB(false, "tableExists") { it.tableExists(tableName) }
    suspend fun tableExistsAsync(tableName: String) = readAsync(false, "tableExistsAsync") { it.tableExists(tableName) }
    
    
    inline fun isTableEmpty(tableName: String) = readDB(true, "isTableEmpty") { it.isTableEmpty(tableName) }
    suspend fun isTableEmptyAsync(tableName: String) = readAsync(true, "isTableEmptyAsync") { it.isTableEmpty(tableName) }
    
    
    inline fun getLastPriority(tableName: String) = readDB(0, "getLastPriority") { it.getLastPriority(tableName) }
    suspend fun getLastPriorityAsync(tableName: String) = readAsync(0, "getLastPriorityAsync") { it.getLastPriority(tableName) }
    
    
    
    
    
}