package com.vankorno.vankornodb.dbManagement.data

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.dbManagement.migration.data.MigrationBundle
import kotlin.reflect.KClass

/**
 * Base metadata interface for all entity definitions used in database migrations.
 *
 * Each entity type implements this interface to describe its current schema version,
 * class reference, and related migration configuration.
 *
 * @property currVersion the latest schema version of this entity. Used by the migration system
 * to determine whether upgrades are needed when comparing against stored DB versions.
 *
 * @property dbRowName the name under which meta-data for an entity is stored in the db (e.g. in the EntityVersions table).
 *
 * @property currClass the current KClass reference of the entity corresponding to [currVersion].
 * This is used for reflection-based mapping, schema generation, and version tracking.
 *
 * @property migrationBundle a lazily-initialized [MigrationBundle] that defines the full set
 * of migrations for this entity — including version transitions, rename maps, and milestone steps.
 *
 * @property limitedToTable if set, restricts this entity to a single table (the one whose name
 * matches this value). When null, the entity can be used by multiple tables. This enables
 * automatic migrations via a single call to `migrateSingleTableEntities()`.
 */
interface BaseEntityMeta {
    val currVersion: Int
    val dbRowName: String
    val currClass: KClass<out DbEntity>
    val migrationBundle: Lazy<MigrationBundle>
    val limitedToTable: String? get() = null
}