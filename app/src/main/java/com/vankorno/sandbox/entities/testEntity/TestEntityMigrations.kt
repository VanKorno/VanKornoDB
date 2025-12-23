package com.vankorno.sandbox.entities.testEntity

import com.vankorno.sandbox.entities.EntityMeta
import com.vankorno.sandbox.entities.testEntity.old.to100.to10.SpecV1_Test
import com.vankorno.sandbox.entities.testEntity.old.to100.to10.SpecV2_Test
import com.vankorno.sandbox.entities.testEntity.old.to100.to10.SpecV3_Test
import com.vankorno.vankornodb.api.defineMigrations

fun migrationsTestEntity() = defineMigrations(EntityMeta.TestEntt) {
    version(1, SpecV1_Test)
    version(2, SpecV2_Test) { rename { CTest.Bool1 from "boo" to "bool1" } }
    version(3, SpecV3_Test)
    
}