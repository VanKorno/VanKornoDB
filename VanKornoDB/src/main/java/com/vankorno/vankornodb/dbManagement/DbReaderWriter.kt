package com.vankorno.vankornodb.dbManagement

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.database.sqlite.transaction
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

abstract class DbReaderWriter(
                                 context: Context,
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
    
    
    
    
    
    
    
}