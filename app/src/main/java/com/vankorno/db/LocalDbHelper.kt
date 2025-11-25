package com.vankorno.db

import android.content.Context
import com.vankorno.db.MyApp.Companion.DbName
import com.vankorno.db.entities.EntityMeta
import com.vankorno.db.entities.TestTable
import com.vankorno.db.entities.testEntity.TestEntity
import com.vankorno.vankornodb.dbManagement.DbHelper
import com.vankorno.vankornodb.dbManagement.createTables
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.dbManagement.migration.DbMigrator

/**
 * Your db-helper that can be extended with ext.functions in separate repo files,
 * and you can add member functions to it if you want.
 * Also you can use DbHelper directly and make extension functions for that class in your repos,
 * if you don't wanna have a local wrapper class, like this one...
 * 
 * Can have a shorter name like Dbh to make repo code more concise ;)
 */
class LocalDbHelper(                                                      context: Context,
                                                                           dbName: String = DbName,
                                                                        dbVersion: Int = 1,
): DbHelper(context, dbName, dbVersion, EntityMeta.entries,
    onCreate = { db ->
        db.createTables(
            TableInfo(TestTable, TestEntity::class)
            
        )
    },
    onUpgrade = { db, oldVersion ->
        DbMigrator(db, EntityMeta.entries).migrateSingleTableEntities()
    }
)