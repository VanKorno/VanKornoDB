package com.vankorno.vankornodb

import com.vankorno.vankornodb.TestConstants.*
import com.vankorno.vankornodb.core.c
import com.vankorno.vankornodb.core.getQuery
import junit.framework.TestCase.assertEquals
import org.junit.Test

class QueryBuilderTest {
    
    @Test
    fun `Simple checks`() {
        assertEquals(
            selectAllFrom + DirtyTable,
            getQuery(DirtyTable).first
        )
        assertEquals(
            selectAllFrom + DirtyTable + orderBy + ID,
            getQuery(DirtyTable, orderBy = ID).first
        )
        assertEquals(
            selectAllFrom + DirtyTable + where + ID+"=?" + orderBy + ID c Name,
            getQuery(DirtyTable, where={ ID equal 1 }, orderBy=ID c Name).first
        )
        
        assertEquals(
            select + ID c Name + from + DirtyTable + where + ID+">=?",
            getQuery(
                table = DirtyTable,
                columns = arrayOf(ID, Name),
                where = { ID greaterEqual 10 }
            ).first
        )
        assertEquals(
            selectAllFrom + DirtyTable + where + ID+">?",
            getQuery(
                table = DirtyTable,
                where = { ID greater 1 }
            ).first
        )
        assertEquals(
            selectAllFrom + DirtyTable + where + ID+"<?",
            getQuery(
                table = DirtyTable,
                where = { ID less 1 }
            ).first
        )
        assertEquals(
            selectAllFrom + DirtyTable + where + ID+"<=?",
            getQuery(
                table = DirtyTable,
                where = { ID lessEqual 1 }
            ).first
        )
        assertEquals(
            selectAllFrom + DirtyTable + where + Bool1+"=?",
            getQuery(
                table = DirtyTable,
                where = { Bool1 equal true }
            ).first
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
            ).first
        )
        
        assertEquals(
            selectAllFrom + DirtyTable +
                where + ID+">=?" +
                and + Name+"=?" +
                and + Priority+"=?" +
                and + ID+"=?",
            getQuery(
                table = DirtyTable,
                where = {
                    ID greaterEqual 10
                    and { Name equal BestName }
                    and { Priority equal 1.1F }
                    and { ID equal "1" }
                }
            ).first
        )
        
        assertEquals(
            selectAllFrom + DirtyTable +
                where + ID+">=?" +
                and + Name+"=?" +
                or + Priority+"=?" +
                or + ID+"=?",
            getQuery(
                table = DirtyTable,
                where = {
                    ID greaterEqual 10
                    and { Name equal BestName }
                    or { Priority equal 1.1F }
                    or { ID equal 1 }
                }
            ).first
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
            ).second.joinToString(comma)
        )
    }
    
    
    
    @Test
    fun `Two lvl conditions orGroup is last`() {
        assertEquals(
            selectAllFrom + DirtyTable +
                where + ID+">=?" +
                and + Name+"=?" +
                or + "("+Priority+"=?" + or + ID+"=?)",
            
            getQuery(
                DirtyTable,
                where = {
                    ID greaterEqual 10
                    and { Name equal BestName }
                    orGroup {
                        Priority equal 1.1F
                        or { ID equal 1 }
                    }
                }
            ).first
        )
    }
    
    @Test
    fun `Two lvl conditions group{} is first`() {
        assertEquals(
            selectAllFrom + DirtyTable +
                where + "("+ID+">=?" + and + Name+"=?)" +
                and + Priority+"=?" + 
                or + ID+"=?"
            ,
            getQuery(
                DirtyTable,
                where = {
                    group {
                        ID greaterEqual 10
                        and { Name equal BestName }
                    }
                    and { Priority equal 1.1F }
                    or { ID equal 1 }
                }
            ).first
        )
    }
    
    @Test
    fun `Two lvl conditions andGroup is mid`() {
        assertEquals(
            selectAllFrom + DirtyTable +
                where + 
                Priority+"=?" +
                and +
                    "("+ID+">=?" + and + Name+"=?)" +
                and + Priority+"=?" + 
                or + ID+"=?"
            ,
            getQuery(
                DirtyTable,
                where = {
                    Priority equal 1.1F
                    andGroup {
                        ID greaterEqual 10
                        and { Name equal BestName }
                    }
                    and { Priority equal 1.1F }
                    or { ID equal 1 }
                }
            ).first
        )
    }
    
    
    @Test
    fun `Five lvl conditions`() {
        val result = getQuery(
            DirtyTable,
            where = {
                Priority equal 1
                andGroup {
                    ID greaterEqual 2
                    orGroup {
                        Name equal BestName
                        andGroup {
                            Name equal BestName
                            orGroup {
                                Name equal BestName
                                or { Priority less 5 }
                            }
                        }
                    }
                }
                and { Priority equal 1.1F }
                or { ID equal 1 }
            }
        )
        
        assertEquals(
            selectAllFrom + DirtyTable +
                where + 
                Priority+"=?" +
                and + "("+
                    ID+">=?" +
                    or + "("+
                        Name+"=?"+
                        and + "("+
                            Name+"=?"+
                            or + "("+
                                Name+"=?"+
                                or + Priority+"<?" +
                            ")" +
                        ")" +
                    ")" +
                ")" +
                and + Priority+"=?" + 
                or + ID+"=?"
            ,
            result.first
        )
        assertEquals(
            arrayOf("1", "2", BestName, BestName, BestName, "5", "1.1", "1").joinToString(", "),
            result.second.joinToString(", ")
        )
        assertEquals(
            "SELECT * FROM DirtyTable WHERE Priority=? AND (ID>=? OR (Name=? AND (Name=? OR (Name=? OR Priority<?)))) AND Priority=? OR ID=?",
            result.first
        )
    }
    
    
    @Test
    fun `Query in query`() {
        val result = getQuery(
            table = "Users",
            columns = arrayOf(Name),
            where = {
                subquery(
                    table = "Posts",
                    columns = arrayOf(countAll),
                    where = {
                        (Posts dot UserID)  equal  (Users dot ID)
                    }
                ) greater 10
            }
        )
        
        assertEquals(
            "SELECT Name FROM Users WHERE (SELECT COUNT(*) FROM Posts WHERE Posts.user_id=?)>?",
            result.first
        )
        assertEquals(
            arrayOf("Users.ID", "10").joinToString(comma),
            result.second.joinToString(comma)
        )
    }
    
    
    
    
    
    
    
    
    
    
    
    
}