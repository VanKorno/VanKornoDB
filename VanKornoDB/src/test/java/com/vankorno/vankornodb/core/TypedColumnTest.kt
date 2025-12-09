package com.vankorno.vankornodb.core

import com.vankorno.vankornodb.dbManagement.data.bListCol
import com.vankorno.vankornodb.dbManagement.data.fListCol
import com.vankorno.vankornodb.dbManagement.data.iListCol
import com.vankorno.vankornodb.dbManagement.data.lListCol
import com.vankorno.vankornodb.dbManagement.data.pListCol
import com.vankorno.vankornodb.dbManagement.data.sListCol
import org.junit.Assert.assertEquals
import org.junit.Test

class TypedColumnTest {
    @Test
    fun `list creation and size`() {
        val colInt = iListCol("int", 10)
        assertEquals(colInt.size, 10)
        assertEquals(colInt[0].name, "int1")
        assertEquals(colInt[3].defaultVal, 0)
        
        val colStr = sListCol("str", 10)
        assertEquals(colStr.size, 10)
        assertEquals(colStr[1].name, "str2")
        assertEquals(colStr[4].defaultVal, "")
        
        val colBool = bListCol("bool", 10)
        assertEquals(colBool.size, 10)
        assertEquals(colBool[3].name, "bool4")
        assertEquals(colBool[5].defaultVal, false)
        
        val colLong = lListCol("long", 10)
        assertEquals(colLong.size, 10)
        assertEquals(colLong[4].name, "long5")
        assertEquals(colLong[6].defaultVal, 0L)
        
        val colFloat = fListCol("float", 10)
        assertEquals(colFloat.size, 10)
        assertEquals(colFloat[6].name, "float7")
        assertEquals(colFloat[7].defaultVal, 0F)
        
        val colBlob = pListCol("blob", 10)
        assertEquals(colBlob.size, 10)
        assertEquals(colBlob[4].name, "blob5")
    }
    
    
    @Test
    fun `defaults`() {
        val colNeg = iListCol("negative", 2, -1)
        assertEquals(colNeg[1].defaultVal, -1)
        
        val colStr = sListCol("str", 2, "yolo")
        assertEquals(colStr[1].defaultVal, "yolo")
    }
    
    
    @Test
    fun `empty list`() {
        val empty = iListCol("iii", 0)
        assertEquals(empty, emptyList<Int>())
        assertEquals(empty.size, 0)
    }
    
    
    @Test
    fun `empty name`() {
        val nameless = iListCol("", 10)
        assertEquals(nameless.size, 10)
        assertEquals(nameless[0].name, "1")
        assertEquals(nameless[9].name, "10")
    }
    
}

