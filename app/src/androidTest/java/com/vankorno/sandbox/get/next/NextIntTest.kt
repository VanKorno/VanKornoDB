package com.vankorno.sandbox.get.next

import androidx.test.filters.MediumTest
import com.vankorno.sandbox._entities.testEntity.CTest.Int1
import com.vankorno.sandbox._entities.testEntity.CTest.Str1
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.using
import org.junit.Assert.assertEquals
import org.junit.Test

@MediumTest
class NextIntTest {
    /**
     * With an empty table:
     * getNextInt returns 1
     * getNextInts returns first values
     */
    @Test
    fun emptyTable() {
        val table = "EmptyTable"
        dbh.createTable(table using _Test)
        assertBothFun(table, 1, 2)
        dbh.deleteTable(table)
    }
    
    
    /**
     * Standard behavior for tables with existing values:
     * getNextInt and getNextInts return values larger than the largest existing value.
     */
    @Test
    fun standardLastValues() {
        val table = "standardLastValues"
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addEntities(tableInfo, List(20) { idx ->
            TestEntity(int1 = idx + 1)
        })
        
        assertBothFun(table, 21, 22)
        
        dbh.deleteRows(table) { Int1 equal 10 }
        assertBothFun(table, 21, 22)
        
        dbh.deleteRows(table) { Int1 less 10 }
        assertBothFun(table, 21, 22)
        
        dbh.addEntityWithoutIdCheck(tableInfo, TestEntity(int1 = 999990))
        assertBothFun(table, 999991, 999992)
        
        dbh.deleteTable(table)
    }


    /** In an empty table with first values reserved, the allocator finds values after them. */
    @Test
    fun reservedInEmptyTable() {
        val table = "reservedInEmptyTable"
        val reserved = setOf(1, 2, 3)
        
        dbh.createTable(table using _Test)
        assertBothFun(table, 4, 5, reserved)
        dbh.deleteTable(table)
    }
    
    
    /** In a filled table with next values reserved, the allocator finds values after all of them. */
    @Test
    fun reservedInFilledTableSequential() {
        val table = "reservedInFilledTableSequential"
        val reserved = setOf(11, 12, 13)
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addEntities(tableInfo, List(10) { idx ->
            TestEntity(int1 = idx + 1)
        })
        
        assertBothFun(table, 14, 15, reserved)
        dbh.deleteTable(table)
    }
    
    
    /** With even values reserved and odd values available, the allocator finds values after them. */
    @Test
    fun reservedEvenNumbersIn() {
        val table = "reservedEvenNumbersIn"
        val reserved = setOf(2, 4, 6)
        
        dbh.createTable(table using _Test)
        assertBothFun(table, 7, 8, reserved)
        dbh.deleteTable(table)
    }
    
    
    /** With Int.MAX_VALUE in a single row, the allocator finds the first available values. */
    @Test
    fun maxValueInSingleRowTable() {
        val table = "maxValueInSingleRowTable"
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addEntityWithoutIdCheck(tableInfo, TestEntity(int1 = Int.MAX_VALUE))
        
        assertBothFun(table, 1, 2)
        dbh.deleteTable(table)
    }
    
    
    /** With Int.MAX_VALUE reserved, the allocator finds the first available values. */
    @Test
    fun maxValueInReserved() {
        val table = "maxValueInReserved"
        dbh.createTable(table using _Test)
        assertBothFun(table, 1, 2, setOf(50, Int.MAX_VALUE))
        dbh.deleteTable(table)
    }
    
    
    /**
     * In case of MAX_VALUE - 1:
     * getNextInt returns MAX_VALUE
     * getNextInts returns first available values
     */
    @Test
    fun closeToMax() {
        val table = "closeToMax"
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addEntityWithoutIdCheck(tableInfo, TestEntity(int1 = Int.MAX_VALUE - 1))
        
        val value1 = dbh.getNextInt(table, Int1)
        val values1 = dbh.getNextInts(table, Int1, 2)
        assertEquals(Int.MAX_VALUE, value1)
        assertEquals(listOf(1, 2), values1)
        
        dbh.deleteTable(table)
    }
    
    
    /**
     * In case of MAX_VALUE - 1 in reserved:
     * getNextInt returns MAX_VALUE
     * getNextInts returns first available values
     */
    @Test
    fun reservedCloseToMax() {
        val table = "reservedCloseToMax"
        val tableInfo = table using _Test
        val reserved = setOf(100, Int.MAX_VALUE - 1)
        
        dbh.createTable(tableInfo)
        dbh.addEntityWithoutIdCheck(tableInfo, TestEntity(int1 = Int.MAX_VALUE - 1))
        
        val value1 = dbh.getNextInt(table, Int1, reserved = reserved)
        val values1 = dbh.getNextInts(table, Int1, 2, reserved = reserved)
        assertEquals(Int.MAX_VALUE, value1)
        assertEquals(listOf(1, 2), values1)
        
        dbh.deleteTable(table)
    }
    
    
    /** With even values already taken, fallback finds the available odd values. */
    @Test
    fun takenEvenNumbersWithMaxValueCeiling() {
        val table = "takenEvenNumbersWithMaxValueCeiling"
        val tableInfo = table using _Test
        
        dbh.createTable(tableInfo)
        dbh.addEntities(tableInfo, listOf(
            TestEntity(int1 = 2),
            TestEntity(int1 = 4),
            TestEntity(int1 = 6),
            TestEntity(int1 = Int.MAX_VALUE)
        ))
        
        assertBothFun(table, 1, 3)
        dbh.deleteTable(table)
    }
    
    
    /** With even values reserved, fallback finds the available odd values. */
    @Test
    fun reservedEvenNumbersWithMaxValueCeiling() {
        val table = "reservedEvenNumbersWithMaxValueCeiling"
        val reserved = setOf(2, 4, 6, Int.MAX_VALUE)
        
        dbh.createTable(table using _Test)
        assertBothFun(table, 1, 3, reserved)
        dbh.deleteTable(table)
    }
    
    
    /**
     * With where filter:
     * Values in rows not matching where clause are ignored,
     * so getNextInt and getNextInts return values larger than the largest matching row.
     */
    @Test
    fun whereFilterStandard() {
        val table = "whereFilterStandard"
        val tableInfo = table using _Test

        dbh.createTable(tableInfo)
        dbh.addEntities(tableInfo, listOf(
            TestEntity(int1 = 10, str1 = "cat"),
            TestEntity(int1 = 20, str1 = "cat"),
            TestEntity(int1 = 100, str1 = "dog")
        ))

        val catNext = dbh.getNextInt(table, Int1) { Str1 equal "cat" }
        val catNexts = dbh.getNextInts(table, Int1, 2) { Str1 equal "cat" }
        assertEquals(21, catNext)
        assertEquals(listOf(21, 22), catNexts)

        val dogNext = dbh.getNextInt(table, Int1) { Str1 equal "dog" }
        val dogNexts = dbh.getNextInts(table, Int1, 2) { Str1 equal "dog" }
        assertEquals(101, dogNext)
        assertEquals(listOf(101, 102), dogNexts)

        // Non-matching where condition behaves like empty table
        val emptyNext = dbh.getNextInt(table, Int1) { Str1 equal "bird" }
        val emptyNexts = dbh.getNextInts(table, Int1, 2) { Str1 equal "bird" }
        assertEquals(1, emptyNext)
        assertEquals(listOf(1, 2), emptyNexts)

        dbh.deleteTable(table)
    }


