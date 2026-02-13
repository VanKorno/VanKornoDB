package com.vankorno.sandbox.AppStart

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

class DemoApp : LibApp() {
    companion object {
        const val DbName = "DbFile.dp"
        var dbFileNameFromDb = "" // useless shit for the demo
        
        var androidTestRun = false
    }
    
    override fun dbInit() {
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
    
    override fun onGoTo(scr: Screen) { Navigator.goTo(scr) }
    
    override fun onGoBack() { BackNav.navigBack() }
    
    override fun onGoToStart() { Navigator.goTo(ScrHome) }
    
    override fun onUpdateScreen() { ScreenUpdater.updateScr() }
    
    
}