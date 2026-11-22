package com.vankorno.sandbox.get.extra

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRatQuant
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._TestEntity
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.delete.clearTable
import com.vankorno.vankornodb.delete.deleteTable
import com.vankorno.vankornodb.get.getLargestInt
import com.vankorno.vankornodb.get.getLargestLong
import com.vankorno.vankornodb.get.getLastPosition
import com.vankorno.vankornodb.get.getNextPosition
import com.vankorno.vankornodb.get.getNextPositions
import com.vankorno.vankornodb.get.id.getLastId
import com.vankorno.vankornodb.get.isTableEmpty
import com.vankorno.vankornodb.newTable.createTable
import com.vankorno.vankornodb.set.reorder
import com.vankorno.vankornodb.set.set
import com.vankorno.vankornodb.set.setInt
import com.vankorno.vankornodb.set.setLong
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@MediumTest
class GetLargestLastTest {
    val ratQuant = LabRatQuant.toLong()
    
    @Test
    fun getLargestLong() {
        val table = _TestEntity.tableNamed("getLargestLong")
        dbh.getLabRats(table.name)
        
        testIdPosition(table.name)
        test0L(table.name)
        testWithValueChange(table.name) // Mutates rats
        
        dbh.clearTable(table)
        testEmptyTable(table.name)
        dbh.deleteTable(table)
    }
    
    
    @Test
    fun getLargestInt() {
        val table = _TestEntity.tableNamed("getLargestInt")
        dbh.getLabRats(table.name)
        var largest = dbh.getLargestInt(table.col { Int1 })
        assertEquals(0, largest)
        
        dbh.setInt(2, table.col { Int1 }) { Id equal 2 }
        largest = dbh.getLargestInt(table.col { Int1 })
        assertEquals(2, largest)
        
        dbh.set(table) { // Sets all rows
            Int2 setTo 10
        }
        largest = dbh.getLargestInt(table.col { Int2 })
        assertEquals(10, largest)
        
        dbh.setInt(1, table.col { Int3 }) { Id equal 1 }
        dbh.setInt(2, table.col { Int3 }) { Id equal 2 }
        dbh.setInt(3, table.col { Int3 }) { Id equal 3 }
        largest = dbh.getLargestInt(table.col { Int3 })
        assertEquals(3, largest)
        
        dbh.clearTable(table)
        largest = dbh.getLargestInt(table.col { Int1 })
        assertEquals(0, largest)
        largest = dbh.getLargestInt(table.col { Int2 })
        assertEquals(0, largest)
        dbh.deleteTable(table)
    }
    
    
    @Test
    fun getLargestWithWhere() {
        val table = _TestEntity.tableNamed("getLargestWithWhere")
        dbh.getLabRats(table.name)

        // Filter getLargestLong by ID
        dbh.setLong(50L, table.col { Long1 }) { Id equal 1 }
        dbh.setLong(100L, table.col { Long1 }) { Id equal 2 }

        val largestForId1 = dbh.getLargestLong(table.col { Long1 }) { Id equal 1 }
        val largestForId2 = dbh.getLargestLong(table.col { Long1 }) { Id equal 2 }
        val largestForNonExistent = dbh.getLargestLong(table.col { Long1 }) { Id equal 999 }
        assertEquals(50L, largestForId1)
        assertEquals(100L, largestForId2)
        assertEquals(0L, largestForNonExistent)

        // Filter getLargestInt by ID
        dbh.setInt(25, table.col { Int1 }) { Id equal 1 }
        dbh.setInt(75, table.col { Int1 }) { Id equal 2 }

        val largestIntForId1 = dbh.getLargestInt(table.col { Int1 }) { Id equal 1 }
        val largestIntForId2 = dbh.getLargestInt(table.col { Int1 }) { Id equal 2 }
        val largestIntForNonExistent = dbh.getLargestInt(table.col { Int1 }) { Id equal 999 }
        assertEquals(25, largestIntForId1)
        assertEquals(75, largestIntForId2)
        assertEquals(0, largestIntForNonExistent)

        // getLastPosition with where
        val lastPosForSubset = dbh.getLastPosition(table) { Id less 10L }
        assertEquals(9L, lastPosForSubset)

        val lastPosNonExistent = dbh.getLastPosition(table) { Id greater 1000L }
        assertEquals(0L, lastPosNonExistent)

        dbh.deleteTable(table)
    }

