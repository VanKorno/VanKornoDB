package com.vankorno.sandbox

import android.content.Context
import com.vankorno.sandbox.entities.EntityMeta
import com.vankorno.sandbox.entities.TestTable
import com.vankorno.sandbox.entities.testEntity.SbTest
import com.vankorno.vankornodb.api.DbHelper
import com.vankorno.vankornodb.api.DbMigrator
import com.vankorno.vankornodb.api.createTables
import com.vankorno.vankornodb.dbManagement.data.TableInfo

const val DbName = "DbFile.dp"
const val DbVersion = 3

var dbFileNameFromDb = "" // useless shit for the demo


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
                                                                        dbVersion: Int = DbVersion,
): DbHelper(context, dbName, dbVersion, EntityMeta.entries,
    onCreate = { db ->
        db.createTables(
            TableInfo(TestTable, SbTest)
            
        )
    },
    onUpgrade = { db, oldVersion ->
        DbMigrator(db, EntityMeta.entries).migrateSingleTableEntities()
    }
)