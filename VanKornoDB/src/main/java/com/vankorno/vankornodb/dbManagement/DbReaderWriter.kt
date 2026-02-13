// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.database.sqlite.transaction
import com.vankorno.vankornodb.api.DbLock
import com.vankorno.vankornodb.api.DbRuntime.dbLock
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class DbReaderWriter(
                                 context: Context,
                                  dbName: String,
                               dbVersion: Int,
                              entityMeta: Collection<BaseEntityMeta>,
                   createExclusiveTables: Boolean = true,
                                    lock: DbLock = dbLock,
                                onCreate: (SQLiteDatabase)->Unit = {},
                               onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
) : DbManager(
    context = context,
    dbName = dbName,
    dbVersion = dbVersion,
    entityMeta = entityMeta,
    createExclusiveTables = createExclusiveTables,
    lock = lock,
    runOnCreate = onCreate,
    runOnUpgrade = onUpgrade,
) {
    /** A shared fun */
    private inline fun writeBase(                                   funName: String = "write",
                                                            crossinline run: (SQLiteDatabase)->Unit,
    ) {
        try {
            lock.withLock {
                currDb.transaction { run(this) }
            }
        } catch (e: Exception) {
            // region LOG
                Log.e(DbTAG, "$funName() failed. Details: ${e.message}", e)
            // endregion
        }
    }
    
    
    /**
     * Better reading performance, optimal for reading, but writing can also be done in a less safe way.
     */
    inline fun <T> read(                                          defaultValue: T,
                                                                       funName: String = "read",
                                                               crossinline run: (SQLiteDatabase)->T,
    ): T {
        return try {
            lock.withLock {
                run(currDb)
            }
        } catch (e: Exception) {
            // region LOG
            Log.e(DbTAG, "$funName() failed. Returning the default value. Details: ${e.message}", e)
            // endregion
            defaultValue
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
                read(Unit, funName) { run(it) }
            }
        } else {
            read(Unit, funName) { run(it) }
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
            writeBase(funName, run)
        }
    }
    
    
    /**
     * All-mighty, but with writing overhead.
     */
    inline fun <T> readWrite(                                    defaultValue: T,
                                                                      funName: String = "readWrite",
                                                              crossinline run: (SQLiteDatabase)->T,
    ): T {
        return try {
            lock.withLock {
                currDb.transaction { run(this) }
            }
        } catch (e: Exception) {
            // region LOG
            Log.e(DbTAG, "$funName() failed. Returning the default value. Details: ${e.message}", e)
            // endregion
            defaultValue
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
        read(defaultValue, funName, run)
    }
    
    
    /**
     * Same as readDB, but does not return anything (for reading db and setting some values from the inside).
     * Suspending non-blocking version (to be used inside coroutines)
     */
    suspend fun voidReadSusp(                                     funName: String = "voidReadSusp",
                                                                      run: (SQLiteDatabase)->Unit,
    ) = withContext(Dispatchers.IO) {
        read(Unit, funName){ run(it) }
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
        readWrite(defaultValue, funName, run)
    }
    
    
    
    
    
    
    
    
    
    
    
    
}