package com.vankorno.sandbox.get

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRat
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.SchemaBundle
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.get.getEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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

private const val GetEntityTestTable = "GetEntityTestTable"


@MediumTest
class GetEntityTest {
    @Test
    fun getEntityTest() {
        dbh.getLabRats(GetEntityTestTable)
        
        getEntityWithGeneratedGetter()
        getEntityWithNullGetter()
        getterHasPriorityOverReflection()
    }
    
    private fun getEntityWithGeneratedGetter() {
        val obj = dbh.getEntity(_Test.table(GetEntityTestTable)) { Id equal 1L }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
    
    private fun getEntityWithNullGetter() {
        val obj = dbh.getEntity(GetEntityTestTable using DummySchema) { Id equal 1L }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
    @Test
    fun getterHasPriorityOverReflection() {
        val obj = dbh.getEntity(GetEntityTestTable using OrmTestWrongGetter) { Id equal 1L }
        
        assertNotNull(obj)
        assertEquals(999, obj!!.id)
        assertEquals("WRONG", obj.name)
    }
}
