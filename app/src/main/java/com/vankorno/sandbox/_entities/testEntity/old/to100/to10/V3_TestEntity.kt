package com.vankorno.sandbox._entities.testEntity.old.to100.to10

import com.vankorno.vankornodb.api.*
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.getBoolean

data class V3_TestEntity(
                           override val id: Long = -1L,
                                  val name: String = "",
                              val position: Long = 0L,
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





object _V3_Test : OldSchemaBundle<V3_TestEntity>(
    clazz = V3_TestEntity::class,

    getter = { cursor ->
        var idx = 0

        V3_TestEntity(
            id = cursor.getLong(idx++),
            name = cursor.getString(idx++),
            position = cursor.getLong(idx++),
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
    fun table(name: String) = (name using this).withDsl(::V3_TestDsl, ::V3_TestOrderDsl, ::V3_TestExtraDsl)
}


interface V3_TestDslColumns {
    val Id: EntityColumn<V3_TestDsl, LongCol>
    val Name: EntityColumn<V3_TestDsl, StrCol>
    val Position: EntityColumn<V3_TestDsl, LongCol>
    val Enabled: EntityColumn<V3_TestDsl, BoolCol>
    val Int1: EntityColumn<V3_TestDsl, IntCol>
    val Int2: EntityColumn<V3_TestDsl, IntCol>
    val Int3: EntityColumn<V3_TestDsl, IntCol>
    val Str1: EntityColumn<V3_TestDsl, StrCol>
    val Str2: EntityColumn<V3_TestDsl, StrCol>
    val Str3: EntityColumn<V3_TestDsl, StrCol>
    val Bool1: EntityColumn<V3_TestDsl, BoolCol>
    val Bool2: EntityColumn<V3_TestDsl, BoolCol>
    val Bool3: EntityColumn<V3_TestDsl, BoolCol>
    val Long1: EntityColumn<V3_TestDsl, LongCol>
    val Long2: EntityColumn<V3_TestDsl, LongCol>
    val Long3: EntityColumn<V3_TestDsl, LongCol>
    val Float1: EntityColumn<V3_TestDsl, FloatCol>
    val Float2: EntityColumn<V3_TestDsl, FloatCol>
    val Float3: EntityColumn<V3_TestDsl, FloatCol>
}

class V3_TestOrderDsl internal constructor(columns: V3_TestDslColumns) : EntityOrderDsl<V3_TestDsl>(), V3_TestDslColumns by columns
class V3_TestExtraDsl internal constructor(columns: V3_TestDslColumns) : EntityExtraDsl<V3_TestDsl>(), V3_TestDslColumns by columns

class V3_TestDsl internal constructor(context: DslContext<V3_TestDsl>) : EntityDsl<V3_TestEntity, V3_TestDsl>(context), V3_TestDslColumns {
    override val Id get() = column(CV3_Test.Id)
    override val Name get() = column(CV3_Test.Name)
    override val Position get() = column(CV3_Test.Position)
    override val Enabled get() = column(CV3_Test.Enabled)
    override val Int1 get() = column(CV3_Test.Int1)
    override val Int2 get() = column(CV3_Test.Int2)
    override val Int3 get() = column(CV3_Test.Int3)
    override val Str1 get() = column(CV3_Test.Str1)
    override val Str2 get() = column(CV3_Test.Str2)
    override val Str3 get() = column(CV3_Test.Str3)
    override val Bool1 get() = column(CV3_Test.Bool1)
    override val Bool2 get() = column(CV3_Test.Bool2)
    override val Bool3 get() = column(CV3_Test.Bool3)
    override val Long1 get() = column(CV3_Test.Long1)
    override val Long2 get() = column(CV3_Test.Long2)
    override val Long3 get() = column(CV3_Test.Long3)
    override val Float1 get() = column(CV3_Test.Float1)
    override val Float2 get() = column(CV3_Test.Float2)
    override val Float3 get() = column(CV3_Test.Float3)
}


private object CV3_Test {
    val Id = lCol("id", -1L)
    val Name = sCol("name", "")
    val Position = lCol("position", 0L)
    val Enabled = bCol("enabled", false)
    val Int1 = iCol("int1", 0)
    val Int2 = iCol("int2", 0)
    val Int3 = iCol("int3", 0)
    val Str1 = sCol("str1", "")
    val Str2 = sCol("str2", "")
    val Str3 = sCol("str3", "")
    val Bool1 = bCol("bool1", false)
    val Bool2 = bCol("bool2", false)
    val Bool3 = bCol("bool3", false)
    val Long1 = lCol("long1", 0L)
    val Long2 = lCol("long2", 0L)
    val Long3 = lCol("long3", 0L)
    val Float1 = fCol("float1", 0F)
    val Float2 = fCol("float2", 0F)
    val Float3 = fCol("float3", 0F)
}
