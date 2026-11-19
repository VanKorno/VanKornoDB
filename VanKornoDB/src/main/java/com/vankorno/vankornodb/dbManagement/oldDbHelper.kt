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
import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.api.LiteEntity
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.api.createTables
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.EntityTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicateInts
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicateLongs
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicatePosition
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicatePositionMult
import com.vankorno.vankornodb.delete.*
import com.vankorno.vankornodb.get.*
import com.vankorno.vankornodb.get.raw.data.RawTableStr
import com.vankorno.vankornodb.get.raw.getRawTableStr
import com.vankorno.vankornodb.misc.data.DbIdAndPosition
import com.vankorno.vankornodb.misc.orderByRowId
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
                                                                           where: WhereDsl.()->Unit,
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
                                                                           where: WhereDsl.()->Unit,
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
                                                                           where: WhereDsl.()->Unit,
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
                                                                           where: WhereDsl.()->Unit,
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
                                                                           where: WhereDsl.()->Unit,
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
                                                                           where: WhereDsl.()->Unit,
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
                                                                      where: WhereDsl.()->Unit = {},
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
                                                                          where: WhereDsl.()->Unit,
    ): Int = readWrite(0, "setEntity") { db ->
        db.setEntity(tableInfo, entity, where)
    }
    
    fun <T : CurrEntity, W> setEntity(                tableInfo: EntityTable<TableInfoNormal<T>, W>,
                                                         entity: T,
                                                          where: W.()->Unit,
    ): Int = setEntity(tableInfo.info, entity, tableInfo.where(where))
    
    
    
    
    
    // ==============================  G E T   E N T I T I E S  ============================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getEntity] */
    fun <T : BaseEntity> getEntity(                               tableInfo: TableInfoBase<T>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ): T? = read(null, "getEntity") { db ->
        db.getEntity(tableInfo, orderBy, extra, where)
    }
    
    fun <T : BaseEntity, W> getEntity(                  tableInfo: EntityTable<TableInfoBase<T>, W>,
                                                          orderBy: OrderDsl.()->Unit = {},
                                                            extra: ExtraDsl.()->Unit = {},
                                                            where: W.()->Unit,
    ): T? = getEntity(tableInfo.info, orderBy, extra, tableInfo.where(where))
    
    /** A wrapper fun for [SQLiteDatabase.getEntity] */
    fun <T : BaseEntity> getEntity(                               tableInfo: TableInfoBase<T>,
                                                                    default: T,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ): T = read(default, "getEntity") { db ->
        db.getEntity(tableInfo, default, orderBy, extra, where)
    }
    
    fun <T : BaseEntity, W> getEntity(                  tableInfo: EntityTable<TableInfoBase<T>, W>,
                                                          default: T,
                                                          orderBy: OrderDsl.()->Unit = {},
                                                            extra: ExtraDsl.()->Unit = {},
                                                            where: W.()->Unit,
    ): T = getEntity(tableInfo.info, default, orderBy, extra, tableInfo.where(where))
    
    
    
    /** A wrapper fun for [SQLiteDatabase.getFirstEntity] */
    fun <T : BaseEntity> getFirstEntity(                          tableInfo: TableInfoBase<out T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getFirstEntity") { db ->
        db.getFirstEntity(tableInfo, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getFirstEntity] */
    fun <T : BaseEntity> getFirstEntity(                          tableInfo: TableInfoBase<out T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
    ): T = read(default, "getFirstEntity") { db ->
        db.getFirstEntity(tableInfo, default, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getLastEntity] */
    fun <T : BaseEntity> getLastEntity(                           tableInfo: TableInfoBase<out T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getLastEntity") { db ->
        db.getLastEntity(tableInfo, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLastEntity] */
    fun <T : BaseEntity> getLastEntity(                           tableInfo: TableInfoBase<out T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
    ): T = read(default, "getLastEntity") { db ->
        db.getLastEntity(tableInfo, default, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getRandomEntity] */
    inline fun <reified T : BaseEntity> getRandomEntity(          tableInfo: TableInfoBase<out T>,
                                                             noinline where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getRandomEntity") { db ->
        db.getRandomEntity<T>(tableInfo, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRandomEntity] */
    inline fun <reified T : BaseEntity> getRandomEntity(          tableInfo: TableInfoBase<out T>,
                                                                    default: T,
                                                             noinline where: WhereDsl.()->Unit = {},
    ): T = read(default, "getRandomEntity") { db ->
        db.getRandomEntity<T>(tableInfo, default, where)
    }
    
    
    
    
    // =========================  M U L T I P L E   E N T I T I E S  ========================= \\
    
    /** A wrapper fun for [SQLiteDatabase.getEntities] */
    fun <T : BaseEntity> getEntities(                 tableInfo: TableInfoBase<T>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getEntities") { db ->
        db.getEntities(tableInfo, orderBy, extra, where)
    }
    
    fun <T : BaseEntity, W> getEntities(              tableInfo: EntityTable<TableInfoBase<T>, W>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: W.()->Unit = {},
    ): List<T> = getEntities(tableInfo.info, orderBy, extra, tableInfo.where(where))
    
    
    /** A wrapper fun for [SQLiteDatabase.getEntityMap] */
    fun <T : BaseEntity> getEntityMap(                tableInfo: TableInfoBase<T>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
    ): Map<Long, T> = read(emptyMap(), "getEntityMap") { db ->
        db.getEntityMap(tableInfo, orderBy, extra, where)
    }
    
    fun <T : BaseEntity, W> getEntityMap(             tableInfo: EntityTable<TableInfoBase<T>, W>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: W.()->Unit = {},
    ): Map<Long, T> = getEntityMap(tableInfo.info, orderBy, extra, tableInfo.where(where))
    
    
    // ===========================  E N T I T Y   P A I R S  =========================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getEntityPair] */
    fun <A : BaseEntity, B : BaseEntity> getEntityPair(                    tableA: TableInfoBase<A>,
                                                                           tableB: TableInfoBase<B>,
                                                                               id: Long,
    ): Pair<A, B>? = read(null, "getEntityPair") { db ->
        db.getEntityPair(tableA, tableB, id)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getEntityPair] */
    fun <A : BaseEntity, B : BaseEntity> getEntityPair(              tableA: TableInfoBase<A>,
                                                                     tableB: TableInfoBase<B>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ): Pair<A, B>? = read(null, "getEntityPair") { db ->
        db.getEntityPair(tableA, tableB, orderBy, extra, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getEntityPairs] */
    fun <A : BaseEntity, B : BaseEntity> getEntityPairs(
                                                         tableA: TableInfoBase<A>,
                                                         tableB: TableInfoBase<B>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
    ): List<Pair<A, B>> = read(emptyList(), "getEntityPairs") { db ->
        db.getEntityPairs(tableA, tableB, orderBy, extra, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getEntityPairMap] */
    fun <A : BaseEntity, B : BaseEntity> getEntityPairMap(
                                                         tableA: TableInfoBase<A>,
                                                         tableB: TableInfoBase<B>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
    ): Map<Long, Pair<A, B>> = read(emptyMap(), "getEntityPairMap") { db ->
        db.getEntityPairMap(tableA, tableB, orderBy, extra, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getEntityTriple] */
    fun <A : BaseEntity, B : BaseEntity, C : BaseEntity> getEntityTriple(
                                                                           tableA: TableInfoBase<A>,
                                                                           tableB: TableInfoBase<B>,
                                                                           tableC: TableInfoBase<C>,
                                                                               id: Long,
    ): Triple<A, B, C>? = read(null, "getEntityTriple") { db ->
        db.getEntityTriple(tableA, tableB, tableC, id)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getEntityTriple] */
    fun <A : BaseEntity, B : BaseEntity, C : BaseEntity> getEntityTriple(
                                                                     tableA: TableInfoBase<A>,
                                                                     tableB: TableInfoBase<B>,
                                                                     tableC: TableInfoBase<C>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ): Triple<A, B, C>? = read(null, "getEntityTriple") { db ->
        db.getEntityTriple(tableA, tableB, tableC, orderBy, extra, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getEntityTriples] */
    fun <A : BaseEntity, B : BaseEntity, C : BaseEntity> getEntityTriples(
                                                         tableA: TableInfoBase<A>,
                                                         tableB: TableInfoBase<B>,
                                                         tableC: TableInfoBase<C>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
    ): List<Triple<A, B, C>> = read(emptyList(), "getEntityTriples") { db ->
        db.getEntityTriples(tableA, tableB, tableC, orderBy, extra, where)
    }
    
    
    
    // =============================  L I T E   E N T I T I E S  ============================= \\
    
    /** A wrapper fun for [SQLiteDatabase.getLiteEntity] */
    fun <T : LiteEntity> getLiteEntity(                           tableInfo: TableInfoLite<T>,
                                                            originalColumns: EntityColumns,
                                                                    default: T,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ): T = read(default, "getLiteEntity") { db ->
        db.getLiteEntity(tableInfo, originalColumns, default, orderBy, extra, where)
    }
    
    fun <T : LiteEntity, W> getLiteEntity(              tableInfo: EntityTable<TableInfoLite<T>, W>,
                                                  originalColumns: EntityColumns,
                                                          default: T,
                                                          orderBy: OrderDsl.()->Unit = {},
                                                            extra: ExtraDsl.()->Unit = {},
                                                            where: W.()->Unit,
    ): T = getLiteEntity(tableInfo.info, originalColumns, default, orderBy, extra, tableInfo.where(where))
    
    /** A wrapper fun for [SQLiteDatabase.getLiteEntities] */
    fun <T : LiteEntity> getLiteEntities(             tableInfo: TableInfoLite<T>,
                                                originalColumns: EntityColumns,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getLiteEntities") { db ->
        db.getLiteEntities(tableInfo, originalColumns, orderBy, extra, where)
    }
    
    fun <T : LiteEntity, W> getLiteEntities(          tableInfo: EntityTable<TableInfoLite<T>, W>,
                                                originalColumns: EntityColumns,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: W.()->Unit = {},
    ): List<T> = getLiteEntities(tableInfo.info, originalColumns, orderBy, extra, tableInfo.where(where))
    
    
    
    
    
    
    
    // ==============================   C O L U M N   V A L s   ============================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getColInts] */
    fun getColInts(                                                   table: String,
                                                                     column: IntCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColInts") { db ->
        db.getColInts(table, column, orderBy, extra, where)
    }
    
    fun <W> getColInts(                                            table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                 orderBy: OrderDsl.()->Unit = {},
                                                                   extra: ExtraDsl.()->Unit = {},
                                                                   where: W.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColInts") { db ->
        db.getColInts(table, column, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getColStrings] */
    fun getColStrings(                                                table: String,
                                                                     column: StrCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStrings") { db ->
        db.getColStrings(table, column, orderBy, extra, where)
    }
    
    fun <W> getColStrings(                                         table: EntityTable<*, W>,
                                                                  column: ColumnSelector<W, StrCol>,
                                                                 orderBy: OrderDsl.()->Unit = {},
                                                                   extra: ExtraDsl.()->Unit = {},
                                                                   where: W.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStrings") { db ->
        db.getColStrings(table, column, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getColBools] */
    fun getColBools(                                                  table: String,
                                                                     column: BoolCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBools") { db ->
        db.getColBools(table, column, orderBy, extra, where)
    }
    
    fun <W> getColBools(                                          table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                orderBy: OrderDsl.()->Unit = {},
                                                                  extra: ExtraDsl.()->Unit = {},
                                                                  where: W.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBools") { db ->
        db.getColBools(table, column, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getColLongs] */
    fun getColLongs(                                                  table: String,
                                                                     column: LongCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongs") { db ->
        db.getColLongs(table, column, orderBy, extra, where)
    }
    
    fun <W> getColLongs(                                          table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                orderBy: OrderDsl.()->Unit = {},
                                                                  extra: ExtraDsl.()->Unit = {},
                                                                  where: W.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongs") { db ->
        db.getColLongs(table, column, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getColFloats] */
    fun getColFloats(                                                 table: String,
                                                                     column: FloatCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloats") { db ->
        db.getColFloats(table, column, orderBy, extra, where)
    }
    
    fun <W> getColFloats(                                        table: EntityTable<*, W>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                               orderBy: OrderDsl.()->Unit = {},
                                                                 extra: ExtraDsl.()->Unit = {},
                                                                 where: W.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloats") { db ->
        db.getColFloats(table, column, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getColBlobs] */
    fun getColBlobs(                                                  table: String,
                                                                     column: BlobCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobs") { db ->
        db.getColBlobs(table, column, orderBy, extra, where)
    }
    
    fun <W> getColBlobs(                                          table: EntityTable<*, W>,
                                                                 column: ColumnSelector<W, BlobCol>,
                                                                orderBy: OrderDsl.()->Unit = {},
                                                                  extra: ExtraDsl.()->Unit = {},
                                                                  where: W.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobs") { db ->
        db.getColBlobs(table, column, orderBy, extra, where)
    }
    
    
    
    
    // =================================   R O W   V A L s   ================================= \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowInts] */
    fun getRowInts(                                                   table: String,
                                                                    columns: Array<IntCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getRowInts") { db ->
        db.getRowInts(table, columns, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getRowStrings] */
    fun getRowStrings(                                                table: String,
                                                                    columns: Array<StrCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStrings") { db ->
        db.getRowStrings(table, columns, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getRowBools] */
    fun getRowBools(                                                  table: String,
                                                                    columns: Array<BoolCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBools") { db ->
        db.getRowBools(table, columns, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getRowLongs] */
    fun getRowLongs(                                                  table: String,
                                                                    columns: Array<LongCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongs") { db ->
        db.getRowLongs(table, columns, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getRowFloats] */
    fun getRowFloats(                                                 table: String,
                                                                    columns: Array<FloatCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloats") { db ->
        db.getRowFloats(table, columns, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getRowBlobs] */
    fun getRowBlobs(                                                  table: String,
                                                                    columns: Array<BlobCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobs") { db ->
        db.getRowBlobs(table, columns, orderBy, extra, where)
    }
    
    
    
    
    
    
    // ===============================   T A B L E   V A L s   =============================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableInts] */
    fun getTableInts(                                                 table: String,
                                                                    columns: Array<IntCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Int>> = read(emptyList(), "getTableInts") { db ->
        db.getTableInts(table, columns, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getTableStrings] */
    fun getTableStrings(                                              table: String,
                                                                    columns: Array<StrCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStrings") { db ->
        db.getTableStrings(table, columns, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getTableBools] */
    fun getTableBools(                                                table: String,
                                                                    columns: Array<BoolCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBools") { db ->
        db.getTableBools(table, columns, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getTableLongs] */
    fun getTableLongs(                                                table: String,
                                                                    columns: Array<LongCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongs") { db ->
        db.getTableLongs(table, columns, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getTableFloats] */
    fun getTableFloats(                                               table: String,
                                                                    columns: Array<FloatCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloats") { db ->
        db.getTableFloats(table, columns, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getTableBlobs] */
    fun getTableBlobs(                                                table: String,
                                                                    columns: Array<BlobCol>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobs") { db ->
        db.getTableBlobs(table, columns, orderBy, extra, where)
    }
    
    
    
    
    
    
    // ===================================   M O D I F Y   =================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.addToInt] */
    fun addToInt(                                                    addend: Number,
                                                                      table: String,
                                                                     column: IntCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("addToInt", async) { db ->
        db.addToInt(addend, table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addToLong] */
    fun addToLong(                                                   addend: Number,
                                                                      table: String,
                                                                     column: LongCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("addToLong", async) { db ->
        db.addToLong(addend, table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addToFloat] */
    fun addToFloat(                                                  addend: Number,
                                                                      table: String,
                                                                     column: FloatCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("addToFloat", async) { db ->
        db.addToFloat(addend, table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.flipBool] */
    fun flipBool(                                                     table: String,
                                                                     column: BoolCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("flipBool", async) { db ->
        db.flipBool(table, column, where)
    }
    
    
    // ---------------------------------  F I X   D U P L I C A T E S  --------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.fixDuplicateLongs] */
    fun fixDuplicateLongs(                                            table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = readWrite(0, "fixDuplicateLongs") { db ->
        db.fixDuplicateLongs(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.fixDuplicateInts] */
    fun fixDuplicateInts(                                             table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = readWrite(0, "fixDuplicateInts") { db ->
        db.fixDuplicateInts(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.fixDuplicatePosition] */
    fun fixDuplicatePosition(                                         table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = readWrite(0, "fixDuplicatePosition") { db ->
        db.fixDuplicatePosition(table, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.fixDuplicatePosition] */
    fun fixDuplicatePositionMult(                             vararg tables: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = readWrite(0, "fixDuplicatePositionMult") { db ->
        db.fixDuplicatePositionMult(*tables, where = where)
    }
    
    
    
    
    
    
    
    // ============================   D E L E T E,  C L E A R   ============================ \\
    
    /** A wrapper fun for [SQLiteDatabase.deleteRows] */
    fun deleteRows(                                                        table: String,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("deleteRows", async) { db ->
        db.deleteRows(table, where)
    }
    
    fun <W> deleteRows(                                                    table: EntityTable<*, W>,
                                                                           async: Boolean = false,
                                                                           where: W.()->Unit,
    ) = deleteRows(table.name, async, table.where(where))
    
    
    /** A wrapper fun for [SQLiteDatabase.deleteRowsFromTables] */
    fun deleteRowsFromTables(                                             tables: List<String>,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("deleteRowsFromTables", async) { db ->
        db.deleteRowsFromTables(tables, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.deleteRowsFromTables] */
    fun deleteRowsFromTables(                                      vararg tables: String,
                                                                           where: WhereDsl.()->Unit,
    ) = write("deleteRowsFromTables") { db ->
        db.deleteRowsFromTables(tables.toList(), where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.deleteFirstRow] */
    inline fun deleteFirstRow(                                              table: String,
                                                                            async: Boolean = false,
    ) = write("deleteFirstRow", async) { db ->
        db.deleteFirstRow(table)
    }
    
    /** A wrapper fun for [SQLiteDatabase.deleteLastRow] */
    inline fun deleteLastRow(                                               table: String,
                                                                            async: Boolean = false,
    ) = write("deleteLastRow", async) { db ->
        db.deleteLastRow(table)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.clearTable] */
    inline fun clearTable(                                                  table: String,
                                                                            async: Boolean = false,
    ) = write("clearTable", async) { it.clearTable(table) }
    
    
    /** A wrapper fun for [SQLiteDatabase.clearTables] */
    inline fun clearTables(                                         vararg tables: String,
                                                                            async: Boolean = false,
    ) = write("clearTables", async) { it.clearTables(*tables) }
    
    /** A wrapper fun for [SQLiteDatabase.clearTables] */
    inline fun clearTables(                                                tables: List<String>,
                                                                            async: Boolean = false,
    ) = write("clearTables", async) { it.clearTables(tables) }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.deleteTable] */
    inline fun deleteTable(                                                 table: String,
                                                                            async: Boolean = false,
    ) = write("deleteTable", async) { it.deleteTable(table) }
    
    
    /** A wrapper fun for [SQLiteDatabase.deleteTables] */
    inline fun deleteTables(                                        vararg tables: String,
                                                                            async: Boolean = false,
    ) = write("deleteTables", async) { it.deleteTables(*tables) }
    
    /** A wrapper fun for [SQLiteDatabase.deleteTables] */
    inline fun deleteTables(                                               tables: List<String>,
                                                                            async: Boolean = false,
    ) = write("deleteTables", async) { it.deleteTables(tables) }
    
    
    
    
    
    // ====================================  C O U N T  ==================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowCount] */
    fun getRowCount(                                                  table: String,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ) = read(0, "getRowCount") { db ->
        db.getRowCount(table, extra, where)
    }
    
    fun <W> getRowCount(                                              table: EntityTable<*, W>,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: W.()->Unit = {},
    ): Int = getRowCount(table.name, extra, table.where(where))
    
    
    /** A wrapper fun for [SQLiteDatabase.getRowCount] */
    fun getRowCount(                                                               table: String
    ) = read(0, "getRowCount") { db ->
        db.getRowCount(table)
    }
    
    
    
    /** A wrapper fun for [SQLiteDatabase.hasRows] */
    fun hasRows(                                                      table: String,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ) = read(false, "hasRows") { db ->
        db.hasRows(table, extra, where)
    }
    
    fun <W> hasRows(                                                  table: EntityTable<*, W>,
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: W.()->Unit = {},
    ): Boolean = hasRows(table.name, extra, table.where(where))
    
    
    /** A wrapper fun for [SQLiteDatabase.hasRows] */
    fun hasRows(                                                                   table: String
    ) = read(false, "hasRows") { db ->
        db.hasRows(table)
    }
    
    
    
    
    
    
    
    // ====================================  O T H E R  ==================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getLastId] */
    inline fun getLastId(table: String): Long = read(0L, "getLastId") { it.getLastId(table) }
    
    
    /** A wrapper fun for [SQLiteDatabase.getId] */
    fun getId(                                                             table: String,
                                                                           where: WhereDsl.()->Unit,
    ): Long = read(-1L, "getId") { db ->
        db.getId(table, where)
    }
    
    fun <W> getId(                                                         table: EntityTable<*, W>,
                                                                           where: W.()->Unit,
    ): Long = getId(table.name, table.where(where))
    
    /** A wrapper fun for [SQLiteDatabase.getIDs] */
    fun getIDs(                                                       table: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ): List<Long> = read(emptyList(), "getIDs") { db ->
        db.getIDs(table, orderBy, where)
    }
    
    fun <W> getIDs(                                                   table: EntityTable<*, W>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      where: W.()->Unit,
    ): List<Long> = getIDs(table.name, orderBy, table.where(where))
    
    /** A wrapper fun for [SQLiteDatabase.getAllIDs] */
    fun getAllIDs(                                                    table: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getAllIDs") { db ->
        db.getAllIDs(table, orderBy)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getNextIDs] */
    fun getNextIDs(                                                   table: String,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
    ): List<Long> = read(emptyList(), "getNextIDs") { db ->
        db.getNextIDs(table, count, reservedIDs)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getNextId] */
    fun getNextId(                                                    table: String,
                                                                reservedIDs: Set<Long> = emptySet(),
    ): Long = read(-1L, "getNextId") { db ->
        db.getNextId(table, reservedIDs)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.tableExists] */
    inline fun tableExists(                                                        table: String
    ): Boolean = read(false, "tableExists") { db ->
        db.tableExists(table)
    }
    
    /** A wrapper fun for [SQLiteDatabase.isTableEmpty] */
    inline fun isTableEmpty(                                                       table: String
    ): Boolean = read(true, "isTableEmpty") { db ->
        db.isTableEmpty(table)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getAppTableNames] */
    inline fun getAppTableNames(): List<String> = read(emptyList(), "getAppTableNames") { db ->
        db.getAppTableNames()
    }
    
    /** A wrapper fun for [SQLiteDatabase.getInternalTableNames] */
    inline fun getInternalTableNames(): List<String> = read(emptyList(), "getInternalTableNames") { db ->
        db.getInternalTableNames()
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getRawTableStr] */
    inline fun getRawTableStr(                                                     table: String
    ): RawTableStr = read(RawTableStr(), "getRawTableStr") { db ->
        db.getRawTableStr(table)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLastPosition] */
    fun getLastPosition(                                              table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Long = read(0L, "getLastPosition") { db ->
        db.getLastPosition(table, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getNextPosition] */
    fun getNextPosition(                                               table: String,
                                                                   reserved: Set<Long> = emptySet(),
                                                                      where: WhereDsl.()->Unit = {},
    ): Long = read(-1L, "getNextPosition") { db ->
        db.getNextPosition(table, reserved, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getNextPositions] */
    fun getNextPositions(                                              table: String,
                                                                      count: Int,
                                                                   reserved: Set<Long> = emptySet(),
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getNextPositions") { db ->
        db.getNextPositions(table, count, reserved, where)
    }
    
    
    
    /** A wrapper fun for [SQLiteDatabase.getNextInt] */
    fun getNextInt(                                                    table: String,
                                                                      column: IntCol,
                                                                   startFrom: Int = 1,
                                                                    reserved: Set<Int> = emptySet(),
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = read(-1, "getNextInt") { db ->
        db.getNextInt(table, column, startFrom, reserved, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getNextInts] */
    fun getNextInts(                                                   table: String,
                                                                      column: IntCol,
                                                                       count: Int,
                                                                   startFrom: Int = 1,
                                                                    reserved: Set<Int> = emptySet(),
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getNextInts") { db ->
        db.getNextInts(table, column, count, startFrom, reserved, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getNextLong] */
    fun getNextLong(                                                  table: String,
                                                                     column: LongCol,
                                                                  startFrom: Long = 1L,
                                                                   reserved: Set<Long> = emptySet(),
                                                                      where: WhereDsl.()->Unit = {},
    ): Long = read(-1L, "getNextLong") { db ->
        db.getNextLong(table, column, startFrom, reserved, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getNextLongs] */
    fun getNextLongs(                                                 table: String,
                                                                     column: LongCol,
                                                                      count: Int,
                                                                  startFrom: Long = 1L,
                                                                   reserved: Set<Long> = emptySet(),
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getNextLongs") { db ->
        db.getNextLongs(table, column, count, startFrom, reserved, where)
    }
    
    
    
    /** A wrapper fun for [SQLiteDatabase.getLargestInt] */
    fun getLargestInt(                                                table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(0, "getLargestInt") { db ->
        db.getLargestInt(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLargestLong] */
    fun getLargestLong(                                               table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(0L, "getLargestLong") { db ->
        db.getLargestLong(table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getDbFileName] */
    inline fun getDbFileName() = read("", "getDbFileName") { it.getDbFileName() }
    
    
    /** A wrapper fun for [SQLiteDatabase.getRandomInt] */
    fun getRandomInt(                                                 table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = read(-1, "getRandomInt") { db ->
        db.getRandomInt(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRandomString] */
    fun getRandomString(                                              table: String,
                                                                     column: StrCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): String = read("", "getRandomString") { db ->
        db.getRandomString(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRandomLong] */
    fun getRandomLong(                                                table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): Long = read(-1L, "getRandomLong") { db ->
        db.getRandomLong(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRandomFloat] */
    fun getRandomFloat(                                               table: String,
                                                                     column: FloatCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): Float = read(-1f, "getRandomFloat") { db ->
        db.getRandomFloat(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRandomId] */
    fun getRandomId(                                                  table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Long = read(-1L, "getRandomId") { db ->
        db.getRandomId(table, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.reorder] */
    fun reorder(                                                      table: String,
                                                                         id: Long,
                                                               moveUpOrBack: Boolean,
                                                            makeFirstOrLast: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
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
