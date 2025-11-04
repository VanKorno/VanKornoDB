package com.vankorno.db.actions

import android.database.sqlite.SQLiteDatabase
import com.vankorno.db.entities.EntityEnum
import com.vankorno.db.entities.TABLE_Versions
import com.vankorno.db.entities.versions.VersionEntity
import com.vankorno.vankornodb.core.DbConstants.Name
import com.vankorno.vankornodb.getSet.hasRows
import com.vankorno.vankornodb.getSet.insertRow
import com.vankorno.vankornodb.getSet.isTableEmpty

class LaunchApp(val db: SQLiteDatabase) {
    
    fun launch() {
        val dbEmpty = db.isTableEmpty(TABLE_Versions)
        if (dbEmpty)
            firstLaunch()
    }
    
    
    
    fun firstLaunch() {
        checkAndFillEntityVersions()
    }
    
    
    fun checkAndFillEntityVersions() {
        for (entity in EntityEnum.entries) {
            val dbName = entity.dbRowName
            val rowExists = db.hasRows(TABLE_Versions) { Name equal dbName }
            if (rowExists)
                continue //\/\/\
            
            db.insertRow(
                TABLE_Versions,
                VersionEntity(dbName, entity.currVersion)
            )
        }
    }
    
}