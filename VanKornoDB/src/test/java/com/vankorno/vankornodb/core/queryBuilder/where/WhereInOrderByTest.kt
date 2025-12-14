package com.vankorno.vankornodb.core.queryBuilder.where

import com.vankorno.vankornodb.api.OrderByBuilder
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.misc.data.SharedCol.shActive
import com.vankorno.vankornodb.misc.data.SharedCol.shID
import com.vankorno.vankornodb.misc.data.SharedCol.shName
import com.vankorno.vankornodb.misc.data.SharedCol.shPosition
import com.vankorno.vankornodb.misc.data.SharedCol.shType
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereInOrderByTest {
    
    @Test
    fun `where() assembles CASE with multiple WhenAndOrders and else`() {
        val builder = OrderByBuilder().apply {
            When(
                orderWhen({ shPosition(); shName.flip() }) { shID equal 1 },
                orderWhen("RANDOM()") { shName notEqual "NotBob" },
                Else = { shID() }
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
        
        val builder = OrderByBuilder().apply {
            When(
                orderWhen({ shPosition(); shName.flip() }) { shID equal 1 },
                orderWhen({ raw("RANDOM()") }) { shName notEqual "NotBob" },
                orderWhen({ score(); shID() }) { shActive equal true },
                Else = { shID() }
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
        val builder = OrderByBuilder().apply {
            When(
                orderWhen({ shPosition() }) { shID equal 42 }
            )
        }
        val sql = builder.buildStr()
        val expectedSql = "CASE WHEN id=? THEN position END"
        
        assertEquals(expectedSql, sql)
        assertEquals(listOf("42"), builder.args)
    }
    
    
    @Test
    fun `When with Else only`() {
        val builder = OrderByBuilder().apply {
            When(
                Else = { shID(); shName() }
            )
        }
        val sql = builder.buildStr()
        val expectedSql = "CASE ELSE id, name END"
        
        assertEquals(expectedSql, sql)
        assertEquals(emptyList<String>(), builder.args)
    }
    
    
    @Test
    fun `When with typed column in orderBy`() {
        val builder = OrderByBuilder().apply {
            When(
                orderWhen(shPosition) { shName equal "NotBob" },
                orderWhen(shType) { shActive equal true },
                orderWhen(iCol("yolo")) { shType equal "test" },
                Else = { shID() }
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
        val builder = OrderByBuilder().apply {
            When(
                orderWhen("ABS(position)+id DESC") { shActive equal true },
                orderWhen("RANDOM()") { shType equal "test" },
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