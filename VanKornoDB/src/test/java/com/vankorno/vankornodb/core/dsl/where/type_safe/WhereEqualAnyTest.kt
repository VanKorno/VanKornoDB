package com.vankorno.vankornodb.core.dsl.where.type_safe

import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereEqualAnyTest {

    @Test
    fun `IntCol equalAny and notEqualAny`() {
        val whereObj = WhereDsl().apply {
            iCol("intCol").equalAny(1, 2, 3)
            iCol("intCol2").notEqualAny(4, 5)
        }

        assertEquals(whereObj.clauses[0], "intCol IN (?, ?, ?)")
        assertEquals(whereObj.args[0], "1")
        assertEquals(whereObj.args[1], "2")
        assertEquals(whereObj.args[2], "3")

        assertEquals(whereObj.clauses[1], "intCol2 NOT IN (?, ?)")
        assertEquals(whereObj.args[3], "4")
        assertEquals(whereObj.args[4], "5")
    }

    @Test
    fun `StrCol equalAny and notEqualAny`() {
        val whereObj = WhereDsl().apply {
            sCol("strCol").equalAny("foo", "bar")
            sCol("strCol2").notEqualAny("baz")
        }

        assertEquals(whereObj.clauses[0], "strCol IN (?, ?)")
        assertEquals(whereObj.args[0], "foo")
        assertEquals(whereObj.args[1], "bar")
        assertEquals(whereObj.clauses[1], "strCol2 NOT IN (?)")
        assertEquals(whereObj.args[2], "baz")
    }

    @Test
    fun `BoolCol equalAny and notEqualAny`() {
        val whereObj = WhereDsl().apply {
            bCol("boolCol").equalAny(true, false)
            bCol("boolCol2").notEqualAny(false)
        }

        assertEquals(whereObj.clauses[0], "boolCol IN (?, ?)")
        assertEquals(whereObj.args[0], "1")
        assertEquals(whereObj.args[1], "0")
        assertEquals(whereObj.clauses[1], "boolCol2 NOT IN (?)")
        assertEquals(whereObj.args[2], "0")
    }

    @Test
    fun `LongCol equalAny and notEqualAny`() {
        val whereObj = WhereDsl().apply {
            lCol("longCol").equalAny(10L, 20L)
            lCol("longCol2").notEqualAny(30L)
        }

        assertEquals(whereObj.clauses[0], "longCol IN (?, ?)")
        assertEquals(whereObj.args[0], "10")
        assertEquals(whereObj.args[1], "20")
        assertEquals(whereObj.clauses[1], "longCol2 NOT IN (?)")
        assertEquals(whereObj.args[2], "30")
    }

    @Test
    fun `FloatCol equalAny and notEqualAny`() {
        val whereObj = WhereDsl().apply {
            fCol("floatCol").equalAny(1.1F, 2.2F)
            fCol("floatCol2").notEqualAny(3.3F)
        }

        assertEquals(whereObj.clauses[0], "floatCol IN (?, ?)")
        assertEquals(whereObj.args[0], "1.1")
        assertEquals(whereObj.args[1], "2.2")
        assertEquals(whereObj.clauses[1], "floatCol2 NOT IN (?)")
        assertEquals(whereObj.args[2], "3.3")
    }
}
