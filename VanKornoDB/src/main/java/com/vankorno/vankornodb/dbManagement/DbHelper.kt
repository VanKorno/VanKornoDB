// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.add.addObj
import com.vankorno.vankornodb.add.addObjects
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
    
    fun useCursor(                                                    table: String,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.getCursor(table, where).use { cursor ->
            block(cursor)
        }
    }
    
    fun useCursor(                                                 table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                   where: WhereDsl.()->Unit = {},
                                                                   block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.getCursor(table, columns, where).use { cursor ->
            block(cursor)
        }
    }
    
    fun useCursor(                                                    table: String,
                                                                     column: TypedColumn<*>,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->Unit,
    ) = write("useCursor") { db ->
        db.getCursor(table, column, where).use { cursor ->
            block(cursor)
        }
    }
    
    
    fun useCursorPro(                                                       table: String,
                                                                              dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->Unit,
    ) = write("useCursorPro") { db ->
        db.getCursorPro(table, dsl).use { cursor ->
            block(cursor)
        }
    }
    
    fun useCursorPro(                                              table: String,
                                                                 columns: Array<out TypedColumn<*>>,
                                                                     dsl: FullDsl.()->Unit,
                                                                   block: (Cursor)->Unit,
    ) = write("useCursorPro") { db ->
        db.getCursorPro(table, columns, dsl).use { cursor ->
            block(cursor)
        }
    }
    
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
    
    fun <T> getFromCursor(                                            table: String,
                                                               defaultValue: T,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursor") { db ->
        db.getCursor(table, where).use { cursor ->
            block(cursor)
        }
    }
    
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
    
    
    fun <T> getFromCursorPro(                                               table: String,
                                                                     defaultValue: T,
                                                                              dsl: FullDsl.()->Unit,
                                                                            block: (Cursor)->T,
    ): T = readWrite(defaultValue, "getFromCursorPro") { db ->
        db.getCursorPro(table, dsl).use { cursor ->
            block(cursor)
        }
    }
    
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
    
    fun setInt(                                                            value: Int,
                                                                           table: String,
                                                                          column: IntCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setInt", async) {
        it.setInt(value, table, column, where)
    }
    
    fun setStr(                                                            value: String,
                                                                           table: String,
                                                                          column: StrCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setStr", async) {
        it.setStr(value, table, column, where)
    }
    
    fun setBool(                                                           value: Boolean,
                                                                           table: String,
                                                                          column: BoolCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setBool", async) {
        it.setBool(value, table, column, where)
    }
    
    fun setLong(                                                           value: Long,
                                                                           table: String,
                                                                          column: LongCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setLong", async) {
        it.setLong(value, table, column, where)
    }
    
    fun setFloat(                                                          value: Float,
                                                                           table: String,
                                                                          column: FloatCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setFloat", async) {
        it.setFloat(value, table, column, where)
    }
    
    fun setBlob(                                                           value: ByteArray,
                                                                           table: String,
                                                                          column: BlobCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setBlob", async) {
        it.setBlob(value, table, column, where)
    }
    
    
    
    
    // =============================  M U L T I - S E T T E R S  ============================= \\
    
    fun set(                                                          table: String,
                                                                      where: WhereDsl.()->Unit = {},
                                                                      async: Boolean = false,
                                                                    actions: SetDsl.()->Unit,
    ) = write("set", async) {
        it.set(table, where, actions)
    }
    
    
    
    // -----------------------------------  O B J E C T  ----------------------------------- \\
    
    inline fun <T : CurrEntity> addObj(                               tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
                                                                          async: Boolean = false,
    ) = write("addObj", async) {
        it.addObj(tableInfo, obj)
    }
    
    
    inline fun <T : CurrEntity> addObjects(                           tableInfo: TableInfoNormal<T>,
                                                                        objects: List<T>,
                                                                          async: Boolean = false,
    ) = write("addObjects", async) {
        it.addObjects(tableInfo, objects)
    }
    
    
    
    fun <T : CurrEntity> setObj(                                      tableInfo: TableInfoNormal<T>,
                                                                            obj: T,
                                                                          async: Boolean = false,
                                                                          where: WhereDsl.()->Unit,
    ) = write("setObj", async) {
        it.setObj(tableInfo, obj, where)
    }
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
    fun getInt(                                                            table: String,
                                                                          column: IntCol,
                                                                           where: WhereDsl.()->Unit,
    ): Int = read(0, "getInt") {
        it.getInt(table, column, where)
    }
    
    fun getStr(                                                            table: String,
                                                                          column: StrCol,
                                                                           where: WhereDsl.()->Unit,
    ): String = read("", "getStr") {
        it.getStr(table, column, where)
    }
    
    fun getBool(                                                           table: String,
                                                                          column: BoolCol,
                                                                           where: WhereDsl.()->Unit,
    ): Boolean = read(false, "getBool") {
        it.getBool(table, column, where)
    }
    
    fun getLong(                                                           table: String,
                                                                          column: LongCol,
                                                                           where: WhereDsl.()->Unit,
    ): Long = read(0L, "getLong") {
        it.getLong(table, column, where)
    }
    
    fun getFloat(                                                          table: String,
                                                                          column: FloatCol,
                                                                           where: WhereDsl.()->Unit,
    ): Float = read(0F, "getFloat") {
        it.getFloat(table, column, where)
    }
    
    fun getBlob(                                                           table: String,
                                                                          column: BlobCol,
                                                                           where: WhereDsl.()->Unit,
    ): ByteArray = read(ByteArray(0), "getBlob") {
        it.getBlob(table, column, where)
    }
    
    
    
    // ----------------------------------  G E T   P R O  ---------------------------------- \\
    
    fun getIntPro(                                                          table: String,
                                                                           column: IntCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Int = read(-1, "getIntPro") {
        it.getIntPro(table, column, dsl)
    }
    
    fun getStrPro(                                                          table: String,
                                                                           column: StrCol,
                                                                              dsl: FullDsl.()->Unit,
    ): String = read("", "getStrPro") {
        it.getStrPro(table, column, dsl)
    }
    
    fun getBoolPro(                                                         table: String,
                                                                           column: BoolCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Boolean = read(false, "getBoolPro") {
        it.getBoolPro(table, column, dsl)
    }
    
    fun getLongPro(                                                         table: String,
                                                                           column: LongCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Long = read(-1L, "getLongPro") {
        it.getLongPro(table, column, dsl)
    }
    
    fun getFloatPro(                                                        table: String,
                                                                           column: FloatCol,
                                                                              dsl: FullDsl.()->Unit,
    ): Float = read(-1F, "getFloatPro") {
        it.getFloatPro(table, column, dsl)
    }
    
    fun getBlobPro(                                                         table: String,
                                                                           column: BlobCol,
                                                                              dsl: FullDsl.()->Unit,
    ): ByteArray = read(ByteArray(0), "getBlobPro") {
        it.getBlobPro(table, column, dsl)
    }
    
    
    
    
    
    
    // ==============================  G E T   O B J E C T S  ============================== \\
    
    fun <T : BaseEntity> getObj(                                  tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getObj") {
        it.getObj(tableInfo, where)
    }
    
    fun <T : BaseEntity> getObj(                                  tableInfo: TableInfoBase<T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
    ): T = read(default, "getObj") {
        it.getObj(tableInfo, default, where)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    fun <T : BaseEntity> getObjPro(                                     tableInfo: TableInfoBase<T>,
                                                                              dsl: FullDsl.()->Unit,
    ): T? = read(null, "getObjPro") {
        it.getObjPro(tableInfo, dsl)
    }
    
    fun <T : BaseEntity> getObjPro(                                 tableInfo: TableInfoBase<T>,
                                                                      default: T,
                                                                          dsl: FullDsl.()->Unit,
    ): T = read(default, "getObjPro") {
        it.getObjPro(tableInfo, default, dsl)
    }
    
    
    
    inline fun <reified T : BaseEntity> getRandomObj(             tableInfo: TableInfoBase<out T>,
                                                             noinline where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getRandomObj") { db ->
        db.getRandomObj<T>(tableInfo, where)
    }
    
    fun <T : BaseEntity> getLastObj(                              tableInfo: TableInfoBase<out T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getLastObj") { db ->
        db.getLastObj(tableInfo, where)
    }
    
    
    
    // =========================  M U L T I P L E   O B J E C T S  ========================= \\
    
    fun <T : BaseEntity> getObjects(                              tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjectsPro(tableInfo) { this.where = where }
    }
    
    
    fun <T : BaseEntity> getObjMap(                               tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): Map<Int, T> = read(emptyMap(), "getObjMap") {
        it.getObjMapPro(tableInfo) { this.where = where }
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    fun <T : BaseEntity> getObjectsPro(                            tableInfo: TableInfoBase<T>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjectsPro") {
        it.getObjectsPro(tableInfo, dsl)
    }
    
    
    fun <T : BaseEntity> getObjMapPro(                             tableInfo: TableInfoBase<T>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): Map<Int, T> = read(emptyMap(), "getObjMapPro") {
        it.getObjMapPro(tableInfo, dsl)
    }
    
    
    
    
    
    
    
    
    // ==============================   C O L U M N   V A L s   ============================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun getColInts(                                                   table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColInts") {
        it.getColInts(table, column, where)
    }
    
    fun getColIntsPro(                                                 table: String,
                                                                      column: IntCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColIntsPro") {
        it.getColIntsPro(table, column, dsl)
    }
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    fun getColStrings(                                                table: String,
                                                                     column: StrCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStrings") {
        it.getColStrings(table, column, where)
    }
    
    fun getColStringsPro(                                              table: String,
                                                                      column: StrCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStringsPro") {
        it.getColStringsPro(table, column, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getColBools(                                                  table: String,
                                                                     column: BoolCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBools") {
        it.getColBools(table, column, where)
    }
    
    fun getColBoolsPro(                                                table: String,
                                                                      column: BoolCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBoolsPro") {
        it.getColBoolsPro(table, column, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getColLongs(                                                  table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongs") {
        it.getColLongs(table, column, where)
    }
    
    fun getColLongsPro(                                                table: String,
                                                                      column: LongCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongsPro") {
        it.getColLongsPro(table, column, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getColFloats(                                                 table: String,
                                                                     column: FloatCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloats") {
        it.getColFloats(table, column, where)
    }
    
    
    fun getColFloatsPro(                                               table: String,
                                                                      column: FloatCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloatsPro") {
        it.getColFloatsPro(table, column, dsl)
    }
    
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getColBlobs(                                                  table: String,
                                                                     column: BlobCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobs") {
        it.getColBlobs(table, column, where)
    }
    
    fun getColBlobsPro(                                                table: String,
                                                                      column: BlobCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobsPro") {
        it.getColBlobsPro(table, column, dsl)
    }
    
    
    
    
    // =================================   R O W   V A L s   ================================= \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun getRowInts(                                                   table: String,
                                                                    columns: Array<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getRowInts") {
        it.getRowInts(table, columns, where)
    }
    
    fun getRowIntsPro(                                                 table: String,
                                                                     columns: Array<IntCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getRowIntsPro") {
        it.getRowIntsPro(table, columns, dsl)
    }
    
    
    //  -----------------------------------  S T R I N G  -----------------------------------  \\
    
    fun getRowStrings(                                                table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStrings") {
        it.getRowStrings(table, columns, where)
    }
    
    fun getRowStringsPro(                                              table: String,
                                                                     columns: Array<StrCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStringsPro") {
        it.getRowStringsPro(table, columns, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getRowBools(                                                  table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBools") {
        it.getRowBools(table, columns, where)
    }
    
    fun getRowBoolsPro(                                                table: String,
                                                                     columns: Array<BoolCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBoolsPro") {
        it.getRowBoolsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getRowLongs(                                                  table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongs") {
        it.getRowLongs(table, columns, where)
    }
    
    fun getRowLongsPro(                                                table: String,
                                                                     columns: Array<LongCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongsPro") {
        it.getRowLongsPro(table, columns, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getRowFloats(                                                 table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloats") {
        it.getRowFloats(table, columns, where)
    }
    
    fun getRowFloatsPro(                                               table: String,
                                                                     columns: Array<FloatCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloatsPro") {
        it.getRowFloatsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getRowBlobs(                                                  table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobs") {
        it.getRowBlobs(table, columns, where)
    }
    
    fun getRowBlobsPro(                                                table: String,
                                                                     columns: Array<BlobCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobsPro") {
        it.getRowBlobsPro(table, columns, dsl)
    }
    
    
    
    
    
    
    // ===============================   T A B L E   V A L s   =============================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun getTableInts(                                                 table: String,
                                                                    columns: Array<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Int>> = read(emptyList(), "getTableInts") {
        it.getTableInts(table, columns, where)
    }
    
    fun getTableIntsPro(                                               table: String,
                                                                     columns: Array<IntCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Int>> = read(emptyList(), "getTableIntsPro") {
        it.getTableIntsPro(table, columns, dsl)
    }
    
    
    //  -----------------------------------  S T R I N G  -----------------------------------  \\
    
    fun getTableStrings(                                              table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStrings") {
        it.getTableStrings(table, columns, where)
    }
    
    fun getTableStringsPro(                                            table: String,
                                                                     columns: Array<StrCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStringsPro") {
        it.getTableStringsPro(table, columns, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getTableBools(                                                table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBools") {
        it.getTableBools(table, columns, where)
    }
    
    fun getTableBoolsPro(                                              table: String,
                                                                     columns: Array<BoolCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBoolsPro") {
        it.getTableBoolsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getTableLongs(                                                table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongs") {
        it.getTableLongs(table, columns, where)
    }
    
    fun getTableLongsPro(                                              table: String,
                                                                     columns: Array<LongCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongsPro") {
        it.getTableLongsPro(table, columns, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getTableFloats(                                               table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloats") {
        it.getTableFloats(table, columns, where)
    }
    
    fun getTableFloatsPro(                                             table: String,
                                                                     columns: Array<FloatCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloatsPro") {
        it.getTableFloatsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getTableBlobs(                                                table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobs") {
        it.getTableBlobs(table, columns, where)
    }
    
    fun getTableBlobsPro(                                              table: String,
                                                                     columns: Array<BlobCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobsPro") {
        it.getTableBlobsPro(table, columns, dsl)
    }
    
    
    
    
    
    
    // ===================================   M O D I F Y   =================================== \\
    
    fun addToInt(                                                    addend: Number,
                                                                      table: String,
                                                                     column: IntCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("addToInt", async) {
        it.addToInt(addend, table, column, where)
    }
    
    
    fun addToLong(                                                   addend: Number,
                                                                      table: String,
                                                                     column: LongCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("addToLong", async) {
        it.addToLong(addend, table, column, where)
    }
    
    
    fun addToFloat(                                                  addend: Number,
                                                                      table: String,
                                                                     column: FloatCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("addToFloat", async) {
        it.addToFloat(addend, table, column, where)
    }
    
    
    fun flipBool(                                                     table: String,
                                                                     column: BoolCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("flipBool", async) {
        it.flipBool(table, column, where)
    }
    
    
    
    
    
    
    
    
    // ============================   D E L E T E,  C L E A R   ============================ \\
    
    inline fun <T> deleteRows(                                              table: String,
                                                                            where: String,
                                                                           equals: T,
                                                                            async: Boolean = false,
    ) = write("deleteRows", async) {
        it.deleteRows(table, where, equals)
    }
    
    fun deleteRows(                                                        table: String,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("deleteRows", async) {
        it.deleteRows(table, where)
    }
    
    
    inline fun deleteFirstRow(                                              table: String,
                                                                            async: Boolean = false,
    ) = write("deleteFirstRow", async) {
        it.deleteFirstRow(table)
    }
    
    inline fun deleteLastRow(                                               table: String,
                                                                            async: Boolean = false,
    ) = write("deleteLastRow", async) {
        it.deleteLastRow(table)
    }
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun clearTable(                                                  table: String,
                                                                            async: Boolean = false,
    ) = write("clearTable", async) { it.clearTable(table) }
    
    
    inline fun clearTables(                                         vararg tables: String,
                                                                            async: Boolean = false,
    ) = write("clearTables", async) { it.clearTables(*tables) }
    
    inline fun clearTables(                                                tables: List<String>,
                                                                            async: Boolean = false,
    ) = write("clearTables", async) { it.clearTables(tables) }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun deleteTable(                                                 table: String,
                                                                            async: Boolean = false,
    ) = write("deleteTable", async) { it.deleteTable(table) }
    
    
    inline fun deleteTables(                                        vararg tables: String,
                                                                            async: Boolean = false,
    ) = write("deleteTables", async) { it.deleteTables(*tables) }
    
    inline fun deleteTables(                                               tables: List<String>,
                                                                            async: Boolean = false,
    ) = write("deleteTables", async) { it.deleteTables(tables) }
    
    
    
    
    
    // ====================================  C O U N T  ==================================== \\
    
    /** Returns the number of rows matching the query conditions. */
    fun getRowCount(                                                  table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(0, "getRowCount") {
        it.getRowCount(table, where)
    }
    
    /** Returns the number of rows matching the query conditions. */
    fun getRowCountPro(                                                table: String,
                                                                         dsl: FullDsl.()->Unit = {},
    ) = read(0, "getRowCountPro") {
        it.getRowCountPro(table, dsl)
    }
    
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRows(                                                      table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(false, "hasRows") {
        it.hasRows(table, where)
    }
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRowsPro(                                                         table: String,
                                                                              dsl: FullDsl.()->Unit,
    ) = read(false, "hasRowsPro") {
        it.hasRowsPro(table, dsl)
    }
    
    
    
    
    
    
    
    
    // ====================================  O T H E R  ==================================== \\
    
    inline fun getLastId(                                                          table: String
    ): Int = read(0, "getLastId") {
        it.getLastId(table)
    }
    
    
    fun getAllIDs(                                                    table: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getAllIDs") {
        it.getAllIDs(table, orderBy)
    }
    
    
    inline fun tableExists(                                                        table: String
    ): Boolean = read(false, "tableExists") {
        it.tableExists(table)
    }
    
    
    inline fun isTableEmpty(                                                       table: String
    ): Boolean = read(true, "isTableEmpty") {
        it.isTableEmpty(table)
    }
    
    
    inline fun getAppTableNames(): List<String> = read(emptyList(), "getAppTableNames") {
        it.getAppTableNames()
    }
    
    inline fun getInternalTableNames(): List<String> = read(emptyList(), "getInternalTableNames") {
        it.getInternalTableNames()
    }
    
    
    inline fun getRawTableStr(                                                     table: String
    ): RawTableStr = read(RawTableStr(), "getRawTableStr") {
        it.getRawTableStr(table)
    }
    
    
    fun getLastPosition(                                              table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = read(0, "getLastPosition") {
        it.getLastPosition(table, where)
    }
    
    
    fun getLargestInt(                                                table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(0, "getLargestInt") {
        it.getLargestInt(table, column, where)
    }
    
    
    inline fun getDbFileName() = read("", "getDbFileName") { it.getDbFileName() }
    
    
    fun getRandomInt(                                                 table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = read(-1, "getRandomInt") { db ->
        db.getRandomInt(table, column, where)
    }
    
    
    fun getRandomId(                                                  table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = read(-1, "getRandomId") { db ->
        db.getRandomId(table, where)
    }
    
    
    fun reorder(                                                      table: String,
                                                                         id: Int,
                                                               moveUpOrBack: Boolean,
                                                            makeFirstOrLast: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ): Boolean = readWrite(false, "reorder") {
        it.reorder(table, id, moveUpOrBack, makeFirstOrLast, where)
    }
    
    
    inline fun createTable(                            tableInfo: TableInfoNormal<out NormalEntity>,
                                                           async: Boolean = false,
    ) = write("createTable", async) {
        it.createTable(tableInfo)
    }
    
    
    inline fun createTables(                       vararg tables: TableInfoNormal<out NormalEntity>,
                                                           async: Boolean = false,
    ) = write("createTables", async) {
        it.createTables(*tables)
    }
    
    
    inline fun createTables(                        tables: List<TableInfoNormal<out NormalEntity>>,
                                                     async: Boolean = false,
    ) = write("createTables", async) {
        it.createTables(tables)
    }
    
    
    
    
    
    
    
}