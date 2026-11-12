package com.vankorno.sandbox.AppStart.logic

import com.vankorno.sandbox.App.Companion.dbFileNameFromDb
import com.vankorno.sandbox._entities.TestTable
import com.vankorno.sandbox._entities._TestTable
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.vankornodb.api.DbRuntime.dbh

fun launchApp() {
    dbFileNameFromDb = dbh.getDbFileName()
    
    createTestTableIfAbsent()
}


fun createTestTableIfAbsent() {
    if (!dbh.tableExists(TestTable)) {
        dbh.createTable(_TestTable)
    }
    if (dbh.isTableEmpty(TestTable)) {
        val testSubjects = List(50) {
            TestEntity(name = "Name" + it, position = it + 1L)
        }
        dbh.addObjects(_TestTable, testSubjects)
    }
}
