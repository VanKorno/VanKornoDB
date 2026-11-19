package com.vankorno.sandbox.add

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRatQuant
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.add.addWithNewIDsAndPositions
import com.vankorno.vankornodb.add.addWithNewIdAndPosition
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.delete.deleteRows
import com.vankorno.vankornodb.delete.deleteTable
import com.vankorno.vankornodb.get.getLastId
import com.vankorno.vankornodb.get.getLastPosition
import com.vankorno.vankornodb.get.getLong
import org.junit.Assert.assertEquals
import org.junit.Test

private const val TestTableName = "AddConvenienceTable"
private val _TestTable = _Test.table(TestTableName)

@MediumTest
class AddConvenienceTest {
    val ratQuant = LabRatQuant.toLong()
    
    @Test
    fun addWithNewIdAndPositionTest() {
        dbh.getLabRats(TestTableName)
        
        sanityCheck()
        simpleNew()
        withValidIdAndPosition()
    }
    
    private fun sanityCheck() {
        val lastId = dbh.getLastId(_TestTable)
        assertEquals(ratQuant, lastId)
        
        val lastPosition = dbh.getLastPosition(_TestTable)
        assertEquals(ratQuant, lastPosition)
    }
    
    private fun testAndKillNewRat(                                                name: String
    ) {
        val lastId = dbh.getLastId(_TestTable)
        val lastPosition = dbh.getLastPosition(_TestTable)
        assertEquals(ratQuant + 1, lastId)
        assertEquals(ratQuant + 1, lastPosition)

        val newRatId = dbh.getLong(_TestTable, { Id }) { Name equal name }
        assertEquals(ratQuant + 1, newRatId)
        
        dbh.deleteRows(_TestTable) { Name equal name }
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
        val tableInfo = _Test.table(table)
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
        
        dbh.deleteTable(_TestTable)
    }
    
    
    
}
