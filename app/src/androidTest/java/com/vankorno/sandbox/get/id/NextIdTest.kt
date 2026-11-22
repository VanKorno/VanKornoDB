package com.vankorno.sandbox.get.id

import androidx.test.filters.MediumTest
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._TestEntity
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.add.addEntityWithoutIdCheck
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.delete.deleteRows
import com.vankorno.vankornodb.delete.deleteTable
import com.vankorno.vankornodb.get.id.getNextIDs
import com.vankorno.vankornodb.get.id.getNextId
import com.vankorno.vankornodb.newTable.createTable
import org.junit.Assert.assertEquals
import org.junit.Test

@MediumTest
class NextIdTest {
    /**
     *  With a non-existent table:
     *  getNewId returns -1
     *  getNewIDs returns an empty List
     */
    @Test
    fun nonExistentTable() {
        val table = _TestEntity.tableNamed("GodTable")
        
        val id1 = dbh.getNextId(table)
        val ids1 = dbh.getNextIDs(table, 3)
        assertEquals(-1L, id1)
        assertEquals(emptyList<Long>(), ids1)
    }
    
    /**
     *  With an empty table:
     *  getNewId returns 1
     *  getNewIDs returns first ids
     */
    @Test
    fun emptyTable() {
        val table = _TestEntity.tableNamed("EmptyTable")
        dbh.createTable(table)
        assertBothFun(table.name, 1L, 2L)
        dbh.deleteTable(table)
    }
    
    /**
     *  Standard behavior for tables with existing rows:
     *  getNewId and getNewIDs return ids larger (next) than the last existing id.
     */
    @Test
    fun standardLastIDs() {
        val table = _TestEntity.tableNamed("standardLastIDs")
        
        dbh.getLabRats(table.name)
        assertBothFun(table.name, 51L, 52L)
        
        // Delete one row
        dbh.deleteRows(table) { Id equal 10 }
        assertBothFun(table.name, 51L, 52L)
        
        // Delete first rows
        dbh.deleteRows(table) { Id less 10 }
        assertBothFun(table.name, 51L, 52L)
        
        // Add larger num ID
        dbh.addEntityWithoutIdCheck(table, TestEntity(999990))
        assertBothFun(table.name, 999991L, 999992L)
        
        dbh.deleteTable(table)
    }
    
    
    /** In an empty table with first IDs reserved, fun find IDs after. */
    @Test
    fun reservedInEmptyTable() {
        val table = _TestEntity.tableNamed("reservedInEmptyTable")
        val reserved = setOf(1L, 2L, 3L)
        
        dbh.createTable(table)
        assertBothFun(table.name, 4L, 5L, reserved)
        dbh.deleteTable(table)
    }
    
    
    /**
     *  In a filled table with first IDs taken and next IDs reserved, fun find IDs after all of them.
     */
    @Test
    fun reservedInFilledTableSequential() {
        val table = _TestEntity.tableNamed("reservedInFilledTableSequential")
        val reserved = setOf(51L, 52L, 53L)
        
        dbh.getLabRats(table.name)
        assertBothFun(table.name, 54L, 55L, reserved)
        
        dbh.deleteTable(table)
    }
    
    
    /** With even IDs reserved and odd not reserved, the fun find IDs after all of them. */
    @Test
    fun reservedEvenNumbersIn() {
        val table = _TestEntity.tableNamed("reservedEvenNumbersIn")
        val reserved = setOf(2L, 4L, 6L)
        
        dbh.createTable(table)
        assertBothFun(table.name, 7L, 8L, reserved)
        dbh.deleteTable(table)
    }
    
    
    /** In a table with a single row that has max Long value ID, fun find first ids */
    @Test
    fun maxValueInSingleRowTable() {
        val table = _TestEntity.tableNamed("maxValueInSingleRowTable")
        
        dbh.createTable(table)
        dbh.addEntityWithoutIdCheck(table, TestEntity(id = Long.MAX_VALUE, name = "Max"))
        
        assertBothFun(table.name, 1L, 2L)
        dbh.deleteTable(table)
    }
    
    
    /** With max Long value in reserved IDs, fun find first ids */
    @Test
    fun maxValueInReserved() {
        val table = _TestEntity.tableNamed("maxValueInReserved")
        dbh.createTable(table)
        assertBothFun(table.name, 1L, 2L, setOf(50L, Long.MAX_VALUE))
        dbh.deleteTable(table)
    }
    
    
    /**
     * In case of MAX_VALUE - 1:
     * getNewId returns MAX_VALUE ID
     * getNewIDs return first IDs
     */
    @Test
    fun closeToMax() {
        val table = _TestEntity.tableNamed("closeToMax")
        
        dbh.createTable(table)
        dbh.addEntityWithoutIdCheck(table, TestEntity(id = Long.MAX_VALUE - 1, name = "Max"))
        
        val id1 = dbh.getNextId(table)
        val ids1 = dbh.getNextIDs(table, 2)
        assertEquals(Long.MAX_VALUE, id1)
        assertEquals(listOf(1L, 2L), ids1)
        
        dbh.deleteTable(table)
    }
    
    
    /**
     * In case of 'MAX_VALUE - 1' in reserved:
     * getNewId returns MAX_VALUE ID
     * getNewIDs return first IDs
     */
    @Test
    fun reservedCloseToMax() {
        val table = _TestEntity.tableNamed("reservedCloseToMax")
        val reserved = setOf(100L, Long.MAX_VALUE - 1)
        
        dbh.createTable(table)
        dbh.addEntityWithoutIdCheck(table, TestEntity(id = Long.MAX_VALUE - 1, name = "Max"))
        
        val id1 = dbh.getNextId(table, reserved)
        val ids1 = dbh.getNextIDs(table, 2, reserved)
        assertEquals(Long.MAX_VALUE, id1)
        assertEquals(listOf(1L, 2L), ids1)
        
        dbh.deleteTable(table)
    }
    
    
    /** With even IDs already taken, Plan-B finds odd IDs. */
    @Test
    fun takenEvenNumbersWithMaxValueCeiling() {
        val table = _TestEntity.tableNamed("takenEvenNumbersWithMaxValueCeiling")
        
        dbh.createTable(table)
        dbh.addEntities(table, listOf(TestEntity(2L), TestEntity(4L), TestEntity(6L), TestEntity(Long.MAX_VALUE)))
        
        assertBothFun(table.name, 1L, 3L)
        dbh.deleteTable(table)
    }
    
    
    /** With even IDs reserved and odd not reserved, Plan-B finds odd IDs. */
    @Test
    fun reservedEvenNumbersWithMaxValueCeiling() {
        val table = _TestEntity.tableNamed("reservedEvenNumbersWithMaxValueCeiling")
        val reserved = setOf(2L, 4L, 6L, Long.MAX_VALUE)
        
        dbh.createTable(table)
        assertBothFun(table.name, 1L, 3L, reserved)
        dbh.deleteTable(table)
    }
    
    
    
    private fun assertBothFun(                                        tableName: String,
                                                              firstExpected: Long,
                                                             secondExpected: Long,
                                                                   reserved: Set<Long> = emptySet(),
    ) {
        val table = _TestEntity.tableNamed(tableName)
        val id1 = dbh.getNextId(table, reserved)
        val ids1 = dbh.getNextIDs(table, 2, reserved)
        assertEquals(firstExpected, id1)
        assertEquals(listOf(firstExpected, secondExpected), ids1)
    }
    
    
    
}
