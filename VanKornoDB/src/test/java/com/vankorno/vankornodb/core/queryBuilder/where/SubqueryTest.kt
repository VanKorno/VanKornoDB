package com.vankorno.vankornodb.core.queryBuilder.where

import com.vankorno.vankornodb.TestConstants
import com.vankorno.vankornodb.core.data.DbConstants
import com.vankorno.vankornodb.core.queryBuilder.getQuery
import com.vankorno.vankornodb.misc.columns
import org.junit.Assert
import org.junit.Test

class SubqueryTest {
    @Test
    fun `Query in query`() {
        val result = getQuery(TestConstants.Users, columns(DbConstants._Name)) {
            where {
                subquery(TestConstants.Posts, columns(DbConstants.countAll)) {
                    where {
                        (TestConstants.Posts dot TestConstants.UserID) equal (TestConstants.Users dot DbConstants._ID)
                    }
                } greater 10
            }
        }
        
        Assert.assertEquals(
            "SELECT ${DbConstants._Name} FROM ${TestConstants.Users} WHERE (SELECT COUNT(*) FROM ${TestConstants.Posts} WHERE ${TestConstants.Posts}.user_id=?)>?",
            result.query
        )
        Assert.assertEquals(
            arrayOf(
                "${TestConstants.Users}.${DbConstants._ID}",
                "10"
            ).joinToString(DbConstants.comma),
            result.args.joinToString(DbConstants.comma)
        )
    }
    
}