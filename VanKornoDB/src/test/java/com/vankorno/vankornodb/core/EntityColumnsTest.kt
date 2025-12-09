package com.vankorno.vankornodb.core

import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.iListCol
import com.vankorno.vankornodb.dbManagement.data.sListCol
import com.vankorno.vankornodb.misc.data.SharedCol.shActive
import com.vankorno.vankornodb.misc.data.SharedCol.shID
import com.vankorno.vankornodb.misc.data.SharedCol.shName
import com.vankorno.vankornodb.misc.data.SharedCol.shRowID
import org.junit.Assert.assertEquals
import org.junit.Test

class EntityColumnsTest {
    
    @Test
    fun `Single columns added`() {
        val cols = object : EntityColumns {
            override val columns = buildColList {
                +shID
                +shName
            }
        }.columns

        assertEquals(2, cols.size)
        assertEquals(shID, cols[0])
        assertEquals(shName, cols[1])
    }
    
    
    @Test
    fun `List columns expanded`() {
        val cols = object : EntityColumns {
            override val columns = buildColList {
                +iListCol("num_", 3, 7)
            }
        }.columns

        assertEquals(3, cols.size)

        assertEquals("num_1", cols[0].name)
        assertEquals(7, (cols[0] as IntCol).defaultVal)

        assertEquals("num_2", cols[1].name)
        assertEquals("num_3", cols[2].name)
    }


    @Test
    fun `Mixed single and list columns`() {
        val cols = object : EntityColumns {
            override val columns = buildColList {
                +shRowID
                +sListCol("tag_", 2, "x")
                +shActive
            }
        }.columns

        assertEquals(4, cols.size)

        assertEquals(shRowID, cols[0])
        assertEquals("tag_1", cols[1].name)
        assertEquals("tag_2", cols[2].name)
        assertEquals(shActive, cols[3])
    }
}

