package com.vankorno.vankornodb.core.dsl.where.noty

import com.vankorno.vankornodb.TestConstants.DirtyTable
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.core.dsl.builderTable
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryBuilderInNotyTest {
    
    @Test
    fun `IN basic`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID + IN + "(?, ?, ?)",
            builderTable(DirtyTable).query( where = { Id.equalAnyOf(1, 2, 3) }).query
        )
    }
    
    @Test
    fun `NOT IN basic`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID + NOT_IN + "(?, ?, ?)",
            builderTable(DirtyTable).query( where = { Id.notEqualAnyOf(1, 2, 3) }).query
        )
    }
    
    @Test
    fun `IN args`() {
        assertEquals(
            arrayOf("1", "2", "3").joinToString(comma),
            builderTable(DirtyTable).query( where = { Id.equalAnyOf(1, 2, 3) }).args.joinToString(comma)
        )
    }
    
    
    
}
