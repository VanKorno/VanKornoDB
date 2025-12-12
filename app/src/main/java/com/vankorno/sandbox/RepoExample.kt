package com.vankorno.sandbox

import com.vankorno.sandbox.entities.TestTable
import com.vankorno.sandbox.entities.testEntity.TestEntity
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.get.getDbFileName
import com.vankorno.vankornodb.get.isTableEmpty
import com.vankorno.vankornodb.get.tableExists
import com.vankorno.vankornodb.set.insertObjects

fun LocalDbHelper.launchApp() = write("launchApp") { db ->
    
    dbFileNameFromDb = db.getDbFileName()
    
    
    if (!db.tableExists(TestTable)) {
        db.createTable(TestTable, TestEntity::class)
    }
    if (db.isTableEmpty(TestTable)) {
        val testSubjects = List(50) { TestEntity("Name" + it) }
        
        db.insertObjects(TestTable, testSubjects)
    }
}



