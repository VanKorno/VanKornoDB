package com.vankorno.vankornodb.core.dsl.where.type_safe

import com.vankorno.vankornodb.api.WhereDsl
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
        val whereObj = WhereDsl().apply {
            iCol("intA") equalCol iCol("intB")
            lCol("longA") equalCol lCol("longB")
            fCol("floatA") equalCol fCol("floatB")
            sCol("strA") equalCol sCol("strB")
            bCol("boolA") equalCol bCol("boolB")
        }

        assertEquals("intA=intB", whereObj.clauses[0])
        assertEquals("longA=longB", whereObj.clauses[1])
        assertEquals("floatA=floatB", whereObj.clauses[2])
        assertEquals("strA=strB", whereObj.clauses[3])
        assertEquals("boolA=boolB", whereObj.clauses[4])
    }

    @Test
    fun `Not equal columns`() {
        val whereObj = WhereDsl().apply {
            iCol("intA") notEqualCol iCol("intB")
            lCol("longA") notEqualCol lCol("longB")
            fCol("floatA") notEqualCol fCol("floatB")
            sCol("strA") notEqualCol sCol("strB")
            bCol("boolA") notEqualCol bCol("boolB")
        }

        assertEquals("intA!=intB", whereObj.clauses[0])
        assertEquals("longA!=longB", whereObj.clauses[1])
        assertEquals("floatA!=floatB", whereObj.clauses[2])
        assertEquals("strA!=strB", whereObj.clauses[3])
        assertEquals("boolA!=boolB", whereObj.clauses[4])
    }

    @Test
    fun `Greater, greaterEqual, less, lessEqual columns`() {
        val whereObj = WhereDsl().apply {
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

        assertEquals("i1>i2", whereObj.clauses[0])
        assertEquals("l1>l2", whereObj.clauses[1])
        assertEquals("f1>f2", whereObj.clauses[2])

        assertEquals("i3>=i4", whereObj.clauses[3])
        assertEquals("l3>=l4", whereObj.clauses[4])
        assertEquals("f3>=f4", whereObj.clauses[5])

        assertEquals("i5<i6", whereObj.clauses[6])
        assertEquals("l5<l6", whereObj.clauses[7])
        assertEquals("f5<f6", whereObj.clauses[8])

        assertEquals("i7<=i8", whereObj.clauses[9])
        assertEquals("l7<=l8", whereObj.clauses[10])
        assertEquals("f7<=f8", whereObj.clauses[11])
    }
}
