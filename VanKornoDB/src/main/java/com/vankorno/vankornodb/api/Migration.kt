package com.vankorno.vankornodb.api
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.migration.DbMigratorInternal
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




/**
 * High-level entry point for running database migrations.
 *
 * This class handles migration logic for entities defined in your [allEntityMeta] enum,
 * which must implement [BaseEntityMeta]. It can be subclassed or extended with custom
 * migration methods for complex or multi-table scenarios.
 *
 * @property db the active [SQLiteDatabase] instance used for all migration operations.
 * @property allEntityMeta a collection of all entity metadata entries. Typically, this is your project's enum
 * that lists every entity type and its migration configuration. Example: DbMigrator(db, EntityMeta.entries)
 *
 * Call [migrateSingleTableEntities] to automatically migrate all entities limited to a single table
 * (those with a non-null [BaseEntityMeta.limitedToTable]).
 */
open class DbMigrator(                                               db: SQLiteDatabase,
                                                          allEntityMeta: Collection<BaseEntityMeta>,
) : DbMigratorInternal(db, allEntityMeta)




