package com.vankorno.vankornodb.core.queryBuilder.where.type_safe

import com.vankorno.vankornodb.api.WhereBuilder
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
        val whereObj = WhereBuilder().apply { iCol("two") notEqual 3 }
        
        assertEquals(whereObj.clauses[0], "two!=?")
        assertEquals(whereObj.args[0], "3")
    }
    
    
    @Test
    fun `NotEqual Str`() {
        val whereObj = WhereBuilder().apply { sCol("geek") notEqual "nerd" }
        
        assertEquals(whereObj.clauses[0], "geek!=?")
        assertEquals(whereObj.args[0], "nerd")
    }
    
    
    @Test
    fun `NotEqual Bool`() {
        val whereObj = WhereBuilder().apply { bCol("kickedBully") notEqual true }
        
        assertEquals(whereObj.clauses[0], "kickedBully!=?")
        assertEquals(whereObj.args[0], "1")
        
        val whereObj2 = WhereBuilder().apply { bCol("kickedBully") notEqual false }
        
        assertEquals(whereObj2.clauses[0], "kickedBully!=?")
        assertEquals(whereObj2.args[0], "0")
    }
    
    
    @Test
    fun `NotEqual Long`() {
        val whereObj = WhereBuilder().apply { lCol("longSadness") notEqual 0L }
        
        assertEquals(whereObj.clauses[0], "longSadness!=?")
        assertEquals(whereObj.args[0], "0")
    }
    
    
    @Test
    fun `NotEqual Float`() {
        val whereObj = WhereBuilder().apply { fCol("floaty") notEqual 3.1415F }
        
        assertEquals(whereObj.clauses[0], "floaty!=?")
        assertEquals(whereObj.args[0], "3.1415")
    }
}
