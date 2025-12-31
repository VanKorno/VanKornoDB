package com.vankorno.sandbox

import com.vankorno.sandbox.MyApp.Companion.dbFileNameFromDb
import com.vankorno.sandbox.entities.TestTable
import com.vankorno.sandbox.entities._TestTable
import com.vankorno.sandbox.entities.testEntity.TestEntity
import com.vankorno.vankornodb.add.addObjects
import com.vankorno.vankornodb.api.DbHelper
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.get.getDbFileName
import com.vankorno.vankornodb.get.isTableEmpty
import com.vankorno.vankornodb.get.tableExists

fun DbHelper.launchApp() = write("launchApp") { db ->
    
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