    @Test
    fun getNextPositionWithWhere() {
        val table = _TestEntity.tableNamed("getNextPositionWithWhere")

        dbh.createTable(table)
        dbh.addEntities(table, listOf(
            TestEntity(position = 5L, str1 = "cat"),
            TestEntity(position = 10L, str1 = "cat"),
            TestEntity(position = 50L, str1 = "dog")
        ))

        val catNext = dbh.getNextPosition(table) { Str1 equal "cat" }
        val catNexts = dbh.getNextPositions(table, 2) { Str1 equal "cat" }
        assertEquals(11L, catNext)
        assertEquals(listOf(11L, 12L), catNexts)

        val dogNext = dbh.getNextPosition(table) { Str1 equal "dog" }
        val dogNexts = dbh.getNextPositions(table, 2) { Str1 equal "dog" }
        assertEquals(51L, dogNext)
        assertEquals(listOf(51L, 52L), dogNexts)

        // Non-matching where condition behaves like empty table (starts from 1L)
        val emptyNext = dbh.getNextPosition(table) { Str1 equal "bird" }
        val emptyNexts = dbh.getNextPositions(table, 2) { Str1 equal "bird" }
        assertEquals(1L, emptyNext)
        assertEquals(listOf(1L, 2L), emptyNexts)

        dbh.deleteTable(table)
    }
    
    
    
    
    
    // ========================================================================================= \\
    
    private fun testIdPosition(                                                tableName: String
    ) {
        val table = _TestEntity.tableNamed(tableName)
        val lastId = dbh.getLargestLong(table.col { Id })
        val lastId2 = dbh.getLastId(table)
        assertEquals(ratQuant, lastId)
        assertEquals(ratQuant, lastId2)
        
        val lastPosition = dbh.getLargestLong(table.col { Position })
        val lastPosition2 = dbh.getLastPosition(table)
        assertEquals(ratQuant, lastPosition)
        assertEquals(ratQuant, lastPosition2)
    }
    
    
    private fun test0L(                                                        tableName: String
    ) {
        val table = _TestEntity.tableNamed(tableName)
        val largestLong1 = dbh.getLargestLong(table.col { Long1 })
        val largestLong2 = dbh.getLargestLong(table.col { Long2 })
        val largestLong3 = dbh.getLargestLong(table.col { Long3 })
        assertEquals(0L, largestLong1)
        assertEquals(0L, largestLong2)
        assertEquals(0L, largestLong3)
    }
    
    
    private fun testWithValueChange(                                           tableName: String
    ) {
        val table = _TestEntity.tableNamed(tableName)
        testSwapLastRows(table.name)
        testSingleValueChange(table.name)
        testMultValuesChange(table.name)
    }
    
    
    private fun testSwapLastRows(                                              tableName: String
    ) {
        val table = _TestEntity.tableNamed(tableName)
        dbh.reorder(table, ratQuant, moveUpOrBack = true)
        
        val lastId = dbh.getLastId(table)
        assertEquals(ratQuant, lastId)
        
        val lastPosition = dbh.getLastPosition(table)
        assertEquals(ratQuant, lastPosition)
    }
    
    
    private fun testSingleValueChange(                                         tableName: String
    ) {
        val table = _TestEntity.tableNamed(tableName)
        val newVal = 10L
        dbh.setLong(newVal, table.col { Long1 }) { Id equal 1 }
        val largestLong1 = dbh.getLargestLong(table.col { Long1 })
        assertEquals(newVal, largestLong1)
    }
    
    
    private fun testMultValuesChange(                                          tableName: String
    ) {
        val table = _TestEntity.tableNamed(tableName)
        val newVal = 20L
        
        dbh.set(table) { // Sets all rows
            Long2 setTo newVal
        }
        val largestLong = dbh.getLargestLong(table.col { Long2 })
        assertEquals(newVal, largestLong)
        
        dbh.setLong(21L, table.col { Long2 }) { Id equal 1 }
        dbh.setLong(22L, table.col { Long2 }) { Id equal 2 }
        dbh.setLong(23L, table.col { Long2 }) { Id equal 3 }
        
        val largestLong2 = dbh.getLargestLong(table.col { Long2 })
        assertEquals(23L, largestLong2)
    }
    
    
    private fun testEmptyTable(                                                tableName: String
    ) {
        val table = _TestEntity.tableNamed(tableName)
        assertTrue(dbh.isTableEmpty(table))
        
        val lastId = dbh.getLargestLong(table.col { Id })
        val lastId2 = dbh.getLastId(table)
        assertEquals(0L, lastId)
        assertEquals(0L, lastId2)
        
        val lastPosition = dbh.getLargestLong(table.col { Position })
        val lastPosition2 = dbh.getLastPosition(table)
        assertEquals(0L, lastPosition)
        assertEquals(0L, lastPosition2)
        
        val largestLong1 = dbh.getLargestLong(table.col { Long1 })
        val largestLong2 = dbh.getLargestLong(table.col { Long2 })
        val largestLong3 = dbh.getLargestLong(table.col { Long3 })
        assertEquals(0L, largestLong1)
        assertEquals(0L, largestLong2)
        assertEquals(0L, largestLong3)
    }
    
    
    
}
