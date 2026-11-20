package com.vankorno.vankornodb.core.dsl.where.noty

import com.vankorno.vankornodb.TestConstants.Posts
import com.vankorno.vankornodb.TestConstants.UserID
import com.vankorno.vankornodb.TestConstants.Users
import com.vankorno.vankornodb.core.data.DbConstants._ID
import com.vankorno.vankornodb.core.data.DbConstants._Name
import com.vankorno.vankornodb.core.data.DbConstants.comma
import com.vankorno.vankornodb.core.data.DbConstants.countAll
import com.vankorno.vankornodb.core.dsl.builderTable
import com.vankorno.vankornodb.core.dsl.query
import com.vankorno.vankornodb.core.dsl.raw
import org.junit.Assert
import org.junit.Test

class SubqueryTest {
    @Test
    fun `Query in query`() {
        val result = builderTable(Users).col { Name }.query(
            where = {
                builderTable(Posts).col { raw(countAll) }.query(
                    where = {
                        rawClause("$Posts.$UserID=?", "$Users.$_ID")
                    }
                ) greater 10
            }
        )
        
        Assert.assertEquals(
            "SELECT ${_Name} FROM ${Users} WHERE (SELECT COUNT(*) FROM ${Posts} WHERE ${Posts}.user_id=?)>?",
            result.query
        )
        Assert.assertEquals(
            arrayOf(
                "${Users}.${_ID}",
                "10"
            ).joinToString(comma),
            result.args.joinToString(comma)
        )
    }
    
}
