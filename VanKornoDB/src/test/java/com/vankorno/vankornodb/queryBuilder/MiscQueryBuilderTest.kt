package com.vankorno.vankornodb.queryBuilder

import com.vankorno.vankornodb.TestConstants.*
import com.vankorno.vankornodb.core.getQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class MiscQueryBuilderTest {
    
    @Test
    fun `App Table Getter query`() {
        val queryStr = "SELECT name FROM sqlite_master WHERE type=? AND name NOT LIKE ? AND name NOT IN (?, ?) ORDER BY name"
        
        val builtQuery = getQuery( // TODO un-duplicate maybe
            table = TABLE_Master,
            columns = arrayOf(Name),
            where = {
                Type equal DbTypeTable
                and { Name notLike "sqlite_%" }
                and { Name.equalNone(TABLE_Master, TABLE_EntityVersions) }
            },
            orderBy = Name
        ).first
        
        assertEquals(queryStr, builtQuery)
    }
}