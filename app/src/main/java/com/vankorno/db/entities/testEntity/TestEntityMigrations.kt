package com.vankorno.db.entities.testEntity

import com.vankorno.db.entities.EntityMeta
import com.vankorno.db.entities.testEntity.old.to100.to10.V1_TestEntity
import com.vankorno.vankornodb.dbManagement.migration.dsl.defineMigrations

fun migrationsTestEntity() = defineMigrations(
    EntityMeta.TestEntt.currVersion,
    EntityMeta.TestEntt.currClass
) {
    version(1, V1_TestEntity::class)
}