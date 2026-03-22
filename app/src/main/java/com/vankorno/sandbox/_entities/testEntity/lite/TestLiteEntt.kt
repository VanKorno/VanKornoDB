package com.vankorno.sandbox._entities.testEntity.lite

import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.api.LiteEntity
import com.vankorno.vankornodb.dbManagement.data.LiteSchemaBundle
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import com.vankorno.vankornodb.misc.getBoolean

data class TestLiteEntt(
                           override val id: Long = -1L,
                                  val name: String = "",
                              val position: Long = 0L,
                               val enabled: Boolean = false,
) : LiteEntity





object _TestLite : LiteSchemaBundle<TestLiteEntt>(
    clazz = TestLiteEntt::class,

    columns = CTestLite,

    getter = { cursor ->
        var idx = 0

        TestLiteEntt(
            id = cursor.getLong(idx++),
            name = cursor.getString(idx++),
            position = cursor.getLong(idx++),
            enabled = cursor.getBoolean(idx++)
        )
    },
)





object CTestLite : EntityColumns {
    val Id = lCol("id", -1L)
    val Name = sCol("name", "")
    val Position = lCol("position", 0L)
    val Enabled = bCol("enabled", false)

    override val columns = buildColList {
        +Id
        +Name
        +Position
        +Enabled
    }
}