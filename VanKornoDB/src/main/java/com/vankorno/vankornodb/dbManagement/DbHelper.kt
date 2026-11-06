/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.add.addObj
import com.vankorno.vankornodb.add.addObjWithoutIdCheck
import com.vankorno.vankornodb.add.addObjects
import com.vankorno.vankornodb.add.addObjectsWithoutIdCheck
import com.vankorno.vankornodb.add.addWithNewIDsAndPositions
import com.vankorno.vankornodb.add.addWithNewIdAndPosition
import com.vankorno.vankornodb.api.*
import com.vankorno.vankornodb.api.DbRuntime.dbLock
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
import com.vankorno.vankornodb.set.*

@Suppress("NOTHING_TO_INLINE", "unused")
abstract class DbHelperInternal(
                                 context: Context,
                                  dbName: String,
                               dbVersion: Int,
                              entityMeta: Collection<BaseEntityMeta>,
                   createExclusiveTables: Boolean = true,
                                    lock: DbLock = dbLock,
                                onCreate: (SQLiteDatabase)->Unit = {},
                               onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
) : DbReaderWriter(
    context = context,
    dbName = dbName,
    dbVersion = dbVersion,
    entityMeta = entityMeta,
    createExclusiveTables = createExclusiveTables,
    lock = lock,
    onCreate = onCreate,
    onUpgrade = onUpgrade,
) {
    // ===================================  C U R S O R  =================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun useCursor(                                                    table: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.useCursor(table, orderBy, extra, where, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun useCursor(                                                 table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                 orderBy: OrderDsl.()->Unit = {},
                                                                   extra: ExtraDsl.()->Unit = {},
                                                                   where: WhereDsl.()->Unit = {},
                                                                   block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.useCursor(table, columns, orderBy, extra, where, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun useCursor(                                                    table: String,
                                                                     column: TypedColumn<*>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.useCursor(table, column, orderBy, extra, where, block)
    }
    
    
    // -------------------------   R E T U R N I N G   V A L U E S   ------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun <T> getFromCursor(                                            table: String,
                                                                    default: T,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->T,
    ): T = readWrite(default, "getFromCursor") { db ->
        db.useCursor(table, orderBy, extra, where, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun <T> getFromCursor(                                         table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                 default: T,
                                                                 orderBy: OrderDsl.()->Unit = {},
                                                                   extra: ExtraDsl.()->Unit = {},
                                                                   where: WhereDsl.()->Unit = {},
                                                                   block: (Cursor)->T,
    ): T = readWrite(default, "getFromCursor") { db ->
        db.useCursor(table, columns, orderBy, extra, where, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun <T> getFromCursor(                                            table: String,
                                                                     column: TypedColumn<*>,
                                                                    default: T,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->T,
    ): T = readWrite(default, "getFromCursor") { db ->
        db.useCursor(table, column, orderBy, extra, where, block)
    }
    
    
    
    
    
    
    
    
    
    
    
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
    
    /** A wrapper fun for [SQLiteDatabase.setStr] */
    fun setStr(                                                            value: String,
                                                                           table: String,
                                                                          column: StrCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
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
    
    /** A wrapper fun for [SQLiteDatabase.setLong] */
    fun setLong(                                                           value: Long,
                                                                           table: String,
                                                                          column: LongCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
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
    
    /** A wrapper fun for [SQLiteDatabase.setBlob] */
    fun setBlob(                                                           value: ByteArray,
                                                                           table: String,
                                                                          column: BlobCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
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
    
    
    
    // -----------------------------------  O B J E C T  ----------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.addObj] */
    inline fun <T : CurrEntity> addObj(                               tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
    ): Long = readWrite(-1L, "addObj") { db ->
        db.addObj(tableInfo, obj)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addObjects] */
    inline fun <T : CurrEntity> addObjects(                           tableInfo: TableInfoNormal<T>,
                                                                        objects: List<T>,
    ): List<Long> = readWrite(emptyList(), "addObjects") { db ->
        db.addObjects(tableInfo, objects)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addObjects] */
    inline fun <T : CurrEntity> addObjects(                           tableInfo: TableInfoNormal<T>,
                                                                 vararg objects: T,
    ): List<Long> = readWrite(emptyList(), "addObjects") { db ->
        db.addObjects(tableInfo, objects.toList())
    }
    
    /** A wrapper fun for [SQLiteDatabase.addObjWithoutIdCheck] */
    inline fun <T : CurrEntity> addObjWithoutIdCheck(                 tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
    ): Long = readWrite(-1L, "addObjWithoutIdCheck") { db ->
        db.addObjWithoutIdCheck(tableInfo, obj)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addObjectsWithoutIdCheck] */
    inline fun <T : CurrEntity> addObjectsWithoutIdCheck(             tableInfo: TableInfoNormal<T>,
                                                                        objects: List<T>,
    ): List<Long> = readWrite(emptyList(), "addObjectsWithoutIdCheck") { db ->
        db.addObjectsWithoutIdCheck(tableInfo, objects)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addWithNewIDsAndPositions] */
    fun <T : CurrEntity> addWithNewIDsAndPositions(                   tableInfo: TableInfoNormal<T>,
                                                                       entities: List<T>,
                                                               setIdAndPosition: (T, Long, Long)->T,
    ): List<DbIdAndPosition> = readWrite(emptyList(), "addWithNewIDsAndPositions") { db ->
        db.addWithNewIDsAndPositions(tableInfo, entities, setIdAndPosition)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addWithNewIdAndPosition] */
    fun <T : CurrEntity> addWithNewIdAndPosition(                     tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
                                                               setIdAndPosition: (T, Long, Long)->T,
    ): DbIdAndPosition = readWrite(DbIdAndPosition(), "addWithNewIdAndPosition") { db ->
        db.addWithNewIdAndPosition(tableInfo, entity, setIdAndPosition)
    }
    
    
    // ---------------------------------------  S E T  --------------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.setObj] */
    fun <T : CurrEntity> setObj(                                      tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
                                                                          where: WhereDsl.()->Unit,
    ): Int = readWrite(0, "setObj") { db ->
        db.setObj(tableInfo, obj, where)
    }
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getInt] */
    fun getInt(                                                       table: String,
                                                                     column: IntCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = read(-1, "getInt") { db ->
        db.getInt(table, column, orderBy, extra, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getStr] */
    fun getStr(                                                       table: String,
                                                                     column: StrCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): String = read("", "getStr") { db ->
        db.getStr(table, column, orderBy, extra, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getBool] */
    fun getBool(                                                      table: String,
                                                                     column: BoolCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): Boolean = read(false, "getBool") { db ->
        db.getBool(table, column, orderBy, extra, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLong] */
    fun getLong(                                                      table: String,
                                                                     column: LongCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): Long = read(-1L, "getLong") { db ->
        db.getLong(table, column, orderBy, extra, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getFloat] */
    fun getFloat(                                                     table: String,
                                                                     column: FloatCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): Float = read(-1F, "getFloat") { db ->
        db.getFloat(table, column, orderBy, extra, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getBlob] */
    fun getBlob(                                                      table: String,
                                                                     column: BlobCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): ByteArray = read(ByteArray(0), "getBlob") { db ->
        db.getBlob(table, column, orderBy, extra, where)
    }
    
    
    
    
    
    
    // ==============================  G E T   O B J E C T S  ============================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getObj] */
    fun <T : BaseEntity> getObj(                                  tableInfo: TableInfoBase<T>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ): T? = read(null, "getObj") { db ->
        db.getObj(tableInfo, orderBy, extra, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getObj] */
    fun <T : BaseEntity> getObj(                                  tableInfo: TableInfoBase<T>,
                                                                    default: T,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ): T = read(default, "getObj") { db ->
        db.getObj(tableInfo, default, orderBy, extra, where)
    }
    
    
    
    /** A wrapper fun for [SQLiteDatabase.getFirstObj] */
    fun <T : BaseEntity> getFirstObj(                             tableInfo: TableInfoBase<out T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getFirstObj") { db ->
        db.getFirstObj(tableInfo, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getFirstObj] */
    fun <T : BaseEntity> getFirstObj(                             tableInfo: TableInfoBase<out T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
    ): T = read(default, "getFirstObj") { db ->
        db.getFirstObj(tableInfo, default, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getLastObj] */
    fun <T : BaseEntity> getLastObj(                              tableInfo: TableInfoBase<out T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getLastObj") { db ->
        db.getLastObj(tableInfo, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLastObj] */
    fun <T : BaseEntity> getLastObj(                              tableInfo: TableInfoBase<out T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
    ): T = read(default, "getLastObj") { db ->
        db.getLastObj(tableInfo, default, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getRandomObj] */
    inline fun <reified T : BaseEntity> getRandomObj(             tableInfo: TableInfoBase<out T>,
                                                             noinline where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getRandomObj") { db ->
        db.getRandomObj<T>(tableInfo, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRandomObj] */
    inline fun <reified T : BaseEntity> getRandomObj(             tableInfo: TableInfoBase<out T>,
                                                                    default: T,
                                                             noinline where: WhereDsl.()->Unit = {},
    ): T = read(default, "getRandomObj") { db ->
        db.getRandomObj<T>(tableInfo, default, where)
    }
    
    
    
    
    // =========================  M U L T I P L E   O B J E C T S  ========================= \\
    
    /** A wrapper fun for [SQLiteDatabase.getObjects] */
    fun <T : BaseEntity> getObjects(                              tableInfo: TableInfoBase<T>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjects") { db ->
        db.getObjects(tableInfo, orderBy, extra, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getObjMap] */
    fun <T : BaseEntity> getObjMap(                               tableInfo: TableInfoBase<T>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
    ): Map<Long, T> = read(emptyMap(), "getObjMap") { db ->
        db.getObjMap(tableInfo, orderBy, extra, where)
    }
    
    
    
    
    
    
    
    
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
    
    
    /** A wrapper fun for [SQLiteDatabase.getColStrings] */
    fun getColStrings(                                                table: String,
                                                                     column: StrCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
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
    
    
    /** A wrapper fun for [SQLiteDatabase.getColLongs] */
    fun getColLongs(                                                  table: String,
                                                                     column: LongCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
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
    
    
    /** A wrapper fun for [SQLiteDatabase.getColBlobs] */
    fun getColBlobs(                                                  table: String,
                                                                     column: BlobCol,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit = {},
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
    
    /** A wrapper fun for [SQLiteDatabase.getIDs] */
    fun getIDs(                                                       table: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
    ): List<Long> = read(emptyList(), "getIDs") { db ->
        db.getIDs(table, orderBy, where)
    }
    
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
