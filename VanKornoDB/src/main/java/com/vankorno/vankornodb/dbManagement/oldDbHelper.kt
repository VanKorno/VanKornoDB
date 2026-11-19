/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.add.addEntitiesWithoutIdCheck
import com.vankorno.vankornodb.add.addEntity
import com.vankorno.vankornodb.add.addEntityWithoutIdCheck
import com.vankorno.vankornodb.add.addWithNewIDsAndPositions
import com.vankorno.vankornodb.add.addWithNewIdAndPosition
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.DbLock
import com.vankorno.vankornodb.api.DbRuntime.dbLock
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicateInts
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicateLongs
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicatePosition
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicatePositionMult
import com.vankorno.vankornodb.delete.*
import com.vankorno.vankornodb.get.*
import com.vankorno.vankornodb.misc.data.DbIdAndPosition
import com.vankorno.vankornodb.newTable.createTable
import com.vankorno.vankornodb.newTable.createTables
import com.vankorno.vankornodb.set.*


@Suppress("NOTHING_TO_INLINE", "unused")
abstract class DbHelperInternal(
                                  dbName: String,
                               dbVersion: Int,
                                 context: Context? = null,
                              entityMeta: Collection<BaseEntityMeta>,
                   createExclusiveTables: Boolean = true,
                          mustHaveTables: List<TableInfo<out DbEntity>> = emptyList(),
                                    lock: DbLock = dbLock,
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
    onUpgrade = onUpgrade,
) {
    // ===================================  S E T T E R S  =================================== \\
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // =============================  M U L T I - S E T T E R S  ============================= \\
    
    // -----------------------------------  O B J E C T  ----------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.addEntity] */
    inline fun <T : DbEntity> addEntity(                            tableInfo: TableInfo<T>,
                                                                         entity: T,
    ): Long = readWrite(-1L, "addEntity") { db ->
        db.addEntity(tableInfo, entity)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addEntities] */
    inline fun <T : DbEntity> addEntities(                          tableInfo: TableInfo<T>,
                                                                       entities: List<T>,
    ): List<Long> = readWrite(emptyList(), "addEntities") { db ->
        db.addEntities(tableInfo, entities)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addEntities] */
    inline fun <T : DbEntity> addEntities(                          tableInfo: TableInfo<T>,
                                                                vararg entities: T,
    ): List<Long> = readWrite(emptyList(), "addEntities") { db ->
        db.addEntities(tableInfo, entities.toList())
    }
    
    /** A wrapper fun for [SQLiteDatabase.addEntityWithoutIdCheck] */
    inline fun <T : DbEntity> addEntityWithoutIdCheck(              tableInfo: TableInfo<T>,
                                                                         entity: T,
    ): Long = readWrite(-1L, "addEntityWithoutIdCheck") { db ->
        db.addEntityWithoutIdCheck(tableInfo, entity)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addEntitiesWithoutIdCheck] */
    inline fun <T : DbEntity> addEntitiesWithoutIdCheck(            tableInfo: TableInfo<T>,
                                                                       entities: List<T>,
    ): List<Long> = readWrite(emptyList(), "addEntitiesWithoutIdCheck") { db ->
        db.addEntitiesWithoutIdCheck(tableInfo, entities)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addWithNewIDsAndPositions] */
    fun <T : DbEntity> addWithNewIDsAndPositions(               tableInfo: TableInfo<T>,
                                                                   entities: List<T>,
                                                         positionConditions: WhereDsl.()->Unit = {},
                                                           setIdAndPosition: (T, Long, Long)->T,
    ): List<DbIdAndPosition> = readWrite(
        emptyList(), "addWithNewIDsAndPositions"
    ) { db ->
        db.addWithNewIDsAndPositions(tableInfo, entities, positionConditions, setIdAndPosition)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addWithNewIdAndPosition] */
    fun <T : DbEntity> addWithNewIdAndPosition(                 tableInfo: TableInfo<T>,
                                                                     entity: T,
                                                         positionConditions: WhereDsl.()->Unit = {},
                                                           setIdAndPosition: (T, Long, Long)->T,
    ): DbIdAndPosition = readWrite(
        DbIdAndPosition(), "addWithNewIdAndPosition"
    ) { db ->
        db.addWithNewIdAndPosition(tableInfo, entity, positionConditions, setIdAndPosition)
    }
    
    
    // ---------------------------------------  S E T  --------------------------------------- \\
    
    
    
    
    
    
    // ===================================   M O D I F Y   =================================== \\
    
    
    
    
    
    
    
    
    
    // ---------------------------------  F I X   D U P L I C A T E S  --------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.fixDuplicateLongs] */
    fun fixDuplicateLongs(                                            table: String,
                                                                     column: LongCol,
                                                                      where: W.()->Unit = {},
    ): Int = readWrite(0, "fixDuplicateLongs") { db ->
        db.fixDuplicateLongs(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.fixDuplicateInts] */
    fun fixDuplicateInts(                                             table: String,
                                                                     column: IntCol,
                                                                      where: W.()->Unit = {},
    ): Int = readWrite(0, "fixDuplicateInts") { db ->
        db.fixDuplicateInts(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.fixDuplicatePosition] */
    fun fixDuplicatePosition(                                         table: String,
                                                                      where: W.()->Unit = {},
    ): Int = readWrite(0, "fixDuplicatePosition") { db ->
        db.fixDuplicatePosition(table, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.fixDuplicatePosition] */
    fun fixDuplicatePositionMult(                             vararg tables: String,
                                                                      where: W.()->Unit = {},
    ): Int = readWrite(0, "fixDuplicatePositionMult") { db ->
        db.fixDuplicatePositionMult(*tables, where = where)
    }
    
    
    
    
    
    
    
    
    
    
    /** A wrapper fun for [SQLiteDatabase.createTable] */
    inline fun createTable(                            tableInfo: TableInfo<out DbEntity>,
                                                           async: Boolean = false,
    ) = write("createTable", async) { db ->
        db.createTable(tableInfo)
    }
    
    /** A wrapper fun for [SQLiteDatabase.createTables] */
    inline fun createTables(                       vararg tables: TableInfo<out DbEntity>,
                                                           async: Boolean = false,
    ) = write("createTables", async) { db ->
        db.createTables(*tables)
    }
    
    /** A wrapper fun for [SQLiteDatabase.createTables] */
    inline fun createTables(                        tables: List<TableInfo<out DbEntity>>,
                                                     async: Boolean = false,
    ) = write("createTables", async) { db ->
        db.createTables(tables)
    }
    
}
