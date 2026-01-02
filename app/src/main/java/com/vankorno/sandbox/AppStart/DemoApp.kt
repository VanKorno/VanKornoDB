package com.vankorno.sandbox.AppStart

import android.app.Application
import com.vankorno.sandbox._entities.DbVersion
import com.vankorno.sandbox._entities.EntityMeta
import com.vankorno.sandbox._entities.TestTable
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.vankornodb.api.DbHelper
import com.vankorno.vankornodb.api.DbMigrator
import com.vankorno.vankornodb.api.createTables
import com.vankorno.vankornodb.core.data.DbConstants.InMemoryDB
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornohelpers.LibMisc

class DemoApp : Application() {
    companion object {
        const val DbName = "DbFile.dp"
        var dbFileNameFromDb = "" // useless shit for the demo
        
        var androidTestRun = false
        
        /** Your globally-available db helper */
        lateinit var dbh: DbHelper
    }
    
    override fun onCreate() {
        super.onCreate()
        
        androidTestRun = LibMisc().isInstrumentedTestRun()
        
        dbh = DbHelper( // Create the DB for the whole app to use if you want
            context = this,
            dbName = if (androidTestRun) InMemoryDB else DbName, // Or whatever your db filename logic is...
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
    
    
}