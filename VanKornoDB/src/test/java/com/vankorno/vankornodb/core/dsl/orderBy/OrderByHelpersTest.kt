package com.vankorno.vankornodb.core.dsl.orderBy

import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import com.vankorno.vankornodb.misc.by
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cRowId
import com.vankorno.vankornodb.misc.orderBy
import com.vankorno.vankornodb.misc.orderById
import com.vankorno.vankornodb.misc.orderByIdFlipped
import com.vankorno.vankornodb.misc.orderByRowId
import com.vankorno.vankornodb.misc.orderByRowIdFlipped
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderByHelpersTest {
    private val colInt = iCol("intCol")
    private val colStr = sCol("strCol")

    @Test
    fun `orderBy adds multiple columns in ascending order`() {
        val order = OrderDsl().apply(orderBy(colInt, colStr)).buildStr()

        assertEquals("intCol, strCol", order)
    }

    @Test
    fun `by adds a single column in ascending order`() {
        val order = OrderDsl().apply(colInt.by()).buildStr()

        assertEquals("intCol", order)
    }

    @Test
    fun `orderById adds ID in ascending order`() {
        val order = OrderDsl().apply(orderById()).buildStr()

        assertEquals(cID.name, order)
    }

    @Test
    fun `orderByIdFlipped adds ID in descending order`() {
        val order = OrderDsl().apply(orderByIdFlipped()).buildStr()

        assertEquals(cID.name + " DESC", order)
    }

    @Test
    fun `orderByRowId adds row ID in ascending order`() {
        val order = OrderDsl().apply(orderByRowId()).buildStr()

        assertEquals(cRowId.name, order)
    }

    @Test
    fun `orderByRowIdFlipped adds row ID in descending order`() {
        val order = OrderDsl().apply(orderByRowIdFlipped()).buildStr()

        assertEquals(cRowId.name + " DESC", order)
    }
}
