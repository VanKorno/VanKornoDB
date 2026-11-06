package com.vankorno.sandbox.add

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRatQuant
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.sandbox.makeRatsTestNest
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import org.junit.Assert.assertEquals
import org.junit.Test

private const val TestTable = "AddConvenienceTable"
private val _TestTable = TestTable using _Test

@MediumTest
class AddConvenienceTest {
    val ratQuant = LabRatQuant.toLong()
    
    @Test
    fun addWithNewIdAndPositionTest() {
        makeRatsTestNest(TestTable)
        
        sanityCheck()
        simpleNew()
        withValidIdAndPosition()
    }
    
    private fun sanityCheck() {
        val lastId = dbh.getLastId(TestTable)
        assertEquals(ratQuant, lastId)
        
        val lastPosition = dbh.getLastPosition(TestTable)
        assertEquals(ratQuant, lastPosition)
    }
    
    private fun testAndKillNewRat(                                                name: String
    ) {
        val lastId = dbh.getLastId(TestTable)
        val lastPosition = dbh.getLastPosition(TestTable)
        assertEquals(ratQuant + 1, lastId)
        assertEquals(ratQuant + 1, lastPosition)

        val newRatId = dbh.getLong(TestTable, cID) { Name = name }
        assertEquals(ratQuant + 1, newRatId)
        
        dbh.deleteRows(TestTable) { Name = name }
    }
    
    
    private fun simpleNew() {
        val name = "new"
        val rat = TestEntity(name = name)
        
        val result = dbh.addWithNewIdAndPosition(_TestTable, rat) { entity, id, position ->
            entity.copy(id = id, position = position)
        }
        assertEquals(ratQuant + 1, result.id)
        assertEquals(ratQuant + 1, result.position)
        assertEquals(true, result.isValid)
        
        testAndKillNewRat(name)
    }
    
    private fun withValidIdAndPosition() {
        val name = "Splinter"
        val rat = TestEntity(777, name, 777)
        
        val result = dbh.addWithNewIdAndPosition(_TestTable, rat) { entity, id, position ->
            entity.copy(id = id, position = position)
        }
        assertEquals(ratQuant + 1, result.id)
        assertEquals(ratQuant + 1, result.position)
        assertEquals(true, result.isValid)
        
        testAndKillNewRat(name)
    }
    
    @Test
    fun addWithNewIDsAndPositionsTest() {
        val table = "addWithNewIDsAndPositionsTest"
        val tableInfo = table using _Test
        dbh.getLabRats(table)
        
        val rats = listOf(TestEntity(name = "Rat1"), TestEntity(name = "Rat2"))
        val results = dbh.addWithNewIDsAndPositions(tableInfo, rats) { entity, id, position ->
            entity.copy(id = id, position = position)
        }
        
        assertEquals(2, results.size)
        assertEquals(ratQuant + 1, results[0].id)
        assertEquals(ratQuant + 1, results[0].position)
        assertEquals(ratQuant + 2, results[1].id)
        assertEquals(ratQuant + 2, results[1].position)
        
        dbh.deleteTable(TestTable)
    }
    
    
    
}
