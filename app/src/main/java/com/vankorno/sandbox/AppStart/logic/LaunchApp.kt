package com.vankorno.sandbox.AppStart.logic

import com.vankorno.sandbox.AppStart.DemoApp.Companion.dbFileNameFromDb
import com.vankorno.sandbox._entities.TestTable
import com.vankorno.sandbox._entities._TestTable
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.vankornocompose.launch.Launcher
import com.vankorno.vankornodb.api.DbRuntime.dbh


fun Launcher.launchApp() {
    dbFileNameFromDb = dbh.getDbFileName()
    
    if (!dbh.tableExists(TestTable)) {
        dbh.createTable(_TestTable)
    }
    if (dbh.isTableEmpty(TestTable)) {
        val testSubjects = List(50) {
            TestEntity(name = "Name" + it, position = it + 1)
        }
        dbh.addObjects(_TestTable, testSubjects)
    }
}

