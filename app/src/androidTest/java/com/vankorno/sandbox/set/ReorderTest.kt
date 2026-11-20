package com.vankorno.sandbox.set

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.get.getLong
import com.vankorno.vankornodb.get.hasRows
import com.vankorno.vankornodb.get.tableExists
import com.vankorno.vankornodb.newTable.createTable
import com.vankorno.vankornodb.set.reorder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private const val RoundKnightTable = "RoundKnightTable"

@MediumTest
class ReorderTest : BaseAndroidTest() {
    
    @Test
    fun testReorder() {
        prep()
        
        simpleReorderDown()
        simpleReorderUp()
        
        // TODO the rest
    }
    
    private fun prep() {
        for (idx in 1..6) {
            val table = _Test.tableNamed(RoundKnightTable + idx)
            dbh.createTable(table)
            
            val objects = List(10) { idx ->
                TestEntity(
                    id = idx + 1L,
                    name = "Knight" + idx + 1,
                    position = idx + 1L,
                )
            }
            dbh.addEntities(table, objects)
        }
        // sanity
        assertTrue(dbh.tableExists(RoundKnightTable + 1))
        assertTrue(dbh.hasRows(_Test.tableNamed(RoundKnightTable + 1)))
    }
    
    
    fun simpleReorderDown() {
        val table = _Test.tableNamed(RoundKnightTable + 1)
        
        // sanity
        assertEquals(1L, dbh.getLong(table.col { Position }) { Id equal 1L })
        
        var reordered = dbh.reorder(table, id = 1L, moveUpOrBack = false, makeFirstOrLast = false)
        
        // swapped with 2
        assertTrue(reordered)
        assertEquals(2L, dbh.getLong(table.col { Position }) { Id equal 1L })
        assertEquals(1L, dbh.getLong(table.col { Position }) { Id equal 2L })
        assertEquals(3L, dbh.getLong(table.col { Position }) { Id equal 3L })
        
        reordered = dbh.reorder(table, id = 1L, moveUpOrBack = false, makeFirstOrLast = false)
        
        // swapped with 3
        assertTrue(reordered)
        assertEquals(3L, dbh.getLong(table.col { Position }) { Id equal 1L })
        assertEquals(2L, dbh.getLong(table.col { Position }) { Id equal 3L })
    }
    
    
    fun simpleReorderUp() {
        val table = _Test.tableNamed(RoundKnightTable + 2)
        
        // sanity
        assertEquals(10L, dbh.getLong(table.col { Position }) { Id equal 10L })
        
        var reordered = dbh.reorder(table, id = 10L, moveUpOrBack = true, makeFirstOrLast = false)
        
        // swapped with 9
        assertTrue(reordered)
        assertEquals(9L, dbh.getLong(table.col { Position }) { Id equal 10L })
        assertEquals(10L, dbh.getLong(table.col { Position }) { Id equal 9L })
        
        reordered = dbh.reorder(table, id = 10L, moveUpOrBack = true, makeFirstOrLast = false)
        
        // swapped with 8
        assertTrue(reordered)
        assertEquals(8L, dbh.getLong(table.col { Position }) { Id equal 10L })
        assertEquals(9L, dbh.getLong(table.col { Position }) { Id equal 8L })
    }
    
    
    fun reorderToEnd() {
        val table = _Test.tableNamed(RoundKnightTable + 3)
        
        // sanity
        assertEquals(1L, dbh.getLong(table.col { Position }) { Id equal 1L })
        assertEquals(10L, dbh.getLong(table.col { Position }) { Id equal 10L })
        
        val reordered = dbh.reorder(table, id = 1L, moveUpOrBack = false, makeFirstOrLast = true)
        
        // moved to end
        assertTrue(reordered)
        assertEquals(11L, dbh.getLong(table.col { Position }) { Id equal 1L })
        
        // others unchanged
        assertTrue(reordered)
        assertEquals(2L, dbh.getLong(table.col { Position }) { Id equal 2L })
        assertEquals(3L, dbh.getLong(table.col { Position }) { Id equal 3L })
        assertEquals(10L, dbh.getLong(table.col { Position }) { Id equal 10L })
    }


    fun reorderToStart() {
        val table = _Test.tableNamed(RoundKnightTable + 4)
        
        // sanity
        assertEquals(10L, dbh.getLong(table.col { Position }) { Id equal 10L })
        assertEquals(1L, dbh.getLong(table.col { Position }) { Id equal 1L })
        
        val reordered = dbh.reorder(table, id = 10L, moveUpOrBack = true, makeFirstOrLast = true)
        
        // moved to start
        assertTrue(reordered)
        assertEquals(1L, dbh.getLong(table.col { Position }) { Id equal 10L })
        
        // everyone else shifted +1
        assertTrue(reordered)
        assertEquals(2L, dbh.getLong(table.col { Position }) { Id equal 1L })
        assertEquals(3L, dbh.getLong(table.col { Position }) { Id equal 2L })
        assertEquals(11L, dbh.getLong(table.col { Position }) { Id equal 9L })
    }
    
    
    fun reorderUp_whenAlreadyAtTop_doesNothing() {
        val table = _Test.tableNamed(RoundKnightTable + 5)
        
        val reordered = dbh.reorder(table, id = 1L, moveUpOrBack = true, makeFirstOrLast = false)
        
        // nothing changed
        assertFalse(reordered)
        assertEquals(1L, dbh.getLong(table.col { Position }) { Id equal 1L })
        assertEquals(2L, dbh.getLong(table.col { Position }) { Id equal 2L })
        assertEquals(3L, dbh.getLong(table.col { Position }) { Id equal 3L })
    }
    
    
    fun reorderDown_whenAlreadyAtBottom_doesNothing() {
        val table = _Test.tableNamed(RoundKnightTable + 6)
        
        val reordered = dbh.reorder(table, id = 10L, moveUpOrBack = false, makeFirstOrLast = false)
        
        // nothing changed
        assertFalse(reordered)
        assertEquals(10L, dbh.getLong(table.col { Position }) { Id equal 10L })
        assertEquals(9L, dbh.getLong(table.col { Position }) { Id equal 9L })
        assertEquals(8L, dbh.getLong(table.col { Position }) { Id equal 8L })
    }
    
    
}
