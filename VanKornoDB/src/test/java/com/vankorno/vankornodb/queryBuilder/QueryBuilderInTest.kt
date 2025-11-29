package com.vankorno.vankornodb.queryBuilder

import com.vankorno.vankornodb.TestConstants.DirtyTable
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.core.getQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryBuilderInTest {
    
    @Test
    fun `IN basic`() {
        assertEquals(
            selectAllFrom + DirtyTable + where + ID + IN + "(?, ?, ?)",
            getQuery(
                DirtyTable,
                where = { ID.equalAny(1, 2, 3) }
            ).query
        )
    }
    
    @Test
    fun `NOT IN basic`() {
        assertEquals(
            selectAllFrom + DirtyTable + where + ID + notIN + "(?, ?, ?)",
            getQuery(
                DirtyTable,
                where = { ID.equalNone(1, 2, 3) }
            ).query
        )
    }
    
    @Test
    fun `IN args`() {
        assertEquals(
            arrayOf("1", "2", "3").joinToString(comma),
            getQuery(
                DirtyTable,
                where = { ID.equalAny(1, 2, 3) }
            ).args.joinToString(comma)
        )
    }
    
    
    
}