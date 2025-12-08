package com.vankorno.vankornodb.queryBuilder.where.type_safe

import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.core.data.SharedCol.shPosition
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
    
    
    
    
    
    
}