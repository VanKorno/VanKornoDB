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

data class V2_TestEntity(
                           override val id: Long = -1L,
                                  val name: String = "",
                              val position: String = "",
                               val enabled: Boolean = false,
                                  val int1: Int = 0,
                                  val int2: Int = 0,
                                  val int3: Int = 0,
                                  val str1: String = "",
                                  val str2: String = "",
                                  val str3: String = "",
                                 val bool1: Boolean = false,
                                 val bool2: Boolean = false,
                                 val bool3: Boolean = false,
                                 val long1: Long = 0L,
                                 val long2: Long = 0L,
                                 val long3: Long = 0L,
                                val float1: Float = 0F,
                                val float2: Float = 0F,
                                val float3: Float = 0F,
) : OldEntity





object _V2_Test : OldSchemaBundle<V2_TestEntity>(
    clazz = V2_TestEntity::class,

    getter = { cursor ->
        var idx = 0

        V2_TestEntity(
            id = cursor.getLong(idx++),
            name = cursor.getString(idx++),
            position = cursor.getString(idx++),
            enabled = cursor.getBoolean(idx++),
            int1 = cursor.getInt(idx++),
            int2 = cursor.getInt(idx++),
            int3 = cursor.getInt(idx++),
            str1 = cursor.getString(idx++),
            str2 = cursor.getString(idx++),
            str3 = cursor.getString(idx++),
            bool1 = cursor.getBoolean(idx++),
            bool2 = cursor.getBoolean(idx++),
            bool3 = cursor.getBoolean(idx++),
            long1 = cursor.getLong(idx++),
            long2 = cursor.getLong(idx++),
            long3 = cursor.getLong(idx++),
            float1 = cursor.getFloat(idx++),
            float2 = cursor.getFloat(idx++),
            float3 = cursor.getFloat(idx++)
        )
    },

    withId = { obj, newId -> obj.copy(id = newId) }
) {
    fun table(name: String) = (name using this).withDsl(::V2_TestDsl, ::V2_TestOrderDsl, ::V2_TestExtraDsl)
}


interface V2_TestDslColumns {
    val Id: EntityColumn<V2_TestDsl, LongCol>
    val Name: EntityColumn<V2_TestDsl, StrCol>
    val Position: EntityColumn<V2_TestDsl, StrCol>
    val Enabled: EntityColumn<V2_TestDsl, BoolCol>
    val Int1: EntityColumn<V2_TestDsl, IntCol>
    val Int2: EntityColumn<V2_TestDsl, IntCol>
    val Int3: EntityColumn<V2_TestDsl, IntCol>
    val Str1: EntityColumn<V2_TestDsl, StrCol>
    val Str2: EntityColumn<V2_TestDsl, StrCol>
    val Str3: EntityColumn<V2_TestDsl, StrCol>
    val Bool1: EntityColumn<V2_TestDsl, BoolCol>
    val Bool2: EntityColumn<V2_TestDsl, BoolCol>
    val Bool3: EntityColumn<V2_TestDsl, BoolCol>
    val Long1: EntityColumn<V2_TestDsl, LongCol>
    val Long2: EntityColumn<V2_TestDsl, LongCol>
    val Long3: EntityColumn<V2_TestDsl, LongCol>
    val Float1: EntityColumn<V2_TestDsl, FloatCol>
    val Float2: EntityColumn<V2_TestDsl, FloatCol>
    val Float3: EntityColumn<V2_TestDsl, FloatCol>
}

class V2_TestOrderDsl internal constructor(                            columns: V2_TestDslColumns
) : OrderDsl<V2_TestDsl, V2_TestOrderDsl>(), V2_TestDslColumns by columns

class V2_TestExtraDsl internal constructor(                            columns: V2_TestDslColumns
) : ExtraDsl<V2_TestDsl>(), V2_TestDslColumns by columns

class V2_TestSetDsl(                                                   columns: V2_TestDslColumns
) : SetDsl<V2_TestDsl>(), V2_TestDslColumns by columns


class V2_TestDsl internal constructor(                     context: DslContext<V2_TestDsl>
) : WhereDsl<V2_TestDsl>(context), V2_TestDslColumns {
    override val Id get() = column(lCol("id", -1L))
    override val Name get() = column(sCol("name", ""))
    override val Position get() = column(sCol("position", ""))
    override val Enabled get() = column(bCol("enabled", false))
    override val Int1 get() = column(iCol("int1", 0))
    override val Int2 get() = column(iCol("int2", 0))
    override val Int3 get() = column(iCol("int3", 0))
    override val Str1 get() = column(sCol("str1", ""))
    override val Str2 get() = column(sCol("str2", ""))
    override val Str3 get() = column(sCol("str3", ""))
    override val Bool1 get() = column(bCol("bool1", false))
    override val Bool2 get() = column(bCol("bool2", false))
    override val Bool3 get() = column(bCol("bool3", false))
    override val Long1 get() = column(lCol("long1", 0L))
    override val Long2 get() = column(lCol("long2", 0L))
    override val Long3 get() = column(lCol("long3", 0L))
    override val Float1 get() = column(fCol("float1", 0F))
    override val Float2 get() = column(fCol("float2", 0F))
    override val Float3 get() = column(fCol("float3", 0F))
}
