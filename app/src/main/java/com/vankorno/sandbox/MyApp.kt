package com.vankorno.sandbox

import android.app.Application
import com.vankorno.vankornodb.core.data.DbConstants.InMemoryDB
import com.vankorno.vankornohelpers.LibMisc

class MyApp : Application() {
    companion object {
        var androidTestRun = false
        
        /** Your globally-available db-helper */
        lateinit var dbh: LocalDbHelper
    }
    
    override fun onCreate() {
        super.onCreate()
        
        androidTestRun = LibMisc().isInstrumentedTestRun()
        val dbName = if (androidTestRun)  InMemoryDB  else  DbName // Or whatever your db filename logic is...
        
        dbh = LocalDbHelper(this, dbName) // Create the DB for the whole app to use if you want
    }
    
    
}