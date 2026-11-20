package com.vankorno.vankornodb.core.dsl.where.noty

import com.vankorno.vankornodb.TestConstants.DirtyTable
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.core.dsl.builderTable
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryBuilderLikeNotyTest {
    
    @Test
    fun `LIKE basic`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _Name + LIKE + "?",
            builderTable(DirtyTable).query( where = { Name like "%abc%" }).query
        )
    }
    
    @Test
    fun `NOT LIKE basic`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _Name + NOT_LIKE + "?",
            builderTable(DirtyTable).query( where = { Name notLike "%xyz%" }).query
        )
    }
    
    @Test
    fun `LIKE args`() {
        assertEquals(
            arrayOf("%abc%", "%xyz%").joinToString(comma),
            builderTable(DirtyTable).query(
                where = {
                    Name like "%abc%"
                    and { Name notLike "%xyz%" }
                }
            ).args.joinToString(comma)
        )
    }
    
    
    
    
    
    @Test
    fun `LIKE ANY basic`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "(name LIKE ? OR name LIKE ? OR name LIKE ?)",
            builderTable(DirtyTable).query(
                where = { Name.likeAny("%a%", "%b%", "%c%") }
            ).query
        )
    }
    
    @Test
    fun `LIKE NONE basic`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "(name NOT LIKE ? AND name NOT LIKE ? AND name NOT LIKE ?)",
            builderTable(DirtyTable).query(
                where = { Name.notLikeAny("%a%", "%b%", "%c%") }
            ).query
        )
    }
    
    @Test
    fun `LIKE ANY args`() {
        assertEquals(
            arrayOf("%a%", "%b%", "%c%").joinToString(comma),
            builderTable(DirtyTable).query(
                where = { Name.likeAny("%a%", "%b%", "%c%") }
            ).args.joinToString(comma)
        )
    }
    
    
    
    
}
