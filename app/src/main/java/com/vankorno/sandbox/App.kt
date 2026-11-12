package com.vankorno.sandbox

import android.app.Application
import com.vankorno.sandbox._entities.DbName
import com.vankorno.sandbox._entities.DbVersion
import com.vankorno.sandbox._entities.EntityMeta
import com.vankorno.sandbox._entities.TestTable
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.vankornodb.api.DbHelper
import com.vankorno.vankornodb.api.DbMigrator
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.api.createTables
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornohelpers.values.LibGlobals.androidTestsRun

class App : Application() {
    companion object {
        var dbFileNameFromDb = "" // useless shit for the demo
    }
    
    override fun onCreate() {
        super.onCreate()
        dbInit()
    }
    
    fun dbInit() {
        dbh = DbHelper( // Create the DB for the whole app to use if you want
            context = this,
            dbName = if (androidTestsRun) ":memory:" else DbName,
            dbVersion = DbVersion,
            entityMeta = EntityMeta.entries,
            createExclusiveTables = true,
            onCreate = { db ->
                db.createTables(
                    TestTable using _Test
                )
            },
            onUpgrade = { db, _ ->
                DbMigrator(db, EntityMeta.entries).migrateSingleTableEntities()
            }
        )
    }
    
    override fun onTerminate() {
        dbh.closeDb()
        super.onTerminate()
    }
}
