package com.vankorno.vankornodb.core.dsl.where.autoAddAND

import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.data.DbConstants.IN
import com.vankorno.vankornodb.core.data.DbConstants.NOT_IN
import com.vankorno.vankornodb.core.data.DbConstants._ID
import com.vankorno.vankornodb.core.data.DbConstants._Name
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import com.vankorno.vankornodb.misc.data.SharedCol.cInt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AutoAddAND4 {

    private val intCol = iCol("int")
    private val strCol = sCol("str")
    private val boolCol = bCol("bool")
    private val longCol = lCol("long")
    private val floatCol = fCol("float")

    private val strCol1 = sCol("str1")
    private val strCol2 = sCol("str2")

    @Test
    fun `Like`() {
        val where: WhereDsl.() -> Unit = {
            strCol like "%test%"
            strCol notLike "%other%"
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                strCol.name + " LIKE ?",
                "AND " + strCol.name + " NOT LIKE ?"
            ),
            whereObj.clauses
        )
        assertEquals(listOf("%test%", "%other%"), whereObj.args)
    }

    @Test
    fun `Like column`() {
        val where: WhereDsl.() -> Unit = {
            strCol likeCol strCol1
            strCol notLikeCol strCol2
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                strCol.name + " LIKE " + strCol1.name,
                "AND " + strCol.name + " NOT LIKE " + strCol2.name
            ),
            whereObj.clauses
        )
        assertTrue(whereObj.args.isEmpty())
    }

    @Test
    fun `Like any`() {
        val where: WhereDsl.() -> Unit = {
            strCol.likeAny("%one%", "%two%")
            strCol.notLikeAny("%three%", "%four%")
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                strCol.name + " LIKE (?, ?)",
                "AND " + strCol.name + " NOT LIKE (?, ?)"
            ),
            whereObj.clauses
        )
        assertEquals(
            listOf("%one%", "%two%", "%three%", "%four%"),
            whereObj.args
        )
    }

    @Test
    fun `Like any column`() {
        val where: WhereDsl.() -> Unit = {
            strCol.likeAnyCol(strCol1, strCol2)
            strCol.notLikeAnyCol(strCol1, strCol2)
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                "(" + strCol.name + " LIKE " + strCol1.name + " OR " + strCol.name + " LIKE " + strCol2.name + ")",
                "AND (" + strCol.name + " NOT LIKE " + strCol1.name + " AND " + strCol.name + " NOT LIKE " + strCol2.name + ")"
            ),
            whereObj.clauses
        )
        assertTrue(whereObj.args.isEmpty())
    }

    @Test
    fun `Equal any arrays`() {
        val where: WhereDsl.() -> Unit = {
            intCol equalAny arrayOf(1, 2)
            strCol equalAny arrayOf("one", "two")
            boolCol equalAny arrayOf(true, false)
            longCol equalAny arrayOf(10L, 20L)
            floatCol equalAny arrayOf(1.5f, 2.5f)
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                intCol.name + IN + "(?, ?)",
                "AND " + strCol.name + IN + "(?, ?)",
                "AND " + boolCol.name + IN + "(?, ?)",
                "AND " + longCol.name + IN + "(?, ?)",
                "AND " + floatCol.name + IN + "(?, ?)"
            ),
            whereObj.clauses
        )
        assertEquals(
            listOf("1", "2", "one", "two", "1", "0", "10", "20", "1.5", "2.5"),
            whereObj.args
        )
    }

    @Test
    fun `Not equal any arrays`() {
        val where: WhereDsl.() -> Unit = {
            intCol notEqualAny arrayOf(1, 2)
            strCol notEqualAny arrayOf("one", "two")
            boolCol notEqualAny arrayOf(true, false)
            longCol notEqualAny arrayOf(10L, 20L)
            floatCol notEqualAny arrayOf(1.5f, 2.5f)
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                intCol.name + NOT_IN + "(?, ?)",
                "AND " + strCol.name + NOT_IN + "(?, ?)",
                "AND " + boolCol.name + NOT_IN + "(?, ?)",
                "AND " + longCol.name + NOT_IN + "(?, ?)",
                "AND " + floatCol.name + NOT_IN + "(?, ?)"
            ),
            whereObj.clauses
        )
        assertEquals(
            listOf("1", "2", "one", "two", "1", "0", "10", "20", "1.5", "2.5"),
            whereObj.args
        )
    }

    @Test
    fun `Equal any lists`() {
        val where: WhereDsl.() -> Unit = {
            intCol equalAny listOf(1, 2)
            strCol equalAny listOf("one", "two")
            boolCol equalAny listOf(true, false)
            longCol equalAny listOf(10L, 20L)
            floatCol equalAny listOf(1.5f, 2.5f)
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                intCol.name + IN + "(?, ?)",
                "AND " + strCol.name + IN + "(?, ?)",
                "AND " + boolCol.name + IN + "(?, ?)",
                "AND " + longCol.name + IN + "(?, ?)",
                "AND " + floatCol.name + IN + "(?, ?)"
            ),
            whereObj.clauses
        )
        assertEquals(
            listOf("1", "2", "one", "two", "1", "0", "10", "20", "1.5", "2.5"),
            whereObj.args
        )
    }

    @Test
    fun `Not equal any lists`() {
        val where: WhereDsl.() -> Unit = {
            intCol notEqualAny listOf(1, 2)
            strCol notEqualAny listOf("one", "two")
            boolCol notEqualAny listOf(true, false)
            longCol notEqualAny listOf(10L, 20L)
            floatCol notEqualAny listOf(1.5f, 2.5f)
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                intCol.name + NOT_IN + "(?, ?)",
                "AND " + strCol.name + NOT_IN + "(?, ?)",
                "AND " + boolCol.name + NOT_IN + "(?, ?)",
                "AND " + longCol.name + NOT_IN + "(?, ?)",
                "AND " + floatCol.name + NOT_IN + "(?, ?)"
            ),
            whereObj.clauses
        )
        assertEquals(
            listOf("1", "2", "one", "two", "1", "0", "10", "20", "1.5", "2.5"),
            whereObj.args
        )
    }

    @Test
    fun `Equal any of`() {
        val where: WhereDsl.() -> Unit = {
            intCol.equalAnyOf(1, 2)
            strCol.equalAnyOf("one", "two")
            boolCol.equalAnyOf(true, false)
            longCol.equalAnyOf(10L, 20L)
            floatCol.equalAnyOf(1.5f, 2.5f)
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                intCol.name + IN + "(?, ?)",
                "AND " + strCol.name + IN + "(?, ?)",
                "AND " + boolCol.name + IN + "(?, ?)",
                "AND " + longCol.name + IN + "(?, ?)",
                "AND " + floatCol.name + IN + "(?, ?)"
            ),
            whereObj.clauses
        )
        assertEquals(
            listOf("1", "2", "one", "two", "1", "0", "10", "20", "1.5", "2.5"),
            whereObj.args
        )
    }

    @Test
    fun `Not equal any of`() {
        val where: WhereDsl.() -> Unit = {
            intCol.notEqualAnyOf(1, 2)
            strCol.notEqualAnyOf("one", "two")
            boolCol.notEqualAnyOf(true, false)
            longCol.notEqualAnyOf(10L, 20L)
            floatCol.notEqualAnyOf(1.5f, 2.5f)
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                intCol.name + NOT_IN + "(?, ?)",
                "AND " + strCol.name + NOT_IN + "(?, ?)",
                "AND " + boolCol.name + NOT_IN + "(?, ?)",
                "AND " + longCol.name + NOT_IN + "(?, ?)",
                "AND " + floatCol.name + NOT_IN + "(?, ?)"
            ),
            whereObj.clauses
        )
        assertEquals(
            listOf("1", "2", "one", "two", "1", "0", "10", "20", "1.5", "2.5"),
            whereObj.args
        )
    }
    
    
    @Test
    fun `ID setter`() {
        val where: WhereDsl.() -> Unit = {
            ID = 1L
            ID = 2L
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                _ID + "=?",
                "AND " + _ID + "=?"
            ),
            whereObj.clauses
        )
        assertEquals(listOf("1", "2"), whereObj.args)
    }

    @Test
    fun `Name setter`() {
        val where: WhereDsl.() -> Unit = {
            Name = "one"
            Name = "two"
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                _Name + "=?",
                "AND " + _Name + "=?"
            ),
            whereObj.clauses
        )
        assertEquals(listOf("one", "two"), whereObj.args)
    }

    @Test
    fun `ID and Name setters mixed with other conditions`() {
        val where: WhereDsl.() -> Unit = {
            ID = 1L
            cInt equal 42
            Name = "test"
        }
        val whereObj = WhereDsl().apply(where)

        assertEquals(
            listOf(
                _ID + "=?",
                "AND " + cInt.name + "=?",
                "AND " + _Name + "=?"
            ),
            whereObj.clauses
        )
        assertEquals(listOf("1", "42", "test"), whereObj.args)
    }
}
