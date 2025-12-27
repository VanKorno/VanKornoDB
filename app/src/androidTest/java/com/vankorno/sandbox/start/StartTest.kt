package com.vankorno.sandbox.start

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.entities.EntityMeta
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_EntityVersions
import com.vankorno.vankornodb.dbManagement.migration.data.OrmVersion
import com.vankorno.vankornodb.dbManagement.migration.data.VersionEntity
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
        val firstRow = dbh.getObj(TABLE_EntityVersions, OrmVersion)
        assertTrue(firstRow != null)
    }
    
    @Test
    fun correctVersionVals() {
        val firstRow = dbh.getObj(TABLE_EntityVersions, OrmVersion, default = VersionEntity())
        
        val target = VersionEntity(
            name = EntityMeta.TestEntt.dbRowName,
            version = 3,
            id = 1
        )
        assertEquals(firstRow.name, target.name)
        assertEquals(firstRow.version, target.version)
        assertEquals(firstRow.id, target.id)
    }
    
    
    
    
}