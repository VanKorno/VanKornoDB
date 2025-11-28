package com.vankorno.vankornodb.queryBuilder

import com.vankorno.vankornodb.TestConstants
import com.vankorno.vankornodb.c
import com.vankorno.vankornodb.core.DbConstants
import com.vankorno.vankornodb.core.getQuery
import junit.framework.TestCase
import org.junit.Test

class QueryBuilderTest { // TODO split tests/files
    
    @Test
    fun `Simple checks`() {
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable,
            getQuery(TestConstants.DirtyTable).first
        )
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable + DbConstants.orderBy + DbConstants.ID,
            getQuery(TestConstants.DirtyTable, orderBy = DbConstants.ID).first
        )
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable + DbConstants.where + DbConstants.ID + "=?" + DbConstants.orderBy + DbConstants.ID c DbConstants.Name,
            getQuery(
                TestConstants.DirtyTable,
                where = { DbConstants.ID equal 1 },
                orderBy = DbConstants.ID c DbConstants.Name
            ).first
        )
        
        TestCase.assertEquals(
            DbConstants.select + DbConstants.ID c DbConstants.Name + DbConstants.from + TestConstants.DirtyTable + DbConstants.where + DbConstants.ID + ">=?",
            getQuery(
                table = TestConstants.DirtyTable,
                columns = arrayOf(DbConstants.ID, DbConstants.Name),
                where = { DbConstants.ID greaterEqual 10 }
            ).first
        )
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable + DbConstants.where + DbConstants.ID + ">?",
            getQuery(
                table = TestConstants.DirtyTable,
                where = { DbConstants.ID greater 1 }
            ).first
        )
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable + DbConstants.where + DbConstants.ID + "<?",
            getQuery(
                table = TestConstants.DirtyTable,
                where = { DbConstants.ID less 1 }
            ).first
        )
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable + DbConstants.where + DbConstants.ID + "<=?",
            getQuery(
                table = TestConstants.DirtyTable,
                where = { DbConstants.ID lessEqual 1 }
            ).first
        )
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable + DbConstants.where + TestConstants.Bool1 + "=?",
            getQuery(
                table = TestConstants.DirtyTable,
                where = { TestConstants.Bool1 equal true }
            ).first
        )
    }
    
    @Test
    fun `Simple AND OR conditions`() {
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable + DbConstants.where + DbConstants.ID + ">=?" + DbConstants.and + DbConstants.Name + "=?",
            getQuery(
                table = TestConstants.DirtyTable,
                where = {
                    DbConstants.ID greaterEqual 10
                    and { DbConstants.Name equal TestConstants.BestName }
                }
            ).first
        )
        
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable +
                DbConstants.where + DbConstants.ID + ">=?" +
                DbConstants.and + DbConstants.Name + "=?" +
                DbConstants.and + DbConstants.Order + "=?" +
                DbConstants.and + DbConstants.ID + "=?",
            getQuery(
                table = TestConstants.DirtyTable,
                where = {
                    DbConstants.ID greaterEqual 10
                    and { DbConstants.Name equal TestConstants.BestName }
                    and { DbConstants.Order equal 1.1F }
                    and { DbConstants.ID equal "1" }
                }
            ).first
        )
        
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable +
                DbConstants.where + DbConstants.ID + ">=?" +
                DbConstants.and + DbConstants.Name + "=?" +
                DbConstants.or + DbConstants.Order + "=?" +
                DbConstants.or + DbConstants.ID + "=?",
            getQuery(
                table = TestConstants.DirtyTable,
                where = {
                    DbConstants.ID greaterEqual 10
                    and { DbConstants.Name equal TestConstants.BestName }
                    or { DbConstants.Order equal 1.1F }
                    or { DbConstants.ID equal 1 }
                }
            ).first
        )
    }
    
    @Test
    fun `condition values`() {
        TestCase.assertEquals(
            arrayOf("1", "1", "1.1", "1").joinToString(DbConstants.comma),
            getQuery(
                table = TestConstants.DirtyTable,
                where = {
                    DbConstants.ID greaterEqual 1
                    and { DbConstants.ID equal 1 }
                    and { DbConstants.ID greaterEqual 1.1F }
                    and { DbConstants.ID greaterEqual 1L }
                }
            ).second.joinToString(DbConstants.comma)
        )
    }
    
    
    
    @Test
    fun `Two lvl conditions orGroup is last`() {
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable +
                DbConstants.where + DbConstants.ID + ">=?" +
                DbConstants.and + DbConstants.Name + "=?" +
                DbConstants.or + "(" + DbConstants.Order + "=?" + DbConstants.or + DbConstants.ID + "=?)",
            
            getQuery(
                TestConstants.DirtyTable,
                where = {
                    DbConstants.ID greaterEqual 10
                    and { DbConstants.Name equal TestConstants.BestName }
                    orGroup {
                        DbConstants.Order equal 1.1F
                        or { DbConstants.ID equal 1 }
                    }
                }
            ).first
        )
    }
    
    @Test
    fun `Two lvl conditions group{} is first`() {
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable +
                DbConstants.where + "(" + DbConstants.ID + ">=?" + DbConstants.and + DbConstants.Name + "=?)" +
                DbConstants.and + DbConstants.Order + "=?" +
                DbConstants.or + DbConstants.ID + "=?",
            getQuery(
                TestConstants.DirtyTable,
                where = {
                    group {
                        DbConstants.ID greaterEqual 10
                        and { DbConstants.Name equal TestConstants.BestName }
                    }
                    and { DbConstants.Order equal 1.1F }
                    or { DbConstants.ID equal 1 }
                }
            ).first
        )
    }
    
    @Test
    fun `Two lvl conditions andGroup is mid`() {
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable +
                DbConstants.where +
                DbConstants.Order + "=?" +
                DbConstants.and +
                "(" + DbConstants.ID + ">=?" + DbConstants.and + DbConstants.Name + "=?)" +
                DbConstants.and + DbConstants.Order + "=?" +
                DbConstants.or + DbConstants.ID + "=?",
            getQuery(
                TestConstants.DirtyTable,
                where = {
                    DbConstants.Order equal 1.1F
                    andGroup {
                        DbConstants.ID greaterEqual 10
                        and { DbConstants.Name equal TestConstants.BestName }
                    }
                    and { DbConstants.Order equal 1.1F }
                    or { DbConstants.ID equal 1 }
                }
            ).first
        )
    }
    
    
    @Test
    fun `Five lvl conditions`() {
        val result = getQuery(
            TestConstants.DirtyTable,
            where = {
                DbConstants.Order equal 1
                andGroup {
                    DbConstants.ID greaterEqual 2
                    orGroup {
                        DbConstants.Name equal TestConstants.BestName
                        andGroup {
                            DbConstants.Name equal TestConstants.BestName
                            orGroup {
                                DbConstants.Name equal TestConstants.BestName
                                or { DbConstants.Order less 5 }
                            }
                        }
                    }
                }
                and { DbConstants.Order equal 1.1F }
                or { DbConstants.ID equal 1 }
            }
        )
        
        TestCase.assertEquals(
            DbConstants.selectAllFrom + TestConstants.DirtyTable +
                DbConstants.where +
                DbConstants.Order + "=?" +
                DbConstants.and + "(" +
                DbConstants.ID + ">=?" +
                DbConstants.or + "(" +
                DbConstants.Name + "=?" +
                DbConstants.and + "(" +
                DbConstants.Name + "=?" +
                DbConstants.or + "(" +
                DbConstants.Name + "=?" +
                DbConstants.or + DbConstants.Order + "<?" +
                ")" +
                ")" +
                ")" +
                ")" +
                DbConstants.and + DbConstants.Order + "=?" +
                DbConstants.or + DbConstants.ID + "=?",
            result.first
        )
        TestCase.assertEquals(
            arrayOf(
                "1",
                "2",
                TestConstants.BestName,
                TestConstants.BestName,
                TestConstants.BestName,
                "5",
                "1.1",
                "1"
            ).joinToString(", "),
            result.second.joinToString(", ")
        )
        TestCase.assertEquals(
            "SELECT * FROM ${TestConstants.DirtyTable} WHERE ${DbConstants.Order}=? AND (${DbConstants.ID}>=? OR (${DbConstants.Name}=? AND (${DbConstants.Name}=? OR (${DbConstants.Name}=? OR ${DbConstants.Order}<?)))) AND ${DbConstants.Order}=? OR ${DbConstants.ID}=?",
            result.first
        )
    }
    
    
    @Test
    fun `Query in query`() {
        val result = getQuery(
            table = TestConstants.Users,
            columns = arrayOf(DbConstants.Name),
            where = {
                subquery(
                    table = TestConstants.Posts,
                    columns = arrayOf(DbConstants.countAll),
                    where = {
                        (TestConstants.Posts dot TestConstants.UserID) equal (TestConstants.Users dot DbConstants.ID)
                    }
                ) greater 10
            }
        )
        
        TestCase.assertEquals(
            "SELECT ${DbConstants.Name} FROM ${TestConstants.Users} WHERE (SELECT COUNT(*) FROM ${TestConstants.Posts} WHERE ${TestConstants.Posts}.user_id=?)>?",
            result.first
        )
        TestCase.assertEquals(
            arrayOf(
                "${TestConstants.Users}.${DbConstants.ID}",
                "10"
            ).joinToString(DbConstants.comma),
            result.second.joinToString(DbConstants.comma)
        )
    }
    
    
    
    
    
    
    
    
    
    
    
    
}