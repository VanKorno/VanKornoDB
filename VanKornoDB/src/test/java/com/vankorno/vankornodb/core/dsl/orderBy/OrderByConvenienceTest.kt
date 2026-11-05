package com.vankorno.vankornodb.core.dsl.orderBy

import com.vankorno.vankornodb.api.OrderDsl
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderByConvenienceTest {
    @Test
    fun `byId ascending`() {
        val order = OrderDsl().apply {
            byId()
        }.buildStr()

        assertEquals("id", order)
    }

    @Test
    fun `byId descending`() {
        val order = OrderDsl().apply {
            byIdFlipped()
        }.buildStr()

        assertEquals("id DESC", order)
    }

    @Test
    fun `byRowId ascending`() {
        val order = OrderDsl().apply {
            byRowId()
        }.buildStr()

        assertEquals("ROWID", order)
    }

    @Test
    fun `byRowId descending`() {
        val order = OrderDsl().apply {
            byRowIdFlipped()
        }.buildStr()

        assertEquals("ROWID DESC", order)
    }

    @Test
    fun `byPosition ascending`() {
        val order = OrderDsl().apply {
            byPosition()
        }.buildStr()

        assertEquals("position", order)
    }

    @Test
    fun `byPosition descending`() {
        val order = OrderDsl().apply {
            byPositionFlipped()
        }.buildStr()

        assertEquals("position DESC", order)
    }

    @Test
    fun `byName ascending`() {
        val order = OrderDsl().apply {
            byName()
        }.buildStr()

        assertEquals("name", order)
    }

    @Test
    fun `byName descending`() {
        val order = OrderDsl().apply {
            byNameFlipped()
        }.buildStr()

        assertEquals("name DESC", order)
    }
}
