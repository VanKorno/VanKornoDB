package com.vankorno.vankornodb.queryBuilder.where.type_safe

import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants
import com.vankorno.vankornodb.core.data.SharedCol.shActive
import com.vankorno.vankornodb.core.data.SharedCol.shName
import com.vankorno.vankornodb.core.data.SharedCol.shPosition
import com.vankorno.vankornodb.core.data.fCol
import com.vankorno.vankornodb.core.data.lCol
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
        val where: WhereBuilder.()->Unit = { shName equal "russiaIsATerroristState" }
        val whereObj = WhereBuilder().apply(where)
        
        assertEquals(whereObj.clauses[0], DbConstants.Name + "=?")
        assertEquals(whereObj.args[0], "russiaIsATerroristState")
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
        val whereObj = WhereBuilder().apply { fCol("ruFloatSucks") equal 13.2F }
        
        assertEquals(whereObj.clauses[0], "ruFloatSucks=?")
        assertEquals(whereObj.args[0], "13.2")
    }
    
    
    
    
    
}