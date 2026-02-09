// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.vankorno.vankornodb.add.addObj
import com.vankorno.vankornodb.api.DbLock
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_EntityVersions
import com.vankorno.vankornodb.core.data.DbConstants._Name
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.migration.data.EntityVersion
import com.vankorno.vankornodb.dbManagement.migration.data.TTTEntityVersion
import com.vankorno.vankornodb.delete.deleteRow
import com.vankorno.vankornodb.get.getColStringsPro
import com.vankorno.vankornodb.get.hasRows
import com.vankorno.vankornodb.get.isTableEmpty
import com.vankorno.vankornodb.get.tableExists
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.newTable.createExclusiveTablesInternal

/**
 * Manages the lifecycle and access of a single SQLite database instance.
 *
 * Responsibilities:
 * - Opens or creates the database file via [writableDatabase].
 * - Initializes and maintains the entity version table for migrations.
 * - Provides safe, synchronized access to the database through [lock] for reading and writing.
 *
 * Lifecycle:
 * - When an instance of [DbManager] is created, the database is opened and assigned to [currDb].
 * - The entity version table is checked and initialized via [handleVersionTable].
 * - [onCreate] is called for a fresh database, creating tables and executing any custom logic via [runOnCreate].
 * - [onUpgrade] handles schema migrations between versions, executing [runOnUpgrade] inside a transaction.
 * - [closeDb] closes the database and invalidates [currDb].
 *
 * Thread-safety:
 * All database operations are automatically synchronized using [lock]. External users
 * should not need to lock manually; all read/write functions in [DbReaderWriter] use it.
 *
 * Entity metadata:
 * The [entityMeta] collection defines entities tracked for versioning and migrations.
 * The system automatically:
 * - Adds missing entities to the version table.
 * - Removes entities no longer present.
 * - Ensures the version table matches the current schema definitions.
 *
 * @property lock a [DbLock] instance that guarantees thread-safe access to the database.
 * @property entityMeta the collection of all entity metadata used for version tracking
 *   and automatic migration.
 * @property createExclusiveTables if true, [onCreate] automatically creates exclusive tables
 *   for entities defined in [entityMeta].
 * @property runOnCreate lambda executed during database creation for custom logic.
 * @property runOnUpgrade lambda executed during database upgrade for custom logic.
 */

abstract class DbManager(        context: Context,
                                  dbName: String,
                               dbVersion: Int,
                          val entityMeta: Collection<BaseEntityMeta>,
               val createExclusiveTables: Boolean = true,
                                val lock: DbLock,
                         val runOnCreate: (SQLiteDatabase)->Unit = {},
                        val runOnUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
    
    private var db: SQLiteDatabase? = null
    
    @PublishedApi
    internal val currDb: SQLiteDatabase
        get() = db ?: error("Database not initialized")
    
    
    init {
        db = writableDatabase
        lock.withLock {
            handleVersionTable(currDb)
        }
    }
    
    
    override fun onCreate(                                                      db: SQLiteDatabase
    ) {
        // region LOG
            Log.d(DbTAG, "onCreate runs")
        // endregion
        lock.withLock {
            db.createTable(TTTEntityVersion)
            
            if (createExclusiveTables)
                db.createExclusiveTablesInternal(entityMeta)
            
            runOnCreate(db)
        }
    }
    
    
    
    override fun onUpgrade(                                                     db: SQLiteDatabase,
                                                                        oldVersion: Int,
                                                                        newVersion: Int,
    ) {
        if (oldVersion >= newVersion)  return  //\/\/\/\/\/\
        // region LOG
            Log.d(DbTAG, "onUpgrade() Migrating...")
        // endregion
        lock.withLock {
            db.beginTransaction()
            try {
                runOnUpgrade(db, oldVersion)
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                // region LOG
                    Log.e(DbTAG, "onUpgrade() failed: ${e.message}", e)
                // endregion
                throw e
            } finally {
                db.endTransaction()
            }
        }
    }
    
    
    fun closeDb() {
        // region LOG
            Log.d(DbTAG, "closeDb()")
        // endregion
        lock.withLock {
            db?.close()
            db = null
        }
    }
    
    
    private fun handleVersionTable(                                              db: SQLiteDatabase
    ) {
        // region LOG
            Log.d(DbTAG, "handleVersionTable() runs")
        // endregion
        ensureEnttVerTableExists(db)
        
        val empty = db.isTableEmpty(TABLE_EntityVersions)
        
        if (empty) {
            // region LOG
                Log.d(DbTAG, "handleVersionTable(): Empty EntityVersions table. Adding all entity data.")
            // endregion
            addAllEntityMeta(db)
            return //\/\/\/\/\/\
        }
        // region LOG
            Log.d(DbTAG, "handleVersionTable(): EntityVersions table already has data. Running a cleanup and adding missing data.")
        // endregion
        cleanupVersionTable(db)
        addMissingEntityMeta(db)
    }
    
    
    private fun ensureEnttVerTableExists(                                        db: SQLiteDatabase
    ) {
        if (!db.tableExists(TABLE_EntityVersions)) {
            db.createTable(TTTEntityVersion)
        }
    }
    
    private fun addAllEntityMeta(                                                db: SQLiteDatabase
    ) {
        for (meta in entityMeta) {
            db.addObj(
                TTTEntityVersion,
                EntityVersion(meta.dbRowName, meta.currVersion)
            )
        }
    }
    
    private fun cleanupVersionTable(                                             db: SQLiteDatabase
    ) {
        val allNames = db.getColStringsPro(TABLE_EntityVersions, cName)
        if (allNames.isEmpty()) return //\/\/\/\/\/\
        
        val namesToDelete = allNames.filter { name ->
            entityMeta.none { meta -> meta.dbRowName == name }
        }
        for (name in namesToDelete) {
            db.deleteRow(TABLE_EntityVersions, _Name, name)
        }
    }
    
    private fun addMissingEntityMeta(                                            db: SQLiteDatabase
    ) {
        for (meta in entityMeta) {
            val dbName = meta.dbRowName
            val rowExists = db.hasRows(TABLE_EntityVersions) { cName equal dbName }
            if (rowExists)
                continue //\/\/\
            
            db.addObj(
                TTTEntityVersion,
                EntityVersion(dbName, meta.currVersion)
            )
        }
    }
    
    
    
    
    
    
    
}