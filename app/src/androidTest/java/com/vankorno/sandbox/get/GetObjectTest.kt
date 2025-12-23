package com.vankorno.sandbox.get

import com.vankorno.sandbox.LabRat
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.entities.testEntity.TestEntity
import com.vankorno.sandbox.entities.testEntity.TestSpec
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.api.EntitySpec
import com.vankorno.vankornodb.misc.whereId
import org.junit.Assert.*
import org.junit.Test

object TestSpecDummy : EntitySpec<TestEntity>(
    clazz = TestEntity::class
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
    }
    
    private fun getObjWithGeneratedGetter() {
        val obj = dbh.getObjPro(GetObjTestTable, TestSpec) { where = whereId(1) }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
    
    private fun getObjWithNullGetter() {
        val obj = dbh.getObjPro(GetObjTestTable, TestSpecDummy) { where = whereId(1) }
        
        assertTrue(obj != null)
        assertEquals(1, obj!!.id)
        assertEquals(LabRat + 1, obj.name)
    }
    
}