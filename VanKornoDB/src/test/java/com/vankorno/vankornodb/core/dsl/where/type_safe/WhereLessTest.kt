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
        
        assertEquals(whereObj.clauses[0], "intValue<?")
        assertEquals(whereObj.args[0], "42")
    }
    
    
    @Test
    fun `Less Long`() {
        val whereObj = WhereDsl().apply { lCol("longValue") less 9999999999L }
        
        assertEquals(whereObj.clauses[0], "longValue<?")
        assertEquals(whereObj.args[0], "9999999999")
    }
    
    
    @Test
    fun `Less Float`() {
        val whereObj = WhereDsl().apply { fCol("floatValue") less 3.14F }
        
        assertEquals(whereObj.clauses[0], "floatValue<?")
        assertEquals(whereObj.args[0], "3.14")
    }
    
    
    
    @Test
    fun `LessEqual Int`() {
        val whereObj = WhereDsl().apply { iCol("intValue") lessEqual 7 }
        
        assertEquals(whereObj.clauses[0], "intValue<=?")
        assertEquals(whereObj.args[0], "7")
    }
    
    
    @Test
    fun `LessEqual Long`() {
        val whereObj = WhereDsl().apply { lCol("longValue") lessEqual 123456789L }
        
        assertEquals(whereObj.clauses[0], "longValue<=?")
        assertEquals(whereObj.args[0], "123456789")
    }
    
    
    @Test
    fun `LessEqual Float`() {
        val whereObj = WhereDsl().apply { fCol("floatValue") lessEqual 1.5F }
        
        assertEquals(whereObj.clauses[0], "floatValue<=?")
        assertEquals(whereObj.args[0], "1.5")
    }
}

