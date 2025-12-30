package com.vankorno.sandbox.entities.testEntity.old.to100.to10

import com.vankorno.vankornodb.api.OldEntity
import com.vankorno.vankornodb.dbManagement.data.OldSchemaBundle
import com.vankorno.vankornodb.misc.getBoolean

data class V1_TestEntity(
                                  val name: String = "",
                                   val boo: Boolean = false,
                                  val int1: Int = 0,
                                  val str1: String = "",
                                 val long1: Long = 0L,
                                val float1: Float = 0F,
                                    val id: Int = -1,
) : OldEntity





object SbV1_Test : OldSchemaBundle<V1_TestEntity>(
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
            id = cursor.getInt(idx++)
        )
    },
)