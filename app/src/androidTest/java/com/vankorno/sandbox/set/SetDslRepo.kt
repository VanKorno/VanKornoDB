package com.vankorno.sandbox.set

import com.vankorno.sandbox.LocalDbHelper
import com.vankorno.sandbox.entities.testEntity.TestEntity
import com.vankorno.sandbox.set.SetDslTest.Companion.LabRat
import com.vankorno.sandbox.set.SetDslTest.Companion.SetValsTestTable
import com.vankorno.vankornodb.add.addObjects
import com.vankorno.vankornodb.api.createTable

fun LocalDbHelper.beforeSetValsTest() = write("launchApp", async = false) { db ->
    db.createTable(SetValsTestTable, TestEntity::class)
    
    val testSubjects = List(50) {
        TestEntity(name = LabRat + (it + 1), position = it + 1)
    }
    db.addObjects(SetValsTestTable, testSubjects)
}