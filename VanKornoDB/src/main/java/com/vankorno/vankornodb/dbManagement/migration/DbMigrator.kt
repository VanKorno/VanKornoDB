package com.vankorno.vankornodb.dbManagement.migration
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.api.migrateMultiStep
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.get.noty.getIntNoty
import com.vankorno.vankornodb.set.noty.setNoty

abstract class DbMigratorInternal(                                           val db: SQLiteDatabase,
                                              private val allEntityMeta: Collection<BaseEntityMeta>,
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
        val oldVer = db.getIntNoty(TABLE_EntityVersions, EntityVersion, _Name, entity.dbRowName)
        val newVer = entity.currVersion
        val tableName = entity.limitedToTable ?: return //\/\/\
        // region LOG
            Log.d(DbTAG, "migrateSingleTableEntity() table = $tableName, oldVer = $oldVer, newVer = $newVer")
        // endregion
        db.migrateMultiStep(
            table = tableName,
            oldVersion = oldVer,
            newVersion = newVer,
            migrationBundle = entity.migrationBundle.value
        )
        db.setNoty(newVer, TABLE_EntityVersions, EntityVersion, _Name, entity.dbRowName)
    }
    
    
    
    /**
     * Migrate multiple tables that use the same entity.
     */
    fun migrateTables(                                                  tables: List<String>,
                                                                    entityMeta: BaseEntityMeta,
                                                              doAfterEachTable: (String)->Unit = {},
    ) {
        val dbRowName = entityMeta.dbRowName
        // region LOG
            Log.d(DbTAG, "migrateTables(): Migrating tables that use the $dbRowName entity...")
        // endregion
        val oldVer = db.getIntNoty(TABLE_EntityVersions, EntityVersion, _Name, dbRowName)
        
        for (tableName in tables) {
            migrateTable(tableName, oldVer, entityMeta, doAfterEachTable)
        }
        val newVer = entityMeta.currVersion
        // region LOG
            Log.d(DbTAG, "migrateTables(): Finished migrating $dbRowName tables. Setting the new entity version ($newVer)...")
        // endregion
        db.setNoty(newVer, TABLE_EntityVersions, EntityVersion, _Name, dbRowName)
    }
    
    
    fun migrateTable(                                                    table: String,
                                                                        oldVer: Int,
                                                                    entityMeta: BaseEntityMeta,
                                                              doAfterEachTable: (String)->Unit = {},
    ) {
        val newVer = entityMeta.currVersion
        
        db.migrateMultiStep(table, oldVer, newVer, entityMeta.migrationBundle.value)
        
        doAfterEachTable(table)
    }
    
    
    
    
    
    
    
    
}