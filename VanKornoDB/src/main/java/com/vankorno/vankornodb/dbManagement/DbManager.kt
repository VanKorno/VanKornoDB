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
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.migration.data.OrmVersion
import com.vankorno.vankornodb.dbManagement.migration.data.VersionEntity
import com.vankorno.vankornodb.delete.deleteRow
import com.vankorno.vankornodb.get.getColStringsPro
import com.vankorno.vankornodb.get.hasRows
import com.vankorno.vankornodb.get.isTableEmpty
import com.vankorno.vankornodb.get.tableExists
import com.vankorno.vankornodb.misc.data.SharedCol.cName

/**
 * Manages the lifecycle and access of a single SQLite database instance.
 *
 * This class is responsible for creating or opening the database file, initializing
 * the entity version table, applying migrations, and providing safe, synchronized
 * access to the database for reading and writing.
 *
 * Lifecycle overview:
 * - When an instance of [DbManager] is created, the database is opened or created
 *   via [writableDatabase] and assigned to [currDb].
 * - The entity version table is immediately checked and initialized if empty.
 * - If the database is fresh, [onCreate] is called, creating the version table
 *   and executing any custom creation logic provided via [runOnCreate].
 * - On subsequent launches, [onCreate] is skipped, but [handleVersionTable]
 *   ensures entity version consistency and applies any missing metadata.
 * - [onUpgrade] handles database upgrades between schema versions and runs
 *   custom logic provided via [runOnUpgrade], wrapped in a transaction.
 * - The database can be closed via [closeDb], after which [currDb] becomes inaccessible.
 *
 * Thread-safety:
 * All database access should be synchronized on [dbLock]. Use [currDb] within
 * synchronized blocks or through the higher-level read/write functions in [DbReaderWriter].
 *
 * Entity metadata:
 * The [entityMeta] collection defines all entities tracked for versioning and
 * migrations. The system automatically:
 * - Adds missing entities to the version table
 * - Cleans up entities no longer present
 * - Ensures the version table is consistent with the current schema definitions
 *
 * @property dbLock a mutex used to synchronize access to the database instance.
 * @property entityMeta the collection of all entity metadata used for version tracking
 *   and automatic migration.
 * @property runOnCreate a lambda that runs custom logic when the database is created.
 * @property runOnUpgrade a lambda that runs custom logic when the database is upgraded.
 */
abstract class DbManager(        context: Context,
                                  dbName: String,
                               dbVersion: Int,
                          val entityMeta: Collection<BaseEntityMeta>,
                         val runOnCreate: (SQLiteDatabase)->Unit = {},
                        val runOnUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
    
) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
    
    val dbLock = Any()
    
    private var db: SQLiteDatabase? = null
    
    @PublishedApi
    internal val currDb: SQLiteDatabase
        get() = db ?: error("Database not initialized")
    
    
    init {
        db = writableDatabase
        synchronized(dbLock) {
            handleVersionTable(currDb)
        }
    }
    
    
    override fun onCreate(                                                      db: SQLiteDatabase
    ) {
        // region LOG
            Log.d(DbTAG, "onCreate runs")
        // endregion
        synchronized(dbLock) {
            db.createTable(TABLE_EntityVersions, OrmVersion)
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
        synchronized(dbLock) {
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
        synchronized(dbLock) {
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
            db.createTable(TABLE_EntityVersions, OrmVersion)
        }
    }
    
    private fun addAllEntityMeta(                                                db: SQLiteDatabase
    ) {
        for (meta in entityMeta) {
            db.addObj(
                TABLE_EntityVersions,
                VersionEntity(meta.dbRowName, meta.currVersion)
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
                TABLE_EntityVersions,
                VersionEntity(dbName, meta.currVersion)
            )
        }
    }
    
    
    
    
    
    
    
}