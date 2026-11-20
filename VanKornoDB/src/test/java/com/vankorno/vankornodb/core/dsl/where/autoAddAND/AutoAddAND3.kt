package com.vankorno.vankornodb.core.dsl.where.autoAddAND

import com.vankorno.vankornodb.core.dsl.BuilderWhere
import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.core.dsl.testPredicate
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AutoAddAND3 {

    private val intCol1 = testColumn(iCol("int1"))
    private val intCol2 = testColumn(iCol("int2"))
    private val strCol1 = testColumn(sCol("str1"))
    private val strCol2 = testColumn(sCol("str2"))
    private val boolCol1 = testColumn(bCol("bool1"))
    private val boolCol2 = testColumn(bCol("bool2"))
    private val longCol1 = testColumn(lCol("long1"))
    private val longCol2 = testColumn(lCol("long2"))
    private val floatCol1 = testColumn(fCol("float1"))
    private val floatCol2 = testColumn(fCol("float2"))

    @Test
    fun `Equal column`() {
        val where: BuilderWhere.() -> Unit = {
            intCol1 equalCol intCol2
            strCol1 equalCol strCol2
            boolCol1 equalCol boolCol2
            longCol1 equalCol longCol2
            floatCol1 equalCol floatCol2
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                intCol1.definition.name + "=" + intCol2.definition.name,
                "AND " + strCol1.definition.name + "=" + strCol2.definition.name,
                "AND " + boolCol1.definition.name + "=" + boolCol2.definition.name,
                "AND " + longCol1.definition.name + "=" + longCol2.definition.name,
                "AND " + floatCol1.definition.name + "=" + floatCol2.definition.name
            ),
            whereObj.clauses
        )
        assertTrue(whereObj.args.isEmpty())
    }

    @Test
    fun `Not equal column`() {
        val where: BuilderWhere.() -> Unit = {
            intCol1 notEqualCol intCol2
            strCol1 notEqualCol strCol2
            boolCol1 notEqualCol boolCol2
            longCol1 notEqualCol longCol2
            floatCol1 notEqualCol floatCol2
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                intCol1.definition.name + "!=" + intCol2.definition.name,
                "AND " + strCol1.definition.name + "!=" + strCol2.definition.name,
                "AND " + boolCol1.definition.name + "!=" + boolCol2.definition.name,
                "AND " + longCol1.definition.name + "!=" + longCol2.definition.name,
                "AND " + floatCol1.definition.name + "!=" + floatCol2.definition.name
            ),
            whereObj.clauses
        )
        assertTrue(whereObj.args.isEmpty())
    }

    @Test
    fun `Greater column`() {
        val where: BuilderWhere.() -> Unit = {
            intCol1 greaterCol intCol2
            longCol1 greaterCol longCol2
            floatCol1 greaterCol floatCol2
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                intCol1.definition.name + ">" + intCol2.definition.name,
                "AND " + longCol1.definition.name + ">" + longCol2.definition.name,
                "AND " + floatCol1.definition.name + ">" + floatCol2.definition.name
            ),
            whereObj.clauses
        )
        assertTrue(whereObj.args.isEmpty())
    }

    @Test
    fun `Greater equal column`() {
        val where: BuilderWhere.() -> Unit = {
            intCol1 greaterEqualCol intCol2
            longCol1 greaterEqualCol longCol2
            floatCol1 greaterEqualCol floatCol2
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                intCol1.definition.name + ">=" + intCol2.definition.name,
                "AND " + longCol1.definition.name + ">=" + longCol2.definition.name,
                "AND " + floatCol1.definition.name + ">=" + floatCol2.definition.name
            ),
            whereObj.clauses
        )
        assertTrue(whereObj.args.isEmpty())
    }

    @Test
    fun `Less column`() {
        val where: BuilderWhere.() -> Unit = {
            intCol1 lessCol intCol2
            longCol1 lessCol longCol2
            floatCol1 lessCol floatCol2
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                intCol1.definition.name + "<" + intCol2.definition.name,
                "AND " + longCol1.definition.name + "<" + longCol2.definition.name,
                "AND " + floatCol1.definition.name + "<" + floatCol2.definition.name
            ),
            whereObj.clauses
        )
        assertTrue(whereObj.args.isEmpty())
    }

    @Test
    fun `Less equal column`() {
        val where: BuilderWhere.() -> Unit = {
            intCol1 lessEqualCol intCol2
            longCol1 lessEqualCol longCol2
            floatCol1 lessEqualCol floatCol2
        }
        val whereObj = testPredicate(where)

        assertEquals(
            listOf(
                intCol1.definition.name + "<=" + intCol2.definition.name,
                "AND " + longCol1.definition.name + "<=" + longCol2.definition.name,
                "AND " + floatCol1.definition.name + "<=" + floatCol2.definition.name
            ),
            whereObj.clauses
        )
        assertTrue(whereObj.args.isEmpty())
    }
}
