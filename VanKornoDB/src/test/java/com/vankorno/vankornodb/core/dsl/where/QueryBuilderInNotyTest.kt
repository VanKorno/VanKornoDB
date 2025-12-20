package com.vankorno.vankornodb.core.dsl.where

import com.vankorno.vankornodb.TestConstants.DirtyTable
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.core.dsl.getQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryBuilderInNotyTest {
    
    @Test
    fun `IN basic`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID + IN + "(?, ?, ?)",
            getQuery(DirtyTable) {
                where { _ID.equalAny(1, 2, 3) }
            }.query
        )
    }
    
    @Test
    fun `NOT IN basic`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID + NOT_IN + "(?, ?, ?)",
            getQuery(DirtyTable) {
                where { _ID.notEqualAny(1, 2, 3) }
            }.query
        )
    }
    
    @Test
    fun `IN args`() {
        assertEquals(
            arrayOf("1", "2", "3").joinToString(comma),
            getQuery(DirtyTable) {
                where { _ID.equalAny(1, 2, 3) }
            }.args.joinToString(comma)
        )
    }
    
    
    
}