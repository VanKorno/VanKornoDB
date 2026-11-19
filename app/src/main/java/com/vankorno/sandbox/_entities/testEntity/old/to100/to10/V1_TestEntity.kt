package com.vankorno.sandbox._entities.testEntity.old.to100.to10

import com.vankorno.vankornodb.api.DslContext
import com.vankorno.vankornodb.api.EntityDsl
import com.vankorno.vankornodb.api.OldEntity
import com.vankorno.vankornodb.api.withDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.getBoolean

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
    fun table(name: String) = (name using this).withDsl(::V1_TestDsl)
}


class V1_TestDsl internal constructor(context: DslContext<V1_TestDsl>) : EntityDsl<V1_TestEntity, V1_TestDsl>(context) {
    val Name get() = column(CV1_Test.Name)
    val Boo get() = column(CV1_Test.Boo)
    val Int1 get() = column(CV1_Test.Int1)
    val Str1 get() = column(CV1_Test.Str1)
    val Long1 get() = column(CV1_Test.Long1)
    val Float1 get() = column(CV1_Test.Float1)
    val Id get() = column(CV1_Test.Id)
}


private object CV1_Test {
    val Name = sCol("name", "")
    val Boo = bCol("boo", false)
    val Int1 = iCol("int1", 0)
    val Str1 = sCol("str1", "")
    val Long1 = lCol("long1", 0L)
    val Float1 = fCol("float1", 0F)
    val Id = lCol("id", -1L)
}
