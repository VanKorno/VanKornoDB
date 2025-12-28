package com.vankorno.vankornodb.newTable

import com.vankorno.vankornodb.dbManagement.data.*
import org.junit.Assert.assertEquals
import org.junit.Test

class NewTableTest {
    @Test
    fun testSimpleEntityTableCreation() {
        val cols = listOf(
            iCol("id"),
            sCol("name", "default"),
            bCol("isActive", true),
            iCol("age", 25)
        )
        
        val sql = newTableQuery("SimpleTable", cols)
        
        assertEquals(
            "CREATE TABLE IF NOT EXISTS SimpleTable (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "name TEXT NOT NULL DEFAULT 'default', " +
                "isActive BOOL NOT NULL DEFAULT 1, " +
                "age INT NOT NULL DEFAULT 25" +
            ")",
            sql
        )
    }
    
    @Test
    fun testComplexEntityTableCreation() {
        val cols = listOf(
            iCol("id"),
            pCol("data"),
            sCol("name", "blob"),
            iCol("flags", 0)
        )
        
        val sql = newTableQuery("ComplexTable", cols)
        
        assertEquals(
            "CREATE TABLE IF NOT EXISTS ComplexTable (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "data BLOB NOT NULL, " +
                "name TEXT NOT NULL DEFAULT 'blob', " +
                "flags INT NOT NULL DEFAULT 0" +
            ")",
            sql
        )
    }
    
    @Test
    fun testUnsupportedTypesAreIgnored() {
        val cols = listOf(
            iCol("id"),
            // intentionally omitted: unsupported types
        )
        
        val sql = newTableQuery("UnsupportedTypes", cols)
        
        assertEquals(
            "CREATE TABLE IF NOT EXISTS UnsupportedTypes (" +
                "id INTEGER NOT NULL PRIMARY KEY" +
            ")",
            sql
        )
    }
    
    @Test
    fun testListEntitySingle() {
        val cols = listOf(
            iCol("id")
        ) + iListCol("scores", 3, 1)
        
        val sql = newTableQuery("ListSingleTable", cols)
        
        assertEquals(
            "CREATE TABLE IF NOT EXISTS ListSingleTable (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "scores1 INT NOT NULL DEFAULT 1, " +
                "scores2 INT NOT NULL DEFAULT 1, " +
                "scores3 INT NOT NULL DEFAULT 1" +
            ")",
            sql
        )
    }
    
    @Test
    fun testListEntityMixed() {
        val cols = listOf(
            iCol("id")
        ) +
            bListCol("flags", 2, true) +
            sListCol("names", 2, "alpha")
        
        val sql = newTableQuery("ListMixedTable", cols)
        
        assertEquals(
            "CREATE TABLE IF NOT EXISTS ListMixedTable (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "flags1 BOOL NOT NULL DEFAULT 1, " +
                "flags2 BOOL NOT NULL DEFAULT 1, " +
                "names1 TEXT NOT NULL DEFAULT 'alpha', " +
                "names2 TEXT NOT NULL DEFAULT 'alpha'" +
            ")",
            sql
        )
    }
    
    
    
    
    
    
    
    
    
    
    
}