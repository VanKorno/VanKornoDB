package com.vankorno.vankornodb.core.queryBuilder.where

import com.vankorno.vankornodb.TestConstants.DirtyTable
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.core.queryBuilder.getQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryBuilderLikeNotyTest {
    
    @Test
    fun `LIKE basic`() {
        assertEquals(
            selectAllFrom + DirtyTable + WHERE + _Name + LIKE + "?",
            getQuery(DirtyTable) {
                where { _Name like "%abc%" }
            }.query
        )
    }
    
    @Test
    fun `NOT LIKE basic`() {
        assertEquals(
            selectAllFrom + DirtyTable + WHERE + _Name + notLIKE + "?",
            getQuery(DirtyTable) {
                where { _Name notLike "%xyz%" }
            }.query
        )
    }
    
    @Test
    fun `LIKE args`() {
        assertEquals(
            arrayOf("%abc%", "%xyz%").joinToString(comma),
            getQuery(DirtyTable) {
                where {
                    _Name like "%abc%"
                    and { _Name notLike "%xyz%" }
                }
            }.args.joinToString(comma)
        )
    }
    
    
    
    
    
    @Test
    fun `LIKE ANY basic`() {
        assertEquals(
            selectAllFrom + DirtyTable + WHERE + _Name + LIKE + "(?, ?, ?)",
            getQuery(DirtyTable) {
                where { _Name.likeAny("%a%", "%b%", "%c%") }
            }.query
        )
    }
    
    @Test
    fun `LIKE NONE basic`() {
        assertEquals(
            selectAllFrom + DirtyTable + WHERE + _Name + notLIKE + "(?, ?, ?)",
            getQuery(DirtyTable) {
                where { _Name.notLikeAny("%a%", "%b%", "%c%") }
            }.query
        )
    }
    
    @Test
    fun `LIKE ANY args`() {
        assertEquals(
            arrayOf("%a%", "%b%", "%c%").joinToString(comma),
            getQuery(DirtyTable) {
                where { _Name.likeAny("%a%", "%b%", "%c%") }
            }.args.joinToString(comma)
        )
    }
    
    
    
    
}