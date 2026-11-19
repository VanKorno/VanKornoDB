package com.vankorno.sandbox._entities.testEntity.old.to100.to10

import com.vankorno.vankornodb.api.DslContext
import com.vankorno.vankornodb.api.EntityDsl
import com.vankorno.vankornodb.api.OldEntity
import com.vankorno.vankornodb.api.withDsl
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
    fun table(name: String) = (name using this).withDsl(::V3_TestDsl)
}


class V3_TestDsl internal constructor(context: DslContext<V3_TestDsl>) : EntityDsl<V3_TestEntity, V3_TestDsl>(context) {
    val Id get() = column(CV3_Test.Id)
    val Name get() = column(CV3_Test.Name)
    val Position get() = column(CV3_Test.Position)
    val Enabled get() = column(CV3_Test.Enabled)
    val Int1 get() = column(CV3_Test.Int1)
    val Int2 get() = column(CV3_Test.Int2)
    val Int3 get() = column(CV3_Test.Int3)
    val Str1 get() = column(CV3_Test.Str1)
    val Str2 get() = column(CV3_Test.Str2)
    val Str3 get() = column(CV3_Test.Str3)
    val Bool1 get() = column(CV3_Test.Bool1)
    val Bool2 get() = column(CV3_Test.Bool2)
    val Bool3 get() = column(CV3_Test.Bool3)
    val Long1 get() = column(CV3_Test.Long1)
    val Long2 get() = column(CV3_Test.Long2)
    val Long3 get() = column(CV3_Test.Long3)
    val Float1 get() = column(CV3_Test.Float1)
    val Float2 get() = column(CV3_Test.Float2)
    val Float3 get() = column(CV3_Test.Float3)
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
