package com.vankorno.vankornodb.queryBuilder

import com.vankorno.vankornodb.TestConstants.BestName
import com.vankorno.vankornodb.TestConstants.Bool1
import com.vankorno.vankornodb.TestConstants.DirtyTable
import com.vankorno.vankornodb.TestConstants.Posts
import com.vankorno.vankornodb.TestConstants.UserID
import com.vankorno.vankornodb.TestConstants.Users
import com.vankorno.vankornodb.c
import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.core.getQuery
import junit.framework.TestCase
import org.junit.Test

class QueryBuilderTest { // TODO split tests/files
    
    @Test
    fun `Simple checks`() {
        TestCase.assertEquals(
            selectAllFrom + DirtyTable,
            getQuery(DirtyTable).first
        )
        TestCase.assertEquals(
            selectAllFrom + DirtyTable + orderBy + ID,
            getQuery(DirtyTable, orderBy = ID).first
        )
        TestCase.assertEquals(
            selectAllFrom + DirtyTable + where + ID + "=?" + orderBy + ID c Name,
            getQuery(
                DirtyTable,
                where = { ID equal 1 },
                orderBy = ID c Name
            ).first
        )
        
        TestCase.assertEquals(
            select + ID c Name + from + DirtyTable + where + ID + ">=?",
            getQuery(
                table = DirtyTable,
                columns = arrayOf(ID, Name),
                where = { ID greaterEqual 10 }
            ).first
        )
        TestCase.assertEquals(
            selectAllFrom + DirtyTable + where + ID + ">?",
            getQuery(
                table = DirtyTable,
                where = { ID greater 1 }
            ).first
        )
        TestCase.assertEquals(
            selectAllFrom + DirtyTable + where + ID + "<?",
            getQuery(
                table = DirtyTable,
                where = { ID less 1 }
            ).first
        )
        TestCase.assertEquals(
            selectAllFrom + DirtyTable + where + ID + "<=?",
            getQuery(
                table = DirtyTable,
                where = { ID lessEqual 1 }
            ).first
        )
        TestCase.assertEquals(
            selectAllFrom + DirtyTable + where + Bool1 + "=?",
            getQuery(
                table = DirtyTable,
                where = { Bool1 equal true }
            ).first
        )
    }
    
    @Test
    fun `Simple AND OR conditions`() {
        TestCase.assertEquals(
            selectAllFrom + DirtyTable + where + ID + ">=?" + and + Name + "=?",
            getQuery(
                table = DirtyTable,
                where = {
                    ID greaterEqual 10
                    and { Name equal BestName }
                }
            ).first
        )
        
        TestCase.assertEquals(
            selectAllFrom + DirtyTable +
                where + ID + ">=?" +
                and + Name + "=?" +
                and + Position + "=?" +
                and + ID + "=?",
            getQuery(
                table = DirtyTable,
                where = {
                    ID greaterEqual 10
                    and { Name equal BestName }
                    and { Position equal 1.1F }
                    and { ID equal "1" }
                }
            ).first
        )
        
        TestCase.assertEquals(
            selectAllFrom + DirtyTable +
                where + ID + ">=?" +
                and + Name + "=?" +
                or + Position + "=?" +
                or + ID + "=?",
            getQuery(
                table = DirtyTable,
                where = {
                    ID greaterEqual 10
                    and { Name equal BestName }
                    or { Position equal 1.1F }
                    or { ID equal 1 }
                }
            ).first
        )
    }
    
    @Test
    fun `condition values`() {
        TestCase.assertEquals(
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
        TestCase.assertEquals(
            selectAllFrom + DirtyTable +
                where + ID + ">=?" +
                and + Name + "=?" +
                or + "(" + Position + "=?" + or + ID + "=?)",
            
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
            ).first
        )
    }
    
    @Test
    fun `Two lvl conditions group{} is first`() {
        TestCase.assertEquals(
            selectAllFrom + DirtyTable +
                where + "(" + ID + ">=?" + and + Name + "=?)" +
                and + Position + "=?" +
                or + ID + "=?",
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
            ).first
        )
    }
    
    @Test
    fun `Two lvl conditions andGroup is mid`() {
        TestCase.assertEquals(
            selectAllFrom + DirtyTable +
                where +
                Position + "=?" +
                and +
                "(" + ID + ">=?" + and + Name + "=?)" +
                and + Position + "=?" +
                or + ID + "=?",
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
            ).first
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
        
        TestCase.assertEquals( // TODO FIX
            selectAllFrom + DirtyTable + where + Position + "=?" +
                and + "(" +
                ID + ">=?" +
                or + "(" +
                Name + "=?" +
                and + "(" +
                Name + "=?" +
                or + "(" +
                Name + "=?" +
                or + Position + "<?" +
                ")" +
                ")" +
                ")" +
                ")" +
                and + Position + "=?" +
                or + ID + "=?",
            result.first
        )
        TestCase.assertEquals(
            arrayOf(
                "1",
                "2",
                BestName,
                BestName,
                BestName,
                "5",
                "1.1",
                "1"
            ).joinToString(", "),
            result.second.joinToString(", ")
        )
        TestCase.assertEquals(
            "SELECT * FROM ${DirtyTable} WHERE ${Position}=? AND (${ID}>=? OR (${Name}=? AND (${Name}=? OR (${Name}=? OR ${Position}<?)))) AND ${Position}=? OR ${ID}=?",
            result.first
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
                        (Posts dot UserID) equal (Users dot ID)
                    }
                ) greater 10
            }
        )
        
        TestCase.assertEquals(
            "SELECT ${Name} FROM ${Users} WHERE (SELECT COUNT(*) FROM ${Posts} WHERE ${Posts}.user_id=?)>?",
            result.first
        )
        TestCase.assertEquals(
            arrayOf(
                "${Users}.${ID}",
                "10"
            ).joinToString(comma),
            result.second.joinToString(comma)
        )
    }
    
    
    
    
    
    
    
    
    
    
    
    
}