package com.vankorno.db.start

import androidx.test.filters.MediumTest
import com.vankorno.db.BaseAndroidTest
import com.vankorno.db.MyApp.Companion.dbh
import com.vankorno.db.entities.EntityEnum
import com.vankorno.db.entities.TABLE_Versions
import com.vankorno.db.entities.versions.VersionEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@MediumTest
class StartTest() : BaseAndroidTest() {
    
    @Test
    fun versionTableCreated() {
        assertTrue(dbh.tableExists(TABLE_Versions))
    }
    
    @Test
    fun rowCreated() {
        val firstRow = dbh.getRowOrNull<VersionEntity>(TABLE_Versions)
        assertTrue(firstRow != null)
    }
    
    @Test
    fun correctVersionVals() {
        val firstRow = dbh.getRowOrNull<VersionEntity>(TABLE_Versions) ?: return
        
        val target = VersionEntity(
            name = EntityEnum.Versions.dbRowName,
            version = 1,
            id = 1
        )
        assertEquals(firstRow.name, target.name)
        assertEquals(firstRow.version, target.version)
        assertEquals(firstRow.id, target.id)
    }
    
    
    
    
}