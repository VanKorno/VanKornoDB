package com.vankorno.sandbox.get.extra

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRatQuant
import com.vankorno.sandbox._entities.testEntity.CTest
import com.vankorno.sandbox._entities.testEntity.CTest.Int1
import com.vankorno.sandbox._entities.testEntity.CTest.Int2
import com.vankorno.sandbox._entities.testEntity.CTest.Int3
import com.vankorno.sandbox._entities.testEntity.CTest.Long1
import com.vankorno.sandbox._entities.testEntity.CTest.Long2
import com.vankorno.sandbox._entities.testEntity.CTest.Long3
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.sandbox.makeRatsTestNest
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@MediumTest
class GetLargestLastTest {
    val ratQuant = LabRatQuant.toLong()
    
    @Test
    fun getLargestLong() {
        val table = "getLargestLong"
        makeRatsTestNest(table)
        
        testIdPosition(table)
        test0L(table)
        testWithValueChange(table) // Mutates rats
        
        dbh.clearTable(table)
        testEmptyTable(table)
        dbh.deleteTable(table)
    }
    
    
    @Test
    fun getLargestInt() {
        val table = "getLargestInt"
        dbh.getLabRats(table)
        var largest = dbh.getLargestInt(table, Int1)
        assertEquals(0, largest)
        
        dbh.setInt(2, table, Int1) { ID = 2 }
        largest = dbh.getLargestInt(table, Int1)
        assertEquals(2, largest)
        
        dbh.set(table) { // Sets all rows
            Int2 setTo 10
        }
        largest = dbh.getLargestInt(table, Int2)
        assertEquals(10, largest)
        
        dbh.setInt(1, table, Int3) { ID = 1 }
        dbh.setInt(2, table, Int3) { ID = 2 }
        dbh.setInt(3, table, Int3) { ID = 3 }
        largest = dbh.getLargestInt(table, Int3)
        assertEquals(3, largest)
        
        dbh.clearTable(table)
        largest = dbh.getLargestInt(table, Int1)
        assertEquals(0, largest)
        largest = dbh.getLargestInt(table, Int2)
        assertEquals(0, largest)
        dbh.deleteTable(table)
    }
    
    
    @Test
    fun getLargestWithWhere() {
        val table = "getLargestWithWhere"
        dbh.getLabRats(table)

        // Filter getLargestLong by ID
        dbh.setLong(50L, table, Long1) { ID = 1 }
        dbh.setLong(100L, table, Long1) { ID = 2 }

        val largestForId1 = dbh.getLargestLong(table, Long1) { ID = 1 }
        val largestForId2 = dbh.getLargestLong(table, Long1) { ID = 2 }
        val largestForNonExistent = dbh.getLargestLong(table, Long1) { ID = 999 }
        assertEquals(50L, largestForId1)
        assertEquals(100L, largestForId2)
        assertEquals(0L, largestForNonExistent)

        // Filter getLargestInt by ID
        dbh.setInt(25, table, Int1) { ID = 1 }
        dbh.setInt(75, table, Int1) { ID = 2 }

        val largestIntForId1 = dbh.getLargestInt(table, Int1) { ID = 1 }
        val largestIntForId2 = dbh.getLargestInt(table, Int1) { ID = 2 }
        val largestIntForNonExistent = dbh.getLargestInt(table, Int1) { ID = 999 }
        assertEquals(25, largestIntForId1)
        assertEquals(75, largestIntForId2)
        assertEquals(0, largestIntForNonExistent)

        // getLastPosition with where
        val lastPosForSubset = dbh.getLastPosition(table) { cID less 10L }
        assertEquals(9L, lastPosForSubset)

        val lastPosNonExistent = dbh.getLastPosition(table) { cID greater 1000L }
        assertEquals(0L, lastPosNonExistent)

        dbh.deleteTable(table)
    }

