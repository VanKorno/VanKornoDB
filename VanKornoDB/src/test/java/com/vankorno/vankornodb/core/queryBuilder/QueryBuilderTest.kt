package com.vankorno.vankornodb.core.queryBuilder

import com.vankorno.vankornodb.TestConstants.*
import com.vankorno.vankornodb.core.data.DbConstants.WHERE
import com.vankorno.vankornodb.core.data.DbConstants._Name
import com.vankorno.vankornodb.misc.columns
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryBuilderTest { // TODO split tests/files
    
    private infix fun String.c(str2: String = "") = this + ", " + str2
    
    
    @Test
    fun `Simple checks`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable,
            getQuery(DirtyTable).query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + ORDER_BY + _ID,
            getQuery(DirtyTable) { orderBy(_ID) }.query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID+"=?" + ORDER_BY + _ID c _Name,
            
            getQuery(DirtyTable) {
                where { ID = 1 }
                orderBy {
                    raw(_ID)
                    raw(_Name)
                }
            }.query
        )
        
        assertEquals(
            SELECT + _ID c _Name + FROM + DirtyTable + WHERE + _ID+">=?",
            
            getQuery(DirtyTable, columns(_ID, _Name)) {
                where { _ID greaterEqual 10 }
            }.query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID+">?",
            
            getQuery(DirtyTable) {
                where { _ID greater 1 }
            }.query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID+"<?",
            
            getQuery(DirtyTable) {
                where { _ID less 1 }
            }.query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID+"<=?",
            
            getQuery(DirtyTable) {
                where { _ID lessEqual 1 }
            }.query
        )
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + Bool1+"=?",
            
            getQuery(DirtyTable) {
                where { Bool1 equal true }
            }.query
        )
    }
    
    @Test
    fun `Simple AND OR conditions`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable + WHERE + _ID+">=?" + and + _Name+"=?",
            
            getQuery(DirtyTable) {
                where {
                    _ID greaterEqual 10
                    and { Name = BestName }
                }
            }.query
        )
        
        assertEquals(
            SELECT_ALL_FROM + DirtyTable +
                WHERE + _ID+">=?" +
                and + _Name+"=?" +
                and + _Position+"=?" +
                and + _ID+"=?",
            getQuery(DirtyTable) {
                where {
                    _ID greaterEqual 10
                    and { _Name equal BestName }
                    and { _Position equal 1.1F }
                    and { _ID equal "1" }
                }
            }.query
        )
        
        assertEquals(
            SELECT_ALL_FROM + DirtyTable +
                WHERE + _ID+">=?" +
                and + _Name+"=?" +
                or + _Position+"=?" +
                or + _ID+"=?",
            getQuery(DirtyTable) {
                where {
                    _ID greaterEqual 10
                    and { _Name equal BestName }
                    or { _Position equal 1.1F }
                    or { _ID equal 1 }
                }
            }.query
        )
    }
    
    @Test
    fun `condition values`() {
        assertEquals(
            arrayOf("1", "1", "1.1", "1").joinToString(comma),
            getQuery(DirtyTable) {
                where {
                    _ID greaterEqual 1
                    and { _ID equal 1 }
                    and { _ID greaterEqual 1.1F }
                    and { _ID greaterEqual 1L }
                }
            }.args.joinToString(comma)
        )
    }
    
    
    
    @Test
    fun `Two lvl conditions orGroup is last`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable +
                WHERE + _ID+">=?" +
                and + _Name+"=?" +
                or + "("+_Position+"=?" + or + _ID+"=?)",
            
            getQuery(DirtyTable) {
                where {
                    _ID greaterEqual 10
                    and { _Name equal BestName }
                    orGroup {
                        _Position equal 1.1F
                        or { _ID equal 1 }
                    }
                }
            }.query
        )
    }
    
    @Test
    fun `Two lvl conditions group{} is first`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable +
                WHERE + "("+_ID+">=?" + and + _Name+"=?)" +
                and + _Position+"=?" + 
                or + _ID+"=?"
            ,
            getQuery(DirtyTable) {
                where {
                    group {
                        _ID greaterEqual 10
                        and { _Name equal BestName }
                    }
                    and { _Position equal 1.1F }
                    or { _ID equal 1 }
                }
            }.query
        )
    }
    
    @Test
    fun `Two lvl conditions andGroup is mid`() {
        assertEquals(
            SELECT_ALL_FROM + DirtyTable +
                WHERE + 
                _Position+"=?" +
                and +
                    "("+_ID+">=?" + and + _Name+"=?)" +
                and + _Position+"=?" + 
                or + _ID+"=?"
            ,
            getQuery(DirtyTable) {
                where {
                    _Position equal 1.1F
                    andGroup {
                        _ID greaterEqual 10
                        and { _Name equal BestName }
                    }
                    and { _Position equal 1.1F }
                    or { _ID equal 1 }
                }
            }.query
        )
    }
    
    
    @Test
    fun `Five lvl conditions`() {
        val result = getQuery(DirtyTable) {
            where {
                _Position equal 1
                andGroup {
                    _ID greaterEqual 2
                    orGroup {
                        _Name equal BestName
                        andGroup {
                            _Name equal BestName
                            orGroup {
                                _Name equal BestName
                                or { _Position less 5 }
                            }
                        }
                    }
                }
                and { _Position equal 1.1F }
                or { _ID equal 1 }
            }
        }
        
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
                                or + _Position+"<?" +
                            ")" +
                        ")" +
                    ")" +
                ")" +
                and + _Position+"=?" + 
                or + _ID+"=?"
            ,
            result.query
        )
        assertEquals(
            arrayOf("1", "2", BestName, BestName, BestName, "5", "1.1", "1").joinToString(", "),
            result.args.joinToString(", ")
        )
        assertEquals(
            "SELECT * FROM $DirtyTable WHERE $_Position=? AND ($_ID>=? OR ($_Name=? AND ($_Name=? OR ($_Name=? OR $_Position<?)))) AND $_Position=? OR $_ID=?",
            result.query
        )
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}