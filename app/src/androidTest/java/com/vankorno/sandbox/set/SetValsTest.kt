package com.vankorno.sandbox.set

import android.content.ContentValues
import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.entities.testEntity.CTest.Bool1
import com.vankorno.sandbox.entities.testEntity.CTest.Float1
import com.vankorno.sandbox.entities.testEntity.CTest.Int1
import com.vankorno.sandbox.entities.testEntity.CTest.Long1
import com.vankorno.sandbox.entities.testEntity.CTest.Str1
import com.vankorno.sandbox.entities.testEntity.TestEntity
import com.vankorno.vankornodb.misc.whereName
import org.junit.Assert.*
import org.junit.Test

const val SetValsTestTable = "SetValsTestTable"
const val LabRat = "Lab rat "

@MediumTest
class SetValsTest : BaseAndroidTest() {
    
    @Test
    fun testSetter() {
        dbh.beforeSetValsTest()
        assertTrue(dbh.tableExists(SetValsTestTable))
        assertFalse(dbh.isTableEmpty(SetValsTestTable))
        
        setCvWorks()
    }
    
    fun setCvWorks() {
        val cv = ContentValues()
        cv.put(Int1.name, 345)
        cv.put(Str1.name, "super pooper")
        cv.put(Bool1.name, true)
        cv.put(Long1.name, 9999999L)
        cv.put(Float1.name, 345.67F)
        
        dbh.setVals(SetValsTestTable, whereName(LabRat + 1)) { setCV(cv) }
        
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(LabRat + 1) }
        
        assertNotEquals(null, obj)
        assertEquals(345, obj!!.int1)
        assertEquals("super pooper", obj!!.str1)
        assertEquals(true, obj!!.bool1)
        assertEquals(9999999L, obj!!.long1)
        assertEquals(345.67F, obj!!.float1)
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}