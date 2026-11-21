package com.vankorno.vankornodb.core.dsl.orderBy

import com.vankorno.vankornodb.core.dsl.testColumn
import com.vankorno.vankornodb.core.dsl.testOrder
import com.vankorno.vankornodb.dbManagement.data.sCol
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderByConvenienceTest {
    @Test
    fun `byName ascending`() {
        val order = testOrder {
            testColumn(sCol("name"))()
        }.buildStr()

        assertEquals("name", order)
    }

    @Test
    fun `byName descending`() {
        val order = testOrder {
            testColumn(sCol("name")).flip()
        }.buildStr()

        assertEquals("name DESC", order)
    }
}
