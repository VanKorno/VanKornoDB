package com.vankorno.sandbox.get.next

import androidx.test.filters.MediumTest
import com.vankorno.sandbox._entities.testEntity.CTest.Long1
import com.vankorno.sandbox._entities.testEntity.CTest.Str1
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import org.junit.Assert.assertEquals
import org.junit.Test

@MediumTest
class NextLongTest {
    /**
     * With an empty table:
     * getNextLong returns 1
     * getNextLongs returns first values
     */
    @Test
    fun emptyTable() {
        val table = "EmptyTable"
        dbh.createTable(table using _Test)
        assertBothFun(table, 1L, 2L)
        dbh.deleteTable(table)
    }
    
    /**
     * Standard behavior for tables with existing rows:
     * getNextLong and getNextLongs return values larger than the last existing value.
     */
    @Test
    fun standardLastValues() {
        val table = "standardLastValues"
        
        dbh.getLabRats(table)
        assertBothFun(table, 51L, 52L)
        
        dbh.deleteRows(table) { ID = 10 }
        assertBothFun(table, 51L, 52L)
        
        dbh.deleteRows(table) { cID less 10 }
        assertBothFun(table, 51L, 52L)
        
        dbh.addObjWithoutIdCheck(table using _Test, TestEntity(999990))
        assertBothFun(table, 999991L, 999992L)
        
        dbh.deleteTable(table)
    }
    
    
    /** In an empty table with first values reserved, the allocator finds values after them */
    @Test
    fun reservedInEmptyTable() {
        val table = "reservedInEmptyTable"
        val reserved = setOf(1L, 2L, 3L)
        
        dbh.createTable(table using _Test)
        assertBothFun(table, 4L, 5L, reserved)
        dbh.deleteTable(table)
    }
    
    
    /** In a filled table with next values reserved, the allocator finds values after all of them */
    @Test
    fun reservedInFilledTableSequential() {
        val table = "reservedInFilledTableSequential"
        val reserved = setOf(51L, 52L, 53L)
        
        dbh.getLabRats(table)
        assertBothFun(table, 54L, 55L, reserved)
        
        dbh.deleteTable(table)
    }
    
    
    /** With even values reserved and odd values available, the allocator finds values after them */
    @Test
    fun reservedEvenNumbersIn() {
        val table = "reservedEvenNumbersIn"
        val reserved = setOf(2L, 4L, 6L)

        dbh.createTable(table using _Test)
        assertBothFun(table, 7L, 8L, reserved)
        dbh.deleteTable(table)
    }
    
    
    /** With Long.MAX_VALUE in a single row, the allocator finds the first available values */
    @Test
    fun maxValueInSingleRowTable() {
        val table = "maxValueInSingleRowTable"
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addObjWithoutIdCheck(tableInfo, TestEntity(id = Long.MAX_VALUE, name = "Max"))
        
        assertBothFun(table, 1L, 2L)
        dbh.deleteTable(table)
    }
    
    
    /** With Long.MAX_VALUE reserved, the allocator finds the first available values */
    @Test
    fun maxValueInReserved() {
        val table = "maxValueInReserved"
        dbh.createTable(table using _Test)
        assertBothFun(table, 1L, 2L, setOf(50L, Long.MAX_VALUE))
        dbh.deleteTable(table)
    }
    
    
    /**
     * In case of MAX_VALUE - 1:
     * getNextLong returns MAX_VALUE
     * getNextLongs returns first available values
     */
    @Test
    fun closeToMax() {
        val table = "closeToMax"
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addObjWithoutIdCheck(tableInfo, TestEntity(id = Long.MAX_VALUE - 1, name = "Max"))
        
        val value1 = dbh.getNextLong(table, cID)
        val values1 = dbh.getNextLongs(table, cID, 2)
        assertEquals(Long.MAX_VALUE, value1)
        assertEquals(listOf(1L, 2L), values1)
        
        dbh.deleteTable(table)
    }
    
    
    /**
     * In case of MAX_VALUE - 1 in reserved:
     * getNextLong returns MAX_VALUE
     * getNextLongs returns first available values
     */
    @Test
    fun reservedCloseToMax() {
        val table = "reservedCloseToMax"
        val tableInfo = table using _Test
        val reserved = setOf(100L, Long.MAX_VALUE - 1)
        
        dbh.createTable(tableInfo)
        dbh.addObjWithoutIdCheck(tableInfo, TestEntity(id = Long.MAX_VALUE - 1, name = "Max"))
        
        val value1 = dbh.getNextLong(table, cID, reserved = reserved)
        val values1 = dbh.getNextLongs(table, cID, 2, reserved = reserved)
        assertEquals(Long.MAX_VALUE, value1)
        assertEquals(listOf(1L, 2L), values1)
        
        dbh.deleteTable(table)
    }
    
    
    /** With even values already taken, fallback finds the available odd values */
    @Test
    fun takenEvenNumbersWithMaxValueCeiling() {
        val table = "takenEvenNumbersWithMaxValueCeiling"
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addObjects(tableInfo, listOf(
            TestEntity(2L),
            TestEntity(4L),
            TestEntity(6L),
            TestEntity(Long.MAX_VALUE)
        ))
        
        assertBothFun(table, 1L, 3L)
        dbh.deleteTable(table)
    }
    
    
    /** With even values reserved, fallback finds the available odd values */
    @Test
    fun reservedEvenNumbersWithMaxValueCeiling() {
        val table = "reservedEvenNumbersWithMaxValueCeiling"
        val reserved = setOf(2L, 4L, 6L, Long.MAX_VALUE)
        
        dbh.createTable(table using _Test)
        assertBothFun(table, 1L, 3L, reserved)
        dbh.deleteTable(table)
    }
    
    
    /**
     * With where filter:
     * Values in rows not matching where clause are ignored,
     * so getNextLong and getNextLongs return values larger than the largest matching row.
     */
    @Test
    fun whereFilterStandard() {
        val table = "whereFilterStandardLong"
        val tableInfo = table using _Test

        dbh.createTable(tableInfo)
        dbh.addObjects(tableInfo, listOf(
            TestEntity(long1 = 10L, str1 = "cat"),
            TestEntity(long1 = 20L, str1 = "cat"),
            TestEntity(long1 = 100L, str1 = "dog")
        ))

        val catNext = dbh.getNextLong(table, Long1) { Str1 equal "cat" }
        val catNexts = dbh.getNextLongs(table, Long1, 2) { Str1 equal "cat" }
        assertEquals(21L, catNext)
        assertEquals(listOf(21L, 22L), catNexts)

        val dogNext = dbh.getNextLong(table, Long1) { Str1 equal "dog" }
        val dogNexts = dbh.getNextLongs(table, Long1, 2) { Str1 equal "dog" }
        assertEquals(101L, dogNext)
        assertEquals(listOf(101L, 102L), dogNexts)

        // Non-matching where condition behaves like empty table
        val emptyNext = dbh.getNextLong(table, Long1) { Str1 equal "bird" }
        val emptyNexts = dbh.getNextLongs(table, Long1, 2) { Str1 equal "bird" }
        assertEquals(1L, emptyNext)
        assertEquals(listOf(1L, 2L), emptyNexts)

        dbh.deleteTable(table)
    }


