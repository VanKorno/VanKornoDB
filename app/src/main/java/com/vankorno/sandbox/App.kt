package com.vankorno.sandbox

import com.vankorno.sandbox._entities.DbName
import com.vankorno.sandbox._entities.DbVersion
import com.vankorno.sandbox._entities.EntityMeta
import com.vankorno.sandbox._entities.TestTable
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox._navig.BackNav
import com.vankorno.sandbox._navig.Navigator
import com.vankorno.sandbox._navig.ScreenUpdater
import com.vankorno.vankornocompose.LibApp
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornodb.api.DbHelper
import com.vankorno.vankornodb.api.DbMigrator
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.api.createTables
import com.vankorno.vankornodb.core.data.DbConstants.InMemoryDB
import com.vankorno.vankornodb.dbManagement.data.using

class App : LibApp( // Don't mind this. Just using my lib that has nothing to do with this project
    dbName = DbName,
    navigator = Navigator,
    backNavigator = BackNav,
    screenUpdater = ScreenUpdater,
) {
    companion object {
        var dbFileNameFromDb = "" // useless shit for the demo
        
        var androidTestRun = false
    }
    
    override fun dbInit(                                                    actualDbName: String
    ) {
        dbh = DbHelper( // Create the DB for the whole app to use if you want
            context = this,
            dbName = actualDbName, // Or whatever you're getting your db filename from...
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
