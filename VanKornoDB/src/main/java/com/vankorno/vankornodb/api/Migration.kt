// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.api

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.dbManagement.migration.DbMigratorInternal
import com.vankorno.vankornodb.dbManagement.migration.data.MigrationBundle
import com.vankorno.vankornodb.dbManagement.migration.dropAndCreateEmptyTablesInternal
import com.vankorno.vankornodb.dbManagement.migration.dsl.MigrationDslInternal
import com.vankorno.vankornodb.dbManagement.migration.dsl.TransformColDslInternal
import com.vankorno.vankornodb.dbManagement.migration.dsl.defineMigrationsInternal
import com.vankorno.vankornodb.dbManagement.migration.migrateMultiStepInternal
import com.vankorno.vankornodb.dbManagement.migration.migrateWithoutChangeInternal

class MigrationDsl() : MigrationDslInternal()


fun defineMigrations(                                             entityMeta: BaseEntityMeta,
                                                                       block: MigrationDsl.()->Unit,
): MigrationBundle = defineMigrationsInternal(
    latestVersion = entityMeta.currVersion,
    latestSpec = entityMeta.currEntitySpec,
    block = block
)


fun <T: BaseEntity> defineMigrations(                          latestVersion: Int,
                                                                  latestSpec: EntitySpec<T>,
                                                                       block: MigrationDsl.()->Unit,
): MigrationBundle = defineMigrationsInternal(latestVersion, latestSpec, block)


class TransformColDsl() : TransformColDslInternal()


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




/**
 * Migrates the contents of a table through multiple versioned entity definitions and optional transformation lambdas.
 *
 * This function performs step-by-step data migration from [oldVersion] to [newVersion], converting each entity instance
 * according to the provided versioned classes, rename history, and transformation lambdas. The table is dropped,
 * recreated using the structure of the final version class, and repopulated with the migrated data.
 *
 * @param table The name of the table to migrate.
 * @param oldVersion The version of the entity currently stored in the table.
 * @param newVersion The target version of the entity to migrate to.
 * @param migrationBundle A bundle of versioned classes, rename history and milestone lambdas.
 * @param onNewDbFilled An optional callback invoked with the list of fully migrated objects after the table has been repopulated.
 *
 * @throws IllegalArgumentException if any expected entity class or migration lambda is missing.
 */
fun SQLiteDatabase.migrateMultiStep(                             table: String,
                                                            oldVersion: Int,
                                                            newVersion: Int,
                                                       migrationBundle: MigrationBundle,
                                                         onNewDbFilled: (List<BaseEntity>)->Unit = {},
) {
    this.migrateMultiStepInternal(
        table = table,
        oldVersion = oldVersion,
        newVersion = newVersion,
        versionedSpecs = migrationBundle.versionedSpecs,
        renameHistory = migrationBundle.renameHistory,
        milestones = migrationBundle.milestones,
        onNewDbFilled = onNewDbFilled,
    )
}




/**
 * Drops and recreates tables that don't need to be migrated.
 * Table content gets deleted.
 * 
 * @param tables A list of table names and entity data classes
 */
fun SQLiteDatabase.dropAndCreateEmptyTables(vararg tables: TableInfo) = this.dropAndCreateEmptyTablesInternal(*tables)


/**
 * Drops and recreates tables and their content without doing any real migrations.
 * Could be useful for things like switching from auto-incremented IDs to non-auto-incremented IDs, etc.
 * 
 * @param tables A list of table names and entity data classes
 */
fun SQLiteDatabase.migrateWithoutChange(vararg tables: TableInfo) = this.migrateWithoutChangeInternal(*tables)






