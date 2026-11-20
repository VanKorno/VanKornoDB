package com.vankorno.vankornodb.core.dsl.orderBy

import com.vankorno.vankornodb.core.dsl.BuilderOrder
import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderBySimpleTest {
    private val colInt = testColumn(iCol("intCol"))
    private val colStr = testColumn(sCol("strCol"))
    private val colBool = testColumn(bCol("boolCol"))
    private val colLong = testColumn(lCol("longCol"))
    private val colFloat = testColumn(fCol("floatCol"))
    
    @Test
    fun `orderBy with single column ascending`() {
        val order = BuilderOrder().apply {
            colInt()
        }.buildStr()
        
        assertEquals("intCol", order)
    }
    
    @Test
    fun `orderBy with single column descending`() {
        val order = BuilderOrder().apply {
            colStr.flip()
        }.buildStr()
        
        assertEquals("strCol DESC", order)
    }
    
    @Test
    fun `orderBy multiple columns mixed`() {
        val order = BuilderOrder().apply {
            colInt()
            colStr.flip()
            colBool()
        }.buildStr()
        
        assertEquals("intCol, strCol DESC, boolCol", order)
    }
    
    @Test
    fun `orderBy with raw string`() {
        val order = BuilderOrder().apply {
            raw("customCol DESC")
        }.buildStr()
        
        assertEquals("customCol DESC", order)
    }
    
    @Test
    fun `orderBy full mix`() {
        val order = BuilderOrder().apply {
            colInt()
            colStr.flip()
            colBool()
            colLong.flip()
            colFloat()
            raw("rawCol ASC")
        }.buildStr()
        
        assertEquals("intCol, strCol DESC, boolCol, longCol DESC, floatCol, rawCol ASC", order)
    }
    
    @Test
    fun `col with multiple ascending columns`() {
        val order = BuilderOrder().apply {
            col(colInt, colStr, colBool, colLong, colFloat)
        }.buildStr()
        
        assertEquals("intCol, strCol, boolCol, longCol, floatCol", order)
    }
    
    @Test
    fun `col with mixed ascending and descending columns`() {
        val order = BuilderOrder().apply {
            colInt()
            colStr.flip()
            colBool()
            colLong.flip()
            colFloat()
        }.buildStr()
        
        assertEquals("intCol, strCol DESC, boolCol, longCol DESC, floatCol", order)
    }
}
