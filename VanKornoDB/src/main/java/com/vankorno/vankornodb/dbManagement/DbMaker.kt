package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.dbManagement.DbManager.mainDb
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.migration.data.VersionEntity
import com.vankorno.vankornodb.getSet.deleteRow
import com.vankorno.vankornodb.getSet.getList
import com.vankorno.vankornodb.getSet.hasRows
import com.vankorno.vankornodb.getSet.insertRow
import com.vankorno.vankornodb.getSet.isTableEmpty


 /* 
 * ==== The lifecycle at runtime:
 * DbMaker() is instantiated → init block runs:
 * DbManager.init(writableDatabase) opens or creates the DB.
 * handleVersionTable(mainDb) runs immediately afterward.
 * If it’s a fresh database:
 * SQLite calls onCreate().
 *    That creates the EntityVersions table.
 *    Then init’s handleVersionTable() runs, sees the empty table, and fills it.
 * On subsequent launches:
 *    onCreate() is skipped (DB already exists).
 *    handleVersionTable() still runs (to ensure consistency).
 */

/**
 * Creates the db file, initializes DbManager, handles the entity version table, onCreate and onUpdate.
 */
open class DbMaker(               context: Context,
                                   dbName: String,
                                dbVersion: Int,
                           val entityMeta: Collection<BaseEntityMeta>,
                          val runOnCreate: (SQLiteDatabase)->Unit = {},
                         val runOnUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> }
    
) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
    
    val dbLock = Any()
    
    init {
        initializeDbManager()
        synchronized(dbLock) {
            handleVersionTable(mainDb)
        }
    }
    
    
    override fun onCreate(                                                      db: SQLiteDatabase
    ) {
        // region LOG
            Log.d(DbTAG, "onCreate runs")
        // endregion
        synchronized(dbLock) {
            db.createTable(TABLE_EntityVersions, VersionEntity::class)
            runOnCreate(db)
        }
    }
    
    
    
    override fun onUpgrade(                                                     db: SQLiteDatabase,
                                                                        oldVersion: Int,
                                                                        newVersion: Int
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
    
    
    private fun initializeDbManager() {
        // region LOG
            Log.d(DbTAG, "initializeDbManager() runs")
        // endregion
        DbManager.init(writableDatabase)
    }
    
    
    private fun handleVersionTable(                                              db: SQLiteDatabase
    ) {
        // region LOG
            Log.d(DbTAG, "handleVersionTable() runs")
        // endregion
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
    
    
    private fun addAllEntityMeta(                                                db: SQLiteDatabase
    ) {
        for (meta in entityMeta) {
            db.insertRow(
                TABLE_EntityVersions,
                VersionEntity(meta.dbRowName, meta.currVersion)
            )
        }
    }
    
    private fun cleanupVersionTable(                                             db: SQLiteDatabase
    ) {
        val allNames = db.getList<String>(TABLE_EntityVersions, Name)
        if (allNames.isEmpty()) return //\/\/\/\/\/\
        
        val namesToDelete = allNames.filter { name ->
            entityMeta.none { meta -> meta.dbRowName == name }
        }
        for (name in namesToDelete) {
            db.deleteRow(TABLE_EntityVersions, Name, name)
        }
    }
    
    private fun addMissingEntityMeta(                                            db: SQLiteDatabase
    ) {
        for (meta in entityMeta) {
            val dbName = meta.dbRowName
            val rowExists = db.hasRows(TABLE_EntityVersions) { Name equal dbName }
            if (rowExists)
                continue //\/\/\
            
            db.insertRow(
                TABLE_EntityVersions,
                VersionEntity(dbName, meta.currVersion)
            )
        }
    }
    
    
    
    
    
    
    
}