package com.vankorno.sandbox.set

import android.content.ContentValues
import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox.LabRat
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.entities.testEntity.CTest.Bool1
import com.vankorno.sandbox.entities.testEntity.CTest.Bool2
import com.vankorno.sandbox.entities.testEntity.CTest.Bool3
import com.vankorno.sandbox.entities.testEntity.CTest.Enabled
import com.vankorno.sandbox.entities.testEntity.CTest.Float1
import com.vankorno.sandbox.entities.testEntity.CTest.Float2
import com.vankorno.sandbox.entities.testEntity.CTest.Int1
import com.vankorno.sandbox.entities.testEntity.CTest.Int2
import com.vankorno.sandbox.entities.testEntity.CTest.Int3
import com.vankorno.sandbox.entities.testEntity.CTest.Long1
import com.vankorno.sandbox.entities.testEntity.CTest.Long2
import com.vankorno.sandbox.entities.testEntity.CTest.Position
import com.vankorno.sandbox.entities.testEntity.CTest.Str1
import com.vankorno.sandbox.entities.testEntity.OrmTest
import com.vankorno.sandbox.entities.testEntity.TestEntity
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.misc.whereName
import org.junit.Assert.*
import org.junit.Test

@MediumTest
class SetDslTest : BaseAndroidTest() {
    companion object {
        const val SetValsTestTable = "SetValsTestTable"
    }
    
    @Test
    fun testSetter() {
        dbh.getLabRats(SetValsTestTable)
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
        setAsIntWithMathWorks()
        setAsLongWithMathWorks()
        setAsFloatWithMathWorks()
        
    }
    
    fun setCvWorks() {
        val rat = LabRat + 1
        
        val cv = ContentValues()
        cv.put(Int1.name, 345)
        cv.put(Str1.name, "super pooper")
        cv.put(Bool1.name, true)
        cv.put(Long1.name, 9999999L)
        cv.put(Float1.name, 345.67F)
        
        dbh.set(SetValsTestTable, whereName(rat)) { setCV(cv) }
        
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(345, obj.int1)
        assertEquals("super pooper", obj.str1)
        assertEquals(true, obj.bool1)
        assertEquals(9999999L, obj.long1)
        assertEquals(345.67F, obj.float1)
    }
    
    
    fun setToSimple() {
        val rat = LabRat + 2
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Int1 setTo 5
            Int2.setTo(5)
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(5, obj.int1)
        assertEquals(5, obj.int2)
    }
    
    
    fun setToMixed() {
        val rat = LabRat + 3
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Str1 setTo "lkjlkjlj"
            Enabled setTo true
            Position setTo 69
            Bool3 setTo true
            Float1 setTo 2.4F
            Long1 setTo 9999L
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals("lkjlkjlj", obj.str1)
        assertEquals(true, obj.enabled)
        assertEquals(69, obj.position)
        assertEquals(true, obj.bool3)
        assertEquals(2.4F, obj.float1)
        assertEquals(9999L, obj.long1)
    }
    
    
    fun flipWorks() {
        val rat = LabRat + 4
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Bool1 setTo true
            Bool2.flip()
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(true, obj.bool1)
        assertEquals(true, obj.bool2)
    }
    
    
    fun numOpsWork() {
        val rat = LabRat + 5
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Int1 setTo 10
            Int1 add 5
            Int2 setTo 20
            Int2 sub 3
            Long1 setTo 7L
            Long1 mult 3
            Float1 setTo 2F
            Float1 mult 1.5F
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(15, obj.int1)
        assertEquals(17, obj.int2)
        assertEquals(21L, obj.long1)
        assertEquals(3F, obj.float1)
    }
    
    
    fun absWorks() {
        val rat = LabRat + 6
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Int1 setTo -42
            Int1.abs()
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(42, obj.int1)
    }
    
    
    fun minMaxWorks() {
        val rat = LabRat + 7
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Int1 setTo 100
            Int1 capAt 50
            Int2 setTo 5
            Int2 floorAt 10
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(50, obj.int1)
        assertEquals(10, obj.int2)
    }
    
    
    fun coerceInWorks() {
        val rat = LabRat + 8
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Int1 setTo 999
            Int1 coerceIn 0..100
            Float1 setTo -5F
            Float1.coerceIn(0F..1F)
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(100, obj.int1)
        assertEquals(0F, obj.float1)
    }
    
    
    fun setAsSimple() {
        val rat = LabRat + 9
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Int2 setTo 77
            Int1 setAs Int2
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(77, obj.int1)
        assertEquals(77, obj.int2)
    }
    
    
    
    
    
    
    fun setAsIntWithMathWorks() {
        val rat = LabRat + 11
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Int1 setAs (Int2 andAdd 5)      // Int1 = 0 + 5 = 5
            Int2 setAs (Int1 andMult 3)     // Int2 = 5 * 3 = 15
            Int3 setAs (Int2 andCoerceIn 0..10) // Int3 = 15 coerced to 10
            Int1 setAs (Int3 andCapAt 100)  // Int1 = min(10, 100) = 10
            Int2 setAs (Int3 andFloorAt 50) // Int2 = max(10, 50?) = 50
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(10, obj.int1)
        assertEquals(50, obj.int2)
        assertEquals(10, obj.int3)
    }
    
    
    fun setAsLongWithMathWorks() {
        val rat = LabRat + 12
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Long1 setAs (Long2 andAdd 5000L)  // Long1 = 0 + 5000
            Long2 setAs (Long1 andMult 3L)    // Long2 = 5000 * 3 = 15000
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(5000L, obj.long1)
        assertEquals(15000L, obj.long2)
    }
    
    
    fun setAsFloatWithMathWorks() {
        val rat = LabRat + 13
        
        dbh.set(SetValsTestTable, whereName(rat)) {
            Float1 setAs (Float2 andAdd 1.5F) // Float1 = 0 + 1.5 = 1.5
            Float2 setAs (Float1 andDiv 2F)   // Float2 = 1.5 / 2 = 0.75
        }
        val obj = dbh.getObj(SetValsTestTable, OrmTest, TestEntity()) { Name = rat }
        
        assertEquals(1.5F, obj.float1)
        assertEquals(0.75F, obj.float2)
    }
    
    
    
    
    
    
    
    
}