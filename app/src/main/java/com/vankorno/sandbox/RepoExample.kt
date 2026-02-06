package com.vankorno.sandbox

import com.vankorno.sandbox.AppStart.DemoApp.Companion.dbFileNameFromDb
import com.vankorno.sandbox._entities.TestTable
import com.vankorno.sandbox._entities._TestTable
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.vankornodb.add.addObjects
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.get.getDbFileName
import com.vankorno.vankornodb.get.isTableEmpty
import com.vankorno.vankornodb.get.tableExists

fun Dbh.launchApp() = write("launchApp") { db ->
    
    dbFileNameFromDb = db.getDbFileName()
    
    
    if (!db.tableExists(TestTable)) {
        db.createTable(_TestTable)
    }
    if (db.isTableEmpty(TestTable)) {
        val testSubjects = List(50) {
            TestEntity(name = "Name" + it, position = it + 1)
        }
        db.addObjects(_TestTable, testSubjects)
    }
}



