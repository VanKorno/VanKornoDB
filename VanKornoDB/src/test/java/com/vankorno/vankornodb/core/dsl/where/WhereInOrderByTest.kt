package com.vankorno.vankornodb.core.dsl.where

import com.vankorno.vankornodb.api.OrderDsl
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
        val builder = OrderDsl().apply {
            When(
                orderWhen({ cPosition(); cName.flip() }) { cID equal 1 },
                orderWhen("RANDOM()") { cName notEqual "NotBob" },
                Else = { cID() }
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
        val score = fCol("score", 0f)
        
        val builder = OrderDsl().apply {
            When(
                orderWhen({ cPosition(); cName.flip() }) { cID equal 1 },
                orderWhen({ raw("RANDOM()") }) { cName notEqual "NotBob" },
                orderWhen({ score(); cID() }) { cActive equal true },
                Else = { cID() }
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
        val builder = OrderDsl().apply {
            When(
                orderWhen({ cPosition() }) { cID equal 42 }
            )
        }
        val sql = builder.buildStr()
        val expectedSql = "CASE WHEN id=? THEN position END"
        
        assertEquals(expectedSql, sql)
        assertEquals(listOf("42"), builder.args)
    }
    
    
    @Test
    fun `When with Else only`() {
        val builder = OrderDsl().apply {
            When(
                Else = { cID(); cName() }
            )
        }
        val sql = builder.buildStr()
        val expectedSql = "CASE ELSE id, name END"
        
        assertEquals(expectedSql, sql)
        assertEquals(emptyList<String>(), builder.args)
    }
    
    
    @Test
    fun `When with typed column in orderBy`() {
        val builder = OrderDsl().apply {
            When(
                orderWhen(cPosition) { cName equal "NotBob" },
                orderWhen(cType) { cActive equal true },
                orderWhen(iCol("yolo")) { cType equal "test" },
                Else = { cID() }
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
        val builder = OrderDsl().apply {
            When(
                orderWhen("ABS(position)+id DESC") { cActive equal true },
                orderWhen("RANDOM()") { cType equal "test" },
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