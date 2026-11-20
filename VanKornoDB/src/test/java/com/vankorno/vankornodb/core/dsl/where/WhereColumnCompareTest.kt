package com.vankorno.vankornodb.core.dsl.where

import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.core.dsl.testPredicate
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereColumnCompareTest {
    
    @Test
    fun `Equal columns`() {
        val whereObj = testPredicate {
            testColumn(iCol("intA")) equalCol testColumn(iCol("intB"))
            testColumn(lCol("longA")) equalCol testColumn(lCol("longB"))
            testColumn(fCol("floatA")) equalCol testColumn(fCol("floatB"))
            testColumn(sCol("strA")) equalCol testColumn(sCol("strB"))
            testColumn(bCol("boolA")) equalCol testColumn(bCol("boolB"))
        }

        assertEquals("intA=intB", whereObj.clauses[0])
        assertEquals("AND longA=longB", whereObj.clauses[1])
        assertEquals("AND floatA=floatB", whereObj.clauses[2])
        assertEquals("AND strA=strB", whereObj.clauses[3])
        assertEquals("AND boolA=boolB", whereObj.clauses[4])
    }

    @Test
    fun `Not equal columns`() {
        val whereObj = testPredicate {
            testColumn(iCol("intA")) notEqualCol testColumn(iCol("intB"))
            testColumn(lCol("longA")) notEqualCol testColumn(lCol("longB"))
            testColumn(fCol("floatA")) notEqualCol testColumn(fCol("floatB"))
            testColumn(sCol("strA")) notEqualCol testColumn(sCol("strB"))
            testColumn(bCol("boolA")) notEqualCol testColumn(bCol("boolB"))
        }

        assertEquals("intA!=intB", whereObj.clauses[0])
        assertEquals("AND longA!=longB", whereObj.clauses[1])
        assertEquals("AND floatA!=floatB", whereObj.clauses[2])
        assertEquals("AND strA!=strB", whereObj.clauses[3])
        assertEquals("AND boolA!=boolB", whereObj.clauses[4])
    }

    @Test
    fun `Greater, greaterEqual, less, lessEqual columns`() {
        val whereObj = testPredicate {
            testColumn(iCol("i1")) greaterCol testColumn(iCol("i2"))
            testColumn(lCol("l1")) greaterCol testColumn(lCol("l2"))
            testColumn(fCol("f1")) greaterCol testColumn(fCol("f2"))

            testColumn(iCol("i3")) greaterEqualCol testColumn(iCol("i4"))
            testColumn(lCol("l3")) greaterEqualCol testColumn(lCol("l4"))
            testColumn(fCol("f3")) greaterEqualCol testColumn(fCol("f4"))

            testColumn(iCol("i5")) lessCol testColumn(iCol("i6"))
            testColumn(lCol("l5")) lessCol testColumn(lCol("l6"))
            testColumn(fCol("f5")) lessCol testColumn(fCol("f6"))

            testColumn(iCol("i7")) lessEqualCol testColumn(iCol("i8"))
            testColumn(lCol("l7")) lessEqualCol testColumn(lCol("l8"))
            testColumn(fCol("f7")) lessEqualCol testColumn(fCol("f8"))
        }

        assertEquals("i1>i2", whereObj.clauses[0])
        assertEquals("AND l1>l2", whereObj.clauses[1])
        assertEquals("AND f1>f2", whereObj.clauses[2])

        assertEquals("AND i3>=i4", whereObj.clauses[3])
        assertEquals("AND l3>=l4", whereObj.clauses[4])
        assertEquals("AND f3>=f4", whereObj.clauses[5])

        assertEquals("AND i5<i6", whereObj.clauses[6])
        assertEquals("AND l5<l6", whereObj.clauses[7])
        assertEquals("AND f5<f6", whereObj.clauses[8])

        assertEquals("AND i7<=i8", whereObj.clauses[9])
        assertEquals("AND l7<=l8", whereObj.clauses[10])
        assertEquals("AND f7<=f8", whereObj.clauses[11])
    }
}
