package com.vankorno.vankornodb

import android.database.sqlite.SQLiteDatabase

object DbManager {
    private var _mainDb: SQLiteDatabase? = null
    
    val mainDb: SQLiteDatabase
        get() = _mainDb ?: throw IllegalStateException("Database not initialized.")
    
    fun init(db: SQLiteDatabase) { _mainDb = db }
    
    fun close() {
        _mainDb?.let {
            if (it.isOpen) it.close()
        }
        _mainDb = null
    }
}