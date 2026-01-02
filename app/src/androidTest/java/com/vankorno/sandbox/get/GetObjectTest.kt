package com.vankorno.sandbox.get

import com.vankorno.sandbox.AppStart.DemoApp.Companion.dbh
import com.vankorno.sandbox.LabRat
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.dbManagement.data.CurrSchemaBundle
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.misc.whereId
import org.junit.Assert.*
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

class GetObjectTest {
    companion object {
        const val GetObjTestTable = "GetObjTestTable"
    }
    
    @Test
    fun getObjectTest() {
        dbh.getLabRats(GetObjTestTable)
        assertTrue(dbh.tableExists(GetObjTestTable))
        assertFalse(dbh.isTableEmpty(GetObjTestTable))
        
        getObjWithGeneratedGetter()
        getObjWithNullGetter()
        getterHasPriorityOverReflection()
    }
    
    private fun getObjWithGeneratedGetter() {
        val obj = dbh.getObjPro(GetObjTestTable using _Test) { where = whereId(1) }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
    
    private fun getObjWithNullGetter() {
        val obj = dbh.getObjPro(GetObjTestTable using DummySchema) { where = whereId(1) }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
    @Test
    fun getterHasPriorityOverReflection() {
        val obj = dbh.getObjPro(GetObjTestTable using OrmTestWrongGetter) { where = whereId(1) }
        
        assertNotNull(obj)
        assertEquals(999, obj!!.id)
        assertEquals("WRONG", obj.name)
    }
    
}