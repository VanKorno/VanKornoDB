package com.vankorno.vankornodb.core.dsl.where.type_safe

import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereLessTest {
    
    @Test
    fun `Less Int`() {
        val whereObj = WhereDsl().apply { iCol("intValue") less 42 }
        
        assertEquals("intValue<?", whereObj.clauses[0])
        assertEquals("42", whereObj.args[0])
    }
    
    
    @Test
    fun `Less Long`() {
        val whereObj = WhereDsl().apply { lCol("longValue") less 9999999999L }
        
        assertEquals("longValue<?", whereObj.clauses[0])
        assertEquals("9999999999", whereObj.args[0])
    }
    
    
    @Test
    fun `Less Float`() {
        val whereObj = WhereDsl().apply { fCol("floatValue") less 3.14F }
        
        assertEquals("floatValue<?", whereObj.clauses[0])
        assertEquals("3.14", whereObj.args[0])
    }
    
    
    
    @Test
    fun `LessEqual Int`() {
        val whereObj = WhereDsl().apply { iCol("intValue") lessEqual 7 }
        
        assertEquals("intValue<=?", whereObj.clauses[0])
        assertEquals("7", whereObj.args[0])
    }
    
    
    @Test
    fun `LessEqual Long`() {
        val whereObj = WhereDsl().apply { lCol("longValue") lessEqual 123456789L }
        
        assertEquals("longValue<=?", whereObj.clauses[0])
        assertEquals("123456789", whereObj.args[0])
    }
    
    
    @Test
    fun `LessEqual Float`() {
        val whereObj = WhereDsl().apply { fCol("floatValue") lessEqual 1.5F }
        
        assertEquals("floatValue<=?", whereObj.clauses[0])
        assertEquals("1.5", whereObj.args[0])
    }
}
