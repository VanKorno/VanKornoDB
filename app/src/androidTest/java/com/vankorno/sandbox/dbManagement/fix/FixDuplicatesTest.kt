package com.vankorno.sandbox.dbManagement.fix

import androidx.test.filters.MediumTest
import com.vankorno.sandbox._entities.testEntity.CTest
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import org.junit.Assert.assertEquals
import org.junit.Test

@MediumTest
class FixDuplicatesTest {
    
    /** With a non-existent table functions return 0 */
    @Test
    fun nonExistentTable() {
        val table = "GodTable"
        assertAllFixFuns(table, 0, 0, 0)
    }
    
    
    /** With an empty table functions return 0 */
    @Test
    fun emptyTable() {
        val table = "EmptyTable"
        dbh.createTable(table using _Test)
        assertAllFixFuns(table, 0, 0, 0)
        dbh.deleteTable(table)
    }
    
    
    /** With three rows containing unique values functions return 0 */
    @Test
    fun nothingToFix() {
        val table = "nothingToFix"
        val tableInfo = table using _Test
        dbh.createTable(table using _Test)
        
        dbh.addObj(tableInfo, TestEntity(1L, "1", 10L, int1 = 100, long1 = 1000L))
        dbh.addObj(tableInfo, TestEntity(2L, "2", 20L, int1 = 200, long1 = 2000L))
        dbh.addObj(tableInfo, TestEntity(3L, "3", 30L, int1 = 300, long1 = 3000L))
        
        assertAllFixFuns(table, 0, 0, 0)
        dbh.deleteTable(table)
    }
    
    
    /**
     *  With two rows containing identical relevant values:
     *  All fixDuplicate functions fix one duplicate.
     */
    @Test
    fun twoDuplicateRows() {
        val table = "twoDuplicateRows"
        val tableInfo = table using _Test
        dbh.createTable(tableInfo)
        
        dbh.addObj(tableInfo, TestEntity(position = 10L, int1 = 100, long1 = 1000L))
        dbh.addObj(tableInfo, TestEntity(position = 10L, int1 = 100, long1 = 1000L))
        assertEquals(2, dbh.getRowCount(table))
        
        assertAllFixFuns(table, 1, 1, 1)
        
        dbh.deleteTable(table)
    }
    
    
    /** With multiple duplicate groups, all duplicates are fixed */
    @Test
    fun multipleDuplicates() {
        val table = "multipleDuplicates"
        val tableInfo = table using _Test
        dbh.createTable(tableInfo)
        
        dbh.addObjects(tableInfo, listOf(
            TestEntity(position = 10L, int1 = 100, long1 = 1000L),
            TestEntity(position = 10L, int1 = 200, long1 = 1000L),
            TestEntity(position = 20L, int1 = 100, long1 = 2000L),
            TestEntity(position = 20L, int1 = 300, long1 = 2000L),
        ))
        assertAllFixFuns(table, 2, 1, 2)
        
        assertEquals(4, dbh.getColLongs(table, CTest.Long1).distinct().size)
        assertEquals(4, dbh.getColInts(table, CTest.Int1).distinct().size)
        assertEquals(4, dbh.getColLongs(table, cPosition).distinct().size)
        
        assertAllFixFuns(table, 0, 0, 0)
        dbh.deleteTable(table)
    }
    
    
    /** With three identical values, two duplicates are fixed */
    @Test
    fun tripleDuplicates() {
        val table = "tripleDuplicates"
        val tableInfo = table using _Test
        dbh.createTable(tableInfo)
        
        dbh.addObjects(tableInfo, listOf(
            TestEntity(position = 10L, int1 = 100, long1 = 1000L),
            TestEntity(position = 10L, int1 = 100, long1 = 1000L),
            TestEntity(position = 10L, int1 = 100, long1 = 1000L),
        ))
        assertAllFixFuns(table, 2, 2, 2)
        
        assertEquals(3, dbh.getColLongs(table, CTest.Long1).distinct().size)
        assertEquals(3, dbh.getColInts(table, CTest.Int1).distinct().size)
        assertEquals(3, dbh.getColLongs(table, cPosition).distinct().size)
        
        assertAllFixFuns(table, 0, 0, 0)
        dbh.deleteTable(table)
    }
    
    
    /** Only duplicates matching the where clause are fixed */
    @Test
    fun whereClause() {
        val table = "whereClause"
        val tableInfo = table using _Test
        dbh.createTable(tableInfo)
    
        dbh.addObjects(tableInfo, listOf(
            TestEntity(position = 10L, int1 = 100, long1 = 1000L, int2 = 1),
            TestEntity(position = 10L, int1 = 100, long1 = 1000L, int2 = 1),
            TestEntity(position = 20L, int1 = 200, long1 = 2000L, int2 = 2),
            TestEntity(position = 20L, int1 = 200, long1 = 2000L, int2 = 2),
        ))
        assertEquals(1, dbh.fixDuplicateLongs(table, CTest.Long1) { CTest.Int2 equal 1 })
        assertEquals(1, dbh.fixDuplicateInts(table, CTest.Int1) { CTest.Int2 equal 1 })
        assertEquals(1, dbh.fixDuplicatePosition(table) { CTest.Int2 equal 1 })
        
        assertAllFixFuns(table, 1, 1, 1)
        
        assertEquals(4, dbh.getColLongs(table, CTest.Long1).distinct().size)
        assertEquals(4, dbh.getColInts(table, CTest.Int1).distinct().size)
        assertEquals(4, dbh.getColLongs(table, cPosition).distinct().size)
        assertEquals(2, dbh.getColInts(table, CTest.Int2).distinct().size)
        
        dbh.deleteTable(table)
    }
    
    
    
    private fun assertAllFixFuns(                                                  table: String,
                                                                           expectedLongs: Int,
                                                                            expectedInts: Int,
                                                                        expectedPosition: Int,
    ) {
        assertEquals(expectedLongs, dbh.fixDuplicateLongs(table, CTest.Long1))
        assertEquals(expectedInts, dbh.fixDuplicateInts(table, CTest.Int1))
        assertEquals(expectedPosition, dbh.fixDuplicatePosition(table))
    }
}
