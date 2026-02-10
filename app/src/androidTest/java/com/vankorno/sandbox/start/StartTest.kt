package com.vankorno.sandbox.start

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox._entities.EntityMeta
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_EntityVersions
import com.vankorno.vankornodb.dbManagement.migration.data.EntityVersion
import com.vankorno.vankornodb.dbManagement.migration.data.TTTEntityVersion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@MediumTest
class StartTest() : BaseAndroidTest() {
    
    @Test
    fun versionTableCreated() {
        assertTrue(dbh.tableExists(TABLE_EntityVersions))
    }
    
    @Test
    fun rowCreated() {
        val firstRow = dbh.getObj(TTTEntityVersion)
        assertTrue(firstRow != null)
    }
    
    @Test
    fun correctVersionVals() {
        val firstRow = dbh.getObj(TTTEntityVersion, default = EntityVersion())
        
        val target = EntityVersion(
            name = EntityMeta.TestEntt.dbRowName,
            version = 3,
            id = 1
        )
        assertEquals(firstRow.name, target.name)
        assertEquals(firstRow.version, target.version)
        assertEquals(firstRow.id, target.id)
    }
    
    
    
    
}
