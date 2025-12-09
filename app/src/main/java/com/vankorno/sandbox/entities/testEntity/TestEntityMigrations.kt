package com.vankorno.sandbox.entities.testEntity

import com.vankorno.sandbox.entities.EntityMeta
import com.vankorno.sandbox.entities.testEntity.old.to100.to10.V1_TestEntity
import com.vankorno.vankornodb.api.defineMigrations

fun migrationsTestEntity() = defineMigrations(
    EntityMeta.TestEntt.currVersion,
    EntityMeta.TestEntt.currClass
) {
    version(1, V1_TestEntity::class)
}