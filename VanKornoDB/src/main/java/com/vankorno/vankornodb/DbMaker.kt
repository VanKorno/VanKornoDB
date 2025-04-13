package com.vankorno.vankornodb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.collections.forEach

private const val TAG = "DbMaker"

open class DbMaker(                   context: Context,
                                       dbName: String,
                                    dbVersion: Int,
                         private val entities: Array<TableAndEntt> = emptyArray<TableAndEntt>(),
                            val onCreateStart: (SQLiteDatabase)->Unit = {},
                           val onCreateFinish: (SQLiteDatabase)->Unit = {},
                                val onUpgrade: (SQLiteDatabase)->Unit = {}
) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
    val dbLock = Any()
    
    
    override fun onCreate(                                                      db: SQLiteDatabase
    ) {
        // region LOG
            Log.d("LibDBHelper", "onCreate runs")
        // endregion
        synchronized(dbLock) {
            onCreateStart(db)
            
            val dbMisc = DbMisc()
            entities.forEach {
                db.execSQL(dbMisc.buildQuery(it.tableName, it.entity))
            }
            onCreateFinish(db)
        }
    }
    
    
    override fun onUpgrade(                                                     db: SQLiteDatabase,
                                                                        oldVersion: Int,
                                                                        newVersion: Int
    ) {
        if (oldVersion >= newVersion)  return  //\/\/\/\/\/\
        // region LOG
            Log.d(TAG, "onUpgrade() Migrating...")
        // endregion
        synchronized(dbLock) {
            onUpgrade(db)
        }
    }
    
    fun initializeDbManager() {
        // region LOG
            Log.d(TAG, "initializeDbManager() runs")
        // endregion
        DbManager.init(writableDatabase)
    }
    
    
    
    
    
    
    
    
    
}