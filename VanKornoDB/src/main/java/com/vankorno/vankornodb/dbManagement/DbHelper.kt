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
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.getSet.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

@Suppress("NOTHING_TO_INLINE", "unused")
open class DbHelper(              context: Context,
                                   dbName: String,
                                dbVersion: Int,
                               entityMeta: Collection<BaseEntityMeta>,
                                 onCreate: (SQLiteDatabase)->Unit = {},
                                onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
    
) : DbMaker(context, dbName, dbVersion, entityMeta, onCreate, onUpgrade) {
    
    /**
     * Better reading performance, optimal for reading, but writing can also be done in a less safe way.
     */
    @JvmOverloads
    fun <T> read(                                                 defaultValue: T,
                                                                       funName: String = "read",
                                                                           run: (SQLiteDatabase)->T,
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
                                                                        run: (SQLiteDatabase)->Unit,
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
                                                                        run: (SQLiteDatabase)->Unit,
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
                                                                        run: (SQLiteDatabase)->T,
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
                                                                         run: (SQLiteDatabase)->T,
    ): T = withContext(Dispatchers.IO) {
        readBase(defaultValue, funName, run)
    }
    
    /**
     * Same as readDB, but does not return anything (for reading db and setting some values from the inside).
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun voidReadSusp(                                     funName: String = "voidReadSusp",
                                                                      run: (SQLiteDatabase)->Unit,
    ) = withContext(Dispatchers.IO) {
        readBase(Unit, funName){ run(it) }
    }
    
    /**
     * Void (can read or write db, but doesn't return anything), with writing overhead.
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun writeSusp(                                          funName: String = "writeSusp",
                                                                        run: (SQLiteDatabase)->Unit,
    ) = withContext(Dispatchers.IO) {
        writeBase(funName) { run(it) }
    }
    
    /**
     * All-mighty, but with writing overhead.
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun <T> readWriteSusp(                          defaultValue: T,
                                                                 funName: String = "readWriteSusp",
                                                                     run: (SQLiteDatabase)->T,
    ): T = withContext(Dispatchers.IO) {
        readWriteBase(defaultValue, funName, run)
    }
    
    
    
    // ====================================  B A S E D  ==================================== \\
    
    inline fun <T> readBase(                                      defaultValue: T,
                                                                       funName: String = "read",
                                                                           run: (SQLiteDatabase)->T,
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
                                                                        run: (SQLiteDatabase)->Unit,
    ) = readBase(Unit, funName){ run(it) }
    
    
    inline fun writeBase(                                           funName: String = "write",
                                                                        run: (SQLiteDatabase)->Unit,
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
                                                                        run: (SQLiteDatabase)->T,
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
                                                                            async: Boolean = false,
    ) = set(value, tableName, column, ID, id, async)
    
    suspend fun setByIdSusp(                                                       value: Any,
                                                                                      id: Int,
                                                                               tableName: String,
                                                                                  column: String,
    ) = setSusp(value, tableName, column, ID, id)
    
    
    inline fun <T> set(                                                     value: Any,
                                                                        tableName: String,
                                                                           column: String,
                                                                            where: String,
                                                                           equals: T,
                                                                            async: Boolean = false,
    ) = write("set", async) {
        it.set(value, tableName, column, where, equals)
    }
    
    fun <T> set(                                                       value: Any,
                                                                   tableName: String,
                                                                      column: String,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("set", async) {
        it.set(value, tableName, column, where)
    }
    
    
    suspend fun <T> setSusp(                                                       value: Any,
                                                                               tableName: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: T,
    ) = writeSusp("setSusp") {
        it.set(value, tableName, column, where, equals)
    }
    
    suspend fun <T> setSusp(                                           value: Any,
                                                                   tableName: String,
                                                                      column: String,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setSusp") {
        it.set(value, tableName, column, where)
    }
    
    
    
    // =============================  M U L T I - S E T T E R S  ============================= \\
    
    fun setRowVals(                                                tableName: String,
                                                                          cv: ContentValues,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("setRowVals", async) {
        it.setRowVals(tableName, cv, where)
    }
    
    suspend fun setRowValsSusp(                                    tableName: String,
                                                                          cv: ContentValues,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setRowValsSusp") {
        it.setRowVals(tableName, cv, where)
    }
    
    
    fun setRowVals(                                                tableName: String,
                                                                       where: WhereBuilder.()->Unit,
                                                                       async: Boolean = false,
                                                               vararg values: Pair<String, Any?>,
    ) = write("setRowVals", async) {
        it.setRowVals(tableName, where, *values)
    }
    
    suspend fun setRowValsSusp(                                    tableName: String,
                                                                       where: WhereBuilder.()->Unit,
                                                                       async: Boolean = false,
                                                               vararg values: Pair<String, Any?>,
    ) = writeSusp("setRowValsSusp") { it.setRowVals(tableName, where, *values) }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun setRowValsById(                                                 id: Int,
                                                                        tableName: String,
                                                                               cv: ContentValues,
                                                                            async: Boolean = false,
    ) = write("setRowValsById", async) {
        it.setRowValsById(id, tableName, cv)
    }
    
    suspend fun setRowValsByIdSusp(                                              id: Int,
                                                                          tableName: String,
                                                                                 cv: ContentValues,
    ) = writeSusp("setRowValsByIdSusp") {
        it.setRowValsById(id, tableName, cv)
    }
    
    
    inline fun setRowValsById(                                               id: Int,
                                                                      tableName: String,
                                                                          async: Boolean = false,
                                                                  vararg values: Pair<String, Any?>,
    ) = write("setRowValsById", async) {
        it.setRowValsById(id, tableName, *values)
    }
    
    suspend fun setRowValsByIdSusp(                                          id: Int,
                                                                      tableName: String,
                                                                  vararg values: Pair<String, Any?>,
    ) = writeSusp("setRowValsByIdSusp") {
        it.setRowValsById(id, tableName, *values)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun setInAllRows(                                                value: Any,
                                                                        tableName: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("setInAllRows", async) {
        it.setInAllRows(value, tableName, column)
    }
    suspend fun setInAllRowsSusp(                                                  value: Any,
                                                                               tableName: String,
                                                                                  column: String,
    ) = writeSusp("setInAllRowsSusp") {
        it.setInAllRows(value, tableName, column)
    }
    
    
    inline fun setRowValsInAllRows(                                   tableName: String,
                                                                          async: Boolean = false,
                                                                  vararg values: Pair<String, Any?>,
    ) = write("setRowValsInAllRows", async) {
        it.setRowValsInAllRows(tableName, *values)
    }
    suspend fun setRowValsInAllRowsSusp(                              tableName: String,
                                                                  vararg values: Pair<String, Any?>,
    ) = writeSusp("setRowValsInAllRowsSusp") {
        it.setRowValsInAllRows(tableName, *values)
    }
    
    
    // ---------------------------------  S E T   R O W S  --------------------------------- \\
    
    inline fun <T : Any> insertObj(                                     tableName: String,
                                                                              obj: T,
                                                                            async: Boolean = false,
    ) = write("insertObj", async) {
        it.insertObj(tableName, obj)
    }
    
    
    suspend fun <T : Any> insertObjSusp(                                       tableName: String,
                                                                                     obj: T,
    ): Long = readWriteSusp(-1L, "insertObjSusp") {
        it.insertObj(tableName, obj)
    }
    
    
    inline fun <T : Any> insertObjects(                                 tableName: String,
                                                                          objects: List<T>,
                                                                            async: Boolean = false,
    ) = write("insertObjects", async) {
        it.insertObjects(tableName, objects)
    }
    
    
    suspend fun <T : Any> insertObjectsSusp(                                   tableName: String,
                                                                                 objects: List<T>,
    ): Int = readWriteSusp(0, "insertObjectsSusp") {
        it.insertObjects(tableName, objects)
    }
    
    
    
    fun <T : Any> updateObj(                                       tableName: String,
                                                                         obj: T,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("updateObj", async) {
        it.updateObj(tableName, obj, where)
    }
    
    
    suspend fun <T : Any> updateObjSusp(                           tableName: String,
                                                                         obj: T,
                                                                       where: WhereBuilder.()->Unit,
    ): Int = readWriteSusp(0, "updateObjSusp") {
        it.updateObj(tableName, obj, where)
    }
    
    
    inline fun <T : Any> updateObjById(                                        id: Int,
                                                                        tableName: String,
                                                                              obj: T,
                                                                            async: Boolean = false,
    ) = write("updateObjById", async) {
        it.updateObjById(id, tableName, obj)
    }
    
    
    suspend fun <T : Any> updateObjByIdSusp(                                          id: Int,
                                                                               tableName: String,
                                                                                     obj: T,
    ): Int = readWriteSusp(0, "updateObjByIdSusp") {
        it.updateObjById(id, tableName, obj)
    }
    
    
    
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
    // Simplified conditions
    
    inline fun <T> getInt(tableName: String, column: String, whereClause: String, whereArg: T) =
        read(0, "getInt") {
            it.getInt(tableName, column, whereClause, whereArg)
        }
    suspend fun <T> getIntSusp(tableName: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0, "getIntSusp") {
            it.getInt(tableName, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getStr(tableName: String, column: String, whereClause: String, whereArg: T): String =
        read("", "getStr") {
            it.getStr(tableName, column, whereClause, whereArg)
        }
    suspend fun <T> getStrSusp(tableName: String, column: String, whereClause: String, whereArg: T): String =
        readSusp("", "getStrSusp") {
            it.getStr(tableName, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getBool(tableName: String, column: String, whereClause: String, whereArg: T) =
        read(false, "getBool") { 
            it.getBool(tableName, column, whereClause, whereArg)
        }
    suspend fun <T> getBoolSusp(tableName: String, column: String, whereClause: String, whereArg: T) =
        readSusp(false, "getBoolSusp") {
            it.getBool(tableName, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getLong(tableName: String, column: String, whereClause: String, whereArg: T) =
        read(0L, "getLong") { 
            it.getLong(tableName, column, whereClause, whereArg)
        }
    suspend fun <T> getLongSusp(tableName: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0L, "getLongSusp") {
            it.getLong(tableName, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getFloat(tableName: String, column: String, whereClause: String, whereArg: T) =
        read(0F, "getFloat") { 
            it.getFloat(tableName, column, whereClause, whereArg)
        }
    suspend fun <T> getFloatSusp(tableName: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0F, "getFloatSusp") {
            it.getFloat(tableName, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getBlob(tableName: String, column: String, whereClause: String, whereArg: T) =
        read(null, "getBlob") {
            it.getBlob(tableName, column, whereClause, whereArg)
        }
    suspend fun <T> getBlobSusp(tableName: String, column: String, whereClause: String, whereArg: T): ByteArray? =
        readSusp(null, "getBlobSusp") {
            it.getBlob(tableName, column, whereClause, whereArg)
        }
    
    
    // DSL
    
    fun getInt(tableName: String, column: String, where: WhereBuilder.()->Unit) =
        read(0, "getInt") {
            it.getInt(tableName, column, where)
        }
    suspend fun getIntSusp(tableName: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(0, "getIntSusp") {
            it.getInt(tableName, column, where)
        }
    
    
    fun getStr(tableName: String, column: String, where: WhereBuilder.()->Unit): String =
        read("", "getStr") {
            it.getStr(tableName, column, where)
        }
    suspend fun getStrSusp(tableName: String, column: String, where: WhereBuilder.()->Unit): String =
        readSusp("", "getStrSusp") {
            it.getStr(tableName, column, where)
        }
    
    
    fun getBool(tableName: String, column: String, where: WhereBuilder.()->Unit) =
        read(false, "getBool") {
            it.getBool(tableName, column, where)
        }
    suspend fun getBoolSusp(tableName: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(false, "getBoolSusp") {
            it.getBool(tableName, column, where)
        }
    
    
    fun getLong(tableName: String, column: String, where: WhereBuilder.()->Unit) =
        read(0L, "getLong") {
            it.getLong(tableName, column, where)
        }
    suspend fun getLongSusp(tableName: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(0L, "getLongSusp") {
            it.getLong(tableName, column, where)
        }
    
    
    fun getFloat(tableName: String, column: String, where: WhereBuilder.()->Unit) =
        read(0F, "getFloat") {
            it.getFloat(tableName, column, where)
        }
    suspend fun getFloatSusp(tableName: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(0F, "getFloatSusp") {
            it.getFloat(tableName, column, where)
        }
    
    
    fun getBlob(tableName: String, column: String, where: WhereBuilder.()->Unit) =
        read(null, "getBlob") {
            it.getBlob(tableName, column, where)
        }
    suspend fun getBlobSusp(tableName: String, column: String, where: WhereBuilder.()->Unit): ByteArray? =
        readSusp(null, "getBlobSusp") {
            it.getBlob(tableName, column, where)
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
    
    inline fun <reified T : Any> getObjOrNullById(                                    id: Int,
                                                                               tableName: String,
    ): T? = read(null, "getObjOrNullById") {
        it.getObjOrNullById<T>(id, tableName)
    }
    
    suspend inline fun <reified T : Any> getObjOrNullByIdSusp(                        id: Int,
                                                                               tableName: String,
    ): T? = readSusp(null, "getObjOrNullByIdSusp") {
        it.getObjOrNullById<T>(id, tableName)
    }
    
    
    inline fun <reified T : Any> getObjOrNull(                    table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
    ): T? = read(null, "getObjOrNull") {
        it.getObjOrNull<T>(table, joins, where, groupBy, having, orderBy, offset, customEnd)
    }
    
    
    suspend inline fun <reified T : Any> getObjOrNullSusp(        table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
    ): T? = readSusp(null, "getObjOrNullSusp") {
        it.getObjOrNull<T>(table, joins, where, groupBy, having, orderBy, offset, customEnd)
    }
    
    
    fun <T : Any> getObjOrNull(                                   clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
    ): T? = read(null, "getObjOrNull") {
        it.getObjOrNull(clazz, table, joins, where, groupBy, having, orderBy, offset, customEnd)
    }
    
    
    suspend fun <T : Any> getObjOrNullSusp(                       clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
    ): T? = readSusp(null, "getObjOrNullSusp") {
        it.getObjOrNull(clazz, table, joins, where, groupBy, having, orderBy, offset, customEnd)
    }
    
    
    
    inline fun <reified T : Any> getObjects(                      table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T)->T = { it },
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjects(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    suspend inline fun <reified T : Any> getObjectsSusp(          table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T)->T = { it },
    ): List<T> = readSusp(emptyList(), "getObjectsSusp") {
        it.getObjects(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    fun <T : Any> getObjects(                                     clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                               mapAfter: (T)->T = { it },
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjects(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    suspend fun <T : Any> getObjectsSusp(                         clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                               mapAfter: (T)->T = { it },
    ): List<T> = readSusp(emptyList(), "getObjectsSusp") {
        it.getObjects(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    
    
    inline fun <reified T : Any> getObjMap(                       table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T)->T = { it },
    ): Map<Int, T> = read(emptyMap(), "getObjMap") {
        it.getObjMap(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    suspend inline fun <reified T : Any> getObjMapSusp(           table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T)->T = { it },
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapSusp") {
        it.getObjMap(table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    fun <T : Any> getObjMap(                                      clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                               mapAfter: (T)->T = { it },
    ): Map<Int, T> = read(emptyMap(), "getObjMap") {
        it.getObjMap(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    suspend fun <T : Any> getObjMapSusp(                          clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                               mapAfter: (T)->T = { it },
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapSusp") {
        it.getObjMap(clazz, table, joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter)
    }
    
    
    // =====================================   L I S T   ===================================== \\
    
    inline fun <reified T> getList(                               table: String,
                                                                 column: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
    ): List<T> = read(emptyList(), "getList") {
        it.getList(table, column, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    }
    
    suspend inline fun <reified T> getListSusp(                   table: String,
                                                                 column: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
    ): List<T> = readSusp(emptyList(), "getListSusp") {
        it.getList(table, column, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    }
    
    
    
    
    // ===================================   M O D I F Y   =================================== \\
    
    inline fun addToInt(                                                   addend: Number,
                                                                               id: Int,
                                                                        tableName: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("addToInt", async) {
        it.addToInt(addend, id, tableName, column)
    }
    
    suspend fun addToIntSusp(                                                     addend: Number,
                                                                                      id: Int,
                                                                               tableName: String,
                                                                                  column: String,
    ) = writeSusp("addToIntSusp") {
        it.addToInt(addend, id, tableName, column)
    }
    
    
    inline fun addToLong(                                                  addend: Number,
                                                                               id: Int,
                                                                        tableName: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("addToLong", async) {
        it.addToLong(addend, id, tableName, column)
    }
    
    suspend fun addToLongSusp(                                                    addend: Number,
                                                                                      id: Int,
                                                                               tableName: String,
                                                                                  column: String,
    ) = writeSusp("addToLongSusp") {
        it.addToLong(addend, id, tableName, column)
    }
    
    
    inline fun addToFloat(                                                 addend: Number,
                                                                               id: Int,
                                                                        tableName: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("addToFloat", async) {
        it.addToFloat(addend, id, tableName, column)
    }
    
    suspend fun addToFloatSusp(                                                   addend: Number,
                                                                                      id: Int,
                                                                               tableName: String,
                                                                                  column: String,
    ) = writeSusp("addToFloatSusp") {
        it.addToFloat(addend, id, tableName, column)
    }
    
    
    
    inline fun toggleBool(                                                     id: Int,
                                                                        tableName: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("toggleBool", async) {
        it.toggleBool(id, tableName, column)
    }
    
    suspend fun toggleBoolSusp(                                                       id: Int,
                                                                               tableName: String,
                                                                                  column: String,
    ) = readWriteSusp(Unit, "toggleBoolSusp") {
        it.toggleBool(id, tableName, column)
    }

    
    
    
    
    
    
    
    
    
    
    
    // ============================   D E L E T E,  C L E A R   ============================ \\
    
    inline fun <T> deleteRow(                                           tableName: String,
                                                                            where: String,
                                                                           equals: T,
                                                                            async: Boolean = false,
    ) = write("deleteRow", async) {
        it.deleteRow(tableName, where, equals)
    }
    
    suspend fun <T> deleteRowSusp(                                             tableName: String,
                                                                                   where: String,
                                                                                  equals: T,
    ) = writeSusp("deleteRowSusp") {
        it.deleteRow(tableName, where, equals)
    }
    
    
    fun deleteRow(                                                 tableName: String,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("deleteRow", async) {
        it.deleteRow(tableName, where)
    }
    
    
    suspend fun deleteRowSusp(                                     tableName: String,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("deleteRowSusp") {
        it.deleteRow(tableName, where)
    }
    
    
    inline fun deleteRowById(                                                  id: Int,
                                                                        tableName: String,
                                                                            async: Boolean = false,
    ) = write("deleteRowById", async) {
        it.deleteRowById(id, tableName)
    }
    
    suspend fun deleteRowByIdSusp(                                                    id: Int,
                                                                               tableName: String,
    ) = writeSusp("deleteRowByIdSusp") {
        it.deleteRowById(id, tableName)
    }
    
    
    
    inline fun deleteFirstRow(                                          tableName: String,
                                                                            async: Boolean = false,
    ) = write("deleteFirstRow", async) {
        it.deleteFirstRow(tableName)
    }
    
    suspend fun deleteFirstRowSusp(                                            tableName: String,
    ) = writeSusp("deleteFirstRowSusp") {
        it.deleteFirstRow(tableName)
    }
    
    
    inline fun deleteLastRow(                                           tableName: String,
                                                                            async: Boolean = false,
    ) = write("deleteLastRow", async) {
        it.deleteLastRow(tableName)
    }
    
    suspend fun deleteLastRowSusp(                                             tableName: String,
    ) = writeSusp("deleteLastRowSusp") {
        it.deleteLastRow(tableName)
    }
    
    
    inline fun clearTable(                                              tableName: String,
                                                                            async: Boolean = false,
    ) = write("clearTable", async) { it.clearTable(tableName) }
    
    suspend fun clearTableSusp(tableName: String) = writeSusp("clearTableSusp") { it.clearTable(tableName) }
    
    
    
    
    // ====================================  C O U N T  ==================================== \\
    
    /** Returns the number of rows matching the query conditions. */
    fun getRowCount(                                          tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = read(0, "getRowCount") {
        it.getRowCount(tableName, joins, where)
    }
    
    /** Returns the number of rows matching the query conditions. */
    suspend fun getRowCountSusp(                              tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = readSusp(0, "getRowCountSusp") {
        it.getRowCount(tableName, joins, where)
    }
    
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRows(                                              tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = read(false, "hasRows") {
        it.hasRows(tableName, joins, where)
    }
    
    /** Returns true if at least one row matches the query conditions. */
    suspend fun hasRowsSusp(                                  tableName: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = readSusp(false, "hasRowsSusp") {
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
    
    
    inline fun <T> getLastPriorityBy(                                          tableName: String,
                                                                             whereColumn: String,
                                                                                  equals: T,
    ) = read(0, "getLastPriorityBy") {
        it.getLastPriorityBy(tableName, whereColumn, equals)
    }
    
    suspend fun <T> getLastPriorityBySusp(                                     tableName: String,
                                                                             whereColumn: String,
                                                                                  equals: T,
    ) = readSusp(0, "getLastPriorityBySusp") {
        it.getLastPriorityBy(tableName, whereColumn, equals)
    }
    
    
    
    
    
    inline fun <T> getLargestInt(                                        tableName: String,
                                                                      targetColumn: String,
                                                                       whereColumn: String? = null,
                                                                            equals: T? = null,
    ) = read(0, "getLargestInt") {
        it.getLargestInt(tableName, targetColumn, whereColumn, equals)
    }
    
    suspend fun <T> getLargestIntSusp(                                   tableName: String,
                                                                      targetColumn: String,
                                                                       whereColumn: String? = null,
                                                                            equals: T? = null,
    ) = readSusp(0, "getLargestIntSusp") {
        it.getLargestInt(tableName, targetColumn, whereColumn, equals)
    }
    
    
    
}