    @Test
    fun getNextPositionWithWhere() {
        val table = "getNextPositionWithWhere"
        val tableInfo = table using _Test

        dbh.createTable(tableInfo)
        dbh.addEntities(tableInfo, listOf(
            TestEntity(position = 5L, str1 = "cat"),
            TestEntity(position = 10L, str1 = "cat"),
            TestEntity(position = 50L, str1 = "dog")
        ))

        val catNext = dbh.getNextPosition(table) { CTest.Str1 equal "cat" }
        val catNexts = dbh.getNextPositions(table, 2) { CTest.Str1 equal "cat" }
        assertEquals(11L, catNext)
        assertEquals(listOf(11L, 12L), catNexts)

        val dogNext = dbh.getNextPosition(table) { CTest.Str1 equal "dog" }
        val dogNexts = dbh.getNextPositions(table, 2) { CTest.Str1 equal "dog" }
        assertEquals(51L, dogNext)
        assertEquals(listOf(51L, 52L), dogNexts)

        // Non-matching where condition behaves like empty table (starts from 1L)
        val emptyNext = dbh.getNextPosition(table) { CTest.Str1 equal "bird" }
        val emptyNexts = dbh.getNextPositions(table, 2) { CTest.Str1 equal "bird" }
        assertEquals(1L, emptyNext)
        assertEquals(listOf(1L, 2L), emptyNexts)

        dbh.deleteTable(table)
    }
    
    
    
    
    
    // ========================================================================================= \\
    
    private fun testIdPosition(table: String) {
        val lastId = dbh.getLargestLong(table, cID)
        val lastId2 = dbh.getLastId(table)
        assertEquals(ratQuant, lastId)
        assertEquals(ratQuant, lastId2)
        
        val lastPosition = dbh.getLargestLong(table, cPosition)
        val lastPosition2 = dbh.getLastPosition(table)
        assertEquals(ratQuant, lastPosition)
        assertEquals(ratQuant, lastPosition2)
    }
    
    
    private fun test0L(table: String) {
        val largestLong1 = dbh.getLargestLong(table, Long1)
        val largestLong2 = dbh.getLargestLong(table, Long2)
        val largestLong3 = dbh.getLargestLong(table, Long3)
        assertEquals(0L, largestLong1)
        assertEquals(0L, largestLong2)
        assertEquals(0L, largestLong3)
    }
    
    
    private fun testWithValueChange(table: String) {
        testSwapLastRows(table)
        testSingleValueChange(table)
        testMultValuesChange(table)
    }
    
    
    private fun testSwapLastRows(table: String) {
        dbh.reorder(table, ratQuant, moveUpOrBack = true)
        
        val lastId = dbh.getLastId(table)
        assertEquals(ratQuant, lastId)
        
        val lastPosition = dbh.getLastPosition(table)
        assertEquals(ratQuant, lastPosition)
    }
    
    
    private fun testSingleValueChange(table: String) {
        val newVal = 10L
        dbh.setLong(newVal, table, Long1) { ID = 1 }
        val largestLong1 = dbh.getLargestLong(table, Long1)
        assertEquals(newVal, largestLong1)
    }
    
    
    private fun testMultValuesChange(table: String) {
        val newVal = 20L
        
        dbh.set(table) { // Sets all rows
            Long2 setTo newVal
        }
        val largestLong = dbh.getLargestLong(table, Long2)
        assertEquals(newVal, largestLong)
        
        dbh.setLong(21L, table, Long2) { ID = 1 }
        dbh.setLong(22L, table, Long2) { ID = 2 }
        dbh.setLong(23L, table, Long2) { ID = 3 }
        
        val largestLong2 = dbh.getLargestLong(table, Long2)
        assertEquals(23L, largestLong2)
    }
    
    
    private fun testEmptyTable(table: String) {
        assertTrue(dbh.isTableEmpty(table))
        
        val lastId = dbh.getLargestLong(table, cID)
        val lastId2 = dbh.getLastId(table)
        assertEquals(0L, lastId)
        assertEquals(0L, lastId2)
        
        val lastPosition = dbh.getLargestLong(table, cPosition)
        val lastPosition2 = dbh.getLastPosition(table)
        assertEquals(0L, lastPosition)
        assertEquals(0L, lastPosition2)
        
        val largestLong1 = dbh.getLargestLong(table, Long1)
        val largestLong2 = dbh.getLargestLong(table, Long2)
        val largestLong3 = dbh.getLargestLong(table, Long3)
        assertEquals(0L, largestLong1)
        assertEquals(0L, largestLong2)
        assertEquals(0L, largestLong3)
    }
    
    
    
}
