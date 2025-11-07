package com.vankorno.db

import android.content.Context
import com.vankorno.db.MyApp.Companion.DbName
import com.vankorno.db.entities.EntityMeta
import com.vankorno.db.entities.versions.VersionEntity
import com.vankorno.vankornodb.core.DbConstants.TABLE_EntityVersions
import com.vankorno.vankornodb.dbManagement.DbHelper
import com.vankorno.vankornodb.dbManagement.createTables
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.dbManagement.migration.DbMigrator

class LocalDbHelper(                                                      context: Context,
                                                                           dbName: String = DbName,
                                                                        dbVersion: Int = 1
): DbHelper(context, dbName, dbVersion,
    onCreate = { db ->
        db.createTables(
            TableInfo(TABLE_EntityVersions, VersionEntity::class),
            
            
        )
    },
    onUpgrade = { db, oldVersion ->
        DbMigrator(db, EntityMeta.entries).migrateSingleTableEntities()
    }
) {
}