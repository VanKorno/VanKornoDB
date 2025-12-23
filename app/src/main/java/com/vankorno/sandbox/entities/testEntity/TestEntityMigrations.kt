package com.vankorno.sandbox.entities.testEntity

import com.vankorno.sandbox.entities.EntityMeta
import com.vankorno.sandbox.entities.testEntity.old.to100.to10.V1_TestEntity
import com.vankorno.sandbox.entities.testEntity.old.to100.to10.V2_TestEntity
import com.vankorno.sandbox.entities.testEntity.old.to100.to10.V3_TestEntity
import com.vankorno.vankornodb.api.defineMigrations

fun migrationsTestEntity() = defineMigrations(EntityMeta.TestEntt) {
    version(1, V1_TestEntity::class)
    version(2, V2_TestEntity::class) { rename { CTest.Bool1 from "boo" to "bool1" } }
    version(3, V3_TestEntity::class)
    
}