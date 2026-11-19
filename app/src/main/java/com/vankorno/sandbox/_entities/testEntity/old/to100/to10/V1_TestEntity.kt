package com.vankorno.sandbox._entities.testEntity.old.to100.to10

import com.vankorno.vankornodb.api.OldEntity
import com.vankorno.vankornodb.core.dsl.DslContext
import com.vankorno.vankornodb.core.dsl.EntityColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.core.dsl.withDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.getBoolean
import com.vankorno.vankornodb.set.dsl.SetDsl

data class V1_TestEntity(
                                  val name: String = "",
                                   val boo: Boolean = false,
                                  val int1: Int = 0,
                                  val str1: String = "",
                                 val long1: Long = 0L,
                                val float1: Float = 0F,
                           override val id: Long = -1L,
) : OldEntity





object _V1_Test : OldSchemaBundle<V1_TestEntity>(
    clazz = V1_TestEntity::class,

    getter = { cursor ->
        var idx = 0

        V1_TestEntity(
            name = cursor.getString(idx++),
            boo = cursor.getBoolean(idx++),
            int1 = cursor.getInt(idx++),
            str1 = cursor.getString(idx++),
            long1 = cursor.getLong(idx++),
            float1 = cursor.getFloat(idx++),
            id = cursor.getLong(idx++)
        )
    },

    withId = { obj, newId -> obj.copy(id = newId) }
) {
    fun table(name: String) = (name using this).withDsl(::V1_TestDsl, ::V1_TestOrderDsl, ::V1_TestExtraDsl)
}


interface V1_TestDslColumns {
    val Name: EntityColumn<V1_TestDsl, StrCol>
    val Boo: EntityColumn<V1_TestDsl, BoolCol>
    val Int1: EntityColumn<V1_TestDsl, IntCol>
    val Str1: EntityColumn<V1_TestDsl, StrCol>
    val Long1: EntityColumn<V1_TestDsl, LongCol>
    val Float1: EntityColumn<V1_TestDsl, FloatCol>
    val Id: EntityColumn<V1_TestDsl, LongCol>
}

class V1_TestOrderDsl internal constructor(                            columns: V1_TestDslColumns
) : OrderDsl<V1_TestDsl, V1_TestOrderDsl>(), V1_TestDslColumns by columns

class V1_TestExtraDsl internal constructor(                            columns: V1_TestDslColumns
) : ExtraDsl<V1_TestDsl>(), V1_TestDslColumns by columns

class V1_TestSetDsl(                                                   columns: V1_TestDslColumns
) : SetDsl<V1_TestDsl>(), V1_TestDslColumns by columns


class V1_TestDsl internal constructor(                     context: DslContext<V1_TestDsl>
) : WhereDsl<V1_TestDsl>(context), V1_TestDslColumns {
    override val Name get() = column(sCol("name", ""))
    override val Boo get() = column(bCol("boo", false))
    override val Int1 get() = column(iCol("int1", 0))
    override val Str1 get() = column(sCol("str1", ""))
    override val Long1 get() = column(lCol("long1", 0L))
    override val Float1 get() = column(fCol("float1", 0F))
    override val Id get() = column(lCol("id", -1L))
}
