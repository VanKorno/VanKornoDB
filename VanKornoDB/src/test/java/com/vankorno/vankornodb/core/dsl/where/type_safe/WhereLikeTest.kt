package com.vankorno.vankornodb.core.dsl.where.type_safe

import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereLikeTest {

    @Test
    fun `Like and NotLike with values`() {
        val whereObj = WhereDsl().apply {
            sCol("colA") like "pattern%"
            sCol("colB") notLike "%suffix"
        }

        assertEquals("colA LIKE ?", whereObj.clauses[0])
        assertEquals("pattern%", whereObj.args[0])
        assertEquals("colB NOT LIKE ?", whereObj.clauses[1])
        assertEquals("%suffix", whereObj.args[1])
    }

    @Test
    fun `LikeCol and NotLikeCol with other columns`() {
        val whereObj = WhereDsl().apply {
            sCol("colA") likeCol sCol("colB")
            sCol("colC") notLikeCol sCol("colD")
        }

        assertEquals("colA LIKE colB", whereObj.clauses[0])
        assertEquals("colC NOT LIKE colD", whereObj.clauses[1])
    }

    @Test
    fun `likeAny and notLikeAny`() {
        val whereObj = WhereDsl().apply {
            sCol("colX").likeAny("a%", "b_", "c%")
            sCol("colY").notLikeAny("x%", "y_")
        }

        assertEquals("colX LIKE (?, ?, ?)", whereObj.clauses[0])
        assertEquals(listOf("a%", "b_", "c%", "x%", "y_"), whereObj.args)
        assertEquals("colY NOT LIKE (?, ?)", whereObj.clauses[1])
    }

    @Test
    fun `likeAnyCol and notLikeAnyCol`() {
        val whereObj = WhereDsl().apply {
            sCol("colM").likeAnyCol(sCol("colN"), sCol("colO"))
            sCol("colP").notLikeAnyCol(sCol("colQ"), sCol("colR"), sCol("colS"))
        }

        assertEquals("(colM LIKE colN OR colM LIKE colO)", whereObj.clauses[0])
        assertEquals("(colP NOT LIKE colQ AND colP NOT LIKE colR AND colP NOT LIKE colS)", whereObj.clauses[1])
    }
}
