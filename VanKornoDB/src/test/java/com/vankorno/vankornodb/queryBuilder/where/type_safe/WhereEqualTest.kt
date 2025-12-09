package com.vankorno.vankornodb.queryBuilder.where.type_safe

import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import com.vankorno.vankornodb.misc.data.SharedCol.shActive
import com.vankorno.vankornodb.misc.data.SharedCol.shPosition
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereEqualTest {
    
    @Test
    fun `Equal Int`() {
        val where: WhereBuilder.()->Unit = { shPosition equal 11 }
        val whereObj = WhereBuilder().apply(where)
        
        assertEquals(whereObj.clauses[0], shPosition.name + "=?")
        assertEquals(whereObj.args[0], "11")
    }
    
    @Test
    fun `Equal Str`() {
        val whereObj = WhereBuilder().apply { sCol("russiaIs") equal "aTerroristState" }
        
        assertEquals(whereObj.clauses[0], "russiaIs=?")
        assertEquals(whereObj.args[0], "aTerroristState")
    }
    
    
    @Test
    fun `Equal Bool`() {
        val whereObj = WhereBuilder().apply { shActive equal true }
        
        assertEquals(whereObj.clauses[0], "active=?")
        assertEquals(whereObj.args[0], "1")
        
        val whereObj2 = WhereBuilder().apply { shActive equal false }
        
        assertEquals(whereObj2.clauses[0], "active=?")
        assertEquals(whereObj2.args[0], "0")
    }
    
    
    @Test
    fun `Equal Long`() {
        val whereObj = WhereBuilder().apply { lCol("shLong") equal 50L }
        
        assertEquals(whereObj.clauses[0], "shLong=?")
        assertEquals(whereObj.args[0], "50")
    }
    
    
    @Test
    fun `Equal Float`() {
        val whereObj = WhereBuilder().apply { fCol("ruFloatDoesntFloat") equal 13.2F }
        
        assertEquals(whereObj.clauses[0], "ruFloatDoesntFloat=?")
        assertEquals(whereObj.args[0], "13.2")
    }
    
    
    
    
    
}