    /**
     * With Int.MAX_VALUE and where filter:
     * Fallback only considers values in matching rows as taken.
     * Values from other rows are free to be assigned.
     */
    @Test
    fun whereFilterWithFallback() {
        val table = "whereFilterWithFallback"
        val tableInfo = table using _Test

        dbh.createTable(tableInfo)
        dbh.addEntities(tableInfo, listOf(
            TestEntity(int1 = 1, str1 = "cat"),
            TestEntity(int1 = 2, str1 = "dog"), // 2 is taken by dog, not cat
            TestEntity(int1 = 3, str1 = "cat"),
            TestEntity(int1 = Int.MAX_VALUE, str1 = "cat")
        ))

        // For cat, 1, 3, and MAX_VALUE are taken. 2 is free!
        val catNext = dbh.getNextInt(table, Int1) { Str1 equal "cat" }
        val catNexts = dbh.getNextInts(table, Int1, 2) { Str1 equal "cat" }
        assertEquals(2, catNext)
        assertEquals(listOf(2, 4), catNexts)

        // For dog, only 2 is taken, and dog does NOT have MAX_VALUE, so it allocates after 2
        val dogNext = dbh.getNextInt(table, Int1) { Str1 equal "dog" }
        val dogNexts = dbh.getNextInts(table, Int1, 2) { Str1 equal "dog" }
        assertEquals(3, dogNext)
        assertEquals(listOf(3, 4), dogNexts)

        dbh.deleteTable(table)
    }


    /**
     * With where filter combined with reserved set and startFrom.
     */
    @Test
    fun whereFilterWithReservedAndStartFrom() {
        val table = "whereFilterWithReservedAndStartFrom"
        val tableInfo = table using _Test

        dbh.createTable(tableInfo)
        dbh.addEntities(tableInfo, listOf(
            TestEntity(int1 = 5, str1 = "cat"),
            TestEntity(int1 = 10, str1 = "cat"),
            TestEntity(int1 = 50, str1 = "dog")
        ))

        val reserved = setOf(11, 12)
        val next = dbh.getNextInt(table, Int1, startFrom = 5, reserved = reserved) { Str1 equal "cat" }
        val nexts = dbh.getNextInts(table, Int1, 2, startFrom = 5, reserved = reserved) { Str1 equal "cat" }
        assertEquals(13, next)
        assertEquals(listOf(13, 14), nexts)

        dbh.deleteTable(table)
    }
    
    
    private fun assertBothFun(                                        table: String,
                                                              firstExpected: Int,
                                                             secondExpected: Int,
                                                                   reserved: Set<Int> = emptySet(),
    ) {
        val value1 = dbh.getNextInt(table, Int1, reserved = reserved)
        val values1 = dbh.getNextInts(table, Int1, 2, reserved = reserved)
        assertEquals(firstExpected, value1)
        assertEquals(listOf(firstExpected, secondExpected), values1)
    }
}
