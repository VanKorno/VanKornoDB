package com.vankorno.vankornodb.core.dsl.where.type_safe

import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereNotEqualTest {
    
    @Test
    fun `NotEqual Int`() {
        val whereObj = WhereDsl().apply { iCol("two") notEqual 3 }
        
        assertEquals("two!=?", whereObj.clauses[0])
        assertEquals("3", whereObj.args[0])
    }
    
    
    @Test
    fun `NotEqual Str`() {
        val whereObj = WhereDsl().apply { sCol("geek") notEqual "nerd" }
        
        assertEquals("geek!=?", whereObj.clauses[0])
        assertEquals("nerd", whereObj.args[0])
    }
    
    
    @Test
    fun `NotEqual Bool`() {
        val whereObj = WhereDsl().apply { bCol("kickedBully") notEqual true }
        
        assertEquals("kickedBully!=?", whereObj.clauses[0])
        assertEquals("1", whereObj.args[0])
        
        val whereObj2 = WhereDsl().apply { bCol("kickedBully") notEqual false }
        
        assertEquals("kickedBully!=?", whereObj2.clauses[0])
        assertEquals("0", whereObj2.args[0])
    }
    
    
    @Test
    fun `NotEqual Long`() {
        val whereObj = WhereDsl().apply { lCol("longSadness") notEqual 0L }
        
        assertEquals("longSadness!=?", whereObj.clauses[0])
        assertEquals("0", whereObj.args[0])
    }
    
    
    @Test
    fun `NotEqual Float`() {
        val whereObj = WhereDsl().apply { fCol("floaty") notEqual 3.1415F }
        
        assertEquals("floaty!=?", whereObj.clauses[0])
        assertEquals("3.1415", whereObj.args[0])
    }
}
