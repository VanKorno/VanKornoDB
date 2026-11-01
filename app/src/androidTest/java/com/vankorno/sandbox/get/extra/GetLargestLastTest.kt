package com.vankorno.sandbox.get.extra

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRatQuant
import com.vankorno.sandbox._entities.testEntity.CTest.Int1
import com.vankorno.sandbox._entities.testEntity.CTest.Int2
import com.vankorno.sandbox._entities.testEntity.CTest.Int3
import com.vankorno.sandbox._entities.testEntity.CTest.Long1
import com.vankorno.sandbox._entities.testEntity.CTest.Long2
import com.vankorno.sandbox._entities.testEntity.CTest.Long3
import com.vankorno.sandbox.makeRatsTestNest
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import org.junit.Assert.*
import org.junit.Test

private const val LongTable = "GetLargestLongTestTable"
private const val IntTable = "GetLargestIntTestTable"


@MediumTest
class GetLargestLastTest {
    val ratQuant = LabRatQuant.toLong()
    
    @Test
    fun getLargestLong() {
        makeRatsTestNest(LongTable)
        
        testIdPosition()
        test0L()
        testWithValueChange() // Mutates rats
        
        dbh.clearTable(LongTable)
        testEmptyTable()
    }
    
    
    @Test
    fun getLargestInt() {
        makeRatsTestNest(IntTable)
        var largest = dbh.getLargestInt(IntTable, Int1)
        assertEquals(0, largest)
        
        dbh.setInt(2, IntTable, Int1) { ID = 2 }
        largest = dbh.getLargestInt(IntTable, Int1)
        assertEquals(2, largest)
        
        dbh.set(IntTable) { // Sets all rows
            Int2 setTo 10
        }
        largest = dbh.getLargestInt(IntTable, Int2)
        assertEquals(10, largest)
        
        dbh.setInt(1, IntTable, Int3) { ID = 1 }
        dbh.setInt(2, IntTable, Int3) { ID = 2 }
        dbh.setInt(3, IntTable, Int3) { ID = 3 }
        largest = dbh.getLargestInt(IntTable, Int3)
        assertEquals(3, largest)
        
        dbh.clearTable(IntTable)
        largest = dbh.getLargestInt(IntTable, Int1)
        assertEquals(0, largest)
        largest = dbh.getLargestInt(IntTable, Int2)
        assertEquals(0, largest)
    }
    
    
    
    
    
    // ========================================================================================= \\
    
    private fun testIdPosition() {
        val lastId = dbh.getLargestLong(LongTable, cID)
        val lastId2 = dbh.getLastId(LongTable)
        assertEquals(ratQuant, lastId)
        assertEquals(ratQuant, lastId2)
        
        val lastPosition = dbh.getLargestLong(LongTable, cPosition)
        val lastPosition2 = dbh.getLastPosition(LongTable)
        assertEquals(ratQuant, lastPosition)
        assertEquals(ratQuant, lastPosition2)
    }
    
    
    private fun test0L() {
        val largestLong1 = dbh.getLargestLong(LongTable, Long1)
        val largestLong2 = dbh.getLargestLong(LongTable, Long2)
        val largestLong3 = dbh.getLargestLong(LongTable, Long3)
        assertEquals(0L, largestLong1)
        assertEquals(0L, largestLong2)
        assertEquals(0L, largestLong3)
    }
    
    
    private fun testWithValueChange() {
        testSwapLastRows()
        testSingleValueChange()
        testMultValuesChange()
    }
    
    
    private fun testSwapLastRows() {
        dbh.reorder(LongTable, ratQuant, moveUpOrBack = true)
        
        val lastId = dbh.getLastId(LongTable)
        assertEquals(ratQuant, lastId)
        
        val lastPosition = dbh.getLastPosition(LongTable)
        assertEquals(ratQuant, lastPosition)
    }
    
    
    private fun testSingleValueChange() {
        val newVal = 10L
        dbh.setLong(newVal, LongTable, Long1) { ID = 1 }
        val largestLong1 = dbh.getLargestLong(LongTable, Long1)
        assertEquals(newVal, largestLong1)
    }
    
    
    private fun testMultValuesChange() {
        val newVal = 20L
        
        dbh.set(LongTable) { // Sets all rows
            Long2 setTo newVal
        }
        val largestLong = dbh.getLargestLong(LongTable, Long2)
        assertEquals(newVal, largestLong)
        
        dbh.setLong(21L, LongTable, Long2) { ID = 1 }
        dbh.setLong(22L, LongTable, Long2) { ID = 2 }
        dbh.setLong(23L, LongTable, Long2) { ID = 3 }
        
        val largestLong2 = dbh.getLargestLong(LongTable, Long2)
        assertEquals(23L, largestLong2)
    }
    
    
    private fun testEmptyTable() {
        assertTrue(dbh.isTableEmpty(LongTable))
        
        val lastId = dbh.getLargestLong(LongTable, cID)
        val lastId2 = dbh.getLastId(LongTable)
        assertEquals(0L, lastId)
        assertEquals(0L, lastId2)
        
        val lastPosition = dbh.getLargestLong(LongTable, cPosition)
        val lastPosition2 = dbh.getLastPosition(LongTable)
        assertEquals(0L, lastPosition)
        assertEquals(0L, lastPosition2)
        
        val largestLong1 = dbh.getLargestLong(LongTable, Long1)
        val largestLong2 = dbh.getLargestLong(LongTable, Long2)
        val largestLong3 = dbh.getLargestLong(LongTable, Long3)
        assertEquals(0L, largestLong1)
        assertEquals(0L, largestLong2)
        assertEquals(0L, largestLong3)
    }
    
    
    
}
