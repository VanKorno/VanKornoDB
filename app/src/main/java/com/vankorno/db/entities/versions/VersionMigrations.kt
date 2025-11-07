package com.vankorno.db.entities.versions

import com.vankorno.db.entities.EntityMeta
import com.vankorno.db.entities.versions.old.to100.to10.V1_Version
import com.vankorno.vankornodb.dbManagement.migration.dsl.defineMigrations

fun migrationsVersions() = defineMigrations(
    EntityMeta.Versions.currVersion,
    EntityMeta.Versions.currClass
) {
    version(1, V1_Version::class)
}