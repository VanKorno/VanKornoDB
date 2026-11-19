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
import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.DbLock
import com.vankorno.vankornodb.api.DbRuntime.dbLock
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.EntityTable
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
import com.vankorno.vankornodb.set.dsl.SetDsl


@Suppress("NOTHING_TO_INLINE", "unused")
abstract class DbHelperInternal(
                                  dbName: String,
                               dbVersion: Int,
                                 context: Context? = null,
                              entityMeta: Collection<BaseEntityMeta>,
                   createExclusiveTables: Boolean = true,
                          mustHaveTables: List<TableInfoNormal<out NormalEntity>> = emptyList(),
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
    
    /** A wrapper fun for [SQLiteDatabase.setInt] */
    fun setInt(                                                            value: Int,
                                                                           table: String,
                                                                          column: IntCol,
                                                                           async: Boolean = false,
                                                                           where: W.()->Unit,
    ) = write("setInt", async) { db ->
        db.setInt(value, table, column, where)
    }
    
    fun <W> setInt(                                                value: Int,
                                                                   table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                   async: Boolean = false,
                                                                   where: W.()->Unit,
    ) = write("setInt", async) { db ->
        db.setInt(value, table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.setStr] */
    fun setStr(                                                            value: String,
                                                                           table: String,
                                                                          column: StrCol,
                                                                           async: Boolean = false,
                                                                           where: W.()->Unit,
    ) = write("setStr", async) { db ->
        db.setStr(value, table, column, where)
    }
    
    fun <W> setStr(                                                value: String,
                                                                   table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, StrCol>,
                                                                   async: Boolean = false,
                                                                   where: W.()->Unit,
    ) = write("setStr", async) { db ->
        db.setStr(value, table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.setBool] */
    fun setBool(                                                           value: Boolean,
                                                                           table: String,
                                                                          column: BoolCol,
                                                                           async: Boolean = false,
                                                                           where: W.()->Unit,
    ) = write("setBool", async) { db ->
        db.setBool(value, table, column, where)
    }
    
    fun <W> setBool(                                              value: Boolean,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit,
    ) = write("setBool", async) { db ->
        db.setBool(value, table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.setLong] */
    fun setLong(                                                           value: Long,
                                                                           table: String,
                                                                          column: LongCol,
                                                                           async: Boolean = false,
                                                                           where: W.()->Unit,
    ) = write("setLong", async) { db ->
        db.setLong(value, table, column, where)
    }
    
    fun <W> setLong(                                              value: Long,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit,
    ) = write("setLong", async) { db ->
        db.setLong(value, table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.setFloat] */
    fun setFloat(                                                          value: Float,
                                                                           table: String,
                                                                          column: FloatCol,
                                                                           async: Boolean = false,
                                                                           where: W.()->Unit,
    ) = write("setFloat", async) { db ->
        db.setFloat(value, table, column, where)
    }
    
    fun <W> setFloat(                                            value: Float,
                                                                 table: EntityTable<*, W>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                                 async: Boolean = false,
                                                                 where: W.()->Unit,
    ) = write("setFloat", async) { db ->
        db.setFloat(value, table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.setBlob] */
    fun setBlob(                                                           value: ByteArray,
                                                                           table: String,
                                                                          column: BlobCol,
                                                                           async: Boolean = false,
                                                                           where: W.()->Unit,
    ) = write("setBlob", async) { db ->
        db.setBlob(value, table, column, where)
    }
    
    fun <W> setBlob(                                              value: ByteArray,
                                                                  table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BlobCol>,
                                                                  async: Boolean = false,
                                                                  where: W.()->Unit,
    ) = write("setBlob", async) { db ->
        db.setBlob(value, table, column, where)
    }
    
    
    
    
    // =============================  M U L T I - S E T T E R S  ============================= \\
    
    /** A wrapper fun for [SQLiteDatabase.set] */
    fun set(                                                          table: String,
                                                                      where: W.()->Unit = {},
                                                                      async: Boolean = false,
                                                                    actions: SetDsl.()->Unit,
    ) = write("set", async) { db ->
        db.set(table, where, actions)
    }
    
    fun <W> set(                                             table: EntityTable<*, W>,
                                                             where: W.()->Unit = {},
                                                             async: Boolean = false,
                                                           actions: SetDsl.()->Unit,
    ) = set(table.name, table.where(where), async, actions)
    
    
    
    // -----------------------------------  O B J E C T  ----------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.addEntity] */
    inline fun <T : CurrEntity> addEntity(                            tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
    ): Long = readWrite(-1L, "addEntity") { db ->
        db.addEntity(tableInfo, entity)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addEntities] */
    inline fun <T : CurrEntity> addEntities(                          tableInfo: TableInfoNormal<T>,
                                                                       entities: List<T>,
    ): List<Long> = readWrite(emptyList(), "addEntities") { db ->
        db.addEntities(tableInfo, entities)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addEntities] */
    inline fun <T : CurrEntity> addEntities(                          tableInfo: TableInfoNormal<T>,
                                                                vararg entities: T,
    ): List<Long> = readWrite(emptyList(), "addEntities") { db ->
        db.addEntities(tableInfo, entities.toList())
    }
    
    /** A wrapper fun for [SQLiteDatabase.addEntityWithoutIdCheck] */
    inline fun <T : CurrEntity> addEntityWithoutIdCheck(              tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
    ): Long = readWrite(-1L, "addEntityWithoutIdCheck") { db ->
        db.addEntityWithoutIdCheck(tableInfo, entity)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addEntitiesWithoutIdCheck] */
    inline fun <T : CurrEntity> addEntitiesWithoutIdCheck(            tableInfo: TableInfoNormal<T>,
                                                                       entities: List<T>,
    ): List<Long> = readWrite(emptyList(), "addEntitiesWithoutIdCheck") { db ->
        db.addEntitiesWithoutIdCheck(tableInfo, entities)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addWithNewIDsAndPositions] */
    fun <T : CurrEntity> addWithNewIDsAndPositions(               tableInfo: TableInfoNormal<T>,
                                                                   entities: List<T>,
                                                         positionConditions: WhereDsl.()->Unit = {},
                                                           setIdAndPosition: (T, Long, Long)->T,
    ): List<DbIdAndPosition> = readWrite(
        emptyList(), "addWithNewIDsAndPositions"
    ) { db ->
        db.addWithNewIDsAndPositions(tableInfo, entities, positionConditions, setIdAndPosition)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addWithNewIdAndPosition] */
    fun <T : CurrEntity> addWithNewIdAndPosition(                 tableInfo: TableInfoNormal<T>,
                                                                     entity: T,
                                                         positionConditions: WhereDsl.()->Unit = {},
                                                           setIdAndPosition: (T, Long, Long)->T,
    ): DbIdAndPosition = readWrite(
        DbIdAndPosition(), "addWithNewIdAndPosition"
    ) { db ->
        db.addWithNewIdAndPosition(tableInfo, entity, positionConditions, setIdAndPosition)
    }
    
    
    // ---------------------------------------  S E T  --------------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.setEntity] */
    fun <T : CurrEntity> setEntity(                                   tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
                                                                          where: W.()->Unit,
    ): Int = readWrite(0, "setEntity") { db ->
        db.setEntity(tableInfo, entity, where)
    }
    
    fun <T : CurrEntity, W> setEntity(                tableInfo: EntityTable<TableInfoNormal<T>, W>,
                                                         entity: T,
                                                          where: W.()->Unit,
    ): Int = setEntity(tableInfo.info, entity, tableInfo.where(where))
    
    
    
    
    
    // ===================================   M O D I F Y   =================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.addToInt] */
    fun addToInt(                                                    addend: Number,
                                                                      table: String,
                                                                     column: IntCol,
                                                                      async: Boolean = false,
                                                                      where: W.()->Unit = {},
    ) = write("addToInt", async) { db ->
        db.addToInt(addend, table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addToLong] */
    fun addToLong(                                                   addend: Number,
                                                                      table: String,
                                                                     column: LongCol,
                                                                      async: Boolean = false,
                                                                      where: W.()->Unit = {},
    ) = write("addToLong", async) { db ->
        db.addToLong(addend, table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addToFloat] */
    fun addToFloat(                                                  addend: Number,
                                                                      table: String,
                                                                     column: FloatCol,
                                                                      async: Boolean = false,
                                                                      where: W.()->Unit = {},
    ) = write("addToFloat", async) { db ->
        db.addToFloat(addend, table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.flipBool] */
    fun flipBool(                                                     table: String,
                                                                     column: BoolCol,
                                                                      async: Boolean = false,
                                                                      where: W.()->Unit = {},
    ) = write("flipBool", async) { db ->
        db.flipBool(table, column, where)
    }
    
    
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
    
    
    
    
    
    
    
    
    /** A wrapper fun for [SQLiteDatabase.reorder] */
    fun reorder(                                                      table: String,
                                                                         id: Long,
                                                               moveUpOrBack: Boolean,
                                                            makeFirstOrLast: Boolean = false,
                                                                      where: W.()->Unit = {},
    ): Boolean = readWrite(false, "reorder") { db ->
        db.reorder(table, id, moveUpOrBack, makeFirstOrLast, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.createTable] */
    inline fun createTable(                            tableInfo: TableInfoNormal<out NormalEntity>,
                                                           async: Boolean = false,
    ) = write("createTable", async) { db ->
        db.createTable(tableInfo)
    }
    
    /** A wrapper fun for [SQLiteDatabase.createTables] */
    inline fun createTables(                       vararg tables: TableInfoNormal<out NormalEntity>,
                                                           async: Boolean = false,
    ) = write("createTables", async) { db ->
        db.createTables(*tables)
    }
    
    /** A wrapper fun for [SQLiteDatabase.createTables] */
    inline fun createTables(                        tables: List<TableInfoNormal<out NormalEntity>>,
                                                     async: Boolean = false,
    ) = write("createTables", async) { db ->
        db.createTables(tables)
    }
    
}
