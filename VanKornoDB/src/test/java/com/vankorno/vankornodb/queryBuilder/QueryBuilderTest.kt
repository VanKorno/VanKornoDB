package com.vankorno.vankornodb.queryBuilder

import com.vankorno.vankornodb.TestConstants.*
import com.vankorno.vankornodb.c
import com.vankorno.vankornodb.core.getQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryBuilderTest { // TODO split tests/files
    
    @Test
    fun `Simple checks`() {
        assertEquals(
            selectAllFrom + DirtyTable,
            getQuery(DirtyTable).query
        )
        assertEquals(
            selectAllFrom + DirtyTable + orderBy + ID,
            getQuery(DirtyTable, orderBy = ID).query
        )
        assertEquals(
            selectAllFrom + DirtyTable + where + ID+"=?" + orderBy + ID c Name,
            getQuery(DirtyTable, where={ ID equal 1 }, orderBy=ID c Name).query
        )
        
        assertEquals(
            select + ID c Name + from + DirtyTable + where + ID+">=?",
            getQuery(
                table = DirtyTable,
                columns = arrayOf(ID, Name),
                where = { ID greaterEqual 10 }
            ).query
        )
        assertEquals(
            selectAllFrom + DirtyTable + where + ID+">?",
            getQuery(
                table = DirtyTable,
                where = { ID greater 1 }
            ).query
        )
        assertEquals(
            selectAllFrom + DirtyTable + where + ID+"<?",
            getQuery(
                table = DirtyTable,
                where = { ID less 1 }
            ).query
        )
        assertEquals(
            selectAllFrom + DirtyTable + where + ID+"<=?",
            getQuery(
                table = DirtyTable,
                where = { ID lessEqual 1 }
            ).query
        )
        assertEquals(
            selectAllFrom + DirtyTable + where + Bool1+"=?",
            getQuery(
                table = DirtyTable,
                where = { Bool1 equal true }
            ).query
        )
    }
    
    @Test
    fun `Simple AND OR conditions`() {
        assertEquals(
            selectAllFrom + DirtyTable + where + ID+">=?" + and + Name+"=?",
            getQuery(
                table = DirtyTable,
                where = {
                    ID greaterEqual 10
                    and { Name equal BestName }
                }
            ).query
        )
        
        assertEquals(
            selectAllFrom + DirtyTable +
                where + ID+">=?" +
                and + Name+"=?" +
                and + Position+"=?" +
                and + ID+"=?",
            getQuery(
                table = DirtyTable,
                where = {
                    ID greaterEqual 10
                    and { Name equal BestName }
                    and { Position equal 1.1F }
                    and { ID equal "1" }
                }
            ).query
        )
        
        assertEquals(
            selectAllFrom + DirtyTable +
                where + ID+">=?" +
                and + Name+"=?" +
                or + Position+"=?" +
                or + ID+"=?",
            getQuery(
                table = DirtyTable,
                where = {
                    ID greaterEqual 10
                    and { Name equal BestName }
                    or { Position equal 1.1F }
                    or { ID equal 1 }
                }
            ).query
        )
    }
    
    @Test
    fun `condition values`() {
        assertEquals(
            arrayOf("1", "1", "1.1", "1").joinToString(comma),
            getQuery(
                table = DirtyTable,
                where = {
                    ID greaterEqual 1
                    and { ID equal 1 }
                    and { ID greaterEqual 1.1F }
                    and { ID greaterEqual 1L }
                }
            ).args.joinToString(comma)
        )
    }
    
    
    
    @Test
    fun `Two lvl conditions orGroup is last`() {
        assertEquals(
            selectAllFrom + DirtyTable +
                where + ID+">=?" +
                and + Name+"=?" +
                or + "("+Position+"=?" + or + ID+"=?)",
            
            getQuery(
                DirtyTable,
                where = {
                    ID greaterEqual 10
                    and { Name equal BestName }
                    orGroup {
                        Position equal 1.1F
                        or { ID equal 1 }
                    }
                }
            ).query
        )
    }
    
    @Test
    fun `Two lvl conditions group{} is first`() {
        assertEquals(
            selectAllFrom + DirtyTable +
                where + "("+ID+">=?" + and + Name+"=?)" +
                and + Position+"=?" + 
                or + ID+"=?"
            ,
            getQuery(
                DirtyTable,
                where = {
                    group {
                        ID greaterEqual 10
                        and { Name equal BestName }
                    }
                    and { Position equal 1.1F }
                    or { ID equal 1 }
                }
            ).query
        )
    }
    
    @Test
    fun `Two lvl conditions andGroup is mid`() {
        assertEquals(
            selectAllFrom + DirtyTable +
                where + 
                Position+"=?" +
                and +
                    "("+ID+">=?" + and + Name+"=?)" +
                and + Position+"=?" + 
                or + ID+"=?"
            ,
            getQuery(
                DirtyTable,
                where = {
                    Position equal 1.1F
                    andGroup {
                        ID greaterEqual 10
                        and { Name equal BestName }
                    }
                    and { Position equal 1.1F }
                    or { ID equal 1 }
                }
            ).query
        )
    }
    
    
    @Test
    fun `Five lvl conditions`() {
        val result = getQuery(
            DirtyTable,
            where = {
                Position equal 1
                andGroup {
                    ID greaterEqual 2
                    orGroup {
                        Name equal BestName
                        andGroup {
                            Name equal BestName
                            orGroup {
                                Name equal BestName
                                or { Position less 5 }
                            }
                        }
                    }
                }
                and { Position equal 1.1F }
                or { ID equal 1 }
            }
        )
        
        assertEquals(
            selectAllFrom + DirtyTable +
                where + 
                Position+"=?" +
                and + "("+
                    ID+">=?" +
                    or + "("+
                        Name+"=?"+
                        and + "("+
                            Name+"=?"+
                            or + "("+
                                Name+"=?"+
                                or + Position+"<?" +
                            ")" +
                        ")" +
                    ")" +
                ")" +
                and + Position+"=?" + 
                or + ID+"=?"
            ,
            result.query
        )
        assertEquals(
            arrayOf("1", "2", BestName, BestName, BestName, "5", "1.1", "1").joinToString(", "),
            result.args.joinToString(", ")
        )
        assertEquals(
            "SELECT * FROM $DirtyTable WHERE $Position=? AND ($ID>=? OR ($Name=? AND ($Name=? OR ($Name=? OR $Position<?)))) AND $Position=? OR $ID=?",
            result.query
        )
    }
    
    
    @Test
    fun `Query in query`() {
        val result = getQuery(
            table = Users,
            columns = arrayOf(Name),
            where = {
                subquery(
                    table = Posts,
                    columns = arrayOf(countAll),
                    where = {
                        (Posts dot UserID)  equal  (Users dot ID)
                    }
                ) greater 10
            }
        )
        
        assertEquals(
            "SELECT $Name FROM $Users WHERE (SELECT COUNT(*) FROM $Posts WHERE $Posts.user_id=?)>?",
            result.query
        )
        assertEquals(
            arrayOf("$Users.$ID", "10").joinToString(comma),
            result.args.joinToString(comma)
        )
    }
    
    
    
    
    
    
    
    
    
    
    
    
}