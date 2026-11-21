/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.sandbox._entities

import com.vankorno.sandbox._entities.crashTester._CrashTester
import com.vankorno.sandbox._entities.crashTester.migrationsCrashTester
import com.vankorno.sandbox._entities.testEntity._TestEntity
import com.vankorno.sandbox._entities.testEntity.migrationsTestEntity
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.data.SchemaBundle
import com.vankorno.vankornodb.dbManagement.migration.data.MigrationBundle

const val DbName = "DbFile.db"
const val DbVersion = 3

/**
 * An example of the enum that contains all entity metadata needed for migrations.
 * It's made at the project level and uses BaseEntityMeta (library-level)
 * 
 * Each entity type implements this interface to describe its current schema version,
 * class reference, and related migration configuration.
 *
 * @property currVersion the latest schema version of this entity. Used by the migration system
 * to determine whether upgrades are needed when comparing against stored DB versions.
 *
 * @property dbRowName the name under which meta-data for an entity is stored in the db (e.g. in the EntityVersions table).
 *
 * @property schemaBundle the current KClass reference of the entity corresponding to [currVersion].
 * This is used for reflection-based mapping, schema generation, and version tracking.
 *
 * @property migrationBundle a lazily-initialized [MigrationBundle] that defines the full set
 * of migrations for this entity — including version transitions, rename maps, and milestone steps.
 *
 * @property limitedToTable if set, restricts this entity to a single table (the one whose name
 * matches this value). When null, the entity can be used by multiple tables. This enables
 * automatic migrations via a single call to `migrateSingleTableEntities()`.
 */
enum class EntityMeta(                              override val currVersion: Int,
                                                      override val dbRowName: String,
                                                   override val schemaBundle: SchemaBundle<*>,
                                                override val migrationBundle: Lazy<MigrationBundle>,
                                                 override val limitedToTable: String? = null,
): BaseEntityMeta {
    TestEntt(3, "TestEntity", _TestEntity, lazy { migrationsTestEntity() }, TestTableName),
    CrashTester(4, "CrashTester", _CrashTester, lazy { migrationsCrashTester() }),
    
}


const val TestTableName = "TestTable"
val TestTable = _TestEntity.tableNamed(TestTableName)
