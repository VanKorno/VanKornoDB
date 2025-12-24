package com.vankorno.sandbox.entities.testEntity

import com.vankorno.sandbox.entities.EntityMeta
import com.vankorno.sandbox.entities.testEntity.old.to100.to10.OrmV1_Test
import com.vankorno.sandbox.entities.testEntity.old.to100.to10.OrmV2_Test
import com.vankorno.sandbox.entities.testEntity.old.to100.to10.OrmV3_Test
import com.vankorno.vankornodb.api.defineMigrations

fun migrationsTestEntity() = defineMigrations(EntityMeta.TestEntt) {
    version(1, OrmV1_Test)
    version(2, OrmV2_Test) { rename { CTest.Bool1 from "boo" to "bool1" } }
    version(3, OrmV3_Test)
    
}