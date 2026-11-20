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
import com.vankorno.vankornodb.core.dsl.builderTable
import com.vankorno.vankornodb.core.dsl.testColumn
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
            
            builderTable(DirtyTable).query() {
                and { Name equal BestName }
            }.query
        )
    }
    
    @Test
    fun `or() behaves like group() if missing first condition`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=?)",
            
            builderTable(DirtyTable).query() {
                or { Name equal BestName }
            }.query
        )
    }
    
    
    @Test
    fun `andGroup behaves like group() if missing first condition`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=? OR ($_Name=?))",
            
            builderTable(DirtyTable).query() {
                and {
                    Name equal BestName
                    or { Name equal "a" }
                }
            }.query
        )
    }
    
    @Test
    fun `orGroup behaves like group() if missing first condition`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=? OR ($_Name=?))",
            
            builderTable(DirtyTable).query() {
                or {
                    Name equal BestName
                    or { Name equal "a" }
                }
            }.query
        )
    }
    
    
    @Test
    fun `condition removes block-2`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "$_Name=?",
            
            builderTable(DirtyTable).query() {
                testColumn(cName) equal BestName
                
                if (false)
                    or { testColumn(cPosition) equal 1L }
            }.query
        )
    }
    
    @Test
    fun `condition removes block-1`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "(${cPosition.name}=?)",
            
            builderTable(DirtyTable).query() {
                if (false)
                    testColumn(cName) equal BestName
                
                or { testColumn(cPosition) equal 1L }
            }.query
        )
    }
    
    @Test
    fun `conditions remove blocks 1 and 2`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=?)",
            
            builderTable(DirtyTable).query() {
                if (false)
                    testColumn(cID) equal 1L
                
                if (false)
                    or { testColumn(cPosition) equal 1L }
                
                and { testColumn(cName) equal BestName }
            }.query
        )
    }
    
    
    @Test
    fun `ALL EMPTY blocks produce no WHERE clause`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable,
            
            builderTable(DirtyTable).query() {
                if (false) {
                    testColumn(cID) equal 1L
                    or { testColumn(cPosition) equal 1L }
                    and { testColumn(cName) equal BestName }
                }
            }.query
        )
    }
    
    @Test
    fun `ALL EMPTY blocks produce no WHERE clause (2)`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable,
            
            builderTable(DirtyTable).query() {
                if (false)
                    testColumn(cName) equal "ignored"
                and {
                    if (false)
                        testColumn(cPosition) equal 1L
                }
                or {
                    if (false)
                        testColumn(cActive) equal true
                    and {
                        if (false)
                            testColumn(cDescr) equal "ignored"
                    }
                }
                group {
                    if (false)
                        testColumn(cFloat) equal 1.5F
                }
            }.query
        )
    }
    
    
    @Test
    fun `multiple missing first conditions`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_Name=?) AND ($_ID=?)",
    
            builderTable(DirtyTable).query() {
                if (false)
                    testColumn(cPosition) equal 2L
    
                and { testColumn(cName) equal "a" }
    
                if (false)
                    or { testColumn(cPosition) equal 1L }
    
                and { testColumn(cID) equal 2L }
            }.query
        )
    }
    
    @Test
    fun `empty nested blocks are ignored`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_ID=?)",
            
            builderTable(DirtyTable).query() {
                and {
                    if (false)
                        testColumn(cName) equal BestName
                }
                or {
                    if (false)
                        and { testColumn(cPosition) equal 1L }
                }
                and { testColumn(cID) equal 1L }
            }.query
        )
    }
    
    
    @Test
    fun `skipped blocks don't add arguments`() {
        val query = builderTable(DirtyTable).query() {
            testColumn(cName) equal "VanKorno"
            testColumn(cActive) equal true
            
            if (false)
                and {
                    testColumn(cPosition) equal 123L
                    testColumn(cText) equal "ignored"
                }
            or {
                if (false)
                    testColumn(cInt) equal 999
                and {
                    if (false)
                        testColumn(cEnabled) equal true
                }
            }
            and { testColumn(cInfo) equal "Gamplified" }
        }
        
        assertArrayEquals(
            arrayOf("VanKorno", "1", "Gamplified"),
            query.args
        )
    }
    
    
    
    
    
    
    
    
    
}
