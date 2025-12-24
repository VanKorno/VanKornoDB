package com.vankorno.sandbox.entities.testEntity.old.to100.to10

import com.vankorno.vankornodb.api.OldEntity
import com.vankorno.vankornodb.dbManagement.data.OldOrmBundle

data class V1_TestEntity(
                                  val name: String = "",
                                   val boo: Boolean = false,
                                  val int1: Int = 0,
                                  val str1: String = "",
                                 val long1: Long = 0L,
                                val float1: Float = 0F,
                                    val id: Int = -1,
) : OldEntity



object OrmV1_Test : OldOrmBundle<V1_TestEntity>(
    clazz = V1_TestEntity::class
)