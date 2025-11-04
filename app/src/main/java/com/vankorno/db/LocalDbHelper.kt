package com.vankorno.db

import android.content.Context
import com.vankorno.db.MyApp.Companion.DbName
import com.vankorno.db.entities.TABLE_Versions
import com.vankorno.db.entities.versions.VersionEntity
import com.vankorno.db.migrations.MigrateDb
import com.vankorno.vankornodb.dbManagement.DbHelper
import com.vankorno.vankornodb.dbManagement.createTables
import com.vankorno.vankornodb.dbManagement.data.TableInfo

class LocalDbHelper(                                                      context: Context,
                                                                           dbName: String = DbName,
                                                                        dbVersion: Int = 1
): DbHelper(context, dbName, dbVersion,
    onCreate = { db ->
        db.createTables(
            TableInfo(TABLE_Versions, VersionEntity::class),
            
            
        )
    },
    onUpgrade = { db, oldVersion ->
        MigrateDb(db).migrate()
    }
) {
}