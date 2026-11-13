package com.vankorno.sandbox.get

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRat
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.makeRatsTestNest
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.CurrSchemaBundle
import com.vankorno.vankornodb.dbManagement.data.using
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

private object DummySchema : CurrSchemaBundle<TestEntity>(
    clazz = TestEntity::class
)
private object OrmTestWrongGetter : CurrSchemaBundle<TestEntity>(
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
        makeRatsTestNest(GetEntityTestTable)
        
        getEntityWithGeneratedGetter()
        getEntityWithNullGetter()
        getterHasPriorityOverReflection()
    }
    
    private fun getEntityWithGeneratedGetter() {
        val obj = dbh.getEntity(GetEntityTestTable using _Test) { ID = 1L }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
    
    private fun getEntityWithNullGetter() {
        val obj = dbh.getEntity(GetEntityTestTable using DummySchema) { ID = 1L }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
    @Test
    fun getterHasPriorityOverReflection() {
        val obj = dbh.getEntity(GetEntityTestTable using OrmTestWrongGetter) { ID = 1L }
        
        assertNotNull(obj)
        assertEquals(999, obj!!.id)
        assertEquals("WRONG", obj.name)
    }
}
