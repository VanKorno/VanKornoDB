package com.vankorno.vankornodb.queryBuilder.where

import com.vankorno.vankornodb.TestConstants.*
import com.vankorno.vankornodb.misc.columns
import com.vankorno.vankornodb.core.getQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class SubqueryTest {
    @Test
    fun `Query in query`() {
        val result = getQuery(Users, columns(Name)) {
            where {
                subquery(Posts, columns(countAll)) {
                    where {
                        (Posts dot UserID)  equal  (Users dot ID)
                    }
                } greater 10
            }
        }
        
        assertEquals(
            "SELECT $Name FROM $Users WHERE (SELECT COUNT(*) FROM $Posts WHERE $Posts.user_id=?)>?",
            result.query
        )
        assertEquals(
            arrayOf("$Users.$ID", "10").joinToString(comma),
            result.args.joinToString(comma)
        )
    }
    
}