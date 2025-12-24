package com.vankorno.sandbox.entities.testEntity.old.to100.to10

import com.vankorno.vankornodb.api.OldEntity
import com.vankorno.vankornodb.dbManagement.data.OldOrmBundle

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



object SpecV2_Test : OldOrmBundle<V2_TestEntity>(
    clazz = V2_TestEntity::class
)