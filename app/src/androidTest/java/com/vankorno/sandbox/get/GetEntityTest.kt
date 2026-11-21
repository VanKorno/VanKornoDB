package com.vankorno.sandbox.get

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRat
import com.vankorno.sandbox._entities.testEntity._TestEntity
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.delete.clearTable
import com.vankorno.vankornodb.get.getEntity
import com.vankorno.vankornodb.get.tableExists
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private const val GetEntityTestTableName = "GetEntityTestTable"
private val GetEntityTestTable = _TestEntity.tableNamed(GetEntityTestTableName)

@MediumTest
class GetEntityTest {
    @Before
    fun prepareTable() {
        if (dbh.tableExists(GetEntityTestTable))
            dbh.clearTable(GetEntityTestTable)
        dbh.getLabRats(GetEntityTestTableName)
    }
    
    
    @Test
    private fun getEntityWithGeneratedGetter() {
        val obj = dbh.getEntity(GetEntityTestTable) { Id equal 1L }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
    
}
