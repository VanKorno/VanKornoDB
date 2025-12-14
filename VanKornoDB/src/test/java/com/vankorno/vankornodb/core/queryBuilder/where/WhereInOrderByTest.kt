package com.vankorno.vankornodb.core.queryBuilder.where

import com.vankorno.vankornodb.api.OrderByBuilder
import com.vankorno.vankornodb.misc.data.SharedCol.shID
import com.vankorno.vankornodb.misc.data.SharedCol.shName
import com.vankorno.vankornodb.misc.data.SharedCol.shPosition
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereInOrderByTest {
    
    /*@Test
    fun `then() produces correct WhereAndOrder`() {
        val where: WhereBuilder.()->Unit = { shID equal 1 }
        val orderBy: OrderByBuilder.()->Unit = { shPosition(); shName.desc() }
        lateinit var wo: WhereOrder
        
        val orderByMeta: OrderByBuilder.()->Unit = {
            wo = where then orderBy
        }
        val builder = OrderByBuilder().apply(orderByMeta)
        
        assertEquals(1, wo.condition.clauses.size)
        assertEquals("id=?", wo.condition.buildStr())
        assertEquals(1, wo.condition.args.size)
        assertEquals(2, wo.order.orderoids.size)
        assertEquals("position", wo.order.orderoids[0])
        assertEquals("name DESC", wo.order.orderoids[1])
    }*/
    
    
    @Test
    fun `where() assembles CASE with multiple WhenAndOrders and else`() {
        val builder = OrderByBuilder().apply {
            where(
                orderWhen({ shPosition(); shName.desc() }) { shID equal 1 },
                orderWhen("RANDOM()") { shName notEqual "suka" },
                else_ = { shID() }
            )
        }
        
        val sql = builder.buildStr()
        val expectedSql =
            "CASE WHEN id=? THEN position, name DESC " +
            "WHEN name!=? THEN RANDOM() ELSE id END"
        
        assertEquals(expectedSql, sql)
        assertEquals(listOf("1", "suka"), builder.args)
    }
    
}