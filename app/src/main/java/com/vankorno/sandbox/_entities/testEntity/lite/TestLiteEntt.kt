package com.vankorno.sandbox._entities.testEntity.lite

import com.vankorno.vankornodb.api.*
import com.vankorno.vankornodb.dbManagement.data.*
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
) {
    fun table(name: String) = (name using this).withDsl(::TestLiteDsl, ::TestLiteOrderDsl, ::TestLiteExtraDsl)
}


interface TestLiteDslColumns {
    val Id: EntityColumn<TestLiteDsl, LongCol>
    val Name: EntityColumn<TestLiteDsl, StrCol>
    val Position: EntityColumn<TestLiteDsl, LongCol>
    val Enabled: EntityColumn<TestLiteDsl, BoolCol>
}

class TestLiteOrderDsl internal constructor(columns: TestLiteDslColumns) : EntityOrderDsl<TestLiteDsl>(), TestLiteDslColumns by columns
class TestLiteExtraDsl internal constructor(columns: TestLiteDslColumns) : EntityExtraDsl<TestLiteDsl>(), TestLiteDslColumns by columns

class TestLiteDsl internal constructor(context: DslContext<TestLiteDsl>) : EntityDsl<TestLiteEntt, TestLiteDsl>(context), TestLiteDslColumns {
    override val Id get() = column(CTestLite.Id)
    override val Name get() = column(CTestLite.Name)
    override val Position get() = column(CTestLite.Position)
    override val Enabled get() = column(CTestLite.Enabled)
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
