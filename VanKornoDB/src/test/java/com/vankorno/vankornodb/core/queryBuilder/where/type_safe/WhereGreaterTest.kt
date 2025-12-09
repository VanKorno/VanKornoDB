package com.vankorno.vankornodb.core.queryBuilder.where.type_safe

import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereGreaterTest {
    
    @Test
    fun `Greater Int`() {
        val whereObj = WhereBuilder().apply { iCol("moreThanMoskoviaIQ") greater 5 }
        
        assertEquals(whereObj.clauses[0], "moreThanMoskoviaIQ>?")
        assertEquals(whereObj.args[0], "5")
    }
    
    
    
    @Test
    fun `Greater Long`() {
        val whereObj = WhereBuilder().apply { lCol("longSize") greater 123456789L }
        
        assertEquals(whereObj.clauses[0], "longSize>?")
        assertEquals(whereObj.args[0], "123456789")
    }
    
    
    @Test
    fun `Greater Float`() {
        val whereObj = WhereBuilder().apply { fCol("floatMoskoviaTears") greater 0.5F }
        
        assertEquals(whereObj.clauses[0], "floatMoskoviaTears>?")
        assertEquals(whereObj.args[0], "0.5")
    }
    
    
    @Test
    fun `GreaterEqual Int`() {
        val whereObj = WhereBuilder().apply { iCol("intLevelOfDignity") greaterEqual 228 }
        
        assertEquals(whereObj.clauses[0], "intLevelOfDignity>=?")
        assertEquals(whereObj.args[0], "228")
    }
    
    
    @Test
    fun `GreaterEqual Long`() {
        val whereObj = WhereBuilder().apply { lCol("longDaysUntilMoskoviaCollapses") greaterEqual 9L }
        
        assertEquals(whereObj.clauses[0], "longDaysUntilMoskoviaCollapses>=?")
        assertEquals(whereObj.args[0], "9")
    }
    
    
    @Test
    fun `GreaterEqual Float`() {
        val whereObj = WhereBuilder().apply { fCol("floatLevelOfHuiloFear") greaterEqual 99999.99F }
        
        assertEquals(whereObj.clauses[0], "floatLevelOfHuiloFear>=?")
        assertEquals(whereObj.args[0], "99999.99")
    }
}







