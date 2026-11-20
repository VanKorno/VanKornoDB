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

class WhereEqualAnyTest {
    @Test
    fun `IntCol equalAny and notEqualAny`() {
        val whereObj = testPredicate {
            testColumn(iCol("intCol")).equalAnyOf(1, 2, 3)
            testColumn(iCol("intCol2")).notEqualAnyOf(4, 5)
        }
        assertEquals("intCol IN (?, ?, ?)", whereObj.clauses[0])
        assertEquals("1", whereObj.args[0])
        assertEquals("2", whereObj.args[1])
        assertEquals("3", whereObj.args[2])
        
        assertEquals("AND intCol2 NOT IN (?, ?)", whereObj.clauses[1])
        assertEquals("4", whereObj.args[3])
        assertEquals("5", whereObj.args[4])
    }
    
    @Test
    fun `StrCol equalAny and notEqualAny`() {
        val whereObj = testPredicate {
            testColumn(sCol("strCol")).equalAnyOf("foo", "bar")
            testColumn(sCol("strCol2")).notEqualAnyOf("baz")
        }
        assertEquals("strCol IN (?, ?)", whereObj.clauses[0])
        assertEquals("foo", whereObj.args[0])
        assertEquals("bar", whereObj.args[1])
        assertEquals("AND strCol2 NOT IN (?)", whereObj.clauses[1])
        assertEquals("baz", whereObj.args[2])
    }
    
    @Test
    fun `BoolCol equalAny and notEqualAny`() {
        val whereObj = testPredicate {
            testColumn(bCol("boolCol")).equalAnyOf(true, false)
            testColumn(bCol("boolCol2")).notEqualAnyOf(false)
        }
        assertEquals("boolCol IN (?, ?)", whereObj.clauses[0])
        assertEquals("1", whereObj.args[0])
        assertEquals("0", whereObj.args[1])
        assertEquals("AND boolCol2 NOT IN (?)", whereObj.clauses[1])
        assertEquals("0", whereObj.args[2])
    }
    
    @Test
    fun `LongCol equalAny and notEqualAny`() {
        val whereObj = testPredicate {
            testColumn(lCol("longCol")).equalAnyOf(10L, 20L)
            testColumn(lCol("longCol2")).notEqualAnyOf(30L)
        }
        assertEquals("longCol IN (?, ?)", whereObj.clauses[0])
        assertEquals("10", whereObj.args[0])
        assertEquals("20", whereObj.args[1])
        assertEquals("AND longCol2 NOT IN (?)", whereObj.clauses[1])
        assertEquals("30", whereObj.args[2])
    }
    
    @Test
    fun `FloatCol equalAny and notEqualAny`() {
        val whereObj = testPredicate {
            testColumn(fCol("floatCol")).equalAnyOf(1.1F, 2.2F)
            testColumn(fCol("floatCol2")).notEqualAnyOf(3.3F)
        }
        assertEquals("floatCol IN (?, ?)", whereObj.clauses[0])
        assertEquals("1.1", whereObj.args[0])
        assertEquals("2.2", whereObj.args[1])
        assertEquals("AND floatCol2 NOT IN (?)", whereObj.clauses[1])
        assertEquals("3.3", whereObj.args[2])
    }
}
