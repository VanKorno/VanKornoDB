package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.TestConstants.BestName
import com.vankorno.vankornodb.TestConstants.Bool1
import com.vankorno.vankornodb.TestConstants.DirtyTable
import com.vankorno.vankornodb.core.data.DbConstants.*
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryBuilderTest { // TODO split tests/files
    
    private infix fun String.c(str2: String = "") = this + ", " + str2
    
    @Test
    fun `Simple checks`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable,
            builderTable(DirtyTable).query().query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + ORDER_BY + _ID,
            builderTable(DirtyTable).query( orderBy = { raw(_ID) }).query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID+"=?" + ORDER_BY + _ID c _Name,
            
            builderTable(DirtyTable).query( where = { Id equal 1 }, 
                orderBy = {
                    raw(_ID)
                    raw(_Name)
                }
            ).query
        )
        
        assertEquals(
            SELECT + _ID c _Name + FROM + DirtyTable + WHERE + _ID+">=?",
            builderTable(DirtyTable).columns { Id then Name }.query( where = { Id greaterEqual 10 }).query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID+">?",
            builderTable(DirtyTable).query( where = { Id greater 1 }).query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID+"<?",
            builderTable(DirtyTable).query( where = { Id less 1 }).query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID+"<=?",
            builderTable(DirtyTable).query( where = { Id lessEqual 1 }).query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + Bool1+"=?",
            builderTable(DirtyTable).query( where = { Bool equal true }).query
        )
    }
    
    @Test
    fun `Simple AND OR conditions`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "$_ID>=? AND ($_Name=?)",
            
            builderTable(DirtyTable).query(
                where = {
                    Id greaterEqual 10
                    and { Name equal BestName }
                }
            ).query
        )
        
        assertEquals(
            SELECT_ALL_FROM + DirtyTable +
                WHERE + _ID+">=?" +
                and + "($_Name=?)" +
                and + "($_Position=?)" +
                and + "($_ID=?)",
            builderTable(DirtyTable).query(
                where = {
                    Id greaterEqual 10
                    and { Name equal BestName }
                    and { Position equal 1.1F }
                    and { Id equal 1 }
                }
            ).query
        )
        
        assertEquals(
            SELECT_ALL_FROM + DirtyTable +
                WHERE + _ID+">=?" +
                and + "($_Name=?)" +
                or + "($_Position=?)" +
                or + "($_ID=?)",
            builderTable(DirtyTable).query(
                where = {
                    Id greaterEqual 10
                    and { Name equal BestName }
                    or { Position equal 1.1F }
                    or { Id equal 1 }
                }
            ).query
        )
    }
    
    @Test
    fun `condition values`() {
        assertEquals(
            arrayOf("1", "1", "1.1", "1").joinToString(comma),
            builderTable(DirtyTable).query(
                where = {
                    Id greaterEqual 1
                    and { Id equal 1 }
                    and { rawClause("id>=?", "1.1") }
                    and { Id greaterEqual 1L }
                }
            ).args.joinToString(comma)
        )
    }
    
    
    
    @Test
    fun `Two lvl conditions orGroup is last`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable +
                WHERE + _ID+">=?" +
                and + "($_Name=?)" +
                or + "($_Position=? OR ($_ID=?))",
            
            builderTable(DirtyTable).query(
                where = {
                    Id greaterEqual 10
                    and { Name equal BestName }
                    or {
                        Position equal 1.1F
                        or { Id equal 1 }
                    }
                }
            ).query
        )
    }
    
    @Test
    fun `Two lvl conditions group{} is first`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + "($_ID>=? AND ($_Name=?)) AND ($_Position=?) OR ($_ID=?)",
            
            builderTable(DirtyTable).query(
                where = {
                    group {
                        Id greaterEqual 10
                        and { Name equal BestName }
                    }
                    and { Position equal 1.1F }
                    or { Id equal 1 }
                }
            ).query
        )
    }
    
    @Test
    fun `Two lvl conditions andGroup is mid`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable +
                WHERE + 
                _Position+"=?" +
                and +
                    "($_ID>=? AND ($_Name=?))" +
                and + "($_Position=?)" + 
                or + "($_ID=?)"
            ,
            builderTable(DirtyTable).query(
                where = {
                    Position equal 1.1F
                    and {
                        Id greaterEqual 10
                        and { Name equal BestName }
                    }
                    and { Position equal 1.1F }
                    or { Id equal 1 }
                }
            ).query
        )
    }
    
    
    @Test
    fun `Five lvl conditions`() {
        val result = builderTable(DirtyTable).query(
            where = {
                rawClause("position=?", "1")
                and {
                    Id greaterEqual 2
                    or {
                        Name equal BestName
                        and {
                            Name equal BestName
                            or {
                                Name equal BestName
                                or { rawClause("position<?", "5") }
                            }
                        }
                    }
                }
                and { Position equal 1.1F }
                or { Id equal 1 }
            }
        )
        
        assertEquals(
            SELECT_ALL_FROM + DirtyTable +
                WHERE + 
                _Position+"=?" +
                and + "("+
                    _ID+">=?" +
                    or + "("+
                        _Name+"=?"+
                        and + "("+
                            _Name+"=?"+
                            or + "("+
                                _Name+"=?"+
                                or + "($_Position<?)" +
                            ")" +
                        ")" +
                    ")" +
                ")" +
                and + "($_Position=?)" + 
                or + "($_ID=?)"
            ,
            result.query
        )
        assertEquals(
            arrayOf("1", "2", BestName, BestName, BestName, "5", "1.1", "1").joinToString(", "),
            result.args.joinToString(", ")
        )
        assertEquals(
            "SELECT * FROM $DirtyTable WHERE $_Position=? AND ($_ID>=? OR ($_Name=? AND ($_Name=? OR ($_Name=? OR ($_Position<?))))) AND ($_Position=?) OR ($_ID=?)",
            result.query
        )
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
