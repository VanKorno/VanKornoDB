package com.vankorno.sandbox._entities.testEntity.lite

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.core.dsl.*
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.getBoolean
import com.vankorno.vankornodb.set.dsl.SetDsl

data class TestLite(
                           override val id: Long = -1L,
                                  val name: String = "",
                              val position: Long = 0L,
                               val enabled: Boolean = false,
) : DbEntity





object _TestLite : SchemaBundle<TestLite>(
    clazz = TestLite::class,

    getter = { cursor ->
        var idx = 0

        TestLite(
            id = cursor.getLong(idx++),
            name = cursor.getString(idx++),
            position = cursor.getLong(idx++),
            enabled = cursor.getBoolean(idx++)
        )
    },

    setter = { e, cv ->
        cv.put("id", e.id)
        cv.put("name", e.name)
        cv.put("position", e.position)
        cv.put("enabled", e.enabled)
        cv
    },

    withId = { obj, newId -> obj.copy(id = newId) },

    columns = buildColList {
        +TestLiteFields.Id
        +TestLiteFields.Name
        +TestLiteFields.Position
        +TestLiteFields.Enabled
    },
) {
    fun tableNamed(name: String) = (name using this).withDsl(::TestLiteDsl, ::TestLiteOrderDsl, ::TestLiteExtraDsl, ::TestLiteSetDsl)
}


interface TestLiteDslColumns {
    val Id: Field<TestLiteDsl, LongCol>
    val Name: Field<TestLiteDsl, StrCol>
    val Position: Field<TestLiteDsl, LongCol>
    val Enabled: Field<TestLiteDsl, BoolCol>
}


class TestLiteOrderDsl internal constructor(columns: TestLiteDslColumns) : OrderDsl<TestLiteDsl, TestLiteOrderDsl>(), TestLiteDslColumns by columns

class TestLiteExtraDsl internal constructor(columns: TestLiteDslColumns) : ExtraDsl<TestLiteDsl>(), TestLiteDslColumns by columns

class TestLiteSetDsl(columns: TestLiteDslColumns) : SetDsl<TestLiteDsl>(), TestLiteDslColumns by columns

typealias TestLiteT = FullDslTable<TableInfo<TestLite>, TestLiteDsl, TestLiteOrderDsl, TestLiteExtraDsl, TestLiteSetDsl>


class TestLiteDsl internal constructor(                     context: DslContext<TestLiteDsl>
) : WhereDsl<TestLiteDsl>(context), TestLiteDslColumns {
    override val Id get() = column(TestLiteFields.Id)
    override val Name get() = column(TestLiteFields.Name)
    override val Position get() = column(TestLiteFields.Position)
    override val Enabled get() = column(TestLiteFields.Enabled)
}

private object TestLiteFields {
    val Id = lCol("id", -1L)
    val Name = sCol("name", "")
    val Position = lCol("position", 0L)
    val Enabled = bCol("enabled", false)
}
