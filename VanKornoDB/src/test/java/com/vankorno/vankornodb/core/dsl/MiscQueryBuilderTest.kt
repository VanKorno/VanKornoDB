package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.DbConstants.*
import org.junit.Assert.assertEquals
import org.junit.Test

class MiscQueryBuilderTest {
    
    @Test
    fun `App Table Getter query`() {
        val queryStr = "SELECT name FROM sqlite_master WHERE type=? AND (name NOT LIKE ?) AND (name NOT IN (?, ?)) ORDER BY name"
        
        val builtQuery = builderTable(TABLE_Master).col { Name }.query(
            where = {
                Type equal DbTypeTable
                and { Name notLike "sqlite_%" }
                and { Name.notEqualAny(listOf(TABLE_AndroidMetadata, TABLE_EntityVersions)) }
            },
            orderBy = { raw(_Name) }
        ).query
        
        assertEquals(queryStr, builtQuery)
    }
}
