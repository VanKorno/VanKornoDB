/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.add.addObj
import com.vankorno.vankornodb.add.addObjWithoutIdCheck
import com.vankorno.vankornodb.add.addObjects
import com.vankorno.vankornodb.add.addObjectsWithoutIdCheck
import com.vankorno.vankornodb.add.addWithNewIdAndPosition
import com.vankorno.vankornodb.api.*
import com.vankorno.vankornodb.api.DbRuntime.dbLock
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicateId
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicateInts
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicateLongs
import com.vankorno.vankornodb.dbManagement.fix.fixDuplicatePosition
import com.vankorno.vankornodb.delete.*
import com.vankorno.vankornodb.get.*
import com.vankorno.vankornodb.get.raw.data.RawTableStr
import com.vankorno.vankornodb.get.raw.getRawTableStr
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
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.useCursor(table, where, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun useCursor(                                                 table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                   where: WhereDsl.()->Unit = {},
                                                                   block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.useCursor(table, columns, where, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun useCursor(                                                    table: String,
                                                                     column: TypedColumn<*>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.useCursor(table, column, where, block)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.useCursorPro] */
    fun useCursorPro(                                                       table: String,
                                                                              dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->Unit,
    ) = write("useCursorPro") { db ->
        db.useCursorPro(table, dsl, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursorPro] */
    fun useCursorPro(                                              table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                     dsl: FullDsl.()->Unit,
                                                                   block: (Cursor)->Unit,
    ) = write("useCursorPro") { db ->
        db.useCursorPro(table, columns, dsl, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursorPro] */
    fun useCursorPro(                                                       table: String,
                                                                           column: TypedColumn<*>,
                                                                              dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->Unit,
    ) = write("useCursorPro") { db ->
        db.useCursorPro(table, column, dsl, block)
    }
    
    
    // -------------------------   R E T U R N I N G   V A L U E S   ------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun <T> getFromCursor(                                            table: String,
                                                               defaultValue: T,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursor") { db ->
        db.useCursor(table, where, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun <T> getFromCursor(                                         table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                            defaultValue: T,
                                                                   where: WhereDsl.()->Unit = {},
                                                                   block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursor") { db ->
        db.useCursor(table, columns, where, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursor] */
    fun <T> getFromCursor(                                            table: String,
                                                                     column: TypedColumn<*>,
                                                               defaultValue: T,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursor") { db ->
        db.useCursor(table, column, where, block)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.useCursorPro] */
    fun <T> getFromCursorPro(                                               table: String,
                                                                     defaultValue: T,
                                                                              dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursorPro") { db ->
        db.useCursorPro(table, dsl, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursorPro] */
    fun <T> getFromCursorPro(                                      table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                            defaultValue: T,
                                                                     dsl: FullDsl.()->Unit,
                                                                   block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursorPro") { db ->
        db.useCursorPro(table, columns, dsl, block)
    }
    
    /** A wrapper fun for [SQLiteDatabase.useCursorPro] */
    fun <T> getFromCursorPro(                                               table: String,
                                                                           column: TypedColumn<*>,
                                                                     defaultValue: T,
                                                                              dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursorPro") { db ->
        db.useCursorPro(table, column, dsl, block)
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
    ) = readWrite(-1L, "addObj") { db ->
        db.addObj(tableInfo, obj)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addObjects] */
    inline fun <T : CurrEntity> addObjects(                           tableInfo: TableInfoNormal<T>,
                                                                        objects: List<T>,
    ) = readWrite(emptyList(), "addObjects") { db ->
        db.addObjects(tableInfo, objects)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addObjWithoutIdCheck] */
    inline fun <T : CurrEntity> addObjWithoutIdCheck(                 tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
    ) = readWrite(-1L, "addObjWithoutIdCheck") { db ->
        db.addObjWithoutIdCheck(tableInfo, obj)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addObjectsWithoutIdCheck] */
    inline fun <T : CurrEntity> addObjectsWithoutIdCheck(             tableInfo: TableInfoNormal<T>,
                                                                        objects: List<T>,
    ) = readWrite(emptyList(), "addObjectsWithoutIdCheck") { db ->
        db.addObjectsWithoutIdCheck(tableInfo, objects)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addWithNewIdAndPosition] */
    fun <T : CurrEntity> addWithNewIdAndPosition(                     tableInfo: TableInfoNormal<T>,
                                                                       entities: List<T>,
                                                                          async: Boolean = false,
                                                               setIdAndPosition: (T, Long, Long)->T,
    ) = write("addWithNewIdAndPosition", async) { db ->
        db.addWithNewIdAndPosition(tableInfo, entities, setIdAndPosition)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addWithNewIdAndPosition] */
    fun <T : CurrEntity> addWithNewIdAndPosition(                     tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
                                                                          async: Boolean = false,
                                                               setIdAndPosition: (T, Long, Long)->T,
    ) = write("addWithNewIdAndPosition", async) { db ->
        db.addWithNewIdAndPosition(tableInfo, listOf(entity), setIdAndPosition)
    }
    
    
    // ---------------------------------------  S E T  --------------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.setObj] */
    fun <T : CurrEntity> setObj(                                      tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
                                                                          async: Boolean = false,
                                                                          where: WhereDsl.()->Unit,
    ) = write("setObj", async) { db ->
        db.setObj(tableInfo, obj, where)
    }
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getInt] */
    fun getInt(                                                            table: String,
                                                                          column: IntCol,
                                                                           where: WhereDsl.()->Unit,
    ): Int = read(-1, "getInt") { db ->
        db.getInt(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getStr] */
    fun getStr(                                                            table: String,
                                                                          column: StrCol,
                                                                           where: WhereDsl.()->Unit,
    ): String = read("", "getStr") { db ->
        db.getStr(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getBool] */
    fun getBool(                                                           table: String,
                                                                          column: BoolCol,
                                                                           where: WhereDsl.()->Unit,
    ): Boolean = read(false, "getBool") { db ->
        db.getBool(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLong] */
    fun getLong(                                                           table: String,
                                                                          column: LongCol,
                                                                           where: WhereDsl.()->Unit,
    ): Long = read(-1L, "getLong") { db ->
        db.getLong(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getFloat] */
    fun getFloat(                                                          table: String,
                                                                          column: FloatCol,
                                                                           where: WhereDsl.()->Unit,
    ): Float = read(-1F, "getFloat") { db ->
        db.getFloat(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getBlob] */
    fun getBlob(                                                           table: String,
                                                                          column: BlobCol,
                                                                           where: WhereDsl.()->Unit,
    ): ByteArray = read(ByteArray(0), "getBlob") { db ->
        db.getBlob(table, column, where)
    }
    
    
    
    // ----------------------------------  G E T   P R O  ---------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.getIntPro] */
    fun getIntPro(                                                          table: String,
                                                                           column: IntCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Int = read(-1, "getIntPro") { db ->
        db.getIntPro(table, column, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getStrPro] */
    fun getStrPro(                                                          table: String,
                                                                           column: StrCol,
                                                                              dsl: FullDsl.()->Unit,
    ): String = read("", "getStrPro") { db ->
        db.getStrPro(table, column, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getBoolPro] */
    fun getBoolPro(                                                         table: String,
                                                                           column: BoolCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Boolean = read(false, "getBoolPro") { db ->
        db.getBoolPro(table, column, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLongPro] */
    fun getLongPro(                                                         table: String,
                                                                           column: LongCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Long = read(-1L, "getLongPro") { db ->
        db.getLongPro(table, column, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getFloatPro] */
    fun getFloatPro(                                                        table: String,
                                                                           column: FloatCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Float = read(-1F, "getFloatPro") { db ->
        db.getFloatPro(table, column, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getBlobPro] */
    fun getBlobPro(                                                         table: String,
                                                                           column: BlobCol,
                                                                              dsl: FullDsl.()->Unit,
    ): ByteArray = read(ByteArray(0), "getBlobPro") { db ->
        db.getBlobPro(table, column, dsl)
    }
    
    
    
    
    
    
    // ==============================  G E T   O B J E C T S  ============================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getObj] */
    fun <T : BaseEntity> getObj(                                  tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getObj") { db ->
        db.getObj(tableInfo, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getObj] */
    fun <T : BaseEntity> getObj(                                  tableInfo: TableInfoBase<T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
    ): T = read(default, "getObj") { db ->
        db.getObj(tableInfo, default, where)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.getObjPro] */
    fun <T : BaseEntity> getObjPro(                                     tableInfo: TableInfoBase<T>,
                                                                              dsl: FullDsl.()->Unit,
    ): T? = read(null, "getObjPro") { db ->
        db.getObjPro(tableInfo, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getObjPro] */
    fun <T : BaseEntity> getObjPro(                                 tableInfo: TableInfoBase<T>,
                                                                      default: T,
                                                                          dsl: FullDsl.()->Unit,
    ): T = read(default, "getObjPro") { db ->
        db.getObjPro(tableInfo, default, dsl)
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
    
    
    
    // =========================  M U L T I P L E   O B J E C T S  ========================= \\
    
    /** A wrapper fun for [SQLiteDatabase.getObjects] */
    fun <T : BaseEntity> getObjects(                              tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjects") { db ->
        db.getObjectsPro(tableInfo) { this.where = where }
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getObjMap] */
    fun <T : BaseEntity> getObjMap(                               tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): Map<Long, T> = read(emptyMap(), "getObjMap") { db ->
        db.getObjMapPro(tableInfo) { this.where = where }
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.getObjectsPro] */
    fun <T : BaseEntity> getObjectsPro(                            tableInfo: TableInfoBase<T>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjectsPro") { db ->
        db.getObjectsPro(tableInfo, dsl)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getObjMapPro] */
    fun <T : BaseEntity> getObjMapPro(                             tableInfo: TableInfoBase<T>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): Map<Long, T> = read(emptyMap(), "getObjMapPro") { db ->
        db.getObjMapPro(tableInfo, dsl)
    }
    
    
    
    
    
    
    
    
    // ==============================   C O L U M N   V A L s   ============================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColInts] */
    fun getColInts(                                                   table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColInts") { db ->
        db.getColInts(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getColIntsPro] */
    fun getColIntsPro(                                                 table: String,
                                                                      column: IntCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColIntsPro") { db ->
        db.getColIntsPro(table, column, dsl)
    }
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColStrings] */
    fun getColStrings(                                                table: String,
                                                                     column: StrCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStrings") { db ->
        db.getColStrings(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getColStringsPro] */
    fun getColStringsPro(                                              table: String,
                                                                      column: StrCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStringsPro") { db ->
        db.getColStringsPro(table, column, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColBools] */
    fun getColBools(                                                  table: String,
                                                                     column: BoolCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBools") { db ->
        db.getColBools(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getColBoolsPro] */
    fun getColBoolsPro(                                                table: String,
                                                                      column: BoolCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBoolsPro") { db ->
        db.getColBoolsPro(table, column, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColLongs] */
    fun getColLongs(                                                  table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongs") { db ->
        db.getColLongs(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getColLongsPro] */
    fun getColLongsPro(                                                table: String,
                                                                      column: LongCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongsPro") { db ->
        db.getColLongsPro(table, column, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColFloats] */
    fun getColFloats(                                                 table: String,
                                                                     column: FloatCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloats") { db ->
        db.getColFloats(table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getColFloatsPro] */
    fun getColFloatsPro(                                               table: String,
                                                                      column: FloatCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloatsPro") { db ->
        db.getColFloatsPro(table, column, dsl)
    }
    
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColBlobs] */
    fun getColBlobs(                                                  table: String,
                                                                     column: BlobCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobs") { db ->
        db.getColBlobs(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getColBlobsPro] */
    fun getColBlobsPro(                                                table: String,
                                                                      column: BlobCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobsPro") { db ->
        db.getColBlobsPro(table, column, dsl)
    }
    
    
    
    
    // =================================   R O W   V A L s   ================================= \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowInts] */
    fun getRowInts(                                                   table: String,
                                                                    columns: Array<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getRowInts") { db ->
        db.getRowInts(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowIntsPro] */
    fun getRowIntsPro(                                                 table: String,
                                                                     columns: Array<IntCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getRowIntsPro") { db ->
        db.getRowIntsPro(table, columns, dsl)
    }
    
    
    //  -----------------------------------  S T R I N G  -----------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowStrings] */
    fun getRowStrings(                                                table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStrings") { db ->
        db.getRowStrings(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowStringsPro] */
    fun getRowStringsPro(                                              table: String,
                                                                     columns: Array<StrCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStringsPro") { db ->
        db.getRowStringsPro(table, columns, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowBools] */
    fun getRowBools(                                                  table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBools") { db ->
        db.getRowBools(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowBoolsPro] */
    fun getRowBoolsPro(                                                table: String,
                                                                     columns: Array<BoolCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBoolsPro") { db ->
        db.getRowBoolsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowLongs] */
    fun getRowLongs(                                                  table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongs") { db ->
        db.getRowLongs(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowLongsPro] */
    fun getRowLongsPro(                                                table: String,
                                                                     columns: Array<LongCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongsPro") { db ->
        db.getRowLongsPro(table, columns, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowFloats] */
    fun getRowFloats(                                                 table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloats") { db ->
        db.getRowFloats(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowFloatsPro] */
    fun getRowFloatsPro(                                               table: String,
                                                                     columns: Array<FloatCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloatsPro") { db ->
        db.getRowFloatsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowBlobs] */
    fun getRowBlobs(                                                  table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobs") { db ->
        db.getRowBlobs(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowBlobsPro] */
    fun getRowBlobsPro(                                                table: String,
                                                                     columns: Array<BlobCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobsPro") { db ->
        db.getRowBlobsPro(table, columns, dsl)
    }
    
    
    
    
    
    
    // ===============================   T A B L E   V A L s   =============================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableInts] */
    fun getTableInts(                                                 table: String,
                                                                    columns: Array<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Int>> = read(emptyList(), "getTableInts") { db ->
        db.getTableInts(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableIntsPro] */
    fun getTableIntsPro(                                               table: String,
                                                                     columns: Array<IntCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Int>> = read(emptyList(), "getTableIntsPro") { db ->
        db.getTableIntsPro(table, columns, dsl)
    }
    
    
    //  -----------------------------------  S T R I N G  -----------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableStrings] */
    fun getTableStrings(                                              table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStrings") { db ->
        db.getTableStrings(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableStringsPro] */
    fun getTableStringsPro(                                            table: String,
                                                                     columns: Array<StrCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStringsPro") { db ->
        db.getTableStringsPro(table, columns, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableBools] */
    fun getTableBools(                                                table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBools") { db ->
        db.getTableBools(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableBoolsPro] */
    fun getTableBoolsPro(                                              table: String,
                                                                     columns: Array<BoolCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBoolsPro") { db ->
        db.getTableBoolsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableLongs] */
    fun getTableLongs(                                                table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongs") { db ->
        db.getTableLongs(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableLongsPro] */
    fun getTableLongsPro(                                              table: String,
                                                                     columns: Array<LongCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongsPro") { db ->
        db.getTableLongsPro(table, columns, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableFloats] */
    fun getTableFloats(                                               table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloats") { db ->
        db.getTableFloats(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableFloatsPro] */
    fun getTableFloatsPro(                                             table: String,
                                                                     columns: Array<FloatCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloatsPro") { db ->
        db.getTableFloatsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableBlobs] */
    fun getTableBlobs(                                                table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobs") { db ->
        db.getTableBlobs(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableBlobsPro] */
    fun getTableBlobsPro(                                              table: String,
                                                                     columns: Array<BlobCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobsPro") { db ->
        db.getTableBlobsPro(table, columns, dsl)
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
    
    /** A wrapper fun for [SQLiteDatabase.fixDuplicateId] */
    fun fixDuplicateId(                                               table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = readWrite(0, "fixDuplicateId") { db ->
        db.fixDuplicateId(table, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.fixDuplicatePosition] */
    fun fixDuplicatePosition(                                         table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = readWrite(0, "fixDuplicatePosition") { db ->
        db.fixDuplicatePosition(table, where)
    }
    
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
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(0, "getRowCount") { db ->
        db.getRowCount(table, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowCountPro] */
    fun getRowCountPro(                                                table: String,
                                                                         dsl: FullDsl.()->Unit = {},
    ) = read(0, "getRowCountPro") { db ->
        db.getRowCountPro(table, dsl)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.hasRows] */
    fun hasRows(                                                      table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(false, "hasRows") { db ->
        db.hasRows(table, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.hasRowsPro] */
    fun hasRowsPro(                                                         table: String,
                                                                              dsl: FullDsl.()->Unit,
    ) = read(false, "hasRowsPro") { db ->
        db.hasRowsPro(table, dsl)
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
                                                                      where: WhereDsl.()->Unit,
                                                                    orderBy: OrderDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getIDs") { db ->
        db.getIDs(table, where, orderBy)
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
    ): Long = read(-1L, "getNextPosition") { db ->
        db.getNextPosition(table, reserved)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getNextPositions] */
    fun getNextPositions(                                              table: String,
                                                                      count: Int,
                                                                   reserved: Set<Long> = emptySet(),
    ): List<Long> = read(emptyList(), "getNextPositions") { db ->
        db.getNextPositions(table, count, reserved)
    }
    
    
    
    /** A wrapper fun for [SQLiteDatabase.getNextInt] */
    fun getNextInt(                                                    table: String,
                                                                      column: IntCol,
                                                                   startFrom: Int = 1,
                                                                    reserved: Set<Int> = emptySet(),
    ): Int = read(-1, "getNextInt") { db ->
        db.getNextInt(table, column, startFrom, reserved)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getNextInts] */
    fun getNextInts(                                                   table: String,
                                                                      column: IntCol,
                                                                       count: Int,
                                                                   startFrom: Int = 1,
                                                                    reserved: Set<Int> = emptySet(),
    ): List<Int> = read(emptyList(), "getNextInts") { db ->
        db.getNextInts(table, column, count, startFrom, reserved)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getNextLong] */
    fun getNextLong(                                                  table: String,
                                                                     column: LongCol,
                                                                  startFrom: Long = 1L,
                                                                   reserved: Set<Long> = emptySet(),
    ): Long = read(-1L, "getNextLong") { db ->
        db.getNextLong(table, column, startFrom, reserved)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getNextLongs] */
    fun getNextLongs(                                                 table: String,
                                                                     column: LongCol,
                                                                      count: Int,
                                                                  startFrom: Long = 1L,
                                                                   reserved: Set<Long> = emptySet(),
    ): List<Long> = read(emptyList(), "getNextLongs") { db ->
        db.getNextLongs(table, column, count, startFrom, reserved)
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
