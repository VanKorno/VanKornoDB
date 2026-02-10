// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement

import android.content.Context
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
    
    // ===================================  S E T T E R S  =================================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun setInt(                                                            value: Int,
                                                                           table: String,
                                                                          column: IntCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setInt", async) {
        it.setInt(value, table, column, where)
    }
    
    suspend fun setIntSusp(                                                value: Int,
                                                                           table: String,
                                                                          column: IntCol,
                                                                           where: WhereDsl.()->Unit,
    ) = writeSusp("setIntSusp") {
        it.setInt(value, table, column, where)
    }
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    fun setStr(                                                            value: String,
                                                                           table: String,
                                                                          column: StrCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setStr", async) {
        it.setStr(value, table, column, where)
    }
    
    suspend fun setStrSusp(                                                value: String,
                                                                           table: String,
                                                                          column: StrCol,
                                                                           where: WhereDsl.()->Unit,
    ) = writeSusp("setStrSusp") {
        it.setStr(value, table, column, where)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun setBool(                                                           value: Boolean,
                                                                           table: String,
                                                                          column: BoolCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setBool", async) {
        it.setBool(value, table, column, where)
    }
    
    suspend fun setBoolSusp(                                               value: Boolean,
                                                                           table: String,
                                                                          column: BoolCol,
                                                                           where: WhereDsl.()->Unit,
    ) = writeSusp("setBoolSusp") {
        it.setBool(value, table, column, where)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun setLong(                                                           value: Long,
                                                                           table: String,
                                                                          column: LongCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setLong", async) {
        it.setLong(value, table, column, where)
    }
    
    suspend fun setLongSusp(                                               value: Long,
                                                                           table: String,
                                                                          column: LongCol,
                                                                           where: WhereDsl.()->Unit,
    ) = writeSusp("setLongSusp") {
        it.setLong(value, table, column, where)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun setFloat(                                                          value: Float,
                                                                           table: String,
                                                                          column: FloatCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setFloat", async) {
        it.setFloat(value, table, column, where)
    }
    
    suspend fun setFloatSusp(                                              value: Float,
                                                                           table: String,
                                                                          column: FloatCol,
                                                                           where: WhereDsl.()->Unit,
    ) = writeSusp("setFloatSusp") {
        it.setFloat(value, table, column, where)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun setBlob(                                                           value: ByteArray,
                                                                           table: String,
                                                                          column: BlobCol,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("setBlob", async) {
        it.setBlob(value, table, column, where)
    }
    
    suspend fun setBlobSusp(                                               value: ByteArray,
                                                                           table: String,
                                                                          column: BlobCol,
                                                                           where: WhereDsl.()->Unit,
    ) = writeSusp("setBlobSusp") {
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
    
    suspend fun setSusp(                                              table: String,
                                                                      where: WhereDsl.()->Unit = {},
                                                                    actions: SetDsl.()->Unit,
    ) = writeSusp("setSusp") {
        it.set(table, where, actions)
    }
    
    
    
    
    
    
    
    
    // -----------------------------------  O B J E C T  ----------------------------------- \\
    
    inline fun <T : CurrEntity> addObj(                           tableInfo: TableInfoNormal<out T>,
                                                                        obj: T,
                                                                      async: Boolean = false,
    ) = write("addObj", async) {
        it.addObj(tableInfo, obj)
    }
    
    
    suspend fun <T : CurrEntity> addObjSusp(                      tableInfo: TableInfoNormal<out T>,
                                                                        obj: T,
    ): Long = readWriteSusp(-1L, "addObjSusp") {
        it.addObj(tableInfo, obj)
    }
    
    
    inline fun <T : CurrEntity> addObjects(                       tableInfo: TableInfoNormal<out T>,
                                                                    objects: List<T>,
                                                                      async: Boolean = false,
    ) = write("addObjects", async) {
        it.addObjects(tableInfo, objects)
    }
    
    
    suspend fun <T : CurrEntity> addObjectsSusp(                  tableInfo: TableInfoNormal<out T>,
                                                                    objects: List<T>,
    ): Int = readWriteSusp(0, "addObjectsSusp") {
        it.addObjects(tableInfo, objects)
    }
    
    
    
    fun <T : CurrEntity> setObj(                                  tableInfo: TableInfoNormal<out T>,
                                                                        obj: T,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit,
    ) = write("setObj", async) {
        it.setObj(tableInfo, obj, where)
    }
    
    
    suspend fun <T : CurrEntity> setObjSusp(                      tableInfo: TableInfoNormal<out T>,
                                                                        obj: T,
                                                                      where: WhereDsl.()->Unit,
    ): Int = readWriteSusp(0, "setObjSusp") {
        it.setObj(tableInfo, obj, where)
    }
    
    
    
    
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun getInt(                                                            table: String,
                                                                          column: IntCol,
                                                                           where: WhereDsl.()->Unit,
    ): Int = read(0, "getInt") {
        it.getInt(table, column, where)
    }
    
    suspend fun getIntSusp(                                                table: String,
                                                                          column: IntCol,
                                                                           where: WhereDsl.()->Unit,
    ): Int = readSusp(0, "getIntSusp") {
        it.getInt(table, column, where)
    }
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    fun getStr(                                                            table: String,
                                                                          column: StrCol,
                                                                           where: WhereDsl.()->Unit,
    ): String = read("", "getStr") {
        it.getStr(table, column, where)
    }
    
    suspend fun getStrSusp(                                                table: String,
                                                                          column: StrCol,
                                                                           where: WhereDsl.()->Unit,
    ): String = readSusp("", "getStrSusp") {
        it.getStr(table, column, where)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getBool(                                                           table: String,
                                                                          column: BoolCol,
                                                                           where: WhereDsl.()->Unit,
    ): Boolean = read(false, "getBool") {
        it.getBool(table, column, where)
    }
    
    suspend fun getBoolSusp(                                               table: String,
                                                                          column: BoolCol,
                                                                           where: WhereDsl.()->Unit,
    ): Boolean = readSusp(false, "getBoolSusp") {
        it.getBool(table, column, where)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getLong(                                                           table: String,
                                                                          column: LongCol,
                                                                           where: WhereDsl.()->Unit,
    ): Long = read(0L, "getLong") {
        it.getLong(table, column, where)
    }
    
    suspend fun getLongSusp(                                               table: String,
                                                                          column: LongCol,
                                                                           where: WhereDsl.()->Unit,
    ): Long = readSusp(0L, "getLongSusp") {
        it.getLong(table, column, where)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getFloat(                                                          table: String,
                                                                          column: FloatCol,
                                                                           where: WhereDsl.()->Unit,
    ): Float = read(0F, "getFloat") {
        it.getFloat(table, column, where)
    }
    
    suspend fun getFloatSusp(                                              table: String,
                                                                          column: FloatCol,
                                                                           where: WhereDsl.()->Unit,
    ): Float = readSusp(0F, "getFloatSusp") {
        it.getFloat(table, column, where)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getBlob(                                                           table: String,
                                                                          column: BlobCol,
                                                                           where: WhereDsl.()->Unit,
    ): ByteArray = read(ByteArray(0), "getBlob") {
        it.getBlob(table, column, where)
    }
    
    suspend fun getBlobSusp(                                               table: String,
                                                                          column: BlobCol,
                                                                           where: WhereDsl.()->Unit,
    ): ByteArray = readSusp(ByteArray(0), "getBlobSusp") {
        it.getBlob(table, column, where)
    }
    
    
    
    
    
    
    
    
    // ==============================  G E T   O B J E C T S  ============================== \\
    
    fun <T : BaseEntity> getObj(                                  tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getObj") {
        it.getObj(tableInfo, where)
    }
    
    
    suspend fun <T : BaseEntity> getObjSusp(                      tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): T? = readSusp(null, "getObjSusp") {
        it.getObj(tableInfo, where)
    }
    
    // With defaults
    
    fun <T : BaseEntity> getObj(                                  tableInfo: TableInfoBase<T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
    ): T = read(default, "getObj") {
        it.getObj(tableInfo, default, where)
    }
    
    suspend fun <T : BaseEntity> getObjSusp(                      tableInfo: TableInfoBase<T>,
                                                                    default: T,
                                                                      where: WhereDsl.()->Unit = {},
    ): T = readSusp(default, "getObjSusp") {
        it.getObj(tableInfo, default, where)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    fun <T : BaseEntity> getObjPro(                                     tableInfo: TableInfoBase<T>,
                                                                              dsl: FullDsl.()->Unit,
    ): T? = read(null, "getObjPro") {
        it.getObjPro(tableInfo, dsl)
    }
    
    
    suspend fun <T : BaseEntity> getObjProSusp(                     tableInfo: TableInfoBase<T>,
                                                                          dsl: FullDsl.()->Unit,
    ): T? = readSusp(null, "getObjProSusp") {
        it.getObjPro(tableInfo, dsl)
    }
    
    
    // With defaults
    
    fun <T : BaseEntity> getObjPro(                                 tableInfo: TableInfoBase<T>,
                                                                      default: T,
                                                                          dsl: FullDsl.()->Unit,
    ): T = read(default, "getObjPro") {
        it.getObjPro(tableInfo, default, dsl)
    }
    
    
    suspend fun <T : BaseEntity> getObjProSusp(                     tableInfo: TableInfoBase<T>,
                                                                      default: T,
                                                                          dsl: FullDsl.()->Unit,
    ): T = readSusp(default, "getObjProSusp") {
        it.getObjPro(tableInfo, default, dsl)
    }
    
    
    
    
    
    
    
    
    
    
    
    // =========================  M U L T I P L E   O B J E C T S  ========================= \\
    
    fun <T : BaseEntity> getObjects(                              tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjectsPro(tableInfo) { this.where = where }
    }
    
    suspend fun <T : BaseEntity> getObjectsSusp(                  tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<T> = readSusp(emptyList(), "getObjectsSusp") {
        it.getObjectsPro(tableInfo) { this.where = where }
    }
    
    
    
    fun <T : BaseEntity> getObjMap(                               tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): Map<Int, T> = read(emptyMap(), "getObjMap") {
        it.getObjMapPro(tableInfo) { this.where = where }
    }
    
    suspend fun <T : BaseEntity> getObjMapSusp(                   tableInfo: TableInfoBase<T>,
                                                                      where: WhereDsl.()->Unit = {},
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapSusp") {
        it.getObjMapPro(tableInfo) { this.where = where }
    }
    
    
    
    
    
    
    // -------------------------------------------------------------------------------------- \\
    
    fun <T : BaseEntity> getObjectsPro(                            tableInfo: TableInfoBase<T>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjectsPro") {
        it.getObjectsPro(tableInfo, dsl)
    }
    
    suspend fun <T : BaseEntity> getObjectsProSusp(                tableInfo: TableInfoBase<T>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<T> = readSusp(emptyList(), "getObjectsProSusp") {
        it.getObjectsPro(tableInfo, dsl)
    }
    
    // -------------------------------------------------------------------------------------- \\
    
    fun <T : BaseEntity> getObjMapPro(                             tableInfo: TableInfoBase<T>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): Map<Int, T> = read(emptyMap(), "getObjMapPro") {
        it.getObjMapPro(tableInfo, dsl)
    }
    
    suspend fun <T : BaseEntity> getObjMapProSusp(                 tableInfo: TableInfoBase<T>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapProSusp") {
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
    
    suspend fun getColIntsSusp(                                       table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = readSusp(emptyList(), "getColIntsSusp") {
        it.getColInts(table, column, where)
    }
    
    
    fun getColIntsPro(                                                 table: String,
                                                                      column: IntCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColIntsPro") {
        it.getColIntsPro(table, column, dsl)
    }
    
    suspend fun getColIntsProSusp(                                     table: String,
                                                                      column: IntCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Int> = readSusp(emptyList(), "getColIntsProSusp") {
        it.getColIntsPro(table, column, dsl)
    }
    
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    
    fun getColStrings(                                                table: String,
                                                                     column: StrCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStrings") {
        it.getColStrings(table, column, where)
    }
    
    suspend fun getColStringsSusp(                                    table: String,
                                                                     column: StrCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = readSusp(emptyList(), "getColStringsSusp") {
        it.getColStrings(table, column, where)
    }
    
    
    fun getColStringsPro(                                              table: String,
                                                                      column: StrCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStringsPro") {
        it.getColStringsPro(table, column, dsl)
    }
    
    suspend fun getColStringsProSusp(                                  table: String,
                                                                      column: StrCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<String> = readSusp(emptyList(), "getColStringsProSusp") {
        it.getColStringsPro(table, column, dsl)
    }
    
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getColBools(                                                  table: String,
                                                                     column: BoolCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBools") {
        it.getColBools(table, column, where)
    }
    
    suspend fun getColBoolsSusp(                                      table: String,
                                                                     column: BoolCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = readSusp(emptyList(), "getColBoolsSusp") {
        it.getColBools(table, column, where)
    }
    
    
    fun getColBoolsPro(                                                table: String,
                                                                      column: BoolCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBoolsPro") {
        it.getColBoolsPro(table, column, dsl)
    }
    
    suspend fun getColBoolsProSusp(                                    table: String,
                                                                      column: BoolCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Boolean> = readSusp(emptyList(), "getColBoolsProSusp") {
        it.getColBoolsPro(table, column, dsl)
    }
    
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getColLongs(                                                  table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongs") {
        it.getColLongs(table, column, where)
    }
    
    suspend fun getColLongsSusp(                                      table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = readSusp(emptyList(), "getColLongsSusp") {
        it.getColLongs(table, column, where)
    }
    
    
    fun getColLongsPro(                                                table: String,
                                                                      column: LongCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongsPro") {
        it.getColLongsPro(table, column, dsl)
    }
    
    suspend fun getColLongsProSusp(                                    table: String,
                                                                      column: LongCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Long> = readSusp(emptyList(), "getColLongsProSusp") {
        it.getColLongsPro(table, column, dsl)
    }
    
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getColFloats(                                                 table: String,
                                                                     column: FloatCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloats") {
        it.getColFloats(table, column, where)
    }
    
    suspend fun getColFloatsSusp(                                     table: String,
                                                                     column: FloatCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = readSusp(emptyList(), "getColFloatsSusp") {
        it.getColFloats(table, column, where)
    }
    
    
    
    fun getColFloatsPro(                                               table: String,
                                                                      column: FloatCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloatsPro") {
        it.getColFloatsPro(table, column, dsl)
    }
    
    suspend fun getColFloatsProSusp(                                   table: String,
                                                                      column: FloatCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Float> = readSusp(emptyList(), "getColFloatsProSusp") {
        it.getColFloatsPro(table, column, dsl)
    }
    
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getColBlobs(                                                  table: String,
                                                                     column: BlobCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobs") {
        it.getColBlobs(table, column, where)
    }
    
    suspend fun getColBlobsSusp(                                      table: String,
                                                                     column: BlobCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = readSusp(emptyList(), "getColBlobsSusp") {
        it.getColBlobs(table, column, where)
    }
    
    
    fun getColBlobsPro(                                                table: String,
                                                                      column: BlobCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobsPro") {
        it.getColBlobsPro(table, column, dsl)
    }
    
    suspend fun getColBlobsProSusp(                                    table: String,
                                                                      column: BlobCol,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = readSusp(emptyList(), "getColBlobsProSusp") {
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
    
    suspend fun getRowIntsSusp(                                       table: String,
                                                                    columns: Array<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Int> = readSusp(emptyList(), "getRowIntsSusp") {
        it.getRowInts(table, columns, where)
    }
    
    fun getRowIntsPro(                                                 table: String,
                                                                     columns: Array<IntCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getRowIntsPro") {
        it.getRowIntsPro(table, columns, dsl)
    }
    
    suspend fun getRowIntsProSusp(                                     table: String,
                                                                     columns: Array<IntCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Int> = readSusp(emptyList(), "getRowIntsProSusp") {
        it.getRowIntsPro(table, columns, dsl)
    }
    
    
    //  -----------------------------------  S T R I N G  -----------------------------------  \\
    
    fun getRowStrings(                                                table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStrings") {
        it.getRowStrings(table, columns, where)
    }
    
    suspend fun getRowStringsSusp(                                    table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<String> = readSusp(emptyList(), "getRowStringsSusp") {
        it.getRowStrings(table, columns, where)
    }
    
    fun getRowStringsPro(                                              table: String,
                                                                     columns: Array<StrCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStringsPro") {
        it.getRowStringsPro(table, columns, dsl)
    }
    
    suspend fun getRowStringsProSusp(                                  table: String,
                                                                     columns: Array<StrCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<String> = readSusp(emptyList(), "getRowStringsProSusp") {
        it.getRowStringsPro(table, columns, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getRowBools(                                                  table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBools") {
        it.getRowBools(table, columns, where)
    }
    
    suspend fun getRowBoolsSusp(                                      table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Boolean> = readSusp(emptyList(), "getRowBoolsSusp") {
        it.getRowBools(table, columns, where)
    }
    
    fun getRowBoolsPro(                                                table: String,
                                                                     columns: Array<BoolCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBoolsPro") {
        it.getRowBoolsPro(table, columns, dsl)
    }
    
    suspend fun getRowBoolsProSusp(                                    table: String,
                                                                     columns: Array<BoolCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Boolean> = readSusp(emptyList(), "getRowBoolsProSusp") {
        it.getRowBoolsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getRowLongs(                                                  table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongs") {
        it.getRowLongs(table, columns, where)
    }
    
    suspend fun getRowLongsSusp(                                      table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Long> = readSusp(emptyList(), "getRowLongsSusp") {
        it.getRowLongs(table, columns, where)
    }
    
    fun getRowLongsPro(                                                table: String,
                                                                     columns: Array<LongCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongsPro") {
        it.getRowLongsPro(table, columns, dsl)
    }
    
    suspend fun getRowLongsProSusp(                                    table: String,
                                                                     columns: Array<LongCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Long> = readSusp(emptyList(), "getRowLongsProSusp") {
        it.getRowLongsPro(table, columns, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getRowFloats(                                                 table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloats") {
        it.getRowFloats(table, columns, where)
    }
    
    suspend fun getRowFloatsSusp(                                     table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<Float> = readSusp(emptyList(), "getRowFloatsSusp") {
        it.getRowFloats(table, columns, where)
    }
    
    fun getRowFloatsPro(                                               table: String,
                                                                     columns: Array<FloatCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloatsPro") {
        it.getRowFloatsPro(table, columns, dsl)
    }
    
    suspend fun getRowFloatsProSusp(                                   table: String,
                                                                     columns: Array<FloatCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<Float> = readSusp(emptyList(), "getRowFloatsProSusp") {
        it.getRowFloatsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getRowBlobs(                                                  table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobs") {
        it.getRowBlobs(table, columns, where)
    }
    
    suspend fun getRowBlobsSusp(                                      table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = readSusp(emptyList(), "getRowBlobsSusp") {
        it.getRowBlobs(table, columns, where)
    }
    
    fun getRowBlobsPro(                                                table: String,
                                                                     columns: Array<BlobCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobsPro") {
        it.getRowBlobsPro(table, columns, dsl)
    }
    
    suspend fun getRowBlobsProSusp(                                    table: String,
                                                                     columns: Array<BlobCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = readSusp(emptyList(), "getRowBlobsProSusp") {
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
    
    suspend fun getTableIntsSusp(                                     table: String,
                                                                    columns: Array<IntCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Int>> = readSusp(emptyList(), "getTableIntsSusp") {
        it.getTableInts(table, columns, where)
    }
    
    fun getTableIntsPro(                                               table: String,
                                                                     columns: Array<IntCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Int>> = read(emptyList(), "getTableIntsPro") {
        it.getTableIntsPro(table, columns, dsl)
    }
    
    suspend fun getTableIntsProSusp(                                   table: String,
                                                                     columns: Array<IntCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Int>> = readSusp(emptyList(), "getTableIntsProSusp") {
        it.getTableIntsPro(table, columns, dsl)
    }
    
    
    //  -----------------------------------  S T R I N G  -----------------------------------  \\
    
    fun getTableStrings(                                              table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStrings") {
        it.getTableStrings(table, columns, where)
    }
    
    suspend fun getTableStringsSusp(                                  table: String,
                                                                    columns: Array<StrCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<String>> = readSusp(emptyList(), "getTableStringsSusp") {
        it.getTableStrings(table, columns, where)
    }
    
    fun getTableStringsPro(                                            table: String,
                                                                     columns: Array<StrCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStringsPro") {
        it.getTableStringsPro(table, columns, dsl)
    }
    
    suspend fun getTableStringsProSusp(                                table: String,
                                                                     columns: Array<StrCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<String>> = readSusp(emptyList(), "getTableStringsProSusp") {
        it.getTableStringsPro(table, columns, dsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getTableBools(                                                table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBools") {
        it.getTableBools(table, columns, where)
    }
    
    suspend fun getTableBoolsSusp(                                    table: String,
                                                                    columns: Array<BoolCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Boolean>> = readSusp(emptyList(), "getTableBoolsSusp") {
        it.getTableBools(table, columns, where)
    }
    
    fun getTableBoolsPro(                                              table: String,
                                                                     columns: Array<BoolCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBoolsPro") {
        it.getTableBoolsPro(table, columns, dsl)
    }
    
    suspend fun getTableBoolsProSusp(                                  table: String,
                                                                     columns: Array<BoolCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Boolean>> = readSusp(emptyList(), "getTableBoolsProSusp") {
        it.getTableBoolsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getTableLongs(                                                table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongs") {
        it.getTableLongs(table, columns, where)
    }
    
    suspend fun getTableLongsSusp(                                    table: String,
                                                                    columns: Array<LongCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Long>> = readSusp(emptyList(), "getTableLongsSusp") {
        it.getTableLongs(table, columns, where)
    }
    
    fun getTableLongsPro(                                              table: String,
                                                                     columns: Array<LongCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongsPro") {
        it.getTableLongsPro(table, columns, dsl)
    }
    
    suspend fun getTableLongsProSusp(                                  table: String,
                                                                     columns: Array<LongCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Long>> = readSusp(emptyList(), "getTableLongsProSusp") {
        it.getTableLongsPro(table, columns, dsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getTableFloats(                                               table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloats") {
        it.getTableFloats(table, columns, where)
    }
    
    suspend fun getTableFloatsSusp(                                   table: String,
                                                                    columns: Array<FloatCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<Float>> = readSusp(emptyList(), "getTableFloatsSusp") {
        it.getTableFloats(table, columns, where)
    }
    
    fun getTableFloatsPro(                                             table: String,
                                                                     columns: Array<FloatCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloatsPro") {
        it.getTableFloatsPro(table, columns, dsl)
    }
    
    suspend fun getTableFloatsProSusp(                                 table: String,
                                                                     columns: Array<FloatCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<Float>> = readSusp(emptyList(), "getTableFloatsProSusp") {
        it.getTableFloatsPro(table, columns, dsl)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getTableBlobs(                                                table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobs") {
        it.getTableBlobs(table, columns, where)
    }
    
    suspend fun getTableBlobsSusp(                                    table: String,
                                                                    columns: Array<BlobCol>,
                                                                      where: WhereDsl.()->Unit = {},
    ): List<List<ByteArray>> = readSusp(emptyList(), "getTableBlobsSusp") {
        it.getTableBlobs(table, columns, where)
    }
    
    fun getTableBlobsPro(                                              table: String,
                                                                     columns: Array<BlobCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobsPro") {
        it.getTableBlobsPro(table, columns, dsl)
    }
    
    suspend fun getTableBlobsProSusp(                                  table: String,
                                                                     columns: Array<BlobCol>,
                                                                         dsl: FullDsl.()->Unit = {},
    ): List<List<ByteArray>> = readSusp(emptyList(), "getTableBlobsProSusp") {
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
    
    suspend fun addToIntSusp(                                        addend: Number,
                                                                      table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = writeSusp("addToIntSusp") {
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
    
    suspend fun addToLongSusp(                                       addend: Number,
                                                                      table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = writeSusp("addToLongSusp") {
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
    
    suspend fun addToFloatSusp(                                      addend: Number,
                                                                      table: String,
                                                                     column: FloatCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = writeSusp("addToFloatSusp") {
        it.addToFloat(addend, table, column, where)
    }
    
    
    
    fun flipBool(                                                     table: String,
                                                                     column: BoolCol,
                                                                      async: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ) = write("flipBool", async) {
        it.flipBool(table, column, where)
    }
    
    suspend fun flipBoolSusp(                                         table: String,
                                                                     column: BoolCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = readWriteSusp(Unit, "flipBoolSusp") {
        it.flipBool(table, column, where)
    }

    
    
    
    
    
    
    
    
    
    
    
    // ============================   D E L E T E,  C L E A R   ============================ \\
    
    inline fun <T> deleteRow(                                               table: String,
                                                                            where: String,
                                                                           equals: T,
                                                                            async: Boolean = false,
    ) = write("deleteRow", async) {
        it.deleteRow(table, where, equals)
    }
    
    suspend fun <T> deleteRowSusp(                                                 table: String,
                                                                                   where: String,
                                                                                  equals: T,
    ) = writeSusp("deleteRowSusp") {
        it.deleteRow(table, where, equals)
    }
    
    
    fun deleteRow(                                                         table: String,
                                                                           async: Boolean = false,
                                                                           where: WhereDsl.()->Unit,
    ) = write("deleteRow", async) {
        it.deleteRow(table, where)
    }
    
    
    suspend fun deleteRowSusp(                                             table: String,
                                                                           where: WhereDsl.()->Unit,
    ) = writeSusp("deleteRowSusp") {
        it.deleteRow(table, where)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun deleteFirstRow(                                              table: String,
                                                                            async: Boolean = false,
    ) = write("deleteFirstRow", async) {
        it.deleteFirstRow(table)
    }
    
    suspend fun deleteFirstRowSusp(                                                table: String,
    ) = writeSusp("deleteFirstRowSusp") {
        it.deleteFirstRow(table)
    }
    
    
    inline fun deleteLastRow(                                               table: String,
                                                                            async: Boolean = false,
    ) = write("deleteLastRow", async) {
        it.deleteLastRow(table)
    }
    
    suspend fun deleteLastRowSusp(                                                 table: String,
    ) = writeSusp("deleteLastRowSusp") {
        it.deleteLastRow(table)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun clearTable(                                                  table: String,
                                                                            async: Boolean = false,
    ) = write("clearTable", async) { it.clearTable(table) }
    
    suspend fun clearTableSusp(table: String) = writeSusp("clearTableSusp") { it.clearTable(table) }
    
    
    inline fun clearTables(                                         vararg tables: String,
                                                                            async: Boolean = false,
    ) = write("clearTables", async) { it.clearTables(*tables) }
    
    suspend fun clearTablesSusp(                                          vararg tables: String
    ) = writeSusp("clearTablesSusp") { it.clearTables(*tables) }
    
    
    inline fun clearTables(                                                tables: List<String>,
                                                                            async: Boolean = false,
    ) = write("clearTables", async) { it.clearTables(tables) }
    
    suspend fun clearTablesSusp(                                              tables: List<String>
    ) = writeSusp("clearTablesSusp") { it.clearTables(tables) }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun deleteTable(                                                 table: String,
                                                                            async: Boolean = false,
    ) = write("deleteTable", async) { it.deleteTable(table) }
    
    suspend fun deleteTableSusp(                                                   table: String
    ) = writeSusp("deleteTableSusp") {
        it.deleteTable(table)
    }
    
    
    inline fun deleteTables(                                        vararg tables: String,
                                                                            async: Boolean = false,
    ) = write("deleteTables", async) { it.deleteTables(*tables) }
    
    suspend fun deleteTablesSusp(                                          vararg tables: String
    ) = writeSusp("deleteTablesSusp") { it.deleteTables(*tables) }
    
    
    inline fun deleteTables(                                               tables: List<String>,
                                                                            async: Boolean = false,
    ) = write("deleteTables", async) { it.deleteTables(tables) }
    
    suspend fun deleteTablesSusp(                                              tables: List<String>
    ) = writeSusp("deleteTablesSusp") { it.deleteTables(tables) }
    
    
    
    
    
    // ====================================  C O U N T  ==================================== \\
    
    /** Returns the number of rows matching the query conditions. */
    fun getRowCount(                                                  table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(0, "getRowCount") {
        it.getRowCount(table, where)
    }
    
    /** Returns the number of rows matching the query conditions. */
    suspend fun getRowCountSusp(                                      table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ) = readSusp(0, "getRowCountSusp") {
        it.getRowCount(table, where)
    }
    
    
    /** Returns the number of rows matching the query conditions. */
    fun getRowCountPro(                                                table: String,
                                                                         dsl: FullDsl.()->Unit = {},
    ) = read(0, "getRowCountPro") {
        it.getRowCountPro(table, dsl)
    }
    
    /** Returns the number of rows matching the query conditions. */
    suspend fun getRowCountProSusp(                                    table: String,
                                                                         dsl: FullDsl.()->Unit = {},
    ) = readSusp(0, "getRowCountProSusp") {
        it.getRowCountPro(table, dsl)
    }
    
    
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRows(                                                      table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(false, "hasRows") {
        it.hasRows(table, where)
    }
    
    /** Returns true if at least one row matches the query conditions. */
    suspend fun hasRowsSusp(                                          table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ) = readSusp(false, "hasRowsSusp") {
        it.hasRows(table, where)
    }
    
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRowsPro(                                                         table: String,
                                                                              dsl: FullDsl.()->Unit,
    ) = read(false, "hasRowsPro") {
        it.hasRowsPro(table, dsl)
    }
    
    /** Returns true if at least one row matches the query conditions. */
    suspend fun hasRowsProSusp(                                             table: String,
                                                                              dsl: FullDsl.()->Unit,
    ) = readSusp(false, "hasRowsProSusp") {
        it.hasRowsPro(table, dsl)
    }
    
    
    
    
    
    
    
    
    // ====================================  O T H E R  ==================================== \\
    
    inline fun getLastId(                                                          table: String
    ): Int = read(0, "getLastId") {
        it.getLastId(table)
    }
    suspend fun getLastIdSusp(                                                     table: String
    ): Int = readSusp(0, "getLastIdSusp") {
        it.getLastId(table)
    }
    
    
    inline fun getAllIDs(                                                          table: String
    ): List<Int> = read(emptyList(), "getAllIDs") {
        it.getAllIDs(table)
    }
    suspend fun getAllIDsSusp(                                                     table: String
    ): List<Int> = readSusp(emptyList(), "getAllIDsSusp") {
        it.getAllIDs(table)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun tableExists(                                                        table: String
    ): Boolean = read(false, "tableExists") {
        it.tableExists(table)
    }
    suspend fun tableExistsSusp(                                                   table: String
    ): Boolean = readSusp(false, "tableExistsSusp") {
        it.tableExists(table)
    }
    
    
    inline fun isTableEmpty(                                                       table: String
    ): Boolean = read(true, "isTableEmpty") {
        it.isTableEmpty(table)
    }
    suspend fun isTableEmptySusp(                                                  table: String
    ): Boolean = readSusp(true, "isTableEmptySusp") {
        it.isTableEmpty(table)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun getAppTableNames(): List<String> = read(emptyList(), "getAppTableNames") {
        it.getAppTableNames()
    }
    suspend fun getAppTableNamesSusp(): List<String> = readSusp(emptyList(), "getAppTableNamesSusp") {
        it.getAppTableNames()
    }
    
    inline fun getInternalTableNames(): List<String> = read(emptyList(), "getInternalTableNames") {
        it.getInternalTableNames()
    }
    suspend fun getInternalTableNamesSusp(): List<String> = readSusp(emptyList(), "getInternalTableNamesSusp") {
        it.getInternalTableNames()
    }
    
    
    inline fun getRawTableStr(                                                     table: String
    ): RawTableStr = read(RawTableStr(), "getRawTableStr") {
        it.getRawTableStr(table)
    }
    suspend fun getRawTableStrSusp(                                                table: String
    ): RawTableStr = readSusp(RawTableStr(), "getRawTableStrSusp") {
        it.getRawTableStr(table)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun getLastPosition(                                                    table: String
    ): Int = read(0, "getLastPosition") {
        it.getLastPosition(table)
    }
    suspend fun getLastPositionSusp(                                               table: String
    ): Int = readSusp(0, "getLastPositionSusp") {
        it.getLastPosition(table)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    fun getLargestInt(                                                table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = read(0, "getLargestInt") {
        it.getLargestInt(table, column, where)
    }
    
    suspend fun getLargestIntSusp(                                    table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ) = readSusp(0, "getLargestIntSusp") {
        it.getLargestInt(table, column, where)
    }
    
    
    inline fun getDbFileName() = read("", "getDbFileName") { it.getDbFileName() }
    suspend fun getDbFileNameSusp() = readSusp("", "getDbFileNameSusp") { it.getDbFileName() }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    fun getRandomInt(                                                 table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = read(-1, "getRandomInt") { db ->
        db.getRandomInt(table, column, where)
    }
    
    suspend fun getRandomIntSusp(                                     table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = readSusp(-1, "getRandomIntSusp") { db ->
        db.getRandomInt(table, column, where)
    }
    
    
    fun getRandomId(                                                  table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = read(-1, "getRandomId") { db ->
        db.getRandomId(table, where)
    }
    
    suspend fun getRandomIdSusp(                                      table: String,
                                                                      where: WhereDsl.()->Unit = {},
    ): Int = readSusp(-1, "getRandomIdSusp") { db ->
        db.getRandomId(table, where)
    }
    
    
    inline fun <reified T : BaseEntity> getRandomObj(             tableInfo: TableInfoBase<out T>,
                                                             noinline where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getRandomObj") { db ->
        db.getRandomObj<T>(tableInfo, where)
    }
    
    suspend inline fun <reified T : BaseEntity> getRandomObjSusp(
                                                                  tableInfo: TableInfoBase<out T>,
                                                             noinline where: WhereDsl.()->Unit = {},
    ): T? = readSusp(null, "getRandomObjSusp") { db ->
        db.getRandomObj<T>(tableInfo, where)
    }
    
    
    
    // -------------------------------------------------------------------------------------- \\
    
    fun reorder(                                                      table: String,
                                                                         id: Int,
                                                               moveUpOrBack: Boolean,
                                                            makeFirstOrLast: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ): Boolean = readWrite(false, "reorder") {
        it.reorder(table, id, moveUpOrBack, makeFirstOrLast, where)
    }
    
    suspend fun reorderSusp(                                          table: String,
                                                                         id: Int,
                                                               moveUpOrBack: Boolean,
                                                            makeFirstOrLast: Boolean = false,
                                                                      where: WhereDsl.()->Unit = {},
    ): Boolean = readWriteSusp(false, "reorderSusp") {
        it.reorder(table, id, moveUpOrBack, makeFirstOrLast, where)
    }
    
    
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun createTable(                            tableInfo: TableInfoNormal<out NormalEntity>,
                                                           async: Boolean = false,
    ) = write("createTable", async) {
        it.createTable(tableInfo)
    }
    
    suspend inline fun createTableSusp(                tableInfo: TableInfoNormal<out NormalEntity>,
    ) = writeSusp("createTableSusp") {
        it.createTable(tableInfo)
    }
    
    
    inline fun createTables(                       vararg tables: TableInfoNormal<out NormalEntity>,
                                                           async: Boolean = false,
    ) = write("createTables", async) {
        it.createTables(*tables)
    }
    
    suspend inline fun createTablesSusp(            vararg tables: TableInfoNormal<out NormalEntity>
    ) = writeSusp("createTablesSusp") {
        it.createTables(*tables)
    }
    
    
    inline fun createTables(                        tables: List<TableInfoNormal<out NormalEntity>>,
                                                     async: Boolean = false,
    ) = write("createTables", async) {
        it.createTables(tables)
    }
    
    suspend inline fun createTablesSusp(             tables: List<TableInfoNormal<out NormalEntity>>
    ) = writeSusp("createTablesSusp") {
        it.createTables(tables)
    }
    
    
    
    
    
    
    
}