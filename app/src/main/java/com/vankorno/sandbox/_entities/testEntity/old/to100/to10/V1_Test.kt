package com.vankorno.sandbox._entities.testEntity.old.to100.to10

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.core.dsl.DslContext
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.Field
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.core.dsl.withDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.getBoolean
import com.vankorno.vankornodb.set.dsl.SetDsl

data class V1_Test(
                                  val name: String = "",
                                   val boo: Boolean = false,
                                  val int1: Int = 0,
                                  val str1: String = "",
                                 val long1: Long = 0L,
                                val float1: Float = 0F,
                           override val id: Long = -1L,
) : DbEntity





object _V1_Test : SchemaBundle<V1_Test>(
    clazz = V1_Test::class,

    getter = { cursor ->
        var idx = 0

        V1_Test(
            name = cursor.getString(idx++),
            boo = cursor.getBoolean(idx++),
            int1 = cursor.getInt(idx++),
            str1 = cursor.getString(idx++),
            long1 = cursor.getLong(idx++),
            float1 = cursor.getFloat(idx++),
            id = cursor.getLong(idx++)
        )
    },

    setter = { e, cv ->
        cv.put("name", e.name)
        cv.put("boo", e.boo)
        cv.put("int1", e.int1)
        cv.put("str1", e.str1)
        cv.put("long1", e.long1)
        cv.put("float1", e.float1)
        cv.put("id", e.id)
        cv
    },

    withId = { obj, newId -> obj.copy(id = newId) },

    columns = buildColList {
        +V1_TestFields.Name
        +V1_TestFields.Boo
        +V1_TestFields.Int1
        +V1_TestFields.Str1
        +V1_TestFields.Long1
        +V1_TestFields.Float1
        +V1_TestFields.Id
    },
) {
    fun tableNamed(name: String) = (name using this).withDsl(::V1_TestDsl, ::V1_TestOrderDsl, ::V1_TestExtraDsl, ::V1_TestSetDsl)
}


interface V1_TestDslColumns {
    val Name: Field<V1_TestDsl, StrCol>
    val Boo: Field<V1_TestDsl, BoolCol>
    val Int1: Field<V1_TestDsl, IntCol>
    val Str1: Field<V1_TestDsl, StrCol>
    val Long1: Field<V1_TestDsl, LongCol>
    val Float1: Field<V1_TestDsl, FloatCol>
    val Id: Field<V1_TestDsl, LongCol>
}


class V1_TestOrderDsl internal constructor(columns: V1_TestDslColumns) : OrderDsl<V1_TestDsl, V1_TestOrderDsl>(), V1_TestDslColumns by columns

class V1_TestExtraDsl internal constructor(columns: V1_TestDslColumns) : ExtraDsl<V1_TestDsl>(), V1_TestDslColumns by columns

class V1_TestSetDsl(columns: V1_TestDslColumns) : SetDsl<V1_TestDsl>(), V1_TestDslColumns by columns


class V1_TestDsl internal constructor(                     context: DslContext<V1_TestDsl>
) : WhereDsl<V1_TestDsl>(context), V1_TestDslColumns {
    override val Name get() = column(V1_TestFields.Name)
    override val Boo get() = column(V1_TestFields.Boo)
    override val Int1 get() = column(V1_TestFields.Int1)
    override val Str1 get() = column(V1_TestFields.Str1)
    override val Long1 get() = column(V1_TestFields.Long1)
    override val Float1 get() = column(V1_TestFields.Float1)
    override val Id get() = column(V1_TestFields.Id)
}

private object V1_TestFields {
    val Name = sCol("name", "")
    val Boo = bCol("boo", false)
    val Int1 = iCol("int1", 0)
    val Str1 = sCol("str1", "")
    val Long1 = lCol("long1", 0L)
    val Float1 = fCol("float1", 0F)
    val Id = lCol("id", -1L)
}
