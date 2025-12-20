package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderByTest {
    private val colInt = iCol("intCol")
    private val colStr = sCol("strCol")
    private val colBool = bCol("boolCol")
    private val colLong = lCol("longCol")
    private val colFloat = fCol("floatCol")

    @Test
    fun `orderBy with single column ascending`() {
        val order = OrderDsl().apply {
            colInt()
        }.buildStr()

        assertEquals("intCol", order)
    }

    @Test
    fun `orderBy with single column descending`() {
        val order = OrderDsl().apply {
            colStr.flip()
        }.buildStr()

        assertEquals("strCol DESC", order)
    }

    @Test
    fun `orderBy multiple columns mixed`() {
        val order = OrderDsl().apply {
            colInt()
            colStr.flip()
            colBool()
        }.buildStr()

        assertEquals("intCol, strCol DESC, boolCol", order)
    }

    @Test
    fun `orderBy with raw string`() {
        val order = OrderDsl().apply {
            raw("customCol DESC")
        }.buildStr()

        assertEquals("customCol DESC", order)
    }

    @Test
    fun `orderBy full mix`() {
        val order = OrderDsl().apply {
            colInt()
            colStr.flip()
            colBool()
            colLong.flip()
            colFloat()
            raw("rawCol ASC")
        }.buildStr()

        assertEquals("intCol, strCol DESC, boolCol, longCol DESC, floatCol, rawCol ASC", order)
    }
}