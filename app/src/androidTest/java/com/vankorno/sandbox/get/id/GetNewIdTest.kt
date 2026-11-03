package com.vankorno.sandbox.get.id

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRatQuant
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import org.junit.Assert.assertEquals
import org.junit.Test

@MediumTest
class GetNewIdTest {
    /**
     *  With a non-existent table:
     *  getNewId returns -1
     *  getNewIDs returns an empty List
     */
    @Test
    fun nonExistentTable() {
        val table = "GodTable"
        
        val id1 = dbh.getNewId(table)
        val ids1 = dbh.getNewIDs(table, 3)
        assertEquals(-1L, id1)
        assertEquals(emptyList<Long>(), ids1)
        
        dbh.deleteTable(table)
    }
    
    /**
     *  With an empty table:
     *  getNewId returns 1
     *  getNewIDs returns first ids
     */
    @Test
    fun emptyTable() {
        val table = "EmptyTable"
        dbh.createTable(table using _Test)
        assertBothFun(table, 1L, 2L)
        dbh.deleteTable(table)
    }
    
    /**
     *  Standard behavior for tables with existing rows:
     *  getNewId and getNewIDs return ids larger (next) than the last existing id.
     */
    @Test
    fun standardLastIDs() {
        val table = "standardLastIDs"
        
        dbh.getLabRats(table)
        assertBothFun(table, 51L, 52L)
        
        // Delete one row
        dbh.deleteRows(table) { ID = 10 }
        assertBothFun(table, 51L, 52L)
        
        // Delete first rows
        dbh.deleteRows(table) { cID less 10 }
        assertBothFun(table, 51L, 52L)
        
        // Add larger num ID
        dbh.addObjWithoutIdCheck(table using _Test, TestEntity(999990))
        assertBothFun(table, 999991L, 999992L)
        
        dbh.deleteTable(table)
    }
    
    
    /** In an empty table with first IDs reserved, fun find IDs after. */
    @Test
    fun reservedInEmptyTable() {
        val table = "reservedInEmptyTable"
        val reserved = setOf(1L, 2L, 3L)
        
        dbh.createTable(table using _Test)
        assertBothFun(table, 4L, 5L, reserved)
        dbh.deleteTable(table)
    }
    
    
    /**
     *  In a filled table with first IDs taken and next IDs reserved, fun find IDs after all of them.
     */
    @Test
    fun reservedInFilledTableSequential() {
        val table = "reservedInFilledTableSequential"
        val reserved = setOf(51L, 52L, 53L)
        
        dbh.getLabRats(table)
        assertBothFun(table, 54L, 55L, reserved)
        
        dbh.deleteTable(table)
    }
    
    
    /** With even IDs reserved and odd not reserved, the fun find IDs after all of them. */
    @Test
    fun reservedEvenNumbersIn() {
        val table = "reservedEvenNumbersIn"
        val reserved = setOf(2L, 4L, 6L)
        
        dbh.createTable(table using _Test)
        assertBothFun(table, 7L, 8L, reserved)
        dbh.deleteTable(table)
    }
    
    
    /** In a table with a single row that has max Long value ID, fun find first ids */
    @Test
    fun maxValueInSingleRowTable() {
        val table = "maxValueInSingleRowTable"
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addObjWithoutIdCheck(tableInfo, TestEntity(id = Long.MAX_VALUE, name = "Max"))
        
        assertBothFun(table, 1L, 2L)
        dbh.deleteTable(table)
    }
    
    
    /** With max Long value in reserved IDs, fun find first ids */
    @Test
    fun maxValueInReserved() {
        val table = "maxValueInReserved"
        dbh.createTable(table using _Test)
        assertBothFun(table, 1L, 2L, setOf(50L, Long.MAX_VALUE))
        dbh.deleteTable(table)
    }
    
    
    /**
     * In case of MAX_VALUE - 1:
     * getNewId returns MAX_VALUE ID
     * getNewIDs return first IDs
     */
    @Test
    fun closeToMax() {
        val table = "closeToMax"
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addObjWithoutIdCheck(tableInfo, TestEntity(id = Long.MAX_VALUE - 1, name = "Max"))
        
        val id1 = dbh.getNewId(table)
        val ids1 = dbh.getNewIDs(table, 2)
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
        val table = "reservedCloseToMax"
        val tableInfo = table using _Test
        val reserved = setOf(100L, Long.MAX_VALUE - 1)
        
        dbh.createTable(tableInfo)
        dbh.addObjWithoutIdCheck(tableInfo, TestEntity(id = Long.MAX_VALUE - 1, name = "Max"))
        
        val id1 = dbh.getNewId(table, reserved)
        val ids1 = dbh.getNewIDs(table, 2, reserved)
        assertEquals(Long.MAX_VALUE, id1)
        assertEquals(listOf(1L, 2L), ids1)
        
        dbh.deleteTable(table)
    }
    
    
    /** With even IDs already taken, Plan-B finds odd IDs. */
    @Test
    fun takenEvenNumbersWithMaxValueCeiling() {
        val table = "takenEvenNumbersWithMaxValueCeiling"
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addObjects(tableInfo, listOf(TestEntity(2L), TestEntity(4L), TestEntity(6L), TestEntity(Long.MAX_VALUE)))
        
        assertBothFun(table, 1L, 3L)
        dbh.deleteTable(table)
    }
    
    
    /** With even IDs reserved and odd not reserved, Plan-B finds odd IDs. */
    @Test
    fun reservedEvenNumbersWithMaxValueCeiling() {
        val table = "reservedEvenNumbersWithMaxValueCeiling"
        val reserved = setOf(2L, 4L, 6L, Long.MAX_VALUE)
        
        dbh.createTable(table using _Test)
        assertBothFun(table, 1L, 3L, reserved)
        dbh.deleteTable(table)
    }
    
    
    
    private fun assertBothFun(                                        table: String,
                                                              firstExpected: Long,
                                                             secondExpected: Long,
                                                                   reserved: Set<Long> = emptySet(),
    ) {
        val id1 = dbh.getNewId(table, reserved)
        val ids1 = dbh.getNewIDs(table, 2, reserved)
        assertEquals(firstExpected, id1)
        assertEquals(listOf(firstExpected, secondExpected), ids1)
    }
    
    
    
}
