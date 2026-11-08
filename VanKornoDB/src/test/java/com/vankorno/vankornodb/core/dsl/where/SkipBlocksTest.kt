package com.vankorno.vankornodb.core.dsl.where

import com.vankorno.vankornodb.TestConstants.BestName
import com.vankorno.vankornodb.TestConstants.DirtyTable
import com.vankorno.vankornodb.TestConstants.cFloat
import com.vankorno.vankornodb.TestConstants.cInfo
import com.vankorno.vankornodb.TestConstants.cInt
import com.vankorno.vankornodb.core.data.DbConstants.SELECT_ALL_FROM
import com.vankorno.vankornodb.core.data.DbConstants.WHERE
import com.vankorno.vankornodb.core.data.DbConstants._ID
import com.vankorno.vankornodb.core.data.DbConstants._Name
import com.vankorno.vankornodb.core.dsl.getQuery
import com.vankorno.vankornodb.misc.data.SharedCol.cActive
import com.vankorno.vankornodb.misc.data.SharedCol.cDescr
import com.vankorno.vankornodb.misc.data.SharedCol.cEnabled
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import com.vankorno.vankornodb.misc.data.SharedCol.cText
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class SkipBlocksTest {
    @Test
    fun `and() behaves like group() if missing first condition`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=?)",
            
            getQuery(DirtyTable) {
                and { Name = BestName }
            }.query
        )
    }
    
    @Test
    fun `or() behaves like group() if missing first condition`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=?)",
            
            getQuery(DirtyTable) {
                or { Name = BestName }
            }.query
        )
    }
    
    
    @Test
    fun `andGroup behaves like group() if missing first condition`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=? OR ($_Name=?))",
            
            getQuery(DirtyTable) {
                and {
                    Name = BestName
                    or { Name = "a" }
                }
            }.query
        )
    }
    
    @Test
    fun `orGroup behaves like group() if missing first condition`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=? OR ($_Name=?))",
            
            getQuery(DirtyTable) {
                or {
                    Name = BestName
                    or { Name = "a" }
                }
            }.query
        )
    }
    
    
    @Test
    fun `condition removes block-2`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "$_Name=?",
            
            getQuery(DirtyTable) {
                cName equal BestName
                
                if (false)
                    or { cPosition equal 1L }
            }.query
        )
    }
    
    @Test
    fun `condition removes block-1`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "(${cPosition.name}=?)",
            
            getQuery(DirtyTable) {
                if (false)
                    cName equal BestName
                
                or { cPosition equal 1L }
            }.query
        )
    }
    
    @Test
    fun `conditions remove blocks 1 and 2`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=?)",
            
            getQuery(DirtyTable) {
                if (false)
                    cID equal 1L
                
                if (false)
                    or { cPosition equal 1L }
                
                and { cName equal BestName }
            }.query
        )
    }
    
    
    @Test
    fun `ALL EMPTY blocks produce no WHERE clause`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable,
            
            getQuery(DirtyTable) {
                if (false) {
                    cID equal 1L
                    or { cPosition equal 1L }
                    and { cName equal BestName }
                }
            }.query
        )
    }
    
    @Test
    fun `ALL EMPTY blocks produce no WHERE clause (2)`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable,
            
            getQuery(DirtyTable) {
                if (false)
                    cName equal "ignored"
                and {
                    if (false)
                        cPosition equal 1L
                }
                or {
                    if (false)
                        cActive equal true
                    and {
                        if (false)
                            cDescr equal "ignored"
                    }
                }
                group {
                    if (false)
                        cFloat equal 1.5F
                }
            }.query
        )
    }
    
    
    @Test
    fun `multiple missing first conditions`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=?) AND ($_ID=?)",
    
            getQuery(DirtyTable) {
                if (false)
                    cPosition equal 2L
    
                and { cName equal "a" }
    
                if (false)
                    or { cPosition equal 1L }
    
                and { cID equal 2L }
            }.query
        )
    }
    
    @Test
    fun `empty nested blocks are ignored`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_ID=?)",
            
            getQuery(DirtyTable) {
                and {
                    if (false)
                        cName equal BestName
                }
                or {
                    if (false)
                        and { cPosition equal 1L }
                }
                and { cID equal 1L }
            }.query
        )
    }
    
    
    @Test
    fun `skipped blocks don't add arguments`() {
        val query = getQuery(DirtyTable) {
            cName equal "VanKorno"
            cActive equal true
            
            if (false)
                and {
                    cPosition equal 123L
                    cText equal "ignored"
                }
            or {
                if (false)
                    cInt equal 999
                and {
                    if (false)
                        cEnabled equal true
                }
            }
            and { cInfo equal "Gamplified" }
        }
        
        assertArrayEquals(
            arrayOf("VanKorno", "1", "Gamplified"),
            query.args
        )
    }
    
    
    
    
    
    
    
    
    
}
