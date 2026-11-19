package com.vankorno.sandbox._entities.testEntity.lite

import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.api.LiteEntity
import com.vankorno.vankornodb.core.dsl.DslContext
import com.vankorno.vankornodb.core.dsl.EntityColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.core.dsl.withDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.getBoolean
import com.vankorno.vankornodb.set.dsl.SetDsl

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
) {
    fun table(name: String) = (name using this).withDsl(::TestLiteDsl, ::TestLiteOrderDsl, ::TestLiteExtraDsl, ::TestLiteSetDsl)
}


interface TestLiteDslColumns {
    val Id: EntityColumn<TestLiteDsl, LongCol>
    val Name: EntityColumn<TestLiteDsl, StrCol>
    val Position: EntityColumn<TestLiteDsl, LongCol>
    val Enabled: EntityColumn<TestLiteDsl, BoolCol>
}

class TestLiteOrderDsl internal constructor(                          columns: TestLiteDslColumns
) : OrderDsl<TestLiteDsl, TestLiteOrderDsl>(), TestLiteDslColumns by columns

class TestLiteExtraDsl internal constructor(                          columns: TestLiteDslColumns
) : ExtraDsl<TestLiteDsl>(), TestLiteDslColumns by columns

class TestLiteSetDsl(                                                 columns: TestLiteDslColumns
) : SetDsl<TestLiteDsl>(), TestLiteDslColumns by columns


class TestLiteDsl internal constructor(                     context: DslContext<TestLiteDsl>
) : WhereDsl<TestLiteDsl>(context), TestLiteDslColumns {
    override val Id get() = column(lCol("id", -1L))
    override val Name get() = column(sCol("name", ""))
    override val Position get() = column(lCol("position", 0L))
    override val Enabled get() = column(bCol("enabled", false))
}





private object CTestLite : EntityColumns {
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
