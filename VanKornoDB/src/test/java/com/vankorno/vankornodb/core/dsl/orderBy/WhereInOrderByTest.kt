package com.vankorno.vankornodb.core.dsl.orderBy

import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.core.dsl.testOrder
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.misc.data.SharedCol.cActive
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import com.vankorno.vankornodb.misc.data.SharedCol.cType
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereInOrderByTest {
    
    @Test
    fun `where() assembles CASE with multiple WhenAndOrders and else`() {
        val builder = testOrder {
            When(
                orderWhen({ testColumn(cPosition)(); testColumn(cName).flip() }) { testColumn(cID) equal 1 },
                orderWhen("RANDOM()") { testColumn(cName) notEqual "NotBob" },
                Else = { testColumn(cID)() }
            )
        }
        
        val sql = builder.buildStr()
        val expectedSql =
            "CASE WHEN id=? THEN position, name DESC " +
            "WHEN name!=? THEN RANDOM() ELSE id END"
        
        assertEquals(expectedSql, sql)
        assertEquals(listOf("1", "NotBob"), builder.args)
    }
    
    
    @Test
    fun `When with multiple OrderWhen entries`() {
        val score = testColumn(fCol("score", 0f))
        
        val builder = testOrder {
            When(
                orderWhen({ testColumn(cPosition)(); testColumn(cName).flip() }) { testColumn(cID) equal 1 },
                orderWhen({ raw("RANDOM()") }) { testColumn(cName) notEqual "NotBob" },
                orderWhen({ score(); testColumn(cID)() }) { testColumn(cActive) equal true },
                Else = { testColumn(cID)() }
            )
        }
        
        val sql = builder.buildStr()
        val expectedSql =
            "CASE WHEN id=? THEN position, name DESC " +
            "WHEN name!=? THEN RANDOM() " +
            "WHEN active=? THEN score, id ELSE id END"
        
        assertEquals(expectedSql, sql)
        assertEquals(listOf("1", "NotBob", "1"), builder.args)
    }
    
    
    @Test
    fun `When with single OrderWhen and no Else`() {
        val builder = testOrder {
            When(
                orderWhen({ testColumn(cPosition)() }) { testColumn(cID) equal 42 }
            )
        }
        val sql = builder.buildStr()
        val expectedSql = "CASE WHEN id=? THEN position END"
        
        assertEquals(expectedSql, sql)
        assertEquals(listOf("42"), builder.args)
    }
    
    
    @Test
    fun `When with Else only`() {
        val builder = testOrder {
            When(
                Else = { testColumn(cID)(); testColumn(cName)() }
            )
        }
        val sql = builder.buildStr()
        val expectedSql = "CASE ELSE id, name END"
        
        assertEquals(expectedSql, sql)
        assertEquals(emptyList<String>(), builder.args)
    }
    
    
    @Test
    fun `When with typed column in orderBy`() {
        val builder = testOrder {
            When(
                orderWhen(testColumn(cPosition)) { testColumn(cName) equal "NotBob" },
                orderWhen(testColumn(cType)) { testColumn(cActive) equal true },
                orderWhen(testColumn(iCol("yolo"))) { testColumn(cType) equal "test" },
                Else = { testColumn(cID)() }
            )
        }
        val sql = builder.buildStr()
        val expectedSql =   "CASE WHEN name=? THEN position " +
                            "WHEN active=? THEN type " +
                            "WHEN type=? THEN yolo ELSE id END"
        
        assertEquals(expectedSql, sql)
        assertEquals(listOf("NotBob", "1", "test"), builder.args)
    }
    
    
    @Test
    fun `When with raw string orders and multiple conditions`() {
        val builder = testOrder {
            When(
                orderWhen("ABS(position)+id DESC") { testColumn(cActive) equal true },
                orderWhen("RANDOM()") { testColumn(cType) equal "test" },
                Else = { raw("id*2") }
            )
        }
        val sql = builder.buildStr()
        val expectedSql =
            "CASE WHEN active=? THEN ABS(position)+id DESC " +
            "WHEN type=? THEN RANDOM() ELSE id*2 END"
        
        assertEquals(expectedSql, sql)
        assertEquals(listOf("1", "test"), builder.args)
    }
    
    
}



