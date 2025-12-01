package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.database.sqlite.transaction
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.ID
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.delete.clearTable
import com.vankorno.vankornodb.delete.deleteFirstRow
import com.vankorno.vankornodb.delete.deleteLastRow
import com.vankorno.vankornodb.delete.deleteRow
import com.vankorno.vankornodb.delete.deleteRowById
import com.vankorno.vankornodb.delete.deleteTable
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
                run(DbProvider.mainDb)
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
                DbProvider.mainDb.transaction { run(this) }
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
                DbProvider.mainDb.transaction { run(this) }
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
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = set(value, table, column, ID, id, async)
    
    suspend fun setByIdSusp(                                                       value: Any,
                                                                                      id: Int,
                                                                                   table: String,
                                                                                  column: String,
    ) = setSusp(value, table, column, ID, id)
    
    
    inline fun <T> set(                                                     value: Any,
                                                                            table: String,
                                                                           column: String,
                                                                            where: String,
                                                                           equals: T,
                                                                            async: Boolean = false,
    ) = write("set", async) {
        it.set(value, table, column, where, equals)
    }
    
    fun <T> set(                                                       value: Any,
                                                                       table: String,
                                                                      column: String,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("set", async) {
        it.set(value, table, column, where)
    }
    
    
    suspend fun <T> setSusp(                                                       value: Any,
                                                                                   table: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: T,
    ) = writeSusp("setSusp") {
        it.set(value, table, column, where, equals)
    }
    
    suspend fun <T> setSusp(                                           value: Any,
                                                                       table: String,
                                                                      column: String,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setSusp") {
        it.set(value, table, column, where)
    }
    
    
    
    // =============================  M U L T I - S E T T E R S  ============================= \\
    
    fun setRowVals(                                                    table: String,
                                                                          cv: ContentValues,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("setRowVals", async) {
        it.setRowVals(table, cv, where)
    }
    
    suspend fun setRowValsSusp(                                        table: String,
                                                                          cv: ContentValues,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setRowValsSusp") {
        it.setRowVals(table, cv, where)
    }
    
    
    fun setRowVals(                                                    table: String,
                                                                       where: WhereBuilder.()->Unit,
                                                                       async: Boolean = false,
                                                               vararg values: Pair<String, Any?>,
    ) = write("setRowVals", async) {
        it.setRowVals(table, where, *values)
    }
    
    suspend fun setRowValsSusp(                                        table: String,
                                                                       where: WhereBuilder.()->Unit,
                                                                       async: Boolean = false,
                                                               vararg values: Pair<String, Any?>,
    ) = writeSusp("setRowValsSusp") { it.setRowVals(table, where, *values) }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun setRowValsById(                                                 id: Int,
                                                                            table: String,
                                                                               cv: ContentValues,
                                                                            async: Boolean = false,
    ) = write("setRowValsById", async) {
        it.setRowValsById(id, table, cv)
    }
    
    suspend fun setRowValsByIdSusp(                                              id: Int,
                                                                              table: String,
                                                                                 cv: ContentValues,
    ) = writeSusp("setRowValsByIdSusp") {
        it.setRowValsById(id, table, cv)
    }
    
    
    inline fun setRowValsById(                                               id: Int,
                                                                          table: String,
                                                                          async: Boolean = false,
                                                                  vararg values: Pair<String, Any?>,
    ) = write("setRowValsById", async) {
        it.setRowValsById(id, table, *values)
    }
    
    suspend fun setRowValsByIdSusp(                                          id: Int,
                                                                          table: String,
                                                                  vararg values: Pair<String, Any?>,
    ) = writeSusp("setRowValsByIdSusp") {
        it.setRowValsById(id, table, *values)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun setInAllRows(                                                value: Any,
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("setInAllRows", async) {
        it.setInAllRows(value, table, column)
    }
    suspend fun setInAllRowsSusp(                                                  value: Any,
                                                                                   table: String,
                                                                                  column: String,
    ) = writeSusp("setInAllRowsSusp") {
        it.setInAllRows(value, table, column)
    }
    
    
    inline fun setRowValsInAllRows(                                       table: String,
                                                                          async: Boolean = false,
                                                                  vararg values: Pair<String, Any?>,
    ) = write("setRowValsInAllRows", async) {
        it.setRowValsInAllRows(table, *values)
    }
    suspend fun setRowValsInAllRowsSusp(                                  table: String,
                                                                  vararg values: Pair<String, Any?>,
    ) = writeSusp("setRowValsInAllRowsSusp") {
        it.setRowValsInAllRows(table, *values)
    }
    
    
    // ---------------------------------  S E T   R O W S  --------------------------------- \\
    
    inline fun <T : DbEntity> insertObj(                                    table: String,
                                                                              obj: T,
                                                                            async: Boolean = false,
    ) = write("insertObj", async) {
        it.insertObj(table, obj)
    }
    
    
    suspend fun <T : DbEntity> insertObjSusp(                                      table: String,
                                                                                     obj: T,
    ): Long = readWriteSusp(-1L, "insertObjSusp") {
        it.insertObj(table, obj)
    }
    
    
    inline fun <T : DbEntity> insertObjects(                                table: String,
                                                                          objects: List<T>,
                                                                            async: Boolean = false,
    ) = write("insertObjects", async) {
        it.insertObjects(table, objects)
    }
    
    
    suspend fun <T : DbEntity> insertObjectsSusp(                                  table: String,
                                                                                 objects: List<T>,
    ): Int = readWriteSusp(0, "insertObjectsSusp") {
        it.insertObjects(table, objects)
    }
    
    
    
    fun <T : DbEntity> updateObj(                                      table: String,
                                                                         obj: T,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("updateObj", async) {
        it.updateObj(table, obj, where)
    }
    
    
    suspend fun <T : DbEntity> updateObjSusp(                          table: String,
                                                                         obj: T,
                                                                       where: WhereBuilder.()->Unit,
    ): Int = readWriteSusp(0, "updateObjSusp") {
        it.updateObj(table, obj, where)
    }
    
    
    inline fun <T : DbEntity> updateObjById(                                   id: Int,
                                                                            table: String,
                                                                              obj: T,
                                                                            async: Boolean = false,
    ) = write("updateObjById", async) {
        it.updateObjById(id, table, obj)
    }
    
    
    suspend fun <T : DbEntity> updateObjByIdSusp(                                     id: Int,
                                                                                   table: String,
                                                                                     obj: T,
    ): Int = readWriteSusp(0, "updateObjByIdSusp") {
        it.updateObjById(id, table, obj)
    }
    
    
    
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
    // Simplified conditions
    
    inline fun <T> getInt(table: String, column: String, whereClause: String, whereArg: T) =
        read(0, "getInt") {
            it.getInt(table, column, whereClause, whereArg)
        }
    suspend fun <T> getIntSusp(table: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0, "getIntSusp") {
            it.getInt(table, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getStr(table: String, column: String, whereClause: String, whereArg: T): String =
        read("", "getStr") {
            it.getStr(table, column, whereClause, whereArg)
        }
    suspend fun <T> getStrSusp(table: String, column: String, whereClause: String, whereArg: T): String =
        readSusp("", "getStrSusp") {
            it.getStr(table, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getBool(table: String, column: String, whereClause: String, whereArg: T) =
        read(false, "getBool") { 
            it.getBool(table, column, whereClause, whereArg)
        }
    suspend fun <T> getBoolSusp(table: String, column: String, whereClause: String, whereArg: T) =
        readSusp(false, "getBoolSusp") {
            it.getBool(table, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getLong(table: String, column: String, whereClause: String, whereArg: T) =
        read(0L, "getLong") { 
            it.getLong(table, column, whereClause, whereArg)
        }
    suspend fun <T> getLongSusp(table: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0L, "getLongSusp") {
            it.getLong(table, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getFloat(table: String, column: String, whereClause: String, whereArg: T) =
        read(0F, "getFloat") { 
            it.getFloat(table, column, whereClause, whereArg)
        }
    suspend fun <T> getFloatSusp(table: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0F, "getFloatSusp") {
            it.getFloat(table, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getBlob(table: String, column: String, whereClause: String, whereArg: T) =
        read(null, "getBlob") {
            it.getBlob(table, column, whereClause, whereArg)
        }
    suspend fun <T> getBlobSusp(table: String, column: String, whereClause: String, whereArg: T): ByteArray? =
        readSusp(null, "getBlobSusp") {
            it.getBlob(table, column, whereClause, whereArg)
        }
    
    
    // DSL
    
    fun getInt(table: String, column: String, where: WhereBuilder.()->Unit) =
        read(0, "getInt") {
            it.getInt(table, column, where)
        }
    suspend fun getIntSusp(table: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(0, "getIntSusp") {
            it.getInt(table, column, where)
        }
    
    
    fun getStr(table: String, column: String, where: WhereBuilder.()->Unit): String =
        read("", "getStr") {
            it.getStr(table, column, where)
        }
    suspend fun getStrSusp(table: String, column: String, where: WhereBuilder.()->Unit): String =
        readSusp("", "getStrSusp") {
            it.getStr(table, column, where)
        }
    
    
    fun getBool(table: String, column: String, where: WhereBuilder.()->Unit) =
        read(false, "getBool") {
            it.getBool(table, column, where)
        }
    suspend fun getBoolSusp(table: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(false, "getBoolSusp") {
            it.getBool(table, column, where)
        }
    
    
    fun getLong(table: String, column: String, where: WhereBuilder.()->Unit) =
        read(0L, "getLong") {
            it.getLong(table, column, where)
        }
    suspend fun getLongSusp(table: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(0L, "getLongSusp") {
            it.getLong(table, column, where)
        }
    
    
    fun getFloat(table: String, column: String, where: WhereBuilder.()->Unit) =
        read(0F, "getFloat") {
            it.getFloat(table, column, where)
        }
    suspend fun getFloatSusp(table: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(0F, "getFloatSusp") {
            it.getFloat(table, column, where)
        }
    
    
    fun getBlob(table: String, column: String, where: WhereBuilder.()->Unit) =
        read(null, "getBlob") {
            it.getBlob(table, column, where)
        }
    suspend fun getBlobSusp(table: String, column: String, where: WhereBuilder.()->Unit): ByteArray? =
        readSusp(null, "getBlobSusp") {
            it.getBlob(table, column, where)
        }
    
    
    
    // By ID
    
    inline fun getIntById(id: Int, table: String, column: String) = getInt(table, column, ID, id)
    suspend fun getIntByIdSusp(id: Int, table: String, column: String) = getIntSusp(table, column, ID, id)
    
    inline fun getStrById(id: Int, table: String, column: String) = getStr(table, column, ID, id)
    suspend fun getStrByIdSusp(id: Int, table: String, column: String) = getStrSusp(table, column, ID, id)
    
    inline fun getBoolById(id: Int, table: String, column: String) = getBool(table, column, ID, id)
    suspend fun getBoolByIdSusp(id: Int, table: String, column: String) = getBoolSusp(table, column, ID, id)
    
    inline fun getLongById(id: Int, table: String, column: String) = getLong(table, column, ID, id)
    suspend fun getLongByIdSusp(id: Int, table: String, column: String) = getLongSusp(table, column, ID, id)
    
    inline fun getFloatById(id: Int, table: String, column: String) = getFloat(table, column, ID, id)
    suspend fun getFloatByIdSusp(id: Int, table: String, column: String) = getFloatSusp(table, column, ID, id)
    
    inline fun getBlobById(id: Int, table: String, column: String): ByteArray? = getBlob(table, column, ID, id)
    suspend fun getBlobByIdSusp(id: Int, table: String, column: String): ByteArray? = getBlobSusp(table, column, ID, id)
    
    
    // Multiple values
    
    inline fun <reified T : Any> getMultiRowVals(                 table: String,
                                                                columns: Array<out String>,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
    ): List<List<T?>> = read(emptyList(), "getMultiRowVals") {
        it.getMultiRowVals(table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    }
    
    suspend inline fun <reified T : Any> getMultiRowValsSusp(     table: String,
                                                                columns: Array<out String>,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
    ): List<List<T?>> = readSusp(emptyList(), "getMultiRowValsSusp") {
        it.getMultiRowVals(table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd)
    }
    
    
    
    inline fun <reified T : Any> getRowVals(                           table: String,
                                                              noinline where: WhereBuilder.()->Unit,
                                                              vararg columns: String,
    ): List<T?> = read(emptyList(), "getRowVals") {
        it.getRowVals(table, where, *columns)
    }
    
    suspend inline fun <reified T : Any> getRowValsSusp(               table: String,
                                                              noinline where: WhereBuilder.()->Unit,
                                                              vararg columns: String,
    ): List<T?> = readSusp(emptyList(), "getRowValsSusp") {
        it.getRowVals(table, where, *columns)
    }
    
    
    
    
    
    
    // -------------------------------  G E T   O B J E C T S  ------------------------------- \\
    
    inline fun <reified T : DbEntity> getObjOrNullById(                               id: Int,
                                                                                   table: String,
    ): T? = read(null, "getObjOrNullById") {
        it.getObjOrNullById<T>(id, table)
    }
    
    suspend inline fun <reified T : DbEntity> getObjOrNullByIdSusp(                   id: Int,
                                                                                   table: String,
    ): T? = readSusp(null, "getObjOrNullByIdSusp") {
        it.getObjOrNullById<T>(id, table)
    }
    
    
    inline fun <reified T : DbEntity> getObjOrNull(               table: String,
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
    
    
    suspend inline fun <reified T : DbEntity> getObjOrNullSusp(
                                                                  table: String,
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
    
    
    fun <T : DbEntity> getObjOrNull(                              clazz: KClass<T>,
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
    
    
    suspend fun <T : DbEntity> getObjOrNullSusp(                  clazz: KClass<T>,
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
    
    
    
    inline fun <reified T : DbEntity> getObjects(                 table: String,
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
    
    suspend inline fun <reified T : DbEntity> getObjectsSusp(     table: String,
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
    
    fun <T : DbEntity> getObjects(                                clazz: KClass<T>,
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
    
    suspend fun <T : DbEntity> getObjectsSusp(                    clazz: KClass<T>,
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
    
    
    
    inline fun <reified T : DbEntity> getObjMap(                  table: String,
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
    
    suspend inline fun <reified T : DbEntity> getObjMapSusp(      table: String,
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
    
    fun <T : DbEntity> getObjMap(                                 clazz: KClass<T>,
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
    
    suspend fun <T : DbEntity> getObjMapSusp(                     clazz: KClass<T>,
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
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("addToInt", async) {
        it.addToInt(addend, id, table, column)
    }
    
    suspend fun addToIntSusp(                                                     addend: Number,
                                                                                      id: Int,
                                                                                   table: String,
                                                                                  column: String,
    ) = writeSusp("addToIntSusp") {
        it.addToInt(addend, id, table, column)
    }
    
    
    inline fun addToLong(                                                  addend: Number,
                                                                               id: Int,
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("addToLong", async) {
        it.addToLong(addend, id, table, column)
    }
    
    suspend fun addToLongSusp(                                                    addend: Number,
                                                                                      id: Int,
                                                                                   table: String,
                                                                                  column: String,
    ) = writeSusp("addToLongSusp") {
        it.addToLong(addend, id, table, column)
    }
    
    
    inline fun addToFloat(                                                 addend: Number,
                                                                               id: Int,
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("addToFloat", async) {
        it.addToFloat(addend, id, table, column)
    }
    
    suspend fun addToFloatSusp(                                                   addend: Number,
                                                                                      id: Int,
                                                                                   table: String,
                                                                                  column: String,
    ) = writeSusp("addToFloatSusp") {
        it.addToFloat(addend, id, table, column)
    }
    
    
    
    inline fun toggleBool(                                                     id: Int,
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("toggleBool", async) {
        it.toggleBool(id, table, column)
    }
    
    suspend fun toggleBoolSusp(                                                       id: Int,
                                                                                   table: String,
                                                                                  column: String,
    ) = readWriteSusp(Unit, "toggleBoolSusp") {
        it.toggleBool(id, table, column)
    }

    
    
    
    
    
    
    
    
    
    
    
    // ============================   D E L E T E,  C L E A R   ============================ \\
    
    inline fun <T> deleteRow(                                               table: String,
                                                                            where: String,
                                                                           equals: T,
                                                                            async: Boolean = false,
    ) = write("deleteRow", async) {
        it.deleteRow(table, where, equals)
    }
    
    suspend fun <T> deleteRowSusp(                                                 table: String,
                                                                                   where: String,
                                                                                  equals: T,
    ) = writeSusp("deleteRowSusp") {
        it.deleteRow(table, where, equals)
    }
    
    
    fun deleteRow(                                                     table: String,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("deleteRow", async) {
        it.deleteRow(table, where)
    }
    
    
    suspend fun deleteRowSusp(                                         table: String,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("deleteRowSusp") {
        it.deleteRow(table, where)
    }
    
    
    inline fun deleteRowById(                                                  id: Int,
                                                                            table: String,
                                                                            async: Boolean = false,
    ) = write("deleteRowById", async) {
        it.deleteRowById(id, table)
    }
    
    suspend fun deleteRowByIdSusp(                                                    id: Int,
                                                                                   table: String,
    ) = writeSusp("deleteRowByIdSusp") {
        it.deleteRowById(id, table)
    }
    
    
    
    inline fun deleteFirstRow(                                              table: String,
                                                                            async: Boolean = false,
    ) = write("deleteFirstRow", async) {
        it.deleteFirstRow(table)
    }
    
    suspend fun deleteFirstRowSusp(                                                table: String,
    ) = writeSusp("deleteFirstRowSusp") {
        it.deleteFirstRow(table)
    }
    
    
    inline fun deleteLastRow(                                               table: String,
                                                                            async: Boolean = false,
    ) = write("deleteLastRow", async) {
        it.deleteLastRow(table)
    }
    
    suspend fun deleteLastRowSusp(                                                 table: String,
    ) = writeSusp("deleteLastRowSusp") {
        it.deleteLastRow(table)
    }
    
    
    inline fun clearTable(                                                  table: String,
                                                                            async: Boolean = false,
    ) = write("clearTable", async) { it.clearTable(table) }
    
    suspend fun clearTableSusp(table: String) = writeSusp("clearTableSusp") { it.clearTable(table) }
    
    
    inline fun deleteTable(                                                 table: String,
                                                                            async: Boolean = false,
    ) = write("deleteTable", async) { it.deleteTable(table) }
    
    suspend fun deleteTableSusp(table: String) = writeSusp("deleteTableSusp") { it.deleteTable(table) }
    
    
    
    
    
    
    // ====================================  C O U N T  ==================================== \\
    
    /** Returns the number of rows matching the query conditions. */
    fun getRowCount(                                              table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = read(0, "getRowCount") {
        it.getRowCount(table, joins, where)
    }
    
    /** Returns the number of rows matching the query conditions. */
    suspend fun getRowCountSusp(                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = readSusp(0, "getRowCountSusp") {
        it.getRowCount(table, joins, where)
    }
    
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRows(                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = read(false, "hasRows") {
        it.hasRows(table, joins, where)
    }
    
    /** Returns true if at least one row matches the query conditions. */
    suspend fun hasRowsSusp(                                      table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = readSusp(false, "hasRowsSusp") {
        it.hasRows(table, joins, where)
    }
    
    
    
    
    
    
    
    // ====================================  O T H E R  ==================================== \\
    
    inline fun getLastId(table: String) = read(0, "getLastId") { it.getLastId(table) }
    suspend fun getLastIdSusp(table: String) = readSusp(0, "getLastIdSusp") { it.getLastId(table) }
    
    
    inline fun getAllIDs(table: String) = read(emptyList(), "getAllIDs") { it.getAllIDs(table) }
    suspend fun getAllIDsSusp(table: String) = readSusp(emptyList(), "getAllIDsSusp") { it.getAllIDs(table) }
    
    
    inline fun tableExists(table: String) = read(false, "tableExists") { it.tableExists(table) }
    suspend fun tableExistsSusp(table: String) = readSusp(false, "tableExistsSusp") { it.tableExists(table) }
    
    
    inline fun isTableEmpty(table: String) = read(true, "isTableEmpty") { it.isTableEmpty(table) }
    suspend fun isTableEmptySusp(table: String) = readSusp(true, "isTableEmptySusp") { it.isTableEmpty(table) }
    
    
    inline fun getAppTableNames() = read(false, "getAppTableNames") { it.getAppTableNames() }
    suspend fun getAppTableNamesSusp() = readSusp(false, "getAppTableNamesSusp") { it.getAppTableNames() }
    
    inline fun getInternalTableNames() = read(false, "getInternalTableNames") { it.getInternalTableNames() }
    suspend fun getInternalTableNamesSusp() = readSusp(false, "getInternalTableNamesSusp") { it.getInternalTableNames() }
    
    
    inline fun getLastPosition(table: String) = read(0, "getLastPosition") { it.getLastPosition(table) }
    suspend fun getLastPositionSusp(table: String) = readSusp(0, "getLastPositionSusp") { it.getLastPosition(table) }
    
    
    inline fun <T> getLastPositionBy(                                              table: String,
                                                                             whereColumn: String,
                                                                                  equals: T,
    ) = read(0, "getLastPositionBy") {
        it.getLastPositionBy(table, whereColumn, equals)
    }
    
    suspend fun <T> getLastPositionBySusp(                                         table: String,
                                                                             whereColumn: String,
                                                                                  equals: T,
    ) = readSusp(0, "getLastPositionBySusp") {
        it.getLastPositionBy(table, whereColumn, equals)
    }
    
    
    
    
    
    inline fun <T> getLargestInt(                                            table: String,
                                                                      targetColumn: String,
                                                                       whereColumn: String? = null,
                                                                            equals: T? = null,
    ) = read(0, "getLargestInt") {
        it.getLargestInt(table, targetColumn, whereColumn, equals)
    }
    
    suspend fun <T> getLargestIntSusp(                                       table: String,
                                                                      targetColumn: String,
                                                                       whereColumn: String? = null,
                                                                            equals: T? = null,
    ) = readSusp(0, "getLargestIntSusp") {
        it.getLargestInt(table, targetColumn, whereColumn, equals)
    }
    
    
    inline fun getDbFileName() = read("", "getDbFileName") { it.getDbFileName() }
    suspend fun getDbFileNameSusp() = readSusp("", "getDbFileNameSusp") { it.getDbFileName() }
    
    
    
    
    
    inline fun <reified T> getRandomVal(                          table: String,
                                                                 column: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
    ): T? = read(null, "getRandomVal") { db ->
        db.getRandomVal<T>(table, column, where)
    }
    
    suspend inline fun <reified T> getRandomValSusp(              table: String,
                                                                 column: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
    ): T? = readSusp(null, "getRandomValSusp") { db ->
        db.getRandomVal<T>(table, column, where)
    }
    
    
    fun getRandomId(                                              table: String,
                                                                  where: WhereBuilder.()->Unit = {},
    ): Int = read(-1, "getRandomId") { db ->
        db.getRandomId(table, where)
    }
    
    suspend fun getRandomIdSusp(                                  table: String,
                                                                  where: WhereBuilder.()->Unit = {},
    ): Int = readSusp(-1, "getRandomIdSusp") { db ->
        db.getRandomId(table, where)
    }
    
    
    inline fun <reified T : DbEntity> getRandomObj(               table: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
    ): T? = read(null, "getRandomObj") { db ->
        db.getRandomObj<T>(table, where)
    }
    
    suspend inline fun <reified T : DbEntity> getRandomObjSusp(
                                                                  table: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
    ): T? = readSusp(null, "getRandomObjSusp") { db ->
        db.getRandomObj<T>(table, where)
    }
    
    
    
    
    
    
    
}