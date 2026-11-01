package com.vankorno.sandbox

import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.vankornodb.add.addObjects
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.dbManagement.data.using
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

const val LabRat = "Lab rat "
const val LabRatQuant = 50

fun Dbh.getLabRats(                                                                table: String
) {
    write("launchApp", async = false) { db ->
        db.createTable(table using _Test)
        
        val testSubjects = List(LabRatQuant) { idx ->
            TestEntity(name = LabRat + (idx + 1), position = idx + 1L)
        }
        db.addObjects(table using _Test, testSubjects)
    }
}


fun makeRatsTestNest(                                                              table: String
) {
    dbh.getLabRats(table)
    assertTrue(dbh.tableExists(table))
    assertFalse(dbh.isTableEmpty(table))
}



