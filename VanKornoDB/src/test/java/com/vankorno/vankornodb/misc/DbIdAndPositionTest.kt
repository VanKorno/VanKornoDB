package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.misc.data.DbIdAndPosition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DbIdAndPositionTest {
    
    @Test
    fun `default values and isValid`() {
        val defaultObj = DbIdAndPosition()
        assertEquals(-1L, defaultObj.id)
        assertEquals(-1L, defaultObj.position)
        assertFalse(defaultObj.isValid)
        
        val validObj = DbIdAndPosition(10L, 5L)
        assertEquals(10L, validObj.id)
        assertEquals(5L, validObj.position)
        assertTrue(validObj.isValid)
        
        assertFalse(DbIdAndPosition(10L, -1L).isValid)
        assertFalse(DbIdAndPosition(-1L, 5L).isValid)
    }
    
    @Test
    fun `constructor with two lists`() {
        val fromLists = DbIdAndPosition(listOf(100L, 200L), listOf(5L, 6L))
        assertEquals(100L, fromLists.id)
        assertEquals(5L, fromLists.position)
        assertTrue(fromLists.isValid)
        
        val fromEmpty = DbIdAndPosition(emptyList(), emptyList())
        assertEquals(-1L, fromEmpty.id)
        assertEquals(-1L, fromEmpty.position)
        assertFalse(fromEmpty.isValid)
        
        val fromNull = DbIdAndPosition(null as List<Long>?, null as List<Long>?)
        assertEquals(-1L, fromNull.id)
        assertEquals(-1L, fromNull.position)
        assertFalse(fromNull.isValid)
    }
    
    @Test
    fun `constructor with Pair of lists`() {
        val pair = listOf(100L, 200L) to listOf(5L, 6L)
        val fromPair = DbIdAndPosition(pair)
        assertEquals(100L, fromPair.id)
        assertEquals(5L, fromPair.position)
        assertTrue(fromPair.isValid)
        
        val emptyPair = emptyList<Long>() to emptyList<Long>()
        val fromEmptyPair = DbIdAndPosition(emptyPair)
        assertEquals(-1L, fromEmptyPair.id)
        assertEquals(-1L, fromEmptyPair.position)
        assertFalse(fromEmptyPair.isValid)
        
        val nullPair = DbIdAndPosition(null as Pair<List<Long>, List<Long>>?)
        assertEquals(-1L, nullPair.id)
        assertEquals(-1L, nullPair.position)
        assertFalse(nullPair.isValid)
    }
    
    @Test
    fun `constructor with List of lists`() {
        val listOfLists = listOf(listOf(100L, 200L), listOf(5L, 6L))
        val fromListOfLists = DbIdAndPosition(listOfLists)
        assertEquals(100L, fromListOfLists.id)
        assertEquals(5L, fromListOfLists.position)
        assertTrue(fromListOfLists.isValid)
        
        val emptyListOfLists = DbIdAndPosition(emptyList())
        assertEquals(-1L, emptyListOfLists.id)
        assertEquals(-1L, emptyListOfLists.position)
        assertFalse(emptyListOfLists.isValid)
        
        val nullListOfLists = DbIdAndPosition(null as List<List<Long>>?)
        assertEquals(-1L, nullListOfLists.id)
        assertEquals(-1L, nullListOfLists.position)
        assertFalse(nullListOfLists.isValid)
    }
    
    @Test
    fun `destructuring support`() {
        val (id, position) = DbIdAndPosition(42L, 7L)
        assertEquals(42L, id)
        assertEquals(7L, position)
    }
}
