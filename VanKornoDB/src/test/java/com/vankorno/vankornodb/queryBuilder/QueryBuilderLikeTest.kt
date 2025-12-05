package com.vankorno.vankornodb.queryBuilder

import com.vankorno.vankornodb.TestConstants.DirtyTable
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.core.getQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryBuilderLikeTest {
    
    @Test
    fun `LIKE basic`() {
        assertEquals(
            selectAllFrom + DirtyTable + where + Name + like + "?",
            getQuery(DirtyTable) {
                where { Name like "%abc%" }
            }.query
        )
    }
    
    @Test
    fun `NOT LIKE basic`() {
        assertEquals(
            selectAllFrom + DirtyTable + where + Name + notLike + "?",
            getQuery(DirtyTable) {
                where { Name notLike "%xyz%" }
            }.query
        )
    }
    
    @Test
    fun `LIKE args`() {
        assertEquals(
            arrayOf("%abc%", "%xyz%").joinToString(comma),
            getQuery(DirtyTable) {
                where {
                    Name like "%abc%"
                    and { Name notLike "%xyz%" }
                }
            }.args.joinToString(comma)
        )
    }
    
    
    
    
    
    @Test
    fun `LIKE ANY basic`() {
        assertEquals(
            selectAllFrom + DirtyTable + where + Name + like + "(?, ?, ?)",
            getQuery(DirtyTable) {
                where { Name.likeAny("%a%", "%b%", "%c%") }
            }.query
        )
    }
    
    @Test
    fun `LIKE NONE basic`() {
        assertEquals(
            selectAllFrom + DirtyTable + where + Name + notLike + "(?, ?, ?)",
            getQuery(DirtyTable) {
                where { Name.likeNone("%a%", "%b%", "%c%") }
            }.query
        )
    }
    
    @Test
    fun `LIKE ANY args`() {
        assertEquals(
            arrayOf("%a%", "%b%", "%c%").joinToString(comma),
            getQuery(DirtyTable) {
                where { Name.likeAny("%a%", "%b%", "%c%") }
            }.args.joinToString(comma)
        )
    }
    
    
    
    
}