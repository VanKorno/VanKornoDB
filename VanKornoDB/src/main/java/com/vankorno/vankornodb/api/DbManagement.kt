/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.api

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.dbManagement.DbHelperInternal
import com.vankorno.vankornodb.dbManagement.DbManager
import com.vankorno.vankornodb.dbManagement.DbReaderWriter
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.dbManagement.lock.DbLockInternal
import com.vankorno.vankornodb.misc.unitTestCase


/**
 * Db-Helper. The primary entry-point class for database operations, schema access, and query execution.
 *
 * It builds upon [DbReaderWriter], which handles thread-safe transactional read/write primitives
 * and unit test auto-mocking, which in turn extends [DbManager], which manages SQLite database lifecycle,
 * versioning, entity version tables, and automatic migrations.
 *
 * [DbHelperInternal] adds comprehensive high-level convenience wrappers and query/mutation helpers:
 * - **Under-the-Hood Delegation**: All helper functions cleanly delegate to base read/write execution primitives (`read`, `write`, `readWrite`) from [DbReaderWriter].
 * - **Consistent API Patterns**: Functions follow predictable argument ordering conventions, placing DSL builders and `where`-blocks last where applicable for clean DSL usage.
 * - **Async Support**: Write and setter functions provide an optional `async` parameter to launch operations asynchronously on `dbScope`.
 * - **Convenience APIs**: Includes wrappers for cursor traversal (`useCursor`, `getFromCursor`), single-value/column getters, and targeted property setters (`setInt`, `setStr`, etc.).
 */
open class Dbh(                   dbName: String,
                               dbVersion: Int,
                                 context: Context? = null,
                              entityMeta: Collection<BaseEntityMeta>,
                   createExclusiveTables: Boolean = true,
                          mustHaveTables: List<TableInfo<out DbEntity>> = emptyList(),
                                    lock: DbLock = DbLock(),
                                onCreate: (SQLiteDatabase)->Unit = {},
                               onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
) : DbReaderWriter(
    dbName = dbName,
    dbVersion = dbVersion,
    context = context,
    entityMeta = entityMeta,
    createExclusiveTables = createExclusiveTables,
    mustHaveTables = mustHaveTables,
    lock = lock,
    onCreate = onCreate,
    onUpgrade = onUpgrade
)




class DbLock {
    private val internalLock = DbLockInternal()
    
    fun <T> withLock(block: () -> T): T = internalLock.doLock { block() }
}


object DbRuntime {
    private var _dbh: Dbh? = null
    
    /**
     * Holds the main app DbHelper
     */
    var dbh: Dbh
        get() {
            if (_dbh == null && unitTestCase) {
                _dbh = Dbh(
                    dbName = ":memory:",
                    dbVersion = 1,
                    context = null,
                    entityMeta = emptyList(),
                    mustHaveTables = emptyList(),
                )
            }
            return _dbh ?: error("DbRuntime.dbh has not been initialized")
        }
        set(value) {
            _dbh = value
        }
    
    var dbLock = DbLock()
}







