package com.vankorno.vankornodb.core.dsl.where.type_safe

import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereColumnComparTest {
    
    @Test
    fun `Equal columns`() {
        val whereObj = WhereBuilder().apply {
            iCol("intA") equalCol iCol("intB")
            lCol("longA") equalCol lCol("longB")
            fCol("floatA") equalCol fCol("floatB")
            sCol("strA") equalCol sCol("strB")
            bCol("boolA") equalCol bCol("boolB")
        }

        assertEquals(whereObj.clauses[0], "intA=intB")
        assertEquals(whereObj.clauses[1], "longA=longB")
        assertEquals(whereObj.clauses[2], "floatA=floatB")
        assertEquals(whereObj.clauses[3], "strA=strB")
        assertEquals(whereObj.clauses[4], "boolA=boolB")
    }

    @Test
    fun `Not equal columns`() {
        val whereObj = WhereBuilder().apply {
            iCol("intA") notEqualCol iCol("intB")
            lCol("longA") notEqualCol lCol("longB")
            fCol("floatA") notEqualCol fCol("floatB")
            sCol("strA") notEqualCol sCol("strB")
            bCol("boolA") notEqualCol bCol("boolB")
        }

        assertEquals(whereObj.clauses[0], "intA!=intB")
        assertEquals(whereObj.clauses[1], "longA!=longB")
        assertEquals(whereObj.clauses[2], "floatA!=floatB")
        assertEquals(whereObj.clauses[3], "strA!=strB")
        assertEquals(whereObj.clauses[4], "boolA!=boolB")
    }

    @Test
    fun `Greater, greaterEqual, less, lessEqual columns`() {
        val whereObj = WhereBuilder().apply {
            iCol("i1") greaterCol iCol("i2")
            lCol("l1") greaterCol lCol("l2")
            fCol("f1") greaterCol fCol("f2")

            iCol("i3") greaterEqualCol iCol("i4")
            lCol("l3") greaterEqualCol lCol("l4")
            fCol("f3") greaterEqualCol fCol("f4")

            iCol("i5") lessCol iCol("i6")
            lCol("l5") lessCol lCol("l6")
            fCol("f5") lessCol fCol("f6")

            iCol("i7") lessEqualCol iCol("i8")
            lCol("l7") lessEqualCol lCol("l8")
            fCol("f7") lessEqualCol fCol("f8")
        }

        assertEquals(whereObj.clauses[0], "i1>i2")
        assertEquals(whereObj.clauses[1], "l1>l2")
        assertEquals(whereObj.clauses[2], "f1>f2")

        assertEquals(whereObj.clauses[3], "i3>=i4")
        assertEquals(whereObj.clauses[4], "l3>=l4")
        assertEquals(whereObj.clauses[5], "f3>=f4")

        assertEquals(whereObj.clauses[6], "i5<i6")
        assertEquals(whereObj.clauses[7], "l5<l6")
        assertEquals(whereObj.clauses[8], "f5<f6")

        assertEquals(whereObj.clauses[9], "i7<=i8")
        assertEquals(whereObj.clauses[10], "l7<=l8")
        assertEquals(whereObj.clauses[11], "f7<=f8")
    }
}

