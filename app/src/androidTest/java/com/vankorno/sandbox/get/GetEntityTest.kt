package com.vankorno.sandbox.get

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRat
import com.vankorno.sandbox._entities.testEntity.TestDsl
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity.TestExtraDsl
import com.vankorno.sandbox._entities.testEntity.TestOrderDsl
import com.vankorno.sandbox._entities.testEntity.TestSetDsl
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.core.dsl.withDsl
import com.vankorno.vankornodb.dbManagement.data.SchemaBundle
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.delete.clearTable
import com.vankorno.vankornodb.get.getEntity
import com.vankorno.vankornodb.get.tableExists
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private object DummySchema : SchemaBundle<TestEntity>(
    clazz = TestEntity::class
)
private object OrmTestWrongGetter : SchemaBundle<TestEntity>(
    clazz = TestEntity::class,
    getter = { _ ->
        TestEntity(id = 999, name = "WRONG")
    }
)

private const val GetEntityTestTableName = "GetEntityTestTable"
private val GetEntityTestTable = _Test.tableNamed(GetEntityTestTableName)

@MediumTest
class GetEntityTest {
    @Before
    fun prepareTable() {
        if (dbh.tableExists(GetEntityTestTable))
            dbh.clearTable(GetEntityTestTable)
        dbh.getLabRats(GetEntityTestTableName)
    }
    
    @Test
    fun getEntityTest() {
        
        getEntityWithGeneratedGetter()
        getEntityWithNullGetter()
    }
    
    private fun getEntityWithGeneratedGetter() {
        val obj = dbh.getEntity(GetEntityTestTable) { Id equal 1L }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
    
    private fun getEntityWithNullGetter() {
        val table = (GetEntityTestTableName using DummySchema).withDsl(::TestDsl, ::TestOrderDsl, ::TestExtraDsl, ::TestSetDsl)
        val obj = dbh.getEntity(table) { Id equal 1L }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
    @Test
    fun getterHasPriorityOverReflection() {
        val table = (GetEntityTestTableName using OrmTestWrongGetter).withDsl(::TestDsl, ::TestOrderDsl, ::TestExtraDsl, ::TestSetDsl)
        val obj = dbh.getEntity(table) { Id equal 1L }
        
        assertNotNull(obj)
        assertEquals(999, obj!!.id)
        assertEquals("WRONG", obj.name)
    }
}
