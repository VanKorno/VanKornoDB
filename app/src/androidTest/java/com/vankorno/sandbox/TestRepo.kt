package com.vankorno.sandbox

import com.vankorno.sandbox.entities.testEntity.TestEntity
import com.vankorno.sandbox.entities.testEntity._Test
import com.vankorno.vankornodb.add.addObjects
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.dbManagement.data.using

const val LabRat = "Lab rat "

fun LocalDbHelper.getLabRats(                                                      table: String
) = write("launchApp", async = false) { db ->
    db.createTable(table using _Test)
    
    val testSubjects = List(50) {
        TestEntity(name = LabRat + (it + 1), position = it + 1)
    }
    db.addObjects(table using _Test, testSubjects)
}






