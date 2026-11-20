package com.vankorno.vankornodb.core.dsl.orderBy

import com.vankorno.vankornodb.core.dsl.BuilderOrder
import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderByFlipTest {
    private val colInt = testColumn(iCol("intCol"))
    private val colStr = testColumn(sCol("strCol"))
    private val colBool = testColumn(bCol("boolCol"))
    private val colLong = testColumn(lCol("longCol"))
    private val colFloat = testColumn(fCol("floatCol"))

    @Test
    fun `flipAll with multiple columns`() {
        val order = BuilderOrder().apply {
            flipAll(colInt, colStr, colBool, colLong, colFloat)
        }.buildStr()

        assertEquals(
            "intCol DESC, strCol DESC, boolCol DESC, longCol DESC, floatCol DESC",
            order
        )
    }

    @Test
    fun `flipAll with mixed ascending and descending columns`() {
        val order = BuilderOrder().apply {
            col(colInt)
            flipAll(colStr, colLong)
            col(colBool, colFloat)
        }.buildStr()

        assertEquals(
            "intCol, strCol DESC, longCol DESC, boolCol, floatCol",
            order
        )
    }

    @Test
    fun `flip single column`() {
        val order = BuilderOrder().apply {
            colInt.flip()
        }.buildStr()

        assertEquals("intCol DESC", order)
    }

    @Test
    fun `flip multiple individual columns`() {
        val order = BuilderOrder().apply {
            colInt.flip()
            colStr.flip()
            colBool.flip()
        }.buildStr()

        assertEquals(
            "intCol DESC, strCol DESC, boolCol DESC",
            order
        )
    }

    @Test
    fun `flipIf true adds descending column`() {
        val order = BuilderOrder().apply {
            colInt.flipIf(true)
        }.buildStr()

        assertEquals("intCol DESC", order)
    }

    @Test
    fun `flipIf false adds ascending column`() {
        val order = BuilderOrder().apply {
            colInt.flipIf(false)
        }.buildStr()

        assertEquals("intCol", order)
    }

    @Test
    fun `flipIf with mixed conditions`() {
        val order = BuilderOrder().apply {
            colInt.flipIf(true)
            colStr.flipIf(false)
            colBool.flipIf(true)
            colLong.flipIf(false)
            colFloat.flipIf(true)
        }.buildStr()

        assertEquals(
            "intCol DESC, strCol, boolCol DESC, longCol, floatCol DESC",
            order
        )
    }
}
