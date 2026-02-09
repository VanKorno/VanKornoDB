// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.api

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.dbManagement.DbHelperInternal
import com.vankorno.vankornodb.dbManagement.DbLockInternal
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.newTable.createExclusiveTablesInternal
import com.vankorno.vankornodb.newTable.createTablesInternal


open class DbHelper(             context: Context,
                                  dbName: String,
                               dbVersion: Int,
                              entityMeta: Collection<BaseEntityMeta>,
                   createExclusiveTables: Boolean = true,
                                    lock: DbLock = DbLock(),
                                onCreate: (SQLiteDatabase)->Unit = {},
                               onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
) : DbHelperInternal(
    context = context,
    dbName = dbName,
    dbVersion = dbVersion,
    entityMeta = entityMeta,
    createExclusiveTables = createExclusiveTables,
    lock = lock,
    onCreate = onCreate,
    onUpgrade = onUpgrade
)


/**
 * An optional shorter name for DbHelper for those who prefer it.
 * The repos are basically files with extension functions on the DbHelper (unless you make your own wrapper),
 * so, having a shorter name will affect every fun in every repo. And some people might prefer a shorter,
 * but less descriptive name in this case.
 */
typealias Dbh = DbHelper



class DbLock {
    private val internalLock = DbLockInternal()
    
    fun <T> withLock(block: () -> T): T = internalLock.doLock { block() }
}




/**
 * Creates a single table in db.
 */
fun SQLiteDatabase.createTable(tableInfo: TableInfoNormal<out NormalEntity>) = createTablesInternal(tableInfo)


/**
 * Creates multiple tables in the database given vararg TableInfo (table name + schema bundle).
 */
fun SQLiteDatabase.createTables(                    vararg tables: TableInfoNormal<out NormalEntity>
) = createTablesInternal(*tables)

/**
 * Creates multiple tables in the database given a list of TableInfo (table name + schema bundle).
 */
fun SQLiteDatabase.createTables(                     tables: List<TableInfoNormal<out NormalEntity>>
) = createTablesInternal(*tables.toTypedArray())



/**
 * Creates tables for all single-table entities, that have EntityMeta.limitedToTable value set.
 * DbHelper/DbManager already runs this fun by default, if you don't disable it by setting the
 * 'createExclusiveTables' param to false.
 */
fun SQLiteDatabase.createExclusiveTables(                  allEntityMeta: Collection<BaseEntityMeta>
) = createExclusiveTablesInternal(allEntityMeta)



