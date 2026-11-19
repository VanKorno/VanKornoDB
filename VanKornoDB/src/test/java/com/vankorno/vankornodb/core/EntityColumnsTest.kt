package com.vankorno.vankornodb.core

import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.buildColList
import com.vankorno.vankornodb.dbManagement.data.iListCol
import com.vankorno.vankornodb.dbManagement.data.sListCol
import com.vankorno.vankornodb.misc.data.SharedCol.cActive
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cRowId
import org.junit.Assert.assertEquals
import org.junit.Test

class EntityColumnsTest {
    
    @Test
    fun `Single columns added`() {
        val cols = buildColList {
            +cID
            +cName
        }

        assertEquals(2, cols.size)
        assertEquals(cID, cols[0])
        assertEquals(cName, cols[1])
    }
    
    
    @Test
    fun `List columns expanded`() {
        val cols = buildColList {
            +iListCol("num_", 3, 7)
        }

        assertEquals(3, cols.size)

        assertEquals("num_1", cols[0].name)
        assertEquals(7, (cols[0] as IntCol).defaultVal)

        assertEquals("num_2", cols[1].name)
        assertEquals("num_3", cols[2].name)
    }


    @Test
    fun `Mixed single and list columns`() {
        val cols = buildColList {
            +cRowId
            +sListCol("tag_", 2, "x")
            +cActive
        }

        assertEquals(4, cols.size)

        assertEquals(cRowId, cols[0])
        assertEquals("tag_1", cols[1].name)
        assertEquals("tag_2", cols[2].name)
        assertEquals(cActive, cols[3])
    }
}

