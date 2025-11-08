package com.vankorno.db

import android.app.Application
import com.vankorno.vankornodb.core.DbConstants.InMemoryDB

class MyApp : Application() {
    companion object {
        const val DbName = "DbFile"
        
        var androidTestRun = false
        
        lateinit var dbh: LocalDbHelper
    }
    
    override fun onCreate() {
        super.onCreate()
        androidTestRun = isInstrumentedTestRun()
        val dbName = if (androidTestRun)  InMemoryDB  else  DbName
        
        dbh = LocalDbHelper(this, dbName)
    }
    
    
    fun isInstrumentedTestRun(): Boolean = try {
        Class.forName("androidx.test.platform.app.InstrumentationRegistry")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}