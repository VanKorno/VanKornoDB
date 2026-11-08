package com.vankorno.vankornodb.core.dsl.where.autoAddAND

import com.vankorno.vankornodb.TestConstants.cFloat
import com.vankorno.vankornodb.TestConstants.cInt
import com.vankorno.vankornodb.api.WhereDsl
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
        val where: WhereDsl.() -> Unit = {
            rawClause("active = 1")
            rawClause("enabled = 0")
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf("active = 1", "AND enabled = 0"),
            whereObj.clauses
        )
        assertTrue(whereObj.args.isEmpty())
    }


    @Test
    fun `Equal Int`() {
        val where: WhereDsl.() -> Unit = {
            cInt equal 42
            cInt equal 43
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(cInt.name + "=?", "AND " + cInt.name + "=?"),
            whereObj.clauses
        )
        assertEquals(listOf("42", "43"), whereObj.args)
    }

    @Test
    fun `Equal String`() {
        val where: WhereDsl.() -> Unit = {
            cName equal "VanKorno"
            cText equal "Gamplified"
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(cName.name + "=?", "AND " + cText.name + "=?"),
            whereObj.clauses
        )
        assertEquals(listOf("VanKorno", "Gamplified"), whereObj.args)
    }

    @Test
    fun `Equal Boolean`() {
        val where: WhereDsl.() -> Unit = {
            cActive equal true
            cEnabled equal false
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(cActive.name + "=?", "AND " + cEnabled.name + "=?"),
            whereObj.clauses
        )
        assertEquals(listOf("1", "0"), whereObj.args)
    }

    @Test
    fun `Equal Long`() {
        val where: WhereDsl.() -> Unit = {
            cPosition equal 11L
            cID equal 22L
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(cPosition.name + "=?", "AND " + cID.name + "=?"),
            whereObj.clauses
        )
        assertEquals(listOf("11", "22"), whereObj.args)
    }

    @Test
    fun `Equal Float`() {
        val where: WhereDsl.() -> Unit = {
            cFloat equal 1.5F
            cFloat equal 2.5F
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(cFloat.name + "=?", "AND " + cFloat.name + "=?"),
            whereObj.clauses
        )
        assertEquals(listOf("1.5", "2.5"), whereObj.args)
    }


    @Test
    fun `Not equal all types`() {
        val where: WhereDsl.() -> Unit = {
            cInt notEqual 42
            cName notEqual "VanKorno"
            cActive notEqual true
            cPosition notEqual 123L
            cFloat notEqual 1.5F
        }
        val whereObj = WhereDsl().apply(where)

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
        val where: WhereDsl.() -> Unit = {
            cInt greater 42
            cPosition greater 123L
            cFloat greater 1.5F
        }
        val whereObj = WhereDsl().apply(where)

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
        val where: WhereDsl.() -> Unit = {
            cInt greaterEqual 42
            cPosition greaterEqual 123L
            cFloat greaterEqual 1.5F
        }
        val whereObj = WhereDsl().apply(where)

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
        val where: WhereDsl.() -> Unit = {
            cInt less 42
            cPosition less 123L
            cFloat less 1.5F
        }
        val whereObj = WhereDsl().apply(where)

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
        val where: WhereDsl.() -> Unit = {
            cInt lessEqual 42
            cPosition lessEqual 123L
            cFloat lessEqual 1.5F
        }
        val whereObj = WhereDsl().apply(where)

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
