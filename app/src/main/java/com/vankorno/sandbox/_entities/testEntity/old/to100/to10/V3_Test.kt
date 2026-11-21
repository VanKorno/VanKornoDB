package com.vankorno.sandbox._entities.testEntity.old.to100.to10

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.core.dsl.*
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.getBoolean
import com.vankorno.vankornodb.set.dsl.SetDsl

data class V3_Test(
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
) : DbEntity





object _V3_Test : SchemaBundle<V3_Test>(
    clazz = V3_Test::class,

    getter = { cursor ->
        var idx = 0

        V3_Test(
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

    setter = { e, cv ->
        cv.put("id", e.id)
        cv.put("name", e.name)
        cv.put("position", e.position)
        cv.put("enabled", e.enabled)
        cv.put("int1", e.int1)
        cv.put("int2", e.int2)
        cv.put("int3", e.int3)
        cv.put("str1", e.str1)
        cv.put("str2", e.str2)
        cv.put("str3", e.str3)
        cv.put("bool1", e.bool1)
        cv.put("bool2", e.bool2)
        cv.put("bool3", e.bool3)
        cv.put("long1", e.long1)
        cv.put("long2", e.long2)
        cv.put("long3", e.long3)
        cv.put("float1", e.float1)
        cv.put("float2", e.float2)
        cv.put("float3", e.float3)
        cv
    },

    withId = { obj, newId -> obj.copy(id = newId) },

    columns = buildColList {
        +V3_TestFields.Id
        +V3_TestFields.Name
        +V3_TestFields.Position
        +V3_TestFields.Enabled
        +V3_TestFields.Int1
        +V3_TestFields.Int2
        +V3_TestFields.Int3
        +V3_TestFields.Str1
        +V3_TestFields.Str2
        +V3_TestFields.Str3
        +V3_TestFields.Bool1
        +V3_TestFields.Bool2
        +V3_TestFields.Bool3
        +V3_TestFields.Long1
        +V3_TestFields.Long2
        +V3_TestFields.Long3
        +V3_TestFields.Float1
        +V3_TestFields.Float2
        +V3_TestFields.Float3
    },
) {
    fun tableNamed(name: String) = (name using this).withDsl(::V3_TestDsl, ::V3_TestOrderDsl, ::V3_TestExtraDsl, ::V3_TestSetDsl)
}


interface V3_TestDslColumns {
    val Id: Field<V3_TestDsl, LongCol>
    val Name: Field<V3_TestDsl, StrCol>
    val Position: Field<V3_TestDsl, LongCol>
    val Enabled: Field<V3_TestDsl, BoolCol>
    val Int1: Field<V3_TestDsl, IntCol>
    val Int2: Field<V3_TestDsl, IntCol>
    val Int3: Field<V3_TestDsl, IntCol>
    val Str1: Field<V3_TestDsl, StrCol>
    val Str2: Field<V3_TestDsl, StrCol>
    val Str3: Field<V3_TestDsl, StrCol>
    val Bool1: Field<V3_TestDsl, BoolCol>
    val Bool2: Field<V3_TestDsl, BoolCol>
    val Bool3: Field<V3_TestDsl, BoolCol>
    val Long1: Field<V3_TestDsl, LongCol>
    val Long2: Field<V3_TestDsl, LongCol>
    val Long3: Field<V3_TestDsl, LongCol>
    val Float1: Field<V3_TestDsl, FloatCol>
    val Float2: Field<V3_TestDsl, FloatCol>
    val Float3: Field<V3_TestDsl, FloatCol>
}


class V3_TestOrderDsl internal constructor(columns: V3_TestDslColumns) : OrderDsl<V3_TestDsl, V3_TestOrderDsl>(), V3_TestDslColumns by columns

class V3_TestExtraDsl internal constructor(columns: V3_TestDslColumns) : ExtraDsl<V3_TestDsl>(), V3_TestDslColumns by columns

class V3_TestSetDsl(columns: V3_TestDslColumns) : SetDsl<V3_TestDsl>(), V3_TestDslColumns by columns

typealias V3_TestT = FullDslTable<TableInfo<V3_Test>, V3_TestDsl, V3_TestOrderDsl, V3_TestExtraDsl, V3_TestSetDsl>


class V3_TestDsl internal constructor(                     context: DslContext<V3_TestDsl>
) : WhereDsl<V3_TestDsl>(context), V3_TestDslColumns {
    override val Id get() = column(V3_TestFields.Id)
    override val Name get() = column(V3_TestFields.Name)
    override val Position get() = column(V3_TestFields.Position)
    override val Enabled get() = column(V3_TestFields.Enabled)
    override val Int1 get() = column(V3_TestFields.Int1)
    override val Int2 get() = column(V3_TestFields.Int2)
    override val Int3 get() = column(V3_TestFields.Int3)
    override val Str1 get() = column(V3_TestFields.Str1)
    override val Str2 get() = column(V3_TestFields.Str2)
    override val Str3 get() = column(V3_TestFields.Str3)
    override val Bool1 get() = column(V3_TestFields.Bool1)
    override val Bool2 get() = column(V3_TestFields.Bool2)
    override val Bool3 get() = column(V3_TestFields.Bool3)
    override val Long1 get() = column(V3_TestFields.Long1)
    override val Long2 get() = column(V3_TestFields.Long2)
    override val Long3 get() = column(V3_TestFields.Long3)
    override val Float1 get() = column(V3_TestFields.Float1)
    override val Float2 get() = column(V3_TestFields.Float2)
    override val Float3 get() = column(V3_TestFields.Float3)
}

private object V3_TestFields {
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
