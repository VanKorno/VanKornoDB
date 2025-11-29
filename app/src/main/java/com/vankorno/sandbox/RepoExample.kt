package com.vankorno.sandbox

import com.vankorno.sandbox.entities.TestTable
import com.vankorno.sandbox.entities.testEntity.TestEntity
import com.vankorno.vankornodb.getSet.insertObjects
import com.vankorno.vankornodb.getSet.isTableEmpty

fun LocalDbHelper.launchApp() = write("launchApp") { db ->
    
    if (db.isTableEmpty(TestTable)) {
        val testSubjects = List(50) { TestEntity("Name" + it) }
        
        db.insertObjects(TestTable, testSubjects)
    }
}



