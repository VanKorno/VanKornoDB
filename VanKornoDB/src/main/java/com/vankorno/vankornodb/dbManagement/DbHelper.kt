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
    
    /** A wrapper fun for [SQLiteDatabase.getCursor] */
    fun useCursor(                                                    table: String,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.getCursor(table, where).use { cursor ->
            block(cursor)
        }
    }
    
    /** A wrapper fun for [SQLiteDatabase.getCursor] */
    fun useCursor(                                                 table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                   where: WhereDsl.()->Unit = {},
                                                                   block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.getCursor(table, columns, where).use { cursor ->
            block(cursor)
        }
    }
    
    /** A wrapper fun for [SQLiteDatabase.getCursor] */
    fun useCursor(                                                    table: String,
                                                                     column: TypedColumn<*>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.getCursor(table, column, where).use { cursor ->
            block(cursor)
        }
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getCursorPro] */
    fun useCursorPro(                                                       table: String,
                                                                              dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->Unit,
    ) = write("useCursorPro") { db ->
        db.getCursorPro(table, dsl).use { cursor ->
            block(cursor)
        }
    }
    
    /** A wrapper fun for [SQLiteDatabase.getCursorPro] */
    fun useCursorPro(                                              table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                     dsl: FullDsl.()->Unit,
                                                                   block: (Cursor)->Unit,
    ) = write("useCursorPro") { db ->
        db.getCursorPro(table, columns, dsl).use { cursor ->
            block(cursor)
        }
    }
    
    /** A wrapper fun for [SQLiteDatabase.getCursorPro] */
    fun useCursorPro(                                                       table: String,
                                                                           column: TypedColumn<*>,
                                                                              dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->Unit,
    ) = write("useCursorPro") { db ->
        db.getCursorPro(table, column, dsl).use { cursor ->
            block(cursor)
        }
    }
    
    
    // -------------------------   R E T U R N I N G   V A L U E S   ------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.getCursor] */
    fun <T> getFromCursor(                                            table: String,
                                                               defaultValue: T,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursor") { db ->
        db.getCursor(table, where).use { cursor ->
            block(cursor)
        }
    }
    
    /** A wrapper fun for [SQLiteDatabase.getCursor] */
    fun <T> getFromCursor(                                         table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                            defaultValue: T,
                                                                   where: WhereDsl.()->Unit = {},
                                                                   block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursor") { db ->
        db.getCursor(table, columns, where).use { cursor ->
            block(cursor)
        }
    }
    
    /** A wrapper fun for [SQLiteDatabase.getCursor] */
    fun <T> getFromCursor(                                            table: String,
                                                                     column: TypedColumn<*>,
                                                               defaultValue: T,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursor") { db ->
        db.getCursor(table, column, where).use { cursor ->
            block(cursor)
        }
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getCursorPro] */
    fun <T> getFromCursorPro(                                               table: String,
                                                                     defaultValue: T,
                                                                              dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursorPro") { db ->
        db.getCursorPro(table, dsl).use { cursor ->
            block(cursor)
        }
    }
    
    /** A wrapper fun for [SQLiteDatabase.getCursorPro] */
    fun <T> getFromCursorPro(                                      table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                            defaultValue: T,
                                                                     dsl: FullDsl.()->Unit,
                                                                   block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursorPro") { db ->
        db.getCursorPro(table, columns, dsl).use { cursor ->
            block(cursor)
        }
    }
    
    /** A wrapper fun for [SQLiteDatabase.getCursorPro] */
    fun <T> getFromCursorPro(                                               table: String,
                                                                           column: TypedColumn<*>,
                                                                     defaultValue: T,
                                                                              dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursorPro") { db ->
        db.getCursorPro(table, column, dsl).use { cursor ->
            block(cursor)
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    // ===================================  S E T T E R S  =================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.setInt] */
    fun setInt(                                                            value: Int,
                                                                           table: String,
                                                                          column: IntCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setInt", async) {
        it.setInt(value, table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.setStr] */
    fun setStr(                                                            value: String,
                                                                           table: String,
                                                                          column: StrCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setStr", async) {
        it.setStr(value, table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.setBool] */
    fun setBool(                                                           value: Boolean,
                                                                           table: String,
                                                                          column: BoolCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setBool", async) {
        it.setBool(value, table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.setLong] */
    fun setLong(                                                           value: Long,
                                                                           table: String,
                                                                          column: LongCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setLong", async) {
        it.setLong(value, table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.setFloat] */
    fun setFloat(                                                          value: Float,
                                                                           table: String,
                                                                          column: FloatCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setFloat", async) {
        it.setFloat(value, table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.setBlob] */
    fun setBlob(                                                           value: ByteArray,
                                                                           table: String,
                                                                          column: BlobCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setBlob", async) {
        it.setBlob(value, table, column, where)
    }
    
    
    
    
    // =============================  M U L T I - S E T T E R S  ============================= \\
    
    /** A wrapper fun for [SQLiteDatabase.set] */
    fun set(                                                          table: String,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      async: Boolean = false,
                                                                    actions: SetDsl.()->Unit,
    ) = write("set", async) {
        it.set(table, where, actions)
    }
    
    
    
    // -----------------------------------  O B J E C T  ----------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.addObj] */
    inline fun <T : CurrEntity> addObj(                               tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
    ) = readWrite(-1L, "addObj") {
        it.addObj(tableInfo, obj)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addObjects] */
    inline fun <T : CurrEntity> addObjects(                           tableInfo: TableInfoNormal<T>,
                                                                        objects: List<T>,
    ) = readWrite(emptyList(), "addObjects") {
        it.addObjects(tableInfo, objects)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addObjWithoutIdCheck] */
    inline fun <T : CurrEntity> addObjWithoutIdCheck(                 tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
    ) = readWrite(-1L, "addObjWithoutIdCheck") {
        it.addObjWithoutIdCheck(tableInfo, obj)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addObjectsWithoutIdCheck] */
    inline fun <T : CurrEntity> addObjectsWithoutIdCheck(             tableInfo: TableInfoNormal<T>,
                                                                        objects: List<T>,
    ) = readWrite(emptyList(), "addObjectsWithoutIdCheck") {
        it.addObjectsWithoutIdCheck(tableInfo, objects)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addWithNewIdAndPosition] */
    fun <T : CurrEntity> addWithNewIdAndPosition(                     tableInfo: TableInfoNormal<T>,
                                                                       entities: List<T>,
                                                                          async: Boolean = false,
                                                               setIdAndPosition: (T, Long, Long)->T,
    ) = write("addWithNewIdAndPosition", async) {
        it.addWithNewIdAndPosition(tableInfo, entities, setIdAndPosition)
    }
    
    /** A wrapper fun for [SQLiteDatabase.addWithNewIdAndPosition] */
    fun <T : CurrEntity> addWithNewIdAndPosition(                     tableInfo: TableInfoNormal<T>,
                                                                         entity: T,
                                                                          async: Boolean = false,
                                                               setIdAndPosition: (T, Long, Long)->T,
    ) = write("addWithNewIdAndPosition", async) {
        it.addWithNewIdAndPosition(tableInfo, listOf(entity), setIdAndPosition)
    }
    
    
    // ---------------------------------------  S E T  --------------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.setObj] */
    fun <T : CurrEntity> setObj(                                      tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
                                                                          async: Boolean = false,
                                                                          where: WhereDsl.()->Unit,
    ) = write("setObj", async) {
        it.setObj(tableInfo, obj, where)
    }
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getInt] */
    fun getInt(                                                            table: String,
                                                                          column: IntCol,
                                                                           where: WhereDsl.()->Unit,
    ): Int = read(-1, "getInt") {
        it.getInt(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getStr] */
    fun getStr(                                                            table: String,
                                                                          column: StrCol,
                                                                           where: WhereDsl.()->Unit,
    ): String = read("", "getStr") {
        it.getStr(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getBool] */
    fun getBool(                                                           table: String,
                                                                          column: BoolCol,
                                                                           where: WhereDsl.()->Unit,
    ): Boolean = read(false, "getBool") {
        it.getBool(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLong] */
    fun getLong(                                                           table: String,
                                                                          column: LongCol,
                                                                           where: WhereDsl.()->Unit,
    ): Long = read(-1L, "getLong") {
        it.getLong(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getFloat] */
    fun getFloat(                                                          table: String,
                                                                          column: FloatCol,
                                                                           where: WhereDsl.()->Unit,
    ): Float = read(-1F, "getFloat") {
        it.getFloat(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getBlob] */
    fun getBlob(                                                           table: String,
                                                                          column: BlobCol,
                                                                           where: WhereDsl.()->Unit,
    ): ByteArray = read(ByteArray(0), "getBlob") {
        it.getBlob(table, column, where)
    }
    
    
    
    // ----------------------------------  G E T   P R O  ---------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.getIntPro] */
    fun getIntPro(                                                          table: String,
                                                                           column: IntCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Int = read(-1, "getIntPro") {
        it.getIntPro(table, column, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getStrPro] */
    fun getStrPro(                                                          table: String,
                                                                           column: StrCol,
                                                                              dsl: FullDsl.()->Unit,
    ): String = read("", "getStrPro") {
        it.getStrPro(table, column, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getBoolPro] */
    fun getBoolPro(                                                         table: String,
                                                                           column: BoolCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Boolean = read(false, "getBoolPro") {
        it.getBoolPro(table, column, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLongPro] */
    fun getLongPro(                                                         table: String,
                                                                           column: LongCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Long = read(-1L, "getLongPro") {
        it.getLongPro(table, column, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getFloatPro] */
    fun getFloatPro(                                                        table: String,
                                                                           column: FloatCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Float = read(-1F, "getFloatPro") {
        it.getFloatPro(table, column, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getBlobPro] */
    fun getBlobPro(                                                         table: String,
                                                                           column: BlobCol,
                                                                              dsl: FullDsl.()->Unit,
    ): ByteArray = read(ByteArray(0), "getBlobPro") {
        it.getBlobPro(table, column, dsl)
    }
    
    
    
    
    
    
    // ==============================  G E T   O B J E C T S  ============================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getObj] */
    fun <T : BaseEntity> getObj(                                  tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getObj") {
        it.getObj(tableInfo, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getObj] */
    fun <T : BaseEntity> getObj(                                  tableInfo: TableInfoBase<T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
    ): T = read(default, "getObj") {
        it.getObj(tableInfo, default, where)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.getObjPro] */
    fun <T : BaseEntity> getObjPro(                                     tableInfo: TableInfoBase<T>,
                                                                              dsl: FullDsl.()->Unit,
    ): T? = read(null, "getObjPro") {
        it.getObjPro(tableInfo, dsl)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getObjPro] */
    fun <T : BaseEntity> getObjPro(                                 tableInfo: TableInfoBase<T>,
                                                                      default: T,
                                                                          dsl: FullDsl.()->Unit,
    ): T = read(default, "getObjPro") {
        it.getObjPro(tableInfo, default, dsl)
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
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjectsPro(tableInfo) { this.where = where }
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getObjMap] */
    fun <T : BaseEntity> getObjMap(                               tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): Map<Long, T> = read(emptyMap(), "getObjMap") {
        it.getObjMapPro(tableInfo) { this.where = where }
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    /** A wrapper fun for [SQLiteDatabase.getObjectsPro] */
    fun <T : BaseEntity> getObjectsPro(                            tableInfo: TableInfoBase<T>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjectsPro") {
        it.getObjectsPro(tableInfo, dsl)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getObjMapPro] */
    fun <T : BaseEntity> getObjMapPro(                             tableInfo: TableInfoBase<T>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): Map<Long, T> = read(emptyMap(), "getObjMapPro") {
        it.getObjMapPro(tableInfo, dsl)
    }
    
    
    
    
    
    
    
    
    // ==============================   C O L U M N   V A L s   ============================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColInts] */
    fun getColInts(                                                   table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColInts") {
        it.getColInts(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getColIntsPro] */
    fun getColIntsPro(                                                 table: String,
                                                                      column: IntCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColIntsPro") {
        it.getColIntsPro(table, column, dsl)
    }
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColStrings] */
    fun getColStrings(                                                table: String,
                                                                     column: StrCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStrings") {
        it.getColStrings(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getColStringsPro] */
    fun getColStringsPro(                                              table: String,
                                                                      column: StrCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStringsPro") {
        it.getColStringsPro(table, column, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColBools] */
    fun getColBools(                                                  table: String,
                                                                     column: BoolCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBools") {
        it.getColBools(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getColBoolsPro] */
    fun getColBoolsPro(                                                table: String,
                                                                      column: BoolCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBoolsPro") {
        it.getColBoolsPro(table, column, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColLongs] */
    fun getColLongs(                                                  table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongs") {
        it.getColLongs(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getColLongsPro] */
    fun getColLongsPro(                                                table: String,
                                                                      column: LongCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongsPro") {
        it.getColLongsPro(table, column, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColFloats] */
    fun getColFloats(                                                 table: String,
                                                                     column: FloatCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloats") {
        it.getColFloats(table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getColFloatsPro] */
    fun getColFloatsPro(                                               table: String,
                                                                      column: FloatCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloatsPro") {
        it.getColFloatsPro(table, column, dsl)
    }
    
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getColBlobs] */
    fun getColBlobs(                                                  table: String,
                                                                     column: BlobCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobs") {
        it.getColBlobs(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getColBlobsPro] */
    fun getColBlobsPro(                                                table: String,
                                                                      column: BlobCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobsPro") {
        it.getColBlobsPro(table, column, dsl)
    }
    
    
    
    
    // =================================   R O W   V A L s   ================================= \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowInts] */
    fun getRowInts(                                                   table: String,
                                                                    columns: Array<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getRowInts") {
        it.getRowInts(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowIntsPro] */
    fun getRowIntsPro(                                                 table: String,
                                                                     columns: Array<IntCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getRowIntsPro") {
        it.getRowIntsPro(table, columns, dsl)
    }
    
    
    //  -----------------------------------  S T R I N G  -----------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowStrings] */
    fun getRowStrings(                                                table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStrings") {
        it.getRowStrings(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowStringsPro] */
    fun getRowStringsPro(                                              table: String,
                                                                     columns: Array<StrCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStringsPro") {
        it.getRowStringsPro(table, columns, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowBools] */
    fun getRowBools(                                                  table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBools") {
        it.getRowBools(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowBoolsPro] */
    fun getRowBoolsPro(                                                table: String,
                                                                     columns: Array<BoolCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBoolsPro") {
        it.getRowBoolsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowLongs] */
    fun getRowLongs(                                                  table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongs") {
        it.getRowLongs(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowLongsPro] */
    fun getRowLongsPro(                                                table: String,
                                                                     columns: Array<LongCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongsPro") {
        it.getRowLongsPro(table, columns, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowFloats] */
    fun getRowFloats(                                                 table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloats") {
        it.getRowFloats(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowFloatsPro] */
    fun getRowFloatsPro(                                               table: String,
                                                                     columns: Array<FloatCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloatsPro") {
        it.getRowFloatsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getRowBlobs] */
    fun getRowBlobs(                                                  table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobs") {
        it.getRowBlobs(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowBlobsPro] */
    fun getRowBlobsPro(                                                table: String,
                                                                     columns: Array<BlobCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobsPro") {
        it.getRowBlobsPro(table, columns, dsl)
    }
    
    
    
    
    
    
    // ===============================   T A B L E   V A L s   =============================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableInts] */
    fun getTableInts(                                                 table: String,
                                                                    columns: Array<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Int>> = read(emptyList(), "getTableInts") {
        it.getTableInts(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableIntsPro] */
    fun getTableIntsPro(                                               table: String,
                                                                     columns: Array<IntCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Int>> = read(emptyList(), "getTableIntsPro") {
        it.getTableIntsPro(table, columns, dsl)
    }
    
    
    //  -----------------------------------  S T R I N G  -----------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableStrings] */
    fun getTableStrings(                                              table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStrings") {
        it.getTableStrings(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableStringsPro] */
    fun getTableStringsPro(                                            table: String,
                                                                     columns: Array<StrCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStringsPro") {
        it.getTableStringsPro(table, columns, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableBools] */
    fun getTableBools(                                                table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBools") {
        it.getTableBools(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableBoolsPro] */
    fun getTableBoolsPro(                                              table: String,
                                                                     columns: Array<BoolCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBoolsPro") {
        it.getTableBoolsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableLongs] */
    fun getTableLongs(                                                table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongs") {
        it.getTableLongs(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableLongsPro] */
    fun getTableLongsPro(                                              table: String,
                                                                     columns: Array<LongCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongsPro") {
        it.getTableLongsPro(table, columns, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableFloats] */
    fun getTableFloats(                                               table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloats") {
        it.getTableFloats(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableFloatsPro] */
    fun getTableFloatsPro(                                             table: String,
                                                                     columns: Array<FloatCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloatsPro") {
        it.getTableFloatsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    /** A wrapper fun for [SQLiteDatabase.getTableBlobs] */
    fun getTableBlobs(                                                table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobs") {
        it.getTableBlobs(table, columns, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getTableBlobsPro] */
    fun getTableBlobsPro(                                              table: String,
                                                                     columns: Array<BlobCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobsPro") {
        it.getTableBlobsPro(table, columns, dsl)
    }
    
    
    
    
    
    
    // ===================================   M O D I F Y   =================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.addToInt] */
    fun addToInt(                                                    addend: Number,
                                                                      table: String,
                                                                     column: IntCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("addToInt", async) {
        it.addToInt(addend, table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addToLong] */
    fun addToLong(                                                   addend: Number,
                                                                      table: String,
                                                                     column: LongCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("addToLong", async) {
        it.addToLong(addend, table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.addToFloat] */
    fun addToFloat(                                                  addend: Number,
                                                                      table: String,
                                                                     column: FloatCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("addToFloat", async) {
        it.addToFloat(addend, table, column, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.flipBool] */
    fun flipBool(                                                     table: String,
                                                                     column: BoolCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("flipBool", async) {
        it.flipBool(table, column, where)
    }
    
    
    
    
    
    
    
    
    // ============================   D E L E T E,  C L E A R   ============================ \\
    
    /** A wrapper fun for [SQLiteDatabase.deleteRows] */
    fun deleteRows(                                                        table: String,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("deleteRows", async) {
        it.deleteRows(table, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.deleteRowsFromTables] */
    fun deleteRowsFromTables(                                             tables: List<String>,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("deleteRowsFromTables", async) {
        it.deleteRowsFromTables(tables, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.deleteRowsFromTables] */
    fun deleteRowsFromTables(                                      vararg tables: String,
                                                                           where: WhereDsl.()->Unit,
    ) = write("deleteRowsFromTables") {
        it.deleteRowsFromTables(tables.toList(), where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.deleteFirstRow] */
    inline fun deleteFirstRow(                                              table: String,
                                                                            async: Boolean = false,
    ) = write("deleteFirstRow", async) {
        it.deleteFirstRow(table)
    }
    
    /** A wrapper fun for [SQLiteDatabase.deleteLastRow] */
    inline fun deleteLastRow(                                               table: String,
                                                                            async: Boolean = false,
    ) = write("deleteLastRow", async) {
        it.deleteLastRow(table)
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
    ) = read(0, "getRowCount") {
        it.getRowCount(table, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getRowCountPro] */
    fun getRowCountPro(                                                table: String,
                                                                         dsl: FullDsl.()->Unit = {},
    ) = read(0, "getRowCountPro") {
        it.getRowCountPro(table, dsl)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.hasRows] */
    fun hasRows(                                                      table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(false, "hasRows") {
        it.hasRows(table, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.hasRowsPro] */
    fun hasRowsPro(                                                         table: String,
                                                                              dsl: FullDsl.()->Unit,
    ) = read(false, "hasRowsPro") {
        it.hasRowsPro(table, dsl)
    }
    
    
    
    
    
    
    
    
    // ====================================  O T H E R  ==================================== \\
    
    /** A wrapper fun for [SQLiteDatabase.getLastId] */
    inline fun getLastId(table: String): Long = read(0L, "getLastId") { it.getLastId(table) }
    
    
    /** A wrapper fun for [SQLiteDatabase.getId] */
    fun getId(                                                             table: String,
                                                                           where: WhereDsl.()->Unit,
    ): Long = read(-1L, "getId") {
        it.getId(table, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getIDs] */
    fun getIDs(                                                       table: String,
                                                                      where: WhereDsl.()->Unit,
                                                                    orderBy: OrderDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getIDs") {
        it.getIDs(table, where, orderBy)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getAllIDs] */
    fun getAllIDs(                                                    table: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getAllIDs") {
        it.getAllIDs(table, orderBy)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getNewIDs] */
    fun getNewIDs(                                                    table: String,
                                                                      count: Int,
                                                                reservedIDs: Set<Long> = emptySet(),
    ): List<Long> = read(emptyList(), "getNewIDs") {
        it.getNewIDs(table, count, reservedIDs)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getNewId] */
    fun getNewId(                                                    table: String,
                                                                reservedIDs: Set<Long> = emptySet(),
    ): Long = read(-1L, "getNewId") {
        it.getNewId(table, reservedIDs)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.tableExists] */
    inline fun tableExists(                                                        table: String
    ): Boolean = read(false, "tableExists") {
        it.tableExists(table)
    }
    
    /** A wrapper fun for [SQLiteDatabase.isTableEmpty] */
    inline fun isTableEmpty(                                                       table: String
    ): Boolean = read(true, "isTableEmpty") {
        it.isTableEmpty(table)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getAppTableNames] */
    inline fun getAppTableNames(): List<String> = read(emptyList(), "getAppTableNames") {
        it.getAppTableNames()
    }
    
    /** A wrapper fun for [SQLiteDatabase.getInternalTableNames] */
    inline fun getInternalTableNames(): List<String> = read(emptyList(), "getInternalTableNames") {
        it.getInternalTableNames()
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.getRawTableStr] */
    inline fun getRawTableStr(                                                     table: String
    ): RawTableStr = read(RawTableStr(), "getRawTableStr") {
        it.getRawTableStr(table)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLastPosition] */
    fun getLastPosition(                                              table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Long = read(0L, "getLastPosition") {
        it.getLastPosition(table, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLargestInt] */
    fun getLargestInt(                                                table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(0, "getLargestInt") {
        it.getLargestInt(table, column, where)
    }
    
    /** A wrapper fun for [SQLiteDatabase.getLargestLong] */
    fun getLargestLong(                                               table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(0L, "getLargestLong") {
        it.getLargestLong(table, column, where)
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
    ): Boolean = readWrite(false, "reorder") {
        it.reorder(table, id, moveUpOrBack, makeFirstOrLast, where)
    }
    
    
    /** A wrapper fun for [SQLiteDatabase.createTable] */
    inline fun createTable(                            tableInfo: TableInfoNormal<out NormalEntity>,
                                                           async: Boolean = false,
    ) = write("createTable", async) {
        it.createTable(tableInfo)
    }
    
    /** A wrapper fun for [SQLiteDatabase.createTables] */
    inline fun createTables(                       vararg tables: TableInfoNormal<out NormalEntity>,
                                                           async: Boolean = false,
    ) = write("createTables", async) {
        it.createTables(*tables)
    }
    
    /** A wrapper fun for [SQLiteDatabase.createTables] */
    inline fun createTables(                        tables: List<TableInfoNormal<out NormalEntity>>,
                                                     async: Boolean = false,
    ) = write("createTables", async) {
        it.createTables(tables)
    }
    
}
