package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.DbTAG

open class DbMaker(              context: Context,
                                  dbName: String,
                               dbVersion: Int,
                         val runOnCreate: (SQLiteDatabase)->Unit = {},
                        val runOnUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> }
    
) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
    
    val dbLock = Any()
    
    override fun onCreate(                                                      db: SQLiteDatabase
    ) {
        // region LOG
            Log.d(DbTAG, "onCreate runs")
        // endregion
        synchronized(dbLock) { runOnCreate(db) }
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
    
    fun initializeDbManager() {
        // region LOG
            Log.d(DbTAG, "initializeDbManager() runs")
        // endregion
        DbManager.init(writableDatabase)
    }
    
}