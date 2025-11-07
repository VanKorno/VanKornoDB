package com.vankorno.vankornodb.dbManagement.migration

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.getSet.getInt
import com.vankorno.vankornodb.getSet.set

/**
 * High-level entry point for running database migrations.
 *
 * This class handles migration logic for entities defined in your [entityMeta] enum,
 * which must implement [BaseEntityMeta]. It can be subclassed or extended with custom
 * migration methods for complex or multi-table scenarios.
 *
 * @property db the active [SQLiteDatabase] instance used for all migration operations.
 * @property entityMeta a collection of all entity metadata entries. Typically, this is your project's enum
 * that lists every entity type and its migration configuration. Example: DbMigrator(db, EntityMeta.entries)
 *
 * Call [migrateSingleTableEntities] to automatically migrate all entities limited to a single table
 * (those with a non-null [BaseEntityMeta.limitedToTable]).
 */
open class DbMigrator(                                            val db: SQLiteDatabase,
                                                  private val entityMeta: Collection<BaseEntityMeta>
) {
    /**
     * Automatically migrate all entities limited to a single table
     * (those with a non-null [BaseEntityMeta.limitedToTable]).
     */
    fun migrateSingleTableEntities() {
        for (entity in entityMeta) {
            if (entity.limitedToTable != null)
                migrateSingleTableEntity(entity)
        }
    }
    
    
    fun migrateSingleTableEntity(                                            entity: BaseEntityMeta
    ) {
        val oldVer = db.getInt(TABLE_EntityVersions, EntityVersion, Name, entity.dbRowName)
        val newVer = entity.currVersion
        val tableName = entity.limitedToTable ?: return //\/\/\
        // region LOG
            Log.d(DbTAG, "migrateSingleTableEntity() table = $tableName, oldVer = $oldVer, newVer = $newVer")
        // endregion
        db.migrateMultiStep(
            tableName = tableName,
            oldVersion = oldVer,
            newVersion = newVer,
            migrationBundle = entity.migrationBundle.value
        )
        db.set(newVer, TABLE_EntityVersions, EntityVersion, Name, entity.dbRowName)
    }
    
    
    
    
    
    
    
}