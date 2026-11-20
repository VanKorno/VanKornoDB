package com.vankorno.vankornodb.core.dsl.where

import com.vankorno.vankornodb.TestConstants.DirtyTable
import com.vankorno.vankornodb.TestConstants.cInt
import com.vankorno.vankornodb.core.data.DbConstants.SELECT_ALL_FROM
import com.vankorno.vankornodb.core.data.DbConstants.WHERE
import com.vankorno.vankornodb.core.data.DbConstants._Name
import com.vankorno.vankornodb.core.dsl.builderTable
import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.misc.data.SharedCol.cActive
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import com.vankorno.vankornodb.misc.data.SharedCol.cText
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class NestedConditions {
    @Test
    fun `arguments preserve SQL order across nested groups`() {
        val query = builderTable(DirtyTable).query() {
            testColumn(cName) equal "first"
            
            and {
                testColumn(cPosition) equal 1L
                or { testColumn(cText) equal "second" }
            }
            or {
                testColumn(cActive) equal true
                and { testColumn(cInt) equal 42 }
            }
        }
        
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE +
                "$_Name=? AND (${cPosition.name}=? OR (${cText.name}=?)) OR (${cActive.name}=? AND (${cInt.name}=?))",
            query.query
        )
        assertArrayEquals(
            arrayOf("first", "1", "second", "1", "42"),
            query.args
        )
    }
    
    
    
    
    
    
    
    
}
