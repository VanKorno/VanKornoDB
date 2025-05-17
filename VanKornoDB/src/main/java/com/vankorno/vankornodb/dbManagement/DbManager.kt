package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
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