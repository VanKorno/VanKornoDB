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
        
        assertEquals(whereObj.clauses[0], cPosition.name + "=?")
        assertEquals(whereObj.args[0], "11")
    }
    
    @Test
    fun `Equal Str`() {
        val whereObj = WhereDsl().apply { sCol("russiaIs") equal "aTerroristState" }
        
        assertEquals(whereObj.clauses[0], "russiaIs=?")
        assertEquals(whereObj.args[0], "aTerroristState")
    }
    
    
    @Test
    fun `Equal Bool`() {
        val whereObj = WhereDsl().apply { cActive equal true }
        
        assertEquals(whereObj.clauses[0], "active=?")
        assertEquals(whereObj.args[0], "1")
        
        val whereObj2 = WhereDsl().apply { cActive equal false }
        
        assertEquals(whereObj2.clauses[0], "active=?")
        assertEquals(whereObj2.args[0], "0")
    }
    
    
    @Test
    fun `Equal Long`() {
        val whereObj = WhereDsl().apply { lCol("shLong") equal 50L }
        
        assertEquals(whereObj.clauses[0], "shLong=?")
        assertEquals(whereObj.args[0], "50")
    }
    
    
    @Test
    fun `Equal Float`() {
        val whereObj = WhereDsl().apply { fCol("ruFloatDoesntFloat") equal 13.2F }
        
        assertEquals(whereObj.clauses[0], "ruFloatDoesntFloat=?")
        assertEquals(whereObj.args[0], "13.2")
    }
    
    
    
    
    
}