    /**
     * With Long.MAX_VALUE and where filter:
     * Fallback only considers values in matching rows as taken.
     * Values from other rows are free to be assigned.
     */
    @Test
    fun whereFilterWithFallback() {
        val table = "whereFilterWithFallbackLong"
        val tableInfo = table using _Test

        dbh.createTable(tableInfo)
        dbh.addObjects(tableInfo, listOf(
            TestEntity(long1 = 1L, str1 = "cat"),
            TestEntity(long1 = 2L, str1 = "dog"), // 2L is taken by dog, not cat
            TestEntity(long1 = 3L, str1 = "cat"),
            TestEntity(long1 = Long.MAX_VALUE, str1 = "cat")
        ))

        // For cat, 1, 3, and MAX_VALUE are taken. 2 is free!
        val catNext = dbh.getNextLong(table, Long1) { Str1 equal "cat" }
        val catNexts = dbh.getNextLongs(table, Long1, 2) { Str1 equal "cat" }
        assertEquals(2L, catNext)
        assertEquals(listOf(2L, 4L), catNexts)

        // For dog, only 2 is taken, and dog does NOT have MAX_VALUE, so it allocates after 2
        val dogNext = dbh.getNextLong(table, Long1) { Str1 equal "dog" }
        val dogNexts = dbh.getNextLongs(table, Long1, 2) { Str1 equal "dog" }
        assertEquals(3L, dogNext)
        assertEquals(listOf(3L, 4L), dogNexts)

        dbh.deleteTable(table)
    }


    /**
     * With where filter combined with reserved set and startFrom.
     */
    @Test
    fun whereFilterWithReservedAndStartFrom() {
        val table = "whereFilterWithReservedAndStartFromLong"
        val tableInfo = table using _Test

        dbh.createTable(tableInfo)
        dbh.addObjects(tableInfo, listOf(
            TestEntity(long1 = 5L, str1 = "cat"),
            TestEntity(long1 = 10L, str1 = "cat"),
            TestEntity(long1 = 50L, str1 = "dog")
        ))

        val reserved = setOf(11L, 12L)
        val next = dbh.getNextLong(table, Long1, startFrom = 5L, reserved = reserved) { Str1 equal "cat" }
        val nexts = dbh.getNextLongs(table, Long1, 2, startFrom = 5L, reserved = reserved) { Str1 equal "cat" }
        assertEquals(13L, next)
        assertEquals(listOf(13L, 14L), nexts)

        dbh.deleteTable(table)
    }
    
    
    private fun assertBothFun(                                        table: String,
                                                              firstExpected: Long,
                                                             secondExpected: Long,
                                                                   reserved: Set<Long> = emptySet(),
    ) {
        val value1 = dbh.getNextLong(table, cID, reserved = reserved)
        val values1 = dbh.getNextLongs(table, cID, 2, reserved = reserved)
        assertEquals(firstExpected, value1)
        assertEquals(listOf(firstExpected, secondExpected), values1)
    }
}
