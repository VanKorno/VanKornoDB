package com.vankorno.sandbox

import com.vankorno.sandbox.entities.TestTable
import com.vankorno.sandbox.entities.testEntity.TestEntity
import com.vankorno.vankornodb.dbManagement.createTable
import com.vankorno.vankornodb.getSet.getDbFileName
import com.vankorno.vankornodb.getSet.insertObjects
import com.vankorno.vankornodb.getSet.isTableEmpty
import com.vankorno.vankornodb.getSet.tableExists

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



