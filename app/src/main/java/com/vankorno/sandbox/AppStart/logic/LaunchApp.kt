package com.vankorno.sandbox.AppStart.logic

import com.vankorno.sandbox.App.Companion.dbFileNameFromDb
import com.vankorno.sandbox._entities.TestTable
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.get.getDbFileName
import com.vankorno.vankornodb.get.isTableEmpty
import com.vankorno.vankornodb.get.tableExists
import com.vankorno.vankornodb.newTable.createTable

fun launchApp() {
    dbFileNameFromDb = dbh.getDbFileName()
    
    createTestTableIfAbsent()
}


fun createTestTableIfAbsent() {
    if (!dbh.tableExists(TestTable)) {
        dbh.createTable(TestTable)
    }
    if (dbh.isTableEmpty(TestTable)) {
        val testSubjects = List(50) {
            TestEntity(name = "Name" + it, position = it + 1L)
        }
        dbh.addEntities(TestTable, testSubjects)
    }
}
