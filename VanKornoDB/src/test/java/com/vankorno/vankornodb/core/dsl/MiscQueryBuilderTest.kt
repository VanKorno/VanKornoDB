package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.TestConstants.*
import com.vankorno.vankornodb.misc.columns
import org.junit.Assert.assertEquals
import org.junit.Test

class MiscQueryBuilderTest {
    
    @Test
    fun `App Table Getter query`() {
        val queryStr = "SELECT name FROM sqlite_master WHERE type=? AND name NOT LIKE ? AND name NOT IN (?, ?) ORDER BY name"
        
        val builtQuery = getQuery(TABLE_Master, columns(_Name)) {
            where {
                _Type equal DbTypeTable
                and { _Name notLike "sqlite_%" }
                and { _Name.notEqualAny(TABLE_AndroidMetadata, TABLE_EntityVersions) }
            }
            orderByName()
        }.query
        
        assertEquals(queryStr, builtQuery)
    }
}