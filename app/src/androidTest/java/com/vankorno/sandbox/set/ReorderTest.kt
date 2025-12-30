package com.vankorno.sandbox.set

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.entities.testEntity.SbTest
import com.vankorno.sandbox.entities.testEntity.TestEntity
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import com.vankorno.vankornodb.misc.whereId
import org.junit.Assert.*
import org.junit.Test

private const val RoundKnightTable = "RoundKnightTable"

@MediumTest
class ReorderTest : BaseAndroidTest() {
    
    @Test
    fun testReorder() {
        prep()
        
        simpleReorderDown()
        simpleReorderUp()
        
        
    }
    
    private fun prep() {
        for (idx in 1..6) {
            val table = RoundKnightTable + idx
            dbh.createTable(table, SbTest)
            
            val objects = List(10) {
                TestEntity(
                    id = it + 1,
                    name = "Knight" + it + 1,
                    position = it + 1,
                )
            }
            dbh.addObjects(table, objects, SbTest)
        }
        // sanity
        assertTrue(dbh.tableExists(RoundKnightTable + 1))
        assertTrue(dbh.hasRows(RoundKnightTable + 1))
    }
    
    
    fun simpleReorderDown() {
        val table = RoundKnightTable + 1
        
        // sanity
        assertEquals(1, dbh.getInt(table, cPosition, whereId(1)))
        
        var reordered = dbh.reorder(table, id = 1, moveUpOrBack = false, makeFirstOrLast = false)
        
        // swapped with 2
        assertTrue(reordered)
        assertEquals(2, dbh.getInt(table, cPosition, whereId(1)))
        assertEquals(1, dbh.getInt(table, cPosition, whereId(2)))
        assertEquals(3, dbh.getInt(table, cPosition, whereId(3)))
        
        reordered = dbh.reorder(table, id = 1, moveUpOrBack = false, makeFirstOrLast = false)
        
        // swapped with 3
        assertTrue(reordered)
        assertEquals(3, dbh.getInt(table, cPosition, whereId(1)))
        assertEquals(2, dbh.getInt(table, cPosition, whereId(3)))
    }
    
    
    fun simpleReorderUp() {
        val table = RoundKnightTable + 2
        
        // sanity
        assertEquals(10, dbh.getInt(table, cPosition, whereId(10)))
        
        var reordered = dbh.reorder(table, id = 10, moveUpOrBack = true, makeFirstOrLast = false)
        
        // swapped with 9
        assertTrue(reordered)
        assertEquals(9, dbh.getInt(table, cPosition, whereId(10)))
        assertEquals(10, dbh.getInt(table, cPosition, whereId(9)))
        
        reordered = dbh.reorder(table, id = 10, moveUpOrBack = true, makeFirstOrLast = false)
        
        // swapped with 8
        assertTrue(reordered)
        assertEquals(8, dbh.getInt(table, cPosition, whereId(10)))
        assertEquals(9, dbh.getInt(table, cPosition, whereId(8)))
    }
    
    
    fun reorderToEnd() {
        val table = RoundKnightTable + 3
        
        // sanity
        assertEquals(1, dbh.getInt(table, cPosition, whereId(1)))
        assertEquals(10, dbh.getInt(table, cPosition, whereId(10)))
        
        val reordered = dbh.reorder(table, id = 1, moveUpOrBack = false, makeFirstOrLast = true)
        
        // moved to end
        assertTrue(reordered)
        assertEquals(11, dbh.getInt(table, cPosition, whereId(1)))
        
        // others unchanged
        assertTrue(reordered)
        assertEquals(2, dbh.getInt(table, cPosition, whereId(2)))
        assertEquals(3, dbh.getInt(table, cPosition, whereId(3)))
        assertEquals(10, dbh.getInt(table, cPosition, whereId(10)))
    }


    fun reorderToStart() {
        val table = RoundKnightTable + 4
        
        // sanity
        assertEquals(10, dbh.getInt(table, cPosition, whereId(10)))
        assertEquals(1, dbh.getInt(table, cPosition, whereId(1)))
        
        val reordered = dbh.reorder(table, id = 10, moveUpOrBack = true, makeFirstOrLast = true)
        
        // moved to start
        assertTrue(reordered)
        assertEquals(1, dbh.getInt(table, cPosition, whereId(10)))
        
        // everyone else shifted +1
        assertTrue(reordered)
        assertEquals(2, dbh.getInt(table, cPosition, whereId(1)))
        assertEquals(3, dbh.getInt(table, cPosition, whereId(2)))
        assertEquals(11, dbh.getInt(table, cPosition, whereId(9)))
    }
    
    
    fun reorderUp_whenAlreadyAtTop_doesNothing() {
        val table = RoundKnightTable + 5
        
        val reordered = dbh.reorder(table, id = 1, moveUpOrBack = true, makeFirstOrLast = false)
        
        // nothing changed
        assertFalse(reordered)
        assertEquals(1, dbh.getInt(table, cPosition, whereId(1)))
        assertEquals(2, dbh.getInt(table, cPosition, whereId(2)))
        assertEquals(3, dbh.getInt(table, cPosition, whereId(3)))
    }
    
    
    fun reorderDown_whenAlreadyAtBottom_doesNothing() {
        val table = RoundKnightTable + 6
        
        val reordered = dbh.reorder(table, id = 10, moveUpOrBack = false, makeFirstOrLast = false)
        
        // nothing changed
        assertFalse(reordered)
        assertEquals(10, dbh.getInt(table, cPosition, whereId(10)))
        assertEquals(9, dbh.getInt(table, cPosition, whereId(9)))
        assertEquals(8, dbh.getInt(table, cPosition, whereId(8)))
    }
    
    
}