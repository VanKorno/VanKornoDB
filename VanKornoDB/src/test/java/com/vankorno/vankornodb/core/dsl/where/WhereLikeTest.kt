package com.vankorno.vankornodb.core.dsl.where

import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.core.dsl.testPredicate
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereLikeTest {

    @Test
    fun `Like and NotLike with values`() {
        val whereObj = testPredicate {
            testColumn(sCol("colA")) like "pattern%"
            testColumn(sCol("colB")) notLike "%suffix"
        }

        assertEquals("colA LIKE ?", whereObj.clauses[0])
        assertEquals("pattern%", whereObj.args[0])
        assertEquals("AND colB NOT LIKE ?", whereObj.clauses[1])
        assertEquals("%suffix", whereObj.args[1])
    }

    @Test
    fun `LikeCol and NotLikeCol with other columns`() {
        val whereObj = testPredicate {
            testColumn(sCol("colA")) likeCol testColumn(sCol("colB"))
            testColumn(sCol("colC")) notLikeCol testColumn(sCol("colD"))
        }

        assertEquals("colA LIKE colB", whereObj.clauses[0])
        assertEquals("AND colC NOT LIKE colD", whereObj.clauses[1])
    }

    @Test
    fun `likeAny and notLikeAny`() {
        val whereObj = testPredicate {
            testColumn(sCol("colX")).likeAny("a%", "b_", "c%")
            testColumn(sCol("colY")).notLikeAny("x%", "y_")
        }

        assertEquals("(colX LIKE ? OR colX LIKE ? OR colX LIKE ?)", whereObj.clauses[0])
        assertEquals(listOf("a%", "b_", "c%", "x%", "y_"), whereObj.args)
        assertEquals("AND (colY NOT LIKE ? AND colY NOT LIKE ?)", whereObj.clauses[1])
    }

    @Test
    fun `likeAnyCol and notLikeAnyCol`() {
        val whereObj = testPredicate {
            testColumn(sCol("colM")).likeAnyCol(testColumn(sCol("colN")), testColumn(sCol("colO")))
            testColumn(sCol("colP")).notLikeAnyCol(testColumn(sCol("colQ")), testColumn(sCol("colR")), testColumn(sCol("colS")))
        }

        assertEquals("(colM LIKE colN OR colM LIKE colO)", whereObj.clauses[0])
        assertEquals("AND (colP NOT LIKE colQ AND colP NOT LIKE colR AND colP NOT LIKE colS)", whereObj.clauses[1])
    }
}
