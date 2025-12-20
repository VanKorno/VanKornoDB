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

        assertEquals(whereObj.clauses[0], "colA LIKE ?")
        assertEquals(whereObj.args[0], "pattern%")
        assertEquals(whereObj.clauses[1], "colB NOT LIKE ?")
        assertEquals(whereObj.args[1], "%suffix")
    }

    @Test
    fun `LikeCol and NotLikeCol with other columns`() {
        val whereObj = WhereDsl().apply {
            sCol("colA") likeCol sCol("colB")
            sCol("colC") notLikeCol sCol("colD")
        }

        assertEquals(whereObj.clauses[0], "colA LIKE colB")
        assertEquals(whereObj.clauses[1], "colC NOT LIKE colD")
    }

    @Test
    fun `likeAny and notLikeAny`() {
        val whereObj = WhereDsl().apply {
            sCol("colX").likeAny("a%", "b_", "c%")
            sCol("colY").notLikeAny("x%", "y_")
        }

        assertEquals(whereObj.clauses[0], "colX LIKE (?, ?, ?)")
        assertEquals(whereObj.args, listOf("a%", "b_", "c%", "x%", "y_"))
        assertEquals(whereObj.clauses[1], "colY NOT LIKE (?, ?)")
    }

    @Test
    fun `likeAnyCol and notLikeAnyCol`() {
        val whereObj = WhereDsl().apply {
            sCol("colM").likeAnyCol(sCol("colN"), sCol("colO"))
            sCol("colP").notLikeAnyCol(sCol("colQ"), sCol("colR"), sCol("colS"))
        }

        assertEquals(whereObj.clauses[0], "(colM LIKE colN OR colM LIKE colO)")
        assertEquals(whereObj.clauses[1], "(colP NOT LIKE colQ AND colP NOT LIKE colR AND colP NOT LIKE colS)")
    }
}
