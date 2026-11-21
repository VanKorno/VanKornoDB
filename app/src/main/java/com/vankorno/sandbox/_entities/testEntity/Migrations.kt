package com.vankorno.sandbox._entities.testEntity

import com.vankorno.sandbox._entities.EntityMeta
import com.vankorno.sandbox._entities.testEntity.old.to100.to10.V1_Test
import com.vankorno.sandbox._entities.testEntity.old.to100.to10.V2_Test
import com.vankorno.vankornodb.api.defineMigrations

fun migrationsTestEntity() = defineMigrations(EntityMeta.TestEntt) {
    version(1, V1_Test::class)
    version(2, V2_Test::class) { rename { "boo" to "bool1" } }
    version(3, TestEntity::class)
    
}
