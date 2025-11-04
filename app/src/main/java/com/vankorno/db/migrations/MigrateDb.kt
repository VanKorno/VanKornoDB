package com.vankorno.db.migrations

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.db.entities.EntityEnum
import com.vankorno.db.entities.TABLE_Versions
import com.vankorno.db.entities.versions.EnttVersion
import com.vankorno.vankornodb.core.DbConstants.Name
import com.vankorno.vankornodb.dbManagement.migration.migrateMultiStep
import com.vankorno.vankornodb.getSet.getInt
import com.vankorno.vankornodb.getSet.set

private const val TAG = "MigrateDb"

class MigrateDb(val db: SQLiteDatabase) {
    fun migrate() {
        migrateLiteSingle(TABLE_Versions, EntityEnum.Versions)
    }
    
    
    fun migrateLiteSingle(                                                     tableName: String,
                                                                                enttEnum: EntityEnum
    ) {
        val oldVer = db.getInt(TABLE_Versions, EnttVersion, Name, enttEnum.dbRowName)
        val newVer = enttEnum.currVersion
        // region LOG
            Log.d(TAG, "migrateLiteSingle() table = $tableName, oldVer = $oldVer, newVer = $newVer")
        // endregion
        db.migrateMultiStep(
            tableName = tableName,
            oldVersion = oldVer,
            newVersion = newVer,
            migrationBundle = enttEnum.migrationBundle.value
        )
        db.set(newVer, TABLE_Versions, EnttVersion, Name, enttEnum.dbRowName)
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}