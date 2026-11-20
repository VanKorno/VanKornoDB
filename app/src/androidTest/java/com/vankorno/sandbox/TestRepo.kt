package com.vankorno.sandbox

import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.newTable.createTable

const val LabRat = "Lab rat "
const val LabRatQuant = 50

fun Dbh.getLabRats(                                                                table: String
) {
    write("launchApp", async = false) { db ->
        db.createTable(_Test.tableNamed(table))
        
        val testSubjects = List(LabRatQuant) { idx ->
            TestEntity(name = LabRat + (idx + 1), position = idx + 1L)
        }
        db.addEntities(_Test.tableNamed(table), testSubjects)
    }
}



