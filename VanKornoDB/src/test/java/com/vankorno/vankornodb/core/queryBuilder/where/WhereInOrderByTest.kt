package com.vankorno.vankornodb.core.queryBuilder.where

import com.vankorno.vankornodb.api.OrderByBuilder
import com.vankorno.vankornodb.misc.data.SharedCol.shID
import com.vankorno.vankornodb.misc.data.SharedCol.shName
import com.vankorno.vankornodb.misc.data.SharedCol.shPosition
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereInOrderByTest {
    
    @Test
    fun `where() assembles CASE with multiple WhenAndOrders and else`() {
        val builder = OrderByBuilder().apply {
            When(
                orderWhen({ shPosition(); shName.desc() }) { shID equal 1 },
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
    
}