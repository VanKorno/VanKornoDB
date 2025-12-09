package com.vankorno.vankornodb.api

import com.vankorno.vankornodb.dbManagement.migration.data.MigrationBundle
import com.vankorno.vankornodb.dbManagement.migration.dsl.MigrationDefinitionBuilderInternal
import com.vankorno.vankornodb.dbManagement.migration.dsl.defineMigrationsInternal
import com.vankorno.vankornodb.getSet.DbEntity
import kotlin.reflect.KClass


class MigrationDefinitionBuilder() : MigrationDefinitionBuilderInternal()



fun <T: DbEntity> defineMigrations(              latestVersion: Int,
                                                   latestClass: KClass<T>,
                                                         block: MigrationDefinitionBuilder.()->Unit,
): MigrationBundle = defineMigrationsInternal(latestVersion, latestClass, block)









