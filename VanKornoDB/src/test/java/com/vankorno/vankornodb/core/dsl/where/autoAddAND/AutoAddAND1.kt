package com.vankorno.vankornodb.core.dsl.where.autoAddAND

import com.vankorno.vankornodb.core.data.DbConstants.IN
import com.vankorno.vankornodb.core.data.DbConstants._Name
import com.vankorno.vankornodb.core.dsl.WhereDslBase
import com.vankorno.vankornodb.misc.data.SharedCol.cActive
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AutoAddAND1 {

    private class TestWhereDsl : WhereDslBase()
    
    @Test
    fun `condition adds AND when needed`() {
        val dsl = TestWhereDsl()

        dsl.condition(_Name, "=", "VanKorno")
        dsl.condition(cActive.name, "=", "1")

        assertEquals(
            listOf(_Name + "=?", "AND " + cActive.name + "=?"),
            dsl.clauses
        )
        assertEquals(listOf("VanKorno", "1"), dsl.args)
    }

    @Test
    fun `condition doesn't add AND to first condition`() {
        val dsl = TestWhereDsl()

        dsl.condition(_Name, "=", "VanKorno")

        assertEquals(listOf(_Name + "=?"), dsl.clauses)
        assertEquals(listOf("VanKorno"), dsl.args)
    }


    @Test
    fun `conditionRaw adds AND when needed`() {
        val dsl = TestWhereDsl()

        dsl.condition(_Name, "=", "VanKorno")
        dsl.conditionRaw(cActive.name, "=", "1")

        assertEquals(
            listOf(_Name + "=?", "AND " + cActive.name + "=1"),
            dsl.clauses
        )
        assertEquals(listOf("VanKorno"), dsl.args)
    }

    @Test
    fun `conditionRaw doesn't add AND to first condition`() {
        val dsl = TestWhereDsl()

        dsl.conditionRaw(cActive.name, "=", "1")

        assertEquals(listOf(cActive.name + "=1"), dsl.clauses)
        assertTrue(dsl.args.isEmpty())
    }


    @Test
    fun `multCompare adds AND when needed`() {
        val dsl = TestWhereDsl()

        dsl.condition(_Name, "=", "VanKorno")
        dsl.multCompare(cPosition.name, IN, arrayOf(1L, 2L, 3L))

        assertEquals(
            listOf(_Name + "=?", "AND " + cPosition.name + IN + "(?, ?, ?)"),
            dsl.clauses
        )
        assertEquals(listOf("VanKorno", "1", "2", "3"), dsl.args)
    }

    @Test
    fun `multCompare doesn't add AND to first condition`() {
        val dsl = TestWhereDsl()

        dsl.multCompare(cPosition.name, IN, arrayOf(1L, 2L))

        assertEquals(listOf(cPosition.name + IN + "(?, ?)"), dsl.clauses)
        assertEquals(listOf("1", "2"), dsl.args)
    }


    @Test
    fun `multCompareRaw adds AND when needed`() {
        val dsl = TestWhereDsl()

        dsl.condition(_Name, "=", "VanKorno")
        dsl.multCompareRaw(cPosition.name, "=", arrayOf("1", "2"), true)

        assertEquals(
            listOf(_Name + "=?", "AND (" + cPosition.name + "=1 OR " + cPosition.name + "=2)"),
            dsl.clauses
        )
        assertEquals(listOf("VanKorno"), dsl.args)
    }

    @Test
    fun `multCompareRaw doesn't add AND to first condition`() {
        val dsl = TestWhereDsl()

        dsl.multCompareRaw(cPosition.name, "=", arrayOf("1", "2"), false)

        assertEquals(
            listOf("(" + cPosition.name + "=1 AND " + cPosition.name + "=2)"),
            dsl.clauses
        )
        assertTrue(dsl.args.isEmpty())
    }
}

