package com.vankorno.sandbox.dbManagement.fix

import androidx.test.filters.MediumTest
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.add.addEntity
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicateInts
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicateLongs
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicatePosition
import com.vankorno.vankornodb.delete.deleteTable
import com.vankorno.vankornodb.get.getColInts
import com.vankorno.vankornodb.get.getColLongs
import com.vankorno.vankornodb.get.getRowCount
import com.vankorno.vankornodb.newTable.createTable
import org.junit.Assert.assertEquals
import org.junit.Test

@MediumTest
class FixDuplicatesTest {
    
    /** With a non-existent table functions return 0 */
    @Test
    fun nonExistentTable() {
        val table = _Test.tableNamed("GodTable")
        assertAllFixFuns(table.name, 0, 0, 0)
    }
    
    
    /** With an empty table functions return 0 */
    @Test
    fun emptyTable() {
        val table = _Test.tableNamed("EmptyTable")
        dbh.createTable(table)
        assertAllFixFuns(table.name, 0, 0, 0)
        dbh.deleteTable(table)
    }
    
    
    /** With three rows containing unique values functions return 0 */
    @Test
    fun nothingToFix() {
        val table = _Test.tableNamed("nothingToFix")
        dbh.createTable(table)
        
        dbh.addEntity(table, TestEntity(1L, "1", 10L, int1 = 100, long1 = 1000L))
        dbh.addEntity(table, TestEntity(2L, "2", 20L, int1 = 200, long1 = 2000L))
        dbh.addEntity(table, TestEntity(3L, "3", 30L, int1 = 300, long1 = 3000L))
        
        assertAllFixFuns(table.name, 0, 0, 0)
        dbh.deleteTable(table)
    }
    
    
    /**
     *  With two rows containing identical relevant values:
     *  All fixDuplicate functions fix one duplicate.
     */
    @Test
    fun twoDuplicateRows() {
        val table = _Test.tableNamed("twoDuplicateRows")
        dbh.createTable(table)
        
        dbh.addEntity(table, TestEntity(position = 10L, int1 = 100, long1 = 1000L))
        dbh.addEntity(table, TestEntity(position = 10L, int1 = 100, long1 = 1000L))
        assertEquals(2, dbh.getRowCount(table))
        
        assertAllFixFuns(table.name, 1, 1, 1)
        
        dbh.deleteTable(table)
    }
    
    
    /** With multiple duplicate groups, all duplicates are fixed */
    @Test
    fun multipleDuplicates() {
        val table = _Test.tableNamed("multipleDuplicates")
        dbh.createTable(table)
        
        dbh.addEntities(table, listOf(
            TestEntity(position = 10L, int1 = 100, long1 = 1000L),
            TestEntity(position = 10L, int1 = 200, long1 = 1000L),
            TestEntity(position = 20L, int1 = 100, long1 = 2000L),
            TestEntity(position = 20L, int1 = 300, long1 = 2000L),
        ))
        assertAllFixFuns(table.name, 2, 1, 2)
        
        assertEquals(4, dbh.getColLongs(table.col { Long1 }).distinct().size)
        assertEquals(4, dbh.getColInts(table.col { Int1 }).distinct().size)
        assertEquals(4, dbh.getColLongs(table.col { Position }).distinct().size)
        
        assertAllFixFuns(table.name, 0, 0, 0)
        dbh.deleteTable(table)
    }
    
    
    /** With three identical values, two duplicates are fixed */
    @Test
    fun tripleDuplicates() {
        val table = _Test.tableNamed("tripleDuplicates")
        dbh.createTable(table)
        
        dbh.addEntities(table, listOf(
            TestEntity(position = 10L, int1 = 100, long1 = 1000L),
            TestEntity(position = 10L, int1 = 100, long1 = 1000L),
            TestEntity(position = 10L, int1 = 100, long1 = 1000L),
        ))
        assertAllFixFuns(table.name, 2, 2, 2)
        
        assertEquals(3, dbh.getColLongs(table.col { Long1 }).distinct().size)
        assertEquals(3, dbh.getColInts(table.col { Int1 }).distinct().size)
        assertEquals(3, dbh.getColLongs(table.col { Position }).distinct().size)
        
        assertAllFixFuns(table.name, 0, 0, 0)
        dbh.deleteTable(table)
    }
    
    
    /** Only duplicates matching the where clause are fixed */
    @Test
    fun whereClause() {
        val table = _Test.tableNamed("whereClause")
        dbh.createTable(table)
    
        dbh.addEntities(table, listOf(
            TestEntity(position = 10L, int1 = 100, long1 = 1000L, int2 = 1),
            TestEntity(position = 10L, int1 = 100, long1 = 1000L, int2 = 1),
            TestEntity(position = 20L, int1 = 200, long1 = 2000L, int2 = 2),
            TestEntity(position = 20L, int1 = 200, long1 = 2000L, int2 = 2),
        ))
        assertEquals(1, dbh.fixDuplicateLongs(table.col { Long1 }) { Int2 equal 1 })
        assertEquals(1, dbh.fixDuplicateInts(table.col { Int1 }) { Int2 equal 1 })
        assertEquals(1, dbh.fixDuplicatePosition(table) { Int2 equal 1 })
        
        assertAllFixFuns(table.name, 1, 1, 1)
        
        assertEquals(4, dbh.getColLongs(table.col { Long1 }).distinct().size)
        assertEquals(4, dbh.getColInts(table.col { Int1 }).distinct().size)
        assertEquals(4, dbh.getColLongs(table.col { Position }).distinct().size)
        assertEquals(2, dbh.getColInts(table.col { Int2 }).distinct().size)
        
        dbh.deleteTable(table)
    }
    
    
    
    private fun assertAllFixFuns(                                                  tableName: String,
                                                                           expectedLongs: Int,
                                                                            expectedInts: Int,
                                                                        expectedPosition: Int,
    ) {
        val table = _Test.tableNamed(tableName)
        assertEquals(expectedLongs, dbh.fixDuplicateLongs(table.col { Long1 }))
        assertEquals(expectedInts, dbh.fixDuplicateInts(table.col { Int1 }))
        assertEquals(expectedPosition, dbh.fixDuplicatePosition(table))
    }
}
