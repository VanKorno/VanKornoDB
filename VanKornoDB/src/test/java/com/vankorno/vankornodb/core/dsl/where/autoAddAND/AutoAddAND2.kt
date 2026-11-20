package com.vankorno.vankornodb.core.dsl.where.autoAddAND

import com.vankorno.vankornodb.TestConstants.cFloat
import com.vankorno.vankornodb.TestConstants.cInt
import com.vankorno.vankornodb.core.dsl.BuilderWhere
import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.core.dsl.testPredicate
import com.vankorno.vankornodb.misc.data.SharedCol.cActive
import com.vankorno.vankornodb.misc.data.SharedCol.cEnabled
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import com.vankorno.vankornodb.misc.data.SharedCol.cText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class AutoAddAND2 {
    
    @Test
    fun `Raw clause`() {
        val where: BuilderWhere.() -> Unit = {
            rawClause("active = 1")
            rawClause("enabled = 0")
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf("active = 1", "AND enabled = 0"),
            whereObj.clauses
        )
        assertTrue(whereObj.args.isEmpty())
    }


    @Test
    fun `Equal Int`() {
        val where: BuilderWhere.() -> Unit = {
            testColumn(cInt) equal 42
            testColumn(cInt) equal 43
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(cInt.name + "=?", "AND " + cInt.name + "=?"),
            whereObj.clauses
        )
        assertEquals(listOf("42", "43"), whereObj.args)
    }

    @Test
    fun `Equal String`() {
        val where: BuilderWhere.() -> Unit = {
            testColumn(cName) equal "VanKorno"
            testColumn(cText) equal "Gamplified"
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(cName.name + "=?", "AND " + cText.name + "=?"),
            whereObj.clauses
        )
        assertEquals(listOf("VanKorno", "Gamplified"), whereObj.args)
    }

    @Test
    fun `Equal Boolean`() {
        val where: BuilderWhere.() -> Unit = {
            testColumn(cActive) equal true
            testColumn(cEnabled) equal false
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(cActive.name + "=?", "AND " + cEnabled.name + "=?"),
            whereObj.clauses
        )
        assertEquals(listOf("1", "0"), whereObj.args)
    }

    @Test
    fun `Equal Long`() {
        val where: BuilderWhere.() -> Unit = {
            testColumn(cPosition) equal 11L
            testColumn(cID) equal 22L
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(cPosition.name + "=?", "AND " + cID.name + "=?"),
            whereObj.clauses
        )
        assertEquals(listOf("11", "22"), whereObj.args)
    }

    @Test
    fun `Equal Float`() {
        val where: BuilderWhere.() -> Unit = {
            testColumn(cFloat) equal 1.5F
            testColumn(cFloat) equal 2.5F
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(cFloat.name + "=?", "AND " + cFloat.name + "=?"),
            whereObj.clauses
        )
        assertEquals(listOf("1.5", "2.5"), whereObj.args)
    }


    @Test
    fun `Not equal all types`() {
        val where: BuilderWhere.() -> Unit = {
            testColumn(cInt) notEqual 42
            testColumn(cName) notEqual "VanKorno"
            testColumn(cActive) notEqual true
            testColumn(cPosition) notEqual 123L
            testColumn(cFloat) notEqual 1.5F
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                cInt.name + "!=?",
                "AND " + cName.name + "!=?",
                "AND " + cActive.name + "!=?",
                "AND " + cPosition.name + "!=?",
                "AND " + cFloat.name + "!=?"
            ),
            whereObj.clauses
        )
        assertEquals(listOf("42", "VanKorno", "1", "123", "1.5"), whereObj.args)
    }


    @Test
    fun `Greater`() {
        val where: BuilderWhere.() -> Unit = {
            testColumn(cInt) greater 42
            testColumn(cPosition) greater 123L
            testColumn(cFloat) greater 1.5F
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                cInt.name + ">?",
                "AND " + cPosition.name + ">?",
                "AND " + cFloat.name + ">?"
            ),
            whereObj.clauses
        )
        assertEquals(listOf("42", "123", "1.5"), whereObj.args)
    }


    @Test
    fun `Greater equal`() {
        val where: BuilderWhere.() -> Unit = {
            testColumn(cInt) greaterEqual 42
            testColumn(cPosition) greaterEqual 123L
            testColumn(cFloat) greaterEqual 1.5F
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                cInt.name + ">=?",
                "AND " + cPosition.name + ">=?",
                "AND " + cFloat.name + ">=?"
            ),
            whereObj.clauses
        )
        assertEquals(listOf("42", "123", "1.5"), whereObj.args)
    }


    @Test
    fun `Less`() {
        val where: BuilderWhere.() -> Unit = {
            testColumn(cInt) less 42
            testColumn(cPosition) less 123L
            testColumn(cFloat) less 1.5F
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                cInt.name + "<?",
                "AND " + cPosition.name + "<?",
                "AND " + cFloat.name + "<?"
            ),
            whereObj.clauses
        )
        assertEquals(listOf("42", "123", "1.5"), whereObj.args)
    }


    @Test
    fun `Less equal`() {
        val where: BuilderWhere.() -> Unit = {
            testColumn(cInt) lessEqual 42
            testColumn(cPosition) lessEqual 123L
            testColumn(cFloat) lessEqual 1.5F
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                cInt.name + "<=?",
                "AND " + cPosition.name + "<=?",
                "AND " + cFloat.name + "<=?"
            ),
            whereObj.clauses
        )
        assertEquals(listOf("42", "123", "1.5"), whereObj.args)
    }
    
    
}
