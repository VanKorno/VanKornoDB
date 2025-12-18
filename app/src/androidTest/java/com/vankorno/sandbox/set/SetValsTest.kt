package com.vankorno.sandbox.set

import android.content.ContentValues
import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.entities.testEntity.CTest.Bool1
import com.vankorno.sandbox.entities.testEntity.CTest.Bool2
import com.vankorno.sandbox.entities.testEntity.CTest.Bool3
import com.vankorno.sandbox.entities.testEntity.CTest.Enabled
import com.vankorno.sandbox.entities.testEntity.CTest.Float1
import com.vankorno.sandbox.entities.testEntity.CTest.Int1
import com.vankorno.sandbox.entities.testEntity.CTest.Int2
import com.vankorno.sandbox.entities.testEntity.CTest.Long1
import com.vankorno.sandbox.entities.testEntity.CTest.Position
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
        setToSimple()
        setToMixed()
        flipWorks()
        numOpsWork()
        absWorks()
        minMaxWorks()
        coerceInWorks()
        setAsSimple()
        setAsWithMath()
    }
    
    fun setCvWorks() {
        val rat = LabRat + 1
        
        val cv = ContentValues()
        cv.put(Int1.name, 345)
        cv.put(Str1.name, "super pooper")
        cv.put(Bool1.name, true)
        cv.put(Long1.name, 9999999L)
        cv.put(Float1.name, 345.67F)
        
        dbh.setVals(SetValsTestTable, whereName(rat)) { setCV(cv) }
        
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(rat) }
        
        assertNotEquals(null, obj)
        assertEquals(345, obj!!.int1)
        assertEquals("super pooper", obj!!.str1)
        assertEquals(true, obj!!.bool1)
        assertEquals(9999999L, obj!!.long1)
        assertEquals(345.67F, obj!!.float1)
    }
    
    
    fun setToSimple() {
        val rat = LabRat + 2
        
        dbh.setVals(SetValsTestTable, whereName(rat)) {
            Int1 setTo 5
            Int2.setTo(5)
        }
        
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(rat) }
        
        assertNotEquals(null, obj)
        assertEquals(5, obj!!.int1)
        assertEquals(5, obj!!.int2)
    }
    
    
    fun setToMixed() {
        val rat = LabRat + 3
        
        dbh.setVals(SetValsTestTable, whereName(rat)) {
            Str1 setTo "lkjlkjlj"
            Enabled setTo true
            Position setTo 69
            Bool3 setTo true
            Float1 setTo 2.4F
            Long1 setTo 9999L
        }
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(rat) }
        
        assertNotEquals(null, obj)
        assertEquals("lkjlkjlj", obj!!.str1)
        assertEquals(true, obj!!.enabled)
        assertEquals(69, obj!!.position)
        assertEquals(true, obj!!.bool3)
        assertEquals(2.4F, obj!!.float1)
        assertEquals(9999L, obj!!.long1)
    }
    
    
    fun flipWorks() {
        val rat = LabRat + 4
        
        dbh.setVals(SetValsTestTable, whereName(rat)) {
            Bool1 setTo true
            Bool2.flip()
        }
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(rat) }
        
        assertNotEquals(null, obj)
        assertEquals(true, obj!!.bool1)
        assertEquals(true, obj!!.bool2)
    }
    
    
    fun numOpsWork() {
        val rat = LabRat + 5
        
        dbh.setVals(SetValsTestTable, whereName(rat)) {
            Int1 setTo 10
            Int1 add 5
            Int2 setTo 20
            Int2 sub 3
            Long1 setTo 7L
            Long1 mult 3
            Float1 setTo 2F
            Float1 mult 1.5F
        }
        
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(rat) }
        
        assertNotEquals(null, obj)
        assertEquals(15, obj!!.int1)
        assertEquals(17, obj!!.int2)
        assertEquals(21L, obj!!.long1)
        assertEquals(3F, obj!!.float1)
    }
    
    
    fun absWorks() {
        val rat = LabRat + 6
        
        dbh.setVals(SetValsTestTable, whereName(rat)) {
            Int1 setTo -42
            Int1.abs()
        }
        
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(rat) }
        
        assertNotEquals(null, obj)
        assertEquals(42, obj!!.int1)
    }
    
    
    fun minMaxWorks() {
        val rat = LabRat + 7
        
        dbh.setVals(SetValsTestTable, whereName(rat)) {
            Int1 setTo 100
            Int1 capAt 50
            Int2 setTo 5
            Int2 floorAt 10
        }
        
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(rat) }
        
        assertNotEquals(null, obj)
        assertEquals(50, obj!!.int1)
        assertEquals(10, obj!!.int2)
    }
    
    
    fun coerceInWorks() {
        val rat = LabRat + 8
        
        dbh.setVals(SetValsTestTable, whereName(rat)) {
            Int1 setTo 999
            Int1 coerceIn 0..100
            Float1 setTo -5F
            Float1.coerceIn(0F..1F)
        }
        
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(rat) }
        
        assertNotEquals(null, obj)
        assertEquals(100, obj!!.int1)
        assertEquals(0F, obj!!.float1)
    }
    
    
    fun setAsSimple() {
        val rat = LabRat + 9
        
        dbh.setVals(SetValsTestTable, whereName(rat)) {
            Int2 setTo 77
            Int1 setAs Int2
        }
        
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(rat) }
        
        assertNotEquals(null, obj)
        assertEquals(77, obj!!.int1)
        assertEquals(77, obj!!.int2)
    }
    
    // TODO
    fun setAsWithMath() {
        val rat = LabRat + 10
        
        dbh.setVals(SetValsTestTable, whereName(rat)) {
            Int2 setTo 10
            Int1.setAs(Int2) {
                Int2 add 5
                Int2 mult 2
            }
        }
        
        val obj = dbh.getObjOrNull<TestEntity>(SetValsTestTable) { where = whereName(rat) }
        
        assertNotEquals(null, obj)
        assertEquals(30, obj!!.int1)
        assertEquals(10, obj!!.int2)
    }
}