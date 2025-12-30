package com.vankorno.sandbox.entities.testEntity.old.to100.to10

import com.vankorno.vankornodb.api.OldEntity
import com.vankorno.vankornodb.dbManagement.data.OldSchemaBundle
import com.vankorno.vankornodb.misc.getBoolean

data class V2_TestEntity(
                                    val id: Int = -1,
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





object OrmV2_Test : OldSchemaBundle<V2_TestEntity>(
    clazz = V2_TestEntity::class,

    getter = { cursor ->
        var idx = 0

        V2_TestEntity(
            id = cursor.getInt(idx++),
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
)