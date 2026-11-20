package com.vankorno.vankornodb.core.dsl.where

import com.vankorno.vankornodb.core.dsl.BuilderWhere
import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.core.dsl.testPredicate
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import com.vankorno.vankornodb.misc.data.SharedCol.cActive
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereEqualTest {
    
    @Test
    fun `Equal Int`() {
        val where: BuilderWhere.()->Unit = { testColumn(cPosition) equal 11 }
        val whereObj = testPredicate(where)
        
        assertEquals(cPosition.name + "=?", whereObj.clauses[0])
        assertEquals("11", whereObj.args[0])
    }
    
    @Test
    fun `Equal Str`() {
        val whereObj = testPredicate { testColumn(sCol("russiaIs")) equal "aTerroristState" }
        
        assertEquals("russiaIs=?", whereObj.clauses[0])
        assertEquals("aTerroristState", whereObj.args[0])
    }
    
    
    @Test
    fun `Equal Bool`() {
        val whereObj = testPredicate { testColumn(cActive) equal true }
        
        assertEquals("active=?", whereObj.clauses[0])
        assertEquals("1", whereObj.args[0])
        
        val whereObj2 = testPredicate { testColumn(cActive) equal false }
        
        assertEquals("active=?", whereObj2.clauses[0])
        assertEquals("0", whereObj2.args[0])
    }
    
    
    @Test
    fun `Equal Long`() {
        val whereObj = testPredicate { testColumn(lCol("shLong")) equal 50L }
        
        assertEquals("shLong=?", whereObj.clauses[0])
        assertEquals("50", whereObj.args[0])
    }
    
    
    @Test
    fun `Equal Float`() {
        val whereObj = testPredicate { testColumn(fCol("ruFloatDoesntFloat")) equal 13.2F }
        
        assertEquals("ruFloatDoesntFloat=?", whereObj.clauses[0])
        assertEquals("13.2", whereObj.args[0])
    }
    
    
    
    
    
}
