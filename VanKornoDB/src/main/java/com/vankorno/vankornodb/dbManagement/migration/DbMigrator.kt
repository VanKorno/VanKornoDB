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
open class DbMigrator(                                            val db: SQLiteDatabase,
                                               private val allEntityMeta: Collection<BaseEntityMeta>
) {
    /**
     * Automatically migrate all entities limited to a single table
     * (those with a non-null [BaseEntityMeta.limitedToTable]).
     */
    fun migrateSingleTableEntities() {
        // region LOG
            Log.d(DbTAG, "migrateSingleTableEntities() runs")
        // endregion
        for (entity in allEntityMeta) {
            if (entity.limitedToTable != null)
                migrateSingleTableEntity(entity)
        }
    }
    
    
    /**
     * Migrate one entity limited to a single table.
     * (with a non-null [BaseEntityMeta.limitedToTable]).
     */
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
    
    
    
    /**
     * Migrate multiple tables that use the same entity.
     */
    fun migrateTables(                                               tableNames: List<String>,
                                                                     entityMeta: BaseEntityMeta,
                                                               doAfterEachTable: (String)->Unit = {}
    ) {
        val dbRowName = entityMeta.dbRowName
        // region LOG
            Log.d(DbTAG, "migrateTables(): Migrating tables that use the $dbRowName entity...")
        // endregion
        val oldVer = db.getInt(TABLE_EntityVersions, EntityVersion, Name, dbRowName)
        
        for (tableName in tableNames) {
            migrateTable(tableName, oldVer, entityMeta, doAfterEachTable)
        }
        val newVer = entityMeta.currVersion
        // region LOG
            Log.d(DbTAG, "migrateTables(): Finished migrating $dbRowName tables. Setting the new entity version ($newVer)...")
        // endregion
        db.set(newVer, TABLE_EntityVersions, EntityVersion, Name, dbRowName)
    }
    
    
    fun migrateTable(                                                 tableName: String,
                                                                         oldVer: Int,
                                                                     entityMeta: BaseEntityMeta,
                                                               doAfterEachTable: (String)->Unit = {}
    ) {
        val newVer = entityMeta.currVersion
        
        db.migrateMultiStep(tableName, oldVer, newVer, entityMeta.migrationBundle.value)
        
        doAfterEachTable(tableName)
    }
    
    
    
    
    
    
    
    
}