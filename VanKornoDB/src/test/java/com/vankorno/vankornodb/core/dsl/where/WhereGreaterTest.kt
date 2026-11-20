package com.vankorno.vankornodb.core.dsl.where

import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.core.dsl.testPredicate
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereGreaterTest {
    
    @Test
    fun `Greater Int`() {
        val whereObj = testPredicate { testColumn(iCol("moreThanMoskoviaIQ")) greater 5 }
        
        assertEquals("moreThanMoskoviaIQ>?", whereObj.clauses[0])
        assertEquals("5", whereObj.args[0])
    }
    
    
    
    @Test
    fun `Greater Long`() {
        val whereObj = testPredicate { testColumn(lCol("longSize")) greater 123456789L }
        
        assertEquals("longSize>?", whereObj.clauses[0])
        assertEquals("123456789", whereObj.args[0])
    }
    
    
    @Test
    fun `Greater Float`() {
        val whereObj = testPredicate { testColumn(fCol("floatMoskoviaTears")) greater 0.5F }
        
        assertEquals("floatMoskoviaTears>?", whereObj.clauses[0])
        assertEquals("0.5", whereObj.args[0])
    }
    
    
    @Test
    fun `GreaterEqual Int`() {
        val whereObj = testPredicate { testColumn(iCol("intLevelOfDignity")) greaterEqual 228 }
        
        assertEquals("intLevelOfDignity>=?", whereObj.clauses[0])
        assertEquals("228", whereObj.args[0])
    }
    
    
    @Test
    fun `GreaterEqual Long`() {
        val whereObj = testPredicate { testColumn(lCol("longDaysUntilMoskoviaCollapses")) greaterEqual 9L }
        
        assertEquals("longDaysUntilMoskoviaCollapses>=?", whereObj.clauses[0])
        assertEquals("9", whereObj.args[0])
    }
    
    
    @Test
    fun `GreaterEqual Float`() {
        val whereObj = testPredicate { testColumn(fCol("floatLevelOfHuiloFear")) greaterEqual 99999.99F }
        
        assertEquals("floatLevelOfHuiloFear>=?", whereObj.clauses[0])
        assertEquals("99999.99", whereObj.args[0])
    }
}
