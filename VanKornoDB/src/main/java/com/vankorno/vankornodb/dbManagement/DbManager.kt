/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.vankorno.vankornodb.add.addEntity
import com.vankorno.vankornodb.api.DbLock
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.api.createTables
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_EntityVersions
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.dbManagement.migration.data.EntityVersion
import com.vankorno.vankornodb.dbManagement.migration.data.TEntityVersions
import com.vankorno.vankornodb.dbManagement.migration.data._TTTEntityVersion
import com.vankorno.vankornodb.delete.deleteRows
import com.vankorno.vankornodb.get.getColStrings
import com.vankorno.vankornodb.get.hasRows
import com.vankorno.vankornodb.get.isTableEmpty
import com.vankorno.vankornodb.get.tableExists
import com.vankorno.vankornodb.misc.dLog
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.unitTestCase
import com.vankorno.vankornodb.newTable.createExclusiveTablesInternal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

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
 * - [closeDb] closes the database, invalidates [currDb], cancels coroutine scope [dbScope].
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
 * 
 * @property entityMeta the collection of all entity metadata used for version tracking
 *   and automatic migration.
 * 
 * @property createExclusiveTables if true, [onCreate] automatically creates exclusive tables
 *   for entities defined in [entityMeta]. Just define tables in Meta, and you don't have to put them
 *   anywhere else to ensure they are created whenever the db gets initialized (if they don't already exist).
 * 
 * @property mustHaveTables Additional tables that have to be created with the db
 *   (the ones you'd usually put in onCreate and maybe somewhere else, to ensure their existence).
 *   These tables might not be a part of [entityMeta], therefore not being covered by [createExclusiveTables].
 *   But if they are covered by [createExclusiveTables], then there's no need to put them here.
 * 
 * @property runOnCreate lambda executed during database creation for custom logic.
 * 
 * @property runOnUpgrade lambda executed during database upgrade for custom logic.
 */

abstract class DbManager(         dbName: String,
                               dbVersion: Int,
                                 context: Context? = null,
                          val entityMeta: Collection<BaseEntityMeta>,
               val createExclusiveTables: Boolean = true,
                      val mustHaveTables: List<TableInfoNormal<out NormalEntity>> = emptyList(),
                                val lock: DbLock,
                         val runOnCreate: (SQLiteDatabase)->Unit = {},
                        val runOnUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
    
    private var db: SQLiteDatabase? = null
    private var versionTableHandled = false
    
    val dbScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    @PublishedApi
    internal val currDb: SQLiteDatabase
        get() = db ?: error("Database not initialized")
    
    
    init {
        if (!unitTestCase) {
            db = writableDatabase
            lock.withLock {
                if (!versionTableHandled)
                    ensureDbStructure(currDb)
            }
        }
    }
    
    
    private fun ensureDbStructure(                                               db: SQLiteDatabase
    ) {
        handleVersionTable(db)
        
        if (createExclusiveTables)
            db.createExclusiveTablesInternal(entityMeta)
        
        if (mustHaveTables.isNotEmpty())
            db.createTables(mustHaveTables)
    }
    
    
    override fun onCreate(                                                      db: SQLiteDatabase
    ) {
        // region LOG
            dLog("onCreate runs")
        // endregion
        lock.withLock {
            db.createTable(_TTTEntityVersion)
            ensureDbStructure(db)
            
            runOnCreate(db)
        }
    }
    
    
    
    override fun onUpgrade(                                                     db: SQLiteDatabase,
                                                                        oldVersion: Int,
                                                                        newVersion: Int,
    ) {
        if (oldVersion >= newVersion)  return  //\/\/\/\/\/\
        // region LOG
            dLog("onUpgrade() Migrating...")
        // endregion
        lock.withLock {
            db.beginTransaction()
            try {
                ensureDbStructure(db)
                
                runOnUpgrade(db, oldVersion)
                
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                // region LOG
                    eLog("onUpgrade() failed: ${e.message}", e)
                // endregion
                throw e
            } finally {
                db.endTransaction()
            }
        }
    }
    
    
    fun closeDb() {
        // region LOG
            dLog("closeDb()")
        // endregion
        lock.withLock {
            db?.close()
            db = null
        }
        dbScope.cancel()
    }
    
    
    private fun handleVersionTable(                                              db: SQLiteDatabase
    ) {
        // region LOG
            dLog("handleVersionTable() runs")
        // endregion
        versionTableHandled = true
        ensureEnttVerTableExists(db)
        
        val empty = db.isTableEmpty(TABLE_EntityVersions)
        
        if (empty) {
            // region LOG
                dLog("handleVersionTable(): Empty EntityVersions table. Adding all entity data.")
            // endregion
            addAllEntityMeta(db)
            return //\/\/\/\/\/\
        }
        // region LOG
            dLog("handleVersionTable(): EntityVersions table already has data. Running a cleanup and adding missing data.")
        // endregion
        cleanupVersionTable(db)
        addMissingEntityMeta(db)
    }
    
    
    private fun ensureEnttVerTableExists(                                        db: SQLiteDatabase
    ) {
        if (!db.tableExists(TABLE_EntityVersions)) {
            db.createTable(_TTTEntityVersion)
        }
    }
    
    private fun addAllEntityMeta(                                                db: SQLiteDatabase
    ) {
        for (meta in entityMeta) {
            db.addEntity(
                _TTTEntityVersion,
                EntityVersion(meta.dbRowName, meta.currVersion)
            )
        }
    }
    
    private fun cleanupVersionTable(                                             db: SQLiteDatabase
    ) {
        val allNames = db.getColStrings(TEntityVersions, { Name })
        if (allNames.isEmpty()) return //\/\/\/\/\/\
        
        val namesToDelete = allNames.filter { name ->
            entityMeta.none { meta -> meta.dbRowName == name }
        }
        for (name in namesToDelete) {
            db.deleteRows(TEntityVersions) { Name equal name }
        }
    }
    
    private fun addMissingEntityMeta(                                            db: SQLiteDatabase
    ) {
        for (meta in entityMeta) {
            val dbName = meta.dbRowName
            val rowExists = db.hasRows(TEntityVersions) { Name equal dbName }
            if (rowExists)
                continue //\/\/\
            
            db.addEntity(
                _TTTEntityVersion,
                EntityVersion(dbName, meta.currVersion)
            )
        }
    }
    
    
    
    
    
    
    
}
