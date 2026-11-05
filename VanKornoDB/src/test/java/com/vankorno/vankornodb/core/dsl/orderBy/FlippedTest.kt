package com.vankorno.vankornodb.core.dsl.orderBy

import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.flipped
import com.vankorno.vankornodb.misc.flippedIf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FlippedTest {
    private val colInt = iCol("intCol")
    private val colStr = sCol("strCol")
    private val colBool = bCol("boolCol")
    private val colLong = lCol("longCol")
    private val colFloat = fCol("floatCol")
    
    @Test
    fun `flipped converts all column types to descending`() {
        val flipped = listOf(
            colInt.flipped(),
            colStr.flipped(),
            colBool.flipped(),
            colLong.flipped(),
            colFloat.flipped()
        )
        assertTrue(flipped.all { it is DescendingColumn })
        assertEquals(
            listOf("intCol", "strCol", "boolCol", "longCol", "floatCol"),
            flipped.map { it.name }
        )
    }
    
    @Test
    fun `flipped columns produce descending order`() {
        val order = OrderDsl().apply {
            col(
                colInt.flipped(),
                colStr.flipped(),
                colBool.flipped(),
                colLong.flipped(),
                colFloat.flipped()
            )
        }.buildStr()
        
        assertEquals(
            "intCol DESC, strCol DESC, boolCol DESC, longCol DESC, floatCol DESC",
            order
        )
    }
    
    @Test
    fun `flippedIf true converts all column types`() {
        val flipped = listOf(
            colInt.flippedIf(true),
            colStr.flippedIf(true),
            colBool.flippedIf(true),
            colLong.flippedIf(true),
            colFloat.flippedIf(true)
        )
        
        assertTrue(flipped.all { it is DescendingColumn })
    }
    
    @Test
    fun `flippedIf false preserves all column types`() {
        val unchanged = listOf(
            colInt.flippedIf(false),
            colStr.flippedIf(false),
            colBool.flippedIf(false),
            colLong.flippedIf(false),
            colFloat.flippedIf(false)
        )
        assertTrue(unchanged.all { it is AscendingColumn<*> })
        assertEquals(
            listOf("intCol", "strCol", "boolCol", "longCol", "floatCol"),
            unchanged.map { it.name }
        )
    }
    
    @Test
    fun `flippedIf condition controls ordering`() {
        val order = OrderDsl().apply {
            col(
                colInt.flippedIf(true),
                colStr.flippedIf(false),
                colBool.flippedIf(true),
                colLong.flippedIf(false),
                colFloat.flippedIf(true)
            )
        }.buildStr()
        
        assertEquals(
            "intCol DESC, strCol, boolCol DESC, longCol, floatCol DESC",
            order
        )
    }
}
