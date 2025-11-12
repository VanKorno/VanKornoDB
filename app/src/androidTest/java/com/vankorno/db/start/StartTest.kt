package com.vankorno.db.start

import androidx.test.filters.MediumTest
import com.vankorno.db.BaseAndroidTest
import com.vankorno.db.MyApp.Companion.dbh
import com.vankorno.db.entities.EntityMeta
import com.vankorno.vankornodb.core.DbConstants.TABLE_EntityVersions
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
        val firstRow = dbh.getObjOrNull<VersionEntity>(TABLE_EntityVersions)
        assertTrue(firstRow != null)
    }
    
    @Test
    fun correctVersionVals() {
        val firstRow = dbh.getObjOrNull<VersionEntity>(TABLE_EntityVersions) ?: return
        
        val target = VersionEntity(
            name = EntityMeta.TestEntt.dbRowName,
            version = 1,
            id = 1
        )
        assertEquals(firstRow.name, target.name)
        assertEquals(firstRow.version, target.version)
        assertEquals(firstRow.id, target.id)
    }
    
    
    
    
}