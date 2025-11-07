package com.vankorno.db.entities

import com.vankorno.db.entities.versions.old.to100.to10.V1_Version
import com.vankorno.vankornodb.dbManagement.migration.dsl.defineMigrations

fun migrationsVersions() = defineMigrations(
    EntityEnum.Versions.currVersion,
    EntityEnum.Versions.currClass
) {
    version(1, V1_Version::class)
}