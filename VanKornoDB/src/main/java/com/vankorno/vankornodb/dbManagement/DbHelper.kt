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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    fun <T> read(                                                 defaultValue: T,
                                                                       funName: String = "read",
                                                                           run: (SQLiteDatabase)->T
    ): T = runBlocking {
        withContext(Dispatchers.IO) {
            readBase(defaultValue, funName, run)
        }
    }
    
    /**
     * Same as read(), but does not return anything. Useful for reading db and setting some values from the inside.
     * Can launch its own fire-and-forget coroutine if [async] is true.
     */
    @JvmOverloads
    fun voidRead(                                                   funName: String = "voidRead",
                                                                      async: Boolean = false,
                                                                        run: (SQLiteDatabase)->Unit
    ) {
        if (async) {
            CoroutineScope(Dispatchers.IO).launch {
                readBase(Unit, funName) { run(it) }
            }
        } else {
            runBlocking {
                withContext(Dispatchers.IO) {
                    readBase(Unit, funName) { run(it) }
                }
            }
        }
    }
    
    /**
     * Void (can read or write db, but doesn't return anything), with writing overhead.
     * Can launch its own fire-and-forget coroutine if [async] is true.
     */
    @JvmOverloads
    fun write(                                                      funName: String = "write",
                                                                      async: Boolean = false,
                                                                        run: (SQLiteDatabase)->Unit
    ) {
        if (async) {
            CoroutineScope(Dispatchers.IO).launch {
                writeBase(funName, run)
            }
        } else {
            runBlocking {
                withContext(Dispatchers.IO) {
                    writeBase(funName, run)
                }
            }
        }
    }
    
    /**
     * All-mighty, but with writing overhead.
     */
    @JvmOverloads
    fun <T> readWrite(                                         defaultValue: T,
                                                                    funName: String = "readWrite",
                                                                        run: (SQLiteDatabase)->T
    ): T = runBlocking {
        withContext(Dispatchers.IO) {
            readWriteBase(defaultValue, funName, run)
        }
    }
    
    
    
    
    
    
    // =================================  S U S P E N D E D  ================================= \\
    
    /**
     * Better reading performance, optimal for reading, but writing can also be done in a less safe way.
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun <T> readSusp(                                   defaultValue: T,
                                                                     funName: String = "readSusp",
                                                                         run: (SQLiteDatabase)->T
    ): T = withContext(Dispatchers.IO) {
        readBase(defaultValue, funName, run)
    }
    
    /**
     * Same as readDB, but does not return anything (for reading db and setting some values from the inside).
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun voidReadSusp(                                     funName: String = "voidReadSusp",
                                                                      run: (SQLiteDatabase)->Unit
    ) = withContext(Dispatchers.IO) {
        readBase(Unit, funName){ run(it) }
    }
    
    /**
     * Void (can read or write db, but doesn't return anything), with writing overhead.
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun writeSusp(                                          funName: String = "writeSusp",
                                                                        run: (SQLiteDatabase)->Unit
    ) = withContext(Dispatchers.IO) {
        writeBase(funName) { run(it) }
    }
    
    /**
     * All-mighty, but with writing overhead.
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun <T> readWriteSusp(                          defaultValue: T,
                                                                 funName: String = "readWriteSusp",
                                                                     run: (SQLiteDatabase)->T
    ): T = withContext(Dispatchers.IO) {
        readWriteBase(defaultValue, funName, run)
    }
    
    
    
    // ====================================  B A S E D  ==================================== \\
    
    inline fun <T> readBase(                                      defaultValue: T,
                                                                       funName: String = "read",
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
    
    inline fun voidReadBase(                                        funName: String = "voidRead",
                                                                        run: (SQLiteDatabase)->Unit
    ) = readBase(Unit, funName){ run(it) }
    
    
    inline fun writeBase(                                           funName: String = "write",
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
                                                                    funName: String = "readWrite",
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
    
    inline fun setById(                                                     value: Any,
                                                                               id: Int,
                                                                        tableName: String,
                                                                           column: String,
                                                                            async: Boolean = false
    ) = set(value, tableName, column, ID, id, async)
    
    suspend fun setByIdSusp(                                                       value: Any,
                                                                                      id: Int,
                                                                               tableName: String,
                                                                                  column: String
    ) = setSusp(value, tableName, column, ID, id)
    
    
    inline fun <T> set(                                                     value: Any,
                                                                        tableName: String,
                                                                           column: String,
                                                                            where: String,
                                                                           equals: T,
                                                                            async: Boolean = false
    ) = write("set", async) {
        it.set(value, tableName, column, where, equals)
    }
    
    fun <T> set(                                                       value: Any,
                                                                   tableName: String,
                                                                      column: String,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit
    ) = write("set", async) {
        it.set(value, tableName, column, where)
    }
    
    
    suspend fun <T> setSusp(                                                       value: Any,
                                                                               tableName: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: T
    ) = writeSusp("setSusp") {
        it.set(value, tableName, column, where, equals)
    }
    
    suspend fun <T> setSusp(                                           value: Any,
                                                                   tableName: String,
                                                                      column: String,
                                                                       where: WhereBuilder.()->Unit
    ) = writeSusp("setSusp") { it.set(value, tableName, column, where) }
    
    
    
    // =============================  M U L T I - S E T T E R S  ============================= \\
    
    fun setMult(                                                   tableName: String,
                                                                          cv: ContentValues,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit
    ) = write("setMult", async) { it.setMult(tableName, cv, where) }
    
    suspend fun setMultSusp(                                       tableName: String,
                                                                          cv: ContentValues,
                                                                       where: WhereBuilder.()->Unit
    ) = writeSusp("setMultSusp") { it.setMult(tableName, cv, where) }
    
    
    fun setMult(                                                   tableName: String,
                                                                      values: Map<String, Any?>,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit
    ) = write("setMult", async) { it.setMult(tableName, values, where) }
    
    suspend fun setMultSusp(                                       tableName: String,
                                                                      values: Map<String, Any?>,
                                                                       where: WhereBuilder.()->Unit
    ) = writeSusp("setMultSusp") { it.setMult(tableName, values, where) }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun setMultById(                                                    id: Int,
                                                                        tableName: String,
                                                                               cv: ContentValues,
                                                                            async: Boolean = false
    ) = write("setMultById", async) { it.setMultById(id, tableName, cv) }
    
    suspend fun setMultByIdSusp(                                                 id: Int,
                                                                          tableName: String,
                                                                                 cv: ContentValues
    ) = writeSusp("setMultByIdSusp") { it.setMultById(id, tableName, cv) }
    
    
    inline fun setMultById(                                                    id: Int,
                                                                        tableName: String,
                                                                           values: Map<String, Any?>,
                                                                            async: Boolean = false
    ) = write("setMultById", async) {
        it.setMultById(id, tableName, values)
    }
    
    suspend fun setMultByIdSusp(                                               id: Int,
                                                                        tableName: String,
                                                                           values: Map<String, Any?>
    ) = writeSusp("setMultByIdSusp") {
        it.setMultById(id, tableName, values)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun setInAll(                                                    value: Any,
                                                                        tableName: String,
                                                                           column: String,
                                                                            async: Boolean = false
    ) = write("setInAll", async) { it.setInAll(value, tableName, column) }
    
    suspend fun setInAllSusp(                                                      value: Any,
                                                                               tableName: String,
                                                                                  column: String
    ) = writeSusp("setInAllSusp") { it.setInAll(value, tableName, column) }
    
    
    inline fun setMultInAll(                                            tableName: String,
                                                                           values: Map<String, Any?>,
                                                                            async: Boolean = false
    ) = write("setMultInAll", async) { it.setMultInAll(tableName, values) }
    
    suspend fun setMultInAllSusp(                                       tableName: String,
                                                                           values: Map<String, Any?>
    ) = writeSusp("setMultInAllSusp") { it.setMultInAll(tableName, values) }
    
    
    
    // ---------------------------------  S E T   R O W S  --------------------------------- \\
    
    inline fun <T : Any> insertRow(                                     tableName: String,
                                                                           entity: T,
                                                                            async: Boolean = false
    ) {
        write("insertRow", async) { it.insertRow(tableName, entity) }
    }
    
    suspend fun <T : Any> insertRowSusp(                                       tableName: String,
                                                                                  entity: T
    ): Long = readWriteSusp(-1L, "insertRowSusp") {
        it.insertRow(tableName, entity)
    }
    
    
    inline fun <T : Any> insertRows(                                    tableName: String,
                                                                         entities: List<T>,
                                                                            async: Boolean = false
    ) {
        write("insertRows", async) { it.insertRows(tableName, entities) }
    }
    
    suspend fun <T : Any> insertRowsSusp(                                      tableName: String,
                                                                                entities: List<T>
    ): Int = readWriteSusp(0, "insertRowsSusp") {
        it.insertRows(tableName, entities)
    }
    
    
    
    fun <T : Any> updateRow(                                       tableName: String,
                                                                      entity: T,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit
    ) {
        write("updateRow", async) { it.updateRow(tableName, entity, where) }
    }
    
    suspend fun <T : Any> updateRowSusp(                           tableName: String,
                                                                      entity: T,
                                                                       where: WhereBuilder.()->Unit
    ): Int = readWriteSusp(0, "updateRowSusp") {
        it.updateRow(tableName, entity, where)
    }
    
    
    inline fun <T : Any> updateRowById(                                        id: Int,
                                                                        tableName: String,
                                                                           entity: T,
                                                                            async: Boolean = false
    ) {
        write("updateRowById", async) { it.updateRowById(id, tableName, entity) }
    }
    
    suspend fun <T : Any> updateRowByIdSusp(                                          id: Int,
                                                                               tableName: String,
                                                                                  entity: T
    ): Int = readWriteSusp(0, "updateRowByIdSusp") {
        it.updateRowById(id, tableName, entity)
    }
    
    
    
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
    inline fun <T> getInt(tableName: String, column: String, whereClause: String, whereArg: T) =
        read(0, "getInt") {
            it.getInt(tableName, column, whereClause, whereArg)
        }
    
    inline fun <T> getStr(tableName: String, column: String, whereClause: String, whereArg: T): String =
        read("", "getStr") {
            it.getStr(tableName, column, whereClause, whereArg)
        }
    
    inline fun <T> getBool(tableName: String, column: String, whereClause: String, whereArg: T) =
        read(false, "getBool") { 
            it.getBool(tableName, column, whereClause, whereArg)
        }
    
    inline fun <T> getLong(tableName: String, column: String, whereClause: String, whereArg: T) =
        read(0L, "getLong") { 
            it.getLong(tableName, column, whereClause, whereArg)
        }
    
    inline fun <T> getFloat(tableName: String, column: String, whereClause: String, whereArg: T) =
        read(0F, "getFloat") { 
            it.getFloat(tableName, column, whereClause, whereArg)
        }
    
    inline fun <T> getBlob(tableName: String, column: String, whereClause: String, whereArg: T) =
        read(null, "getBlob") {
            it.getBlob(tableName, column, whereClause, whereArg)
        }
    
    
    // -----------------------------------  SUSPEND FUN  ----------------------------------- \\
    
    suspend fun <T> getIntSusp(tableName: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0, "getIntSusp") {
            it.getInt(tableName, column, whereClause, whereArg)
        }
    
    suspend fun <T> getStrSusp(tableName: String, column: String, whereClause: String, whereArg: T): String =
        readSusp("", "getStrSusp") {
            it.getStr(tableName, column, whereClause, whereArg)
        }
    
    suspend fun <T> getBoolSusp(tableName: String, column: String, whereClause: String, whereArg: T) =
        readSusp(false, "getBoolSusp") { 
            it.getBool(tableName, column, whereClause, whereArg)
        }
    
    suspend fun <T> getLongSusp(tableName: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0L, "getLongSusp") { 
            it.getLong(tableName, column, whereClause, whereArg)
        }
    
    suspend fun <T> getFloatSusp(tableName: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0F, "getFloatSusp") { 
            it.getFloat(tableName, column, whereClause, whereArg)
        }
    
    suspend fun <T> getBlobSusp(tableName: String, column: String, whereClause: String, whereArg: T): ByteArray? =
        readSusp(null, "getBlobSusp") {
            it.getBlob(tableName, column, whereClause, whereArg)
        }
    
    // By ID
    
    inline fun getIntById(id: Int, tableName: String, column: String) = getInt(tableName, column, ID, id)
    suspend fun getIntByIdSusp(id: Int, tableName: String, column: String) = getIntSusp(tableName, column, ID, id)
    
    inline fun getStrById(id: Int, tableName: String, column: String) = getStr(tableName, column, ID, id)
    suspend fun getStrByIdSusp(id: Int, tableName: String, column: String) = getStrSusp(tableName, column, ID, id)
    
    inline fun getBoolById(id: Int, tableName: String, column: String) = getBool(tableName, column, ID, id)
    suspend fun getBoolByIdSusp(id: Int, tableName: String, column: String) = getBoolSusp(tableName, column, ID, id)
    
    inline fun getLongById(id: Int, tableName: String, column: String) = getLong(tableName, column, ID, id)
    suspend fun getLongByIdSusp(id: Int, tableName: String, column: String) = getLongSusp(tableName, column, ID, id)
    
    inline fun getFloatById(id: Int, tableName: String, column: String) = getFloat(tableName, column, ID, id)
    suspend fun getFloatByIdSusp(id: Int, tableName: String, column: String) = getFloatSusp(tableName, column, ID, id)
    
    inline fun getBlobById(id: Int, tableName: String, column: String): ByteArray? = getBlob(tableName, column, ID, id)
    suspend fun getBlobByIdSusp(id: Int, tableName: String, column: String): ByteArray? = getBlobSusp(tableName, column, ID, id)
    
    
    
    
    // ---------------------------------  G E T   R O W S  --------------------------------- \\
    
    inline fun <reified T : Any> getRowOrNullById(                                    id: Int,
                                                                               tableName: String
    ): T? = read(null, "getRowOrNullById") {
        it.getRowOrNullById<T>(id, tableName)
    }
    
    suspend inline fun <reified T : Any> getRowOrNullByIdSusp(                        id: Int,
                                                                               tableName: String
    ): T? = readSusp(null, "getRowOrNullByIdSusp") {
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
    ): T? = read(null, "getRowOrNull") {
        it.getRowOrNull<T>(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    }
    
    
    suspend inline fun <reified T : Any> getRowOrNullSusp(        table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = 1,
                                                                 offset: Int? = null,
                                                              customEnd: String = ""
    ): T? = readSusp(null, "getRowOrNullSusp") {
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
    ): T? = read(null, "getRowOrNull") {
        it.getRowOrNull(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    }
    
    
    suspend fun <T : Any> getRowOrNullSusp(                       clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = 1,
                                                                 offset: Int? = null,
                                                              customEnd: String = ""
    ): T? = readSusp(null, "getRowOrNullSusp") {
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
    ): List<T> = read(emptyList(), "getRows") {
        it.getRows(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    suspend inline fun <reified T : Any> getRowsSusp(             table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T) -> T = { it }
    ): List<T> = readSusp(emptyList(), "getRowsSusp") {
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
    ): List<T> = read(emptyList(), "getRows") {
        it.getRows(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    suspend fun <T : Any> getRowsSusp(                            clazz: KClass<T>,
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
    ): List<T> = readSusp(emptyList(), "getRowsSusp") {
        it.getRows(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    
    
    inline fun <reified T : Any> getRowMap(                       table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T) -> T = { it }
    ): Map<Int, T> = read(emptyMap(), "getRowMap") {
        it.getRowMap(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    suspend inline fun <reified T : Any> getRowMapSusp(           table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T) -> T = { it }
    ): Map<Int, T> = readSusp(emptyMap(), "getRowMapSusp") {
        it.getRowMap(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    fun <T : Any> getRowMap(                                      clazz: KClass<T>,
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
    ): Map<Int, T> = read(emptyMap(), "getRowMap") {
        it.getRowMap(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    suspend fun <T : Any> getRowMapSusp(                          clazz: KClass<T>,
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
    ): Map<Int, T> = readSusp(emptyMap(), "getRowMapSusp") {
        it.getRowMap(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    
    
    
    
    
    
    // ============================   D E L E T E,  C L E A R   ============================ \\
    
    inline fun <T> deleteRow(                                           tableName: String,
                                                                            where: String,
                                                                           equals: T,
                                                                            async: Boolean = false
    ) = write("deleteRow", async) {
        it.deleteRow(tableName, where, equals)
    }
    
    suspend fun <T> deleteRowSusp(                                             tableName: String,
                                                                                   where: String,
                                                                                  equals: T
    ) = writeSusp("deleteRowSusp") {
        it.deleteRow(tableName, where, equals)
    }
    
    
    fun deleteRow(                                                 tableName: String,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit
    ) = write("deleteRow", async) {
        it.deleteRow(tableName, where)
    }
    
    
    suspend fun deleteRowSusp(                                     tableName: String,
                                                                       where: WhereBuilder.()->Unit
    ) = writeSusp("deleteRowSusp") {
        it.deleteRow(tableName, where)
    }
    
    
    inline fun deleteRowById(                                                  id: Int,
                                                                        tableName: String,
                                                                            async: Boolean = false
    ) = write("deleteRowById", async) {
        it.deleteRowById(id, tableName)
    }
    
    suspend fun deleteRowByIdSusp(                                                    id: Int,
                                                                               tableName: String
    ) = writeSusp("deleteRowByIdSusp") {
        it.deleteRowById(id, tableName)
    }
    
    
    
    inline fun deleteFirstRow(                                          tableName: String,
                                                                            async: Boolean = false
    ) = write("deleteFirstRow", async) {
        it.deleteFirstRow(tableName)
    }
    
    suspend fun deleteFirstRowSusp(                                            tableName: String
    ) = writeSusp("deleteFirstRowSusp") {
        it.deleteFirstRow(tableName)
    }
    
    
    inline fun deleteLastRow(                                           tableName: String,
                                                                            async: Boolean = false
    ) = write("deleteLastRow", async) {
        it.deleteLastRow(tableName)
    }
    
    suspend fun deleteLastRowSusp(                                             tableName: String
    ) = writeSusp("deleteLastRowSusp") {
        it.deleteLastRow(tableName)
    }
    
    
    inline fun clearTable(                                              tableName: String,
                                                                            async: Boolean = false
    ) = write("clearTable", async) { it.clearTable(tableName) }
    
    suspend fun clearTableSusp(tableName: String) = writeSusp("clearTableSusp") { it.clearTable(tableName) }
    
    
    
    
    // ====================================  C O U N T  ==================================== \\
    
    /** Returns the number of rows matching the query conditions. */
    fun getRowCount(                                          tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {}
    ) = read(0, "getRowCount") {
        it.getRowCount(tableName, joins, where)
    }
    
    /** Returns the number of rows matching the query conditions. */
    suspend fun getRowCountSusp(                              tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {}
    ) = readSusp(0, "getRowCountSusp") {
        it.getRowCount(tableName, joins, where)
    }
    
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRows(                                              tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {}
    ) = read(0, "hasRows") {
        it.hasRows(tableName, joins, where)
    }
    
    /** Returns true if at least one row matches the query conditions. */
    suspend fun hasRowsSusp(                                  tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {}
    ) = readSusp(0, "hasRowsSusp") {
        it.hasRows(tableName, joins, where)
    }
    
    
    
    
    
    
    
    // ====================================  O T H E R  ==================================== \\
    
    inline fun getLastId(tableName: String) = read(0, "getLastId") { it.getLastId(tableName) }
    suspend fun getLastIdSusp(tableName: String) = readSusp(0, "getLastIdSusp") { it.getLastId(tableName) }
    
    
    inline fun getAllIDs(tableName: String) = read(emptyList(), "getAllIDs") { it.getAllIDs(tableName) }
    suspend fun getAllIDsSusp(tableName: String) = readSusp(emptyList(), "getAllIDsSusp") { it.getAllIDs(tableName) }
    
    
    inline fun tableExists(tableName: String) = read(false, "tableExists") { it.tableExists(tableName) }
    suspend fun tableExistsSusp(tableName: String) = readSusp(false, "tableExistsSusp") { it.tableExists(tableName) }
    
    
    inline fun isTableEmpty(tableName: String) = read(true, "isTableEmpty") { it.isTableEmpty(tableName) }
    suspend fun isTableEmptySusp(tableName: String) = readSusp(true, "isTableEmptySusp") { it.isTableEmpty(tableName) }
    
    
    inline fun getLastPriority(tableName: String) = read(0, "getLastPriority") { it.getLastPriority(tableName) }
    suspend fun getLastPrioritySusp(tableName: String) = readSusp(0, "getLastPrioritySusp") { it.getLastPriority(tableName) }
    
    
    
    
    
}