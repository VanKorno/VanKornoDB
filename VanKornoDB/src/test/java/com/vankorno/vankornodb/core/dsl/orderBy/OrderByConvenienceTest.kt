package com.vankorno.vankornodb.core.dsl.orderBy

import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.core.dsl.testOrder
import com.vankorno.vankornodb.dbManagement.data.sCol
import com.vankorno.vankornodb.misc.orderById
import com.vankorno.vankornodb.misc.orderByIdFlipped
import com.vankorno.vankornodb.misc.orderByPosition
import com.vankorno.vankornodb.misc.orderByPositionFlipped
import com.vankorno.vankornodb.misc.orderByRowId
import com.vankorno.vankornodb.misc.orderByRowIdFlipped
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderByConvenienceTest {
    @Test
    fun `byId ascending`() {
        val order = testOrder(orderById()).buildStr()

        assertEquals("id", order)
    }

    @Test
    fun `byId descending`() {
        val order = testOrder(orderByIdFlipped()).buildStr()

        assertEquals("id DESC", order)
    }

    @Test
    fun `byRowId ascending`() {
        val order = testOrder(orderByRowId()).buildStr()

        assertEquals("ROWID", order)
    }

    @Test
    fun `byRowId descending`() {
        val order = testOrder(orderByRowIdFlipped()).buildStr()

        assertEquals("ROWID DESC", order)
    }

    @Test
    fun `byPosition ascending`() {
        val order = testOrder(orderByPosition()).buildStr()

        assertEquals("position", order)
    }

    @Test
    fun `byPosition descending`() {
        val order = testOrder(orderByPositionFlipped()).buildStr()

        assertEquals("position DESC", order)
    }

    @Test
    fun `byName ascending`() {
        val order = testOrder {
            testColumn(sCol("name"))()
        }.buildStr()

        assertEquals("name", order)
    }

    @Test
    fun `byName descending`() {
        val order = testOrder {
            testColumn(sCol("name")).flip()
        }.buildStr()

        assertEquals("name DESC", order)
    }
}
