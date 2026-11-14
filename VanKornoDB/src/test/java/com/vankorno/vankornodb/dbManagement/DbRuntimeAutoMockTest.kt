package com.vankorno.vankornodb.dbManagement

import com.vankorno.vankornodb.api.DbRuntime
import org.junit.Assert.assertNotNull
import org.junit.Test

class DbRuntimeAutoMockTest {
    
    @Test
    fun `DbRuntime dbh auto initializes with dummy instance in unit tests`() {
        
        val dbhInstance = DbRuntime.dbh
        
        assertNotNull(dbhInstance)
    }
}
