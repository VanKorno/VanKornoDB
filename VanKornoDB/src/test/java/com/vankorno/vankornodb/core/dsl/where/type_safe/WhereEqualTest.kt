package com.vankorno.vankornodb.core.dsl.where.type_safe

import com.vankorno.vankornodb.api.WhereDsl
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
        val where: WhereDsl.()->Unit = { cPosition equal 11 }
        val whereObj = WhereDsl().apply(where)
        
        assertEquals(cPosition.name + "=?", whereObj.clauses[0])
        assertEquals("11", whereObj.args[0])
    }
    
    @Test
    fun `Equal Str`() {
        val whereObj = WhereDsl().apply { sCol("russiaIs") equal "aTerroristState" }
        
        assertEquals("russiaIs=?", whereObj.clauses[0])
        assertEquals("aTerroristState", whereObj.args[0])
    }
    
    
    @Test
    fun `Equal Bool`() {
        val whereObj = WhereDsl().apply { cActive equal true }
        
        assertEquals("active=?", whereObj.clauses[0])
        assertEquals("1", whereObj.args[0])
        
        val whereObj2 = WhereDsl().apply { cActive equal false }
        
        assertEquals("active=?", whereObj2.clauses[0])
        assertEquals("0", whereObj2.args[0])
    }
    
    
    @Test
    fun `Equal Long`() {
        val whereObj = WhereDsl().apply { lCol("shLong") equal 50L }
        
        assertEquals("shLong=?", whereObj.clauses[0])
        assertEquals("50", whereObj.args[0])
    }
    
    
    @Test
    fun `Equal Float`() {
        val whereObj = WhereDsl().apply { fCol("ruFloatDoesntFloat") equal 13.2F }
        
        assertEquals("ruFloatDoesntFloat=?", whereObj.clauses[0])
        assertEquals("13.2", whereObj.args[0])
    }
    
    
    
    
    
}
