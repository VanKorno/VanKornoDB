package com.vankorno.vankornodb.dbManagement

import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.iCol
import org.junit.Assert.*
import org.junit.Test

class DbAutoMockingTest {
    
    @Test
    fun `DbRuntime dbh auto initializes in unit tests`() {
        val helper = dbh
        assertNotNull(helper)
    }
    
    
    @Test
    fun `read operation returns default value during unit test`() {
        val result = dbh.read(default = 999) { _ ->
            fail("Database read block should not be executed during unit test")
            123
        }
        assertEquals(999, result)
    }
    
    
    @Test
    fun `write operation does nothing as no-op during unit test`() {
        var executed = false
        dbh.write { _ ->
            executed = true
        }
        assertFalse("Write block should not execute during unit test", executed)
    }
    
    
    @Test
    fun `readWrite operation returns default value during unit test`() {
        val result = dbh.readWrite(default = "default_val") { _ ->
            fail("ReadWrite block should not be executed during unit test")
            "actual_val"
        }
        assertEquals("default_val", result)
    }
    
    
    @Test
    fun `voidRead operation does not exit block during unit test`() {
        var executed = false
        dbh.voidRead { _ ->
            executed = true
        }
        assertFalse("VoidRead block should not execute during unit test", executed)
    }
    
    
    @Test
    fun `DbHelperInternal helper functions return defaults or noop during unit test`() {
        assertFalse(dbh.tableExists("TestTable"))
        assertTrue(dbh.isTableEmpty("TestTable"))
        assertEquals(0, dbh.getRowCount("TestTable"))
        
        dbh.setInt(42, "TestTable", iCol("score")) { ID = 1L }
    }
    
    
}
