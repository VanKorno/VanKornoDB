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
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.FullDsl
import com.vankorno.vankornodb.api.SetDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.delete.clearTable
import com.vankorno.vankornodb.delete.deleteFirstRow
import com.vankorno.vankornodb.delete.deleteLastRow
import com.vankorno.vankornodb.delete.deleteRow
import com.vankorno.vankornodb.delete.deleteTable
import com.vankorno.vankornodb.get.*
import com.vankorno.vankornodb.set.*
import kotlin.reflect.KClass

@Suppress("NOTHING_TO_INLINE", "unused")
abstract class DbHelperInternal(
                                 context: Context,
                                  dbName: String,
                               dbVersion: Int,
                              entityMeta: Collection<BaseEntityMeta>,
                                onCreate: (SQLiteDatabase)->Unit = {},
                               onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
) : DbReaderWriter(context, dbName, dbVersion, entityMeta, onCreate, onUpgrade) {
    
    // ===================================  S E T T E R S  =================================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun setInt(                                                        value: Int,
                                                                       table: String,
                                                                      column: IntCol,
                                                                       async: Boolean = false,
                                                                       where: WhereDsl.()->Unit,
    ) = write("setInt", async) {
        it.setInt(value, table, column, where)
    }
    
    suspend fun setIntSusp(                                            value: Int,
                                                                       table: String,
                                                                      column: IntCol,
                                                                       where: WhereDsl.()->Unit,
    ) = writeSusp("setIntSusp") {
        it.setInt(value, table, column, where)
    }
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    fun setStr(                                                        value: String,
                                                                       table: String,
                                                                      column: StrCol,
                                                                       async: Boolean = false,
                                                                       where: WhereDsl.()->Unit,
    ) = write("setStr", async) {
        it.setStr(value, table, column, where)
    }
    
    suspend fun setStrSusp(                                            value: String,
                                                                       table: String,
                                                                      column: StrCol,
                                                                       where: WhereDsl.()->Unit,
    ) = writeSusp("setStrSusp") {
        it.setStr(value, table, column, where)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun setBool(                                                       value: Boolean,
                                                                       table: String,
                                                                      column: BoolCol,
                                                                       async: Boolean = false,
                                                                       where: WhereDsl.()->Unit,
    ) = write("setBool", async) {
        it.setBool(value, table, column, where)
    }
    
    suspend fun setBoolSusp(                                           value: Boolean,
                                                                       table: String,
                                                                      column: BoolCol,
                                                                       where: WhereDsl.()->Unit,
    ) = writeSusp("setBoolSusp") {
        it.setBool(value, table, column, where)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun setLong(                                                       value: Long,
                                                                       table: String,
                                                                      column: LongCol,
                                                                       async: Boolean = false,
                                                                       where: WhereDsl.()->Unit,
    ) = write("setLong", async) {
        it.setLong(value, table, column, where)
    }
    
    suspend fun setLongSusp(                                           value: Long,
                                                                       table: String,
                                                                      column: LongCol,
                                                                       where: WhereDsl.()->Unit,
    ) = writeSusp("setLongSusp") {
        it.setLong(value, table, column, where)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun setFloat(                                                      value: Float,
                                                                       table: String,
                                                                      column: FloatCol,
                                                                       async: Boolean = false,
                                                                       where: WhereDsl.()->Unit,
    ) = write("setFloat", async) {
        it.setFloat(value, table, column, where)
    }
    
    suspend fun setFloatSusp(                                          value: Float,
                                                                       table: String,
                                                                      column: FloatCol,
                                                                       where: WhereDsl.()->Unit,
    ) = writeSusp("setFloatSusp") {
        it.setFloat(value, table, column, where)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun setBlob(                                                       value: ByteArray,
                                                                       table: String,
                                                                      column: BlobCol,
                                                                       async: Boolean = false,
                                                                       where: WhereDsl.()->Unit,
    ) = write("setBlob", async) {
        it.setBlob(value, table, column, where)
    }
    
    suspend fun setBlobSusp(                                           value: ByteArray,
                                                                       table: String,
                                                                      column: BlobCol,
                                                                       where: WhereDsl.()->Unit,
    ) = writeSusp("setBlobSusp") {
        it.setBlob(value, table, column, where)
    }
    
    
    
    
    
    
    
    
    // =============================  M U L T I - S E T T E R S  ============================= \\
    
    fun set(                                                      table: String,
                                                                  where: WhereDsl.()->Unit = {},
                                                                  async: Boolean = false,
                                                                actions: SetDsl.()->Unit,
    ) = write("set", async) {
        it.set(table, where, actions)
    }
    
    suspend fun setSusp(                                          table: String,
                                                                  where: WhereDsl.()->Unit = {},
                                                                actions: SetDsl.()->Unit,
    ) = writeSusp("setSusp") {
        it.set(table, where, actions)
    }
    
    
    
    
    
    
    
    
    // -----------------------------------  O B J E C T  ----------------------------------- \\
    
    inline fun <T : DbEntity> addObj(                                       table: String,
                                                                              obj: T,
                                                                            async: Boolean = false,
    ) = write("addObj", async) {
        it.addObj(table, obj)
    }
    
    
    suspend fun <T : DbEntity> addObjSusp(                                         table: String,
                                                                                     obj: T,
    ): Long = readWriteSusp(-1L, "addObjSusp") {
        it.addObj(table, obj)
    }
    
    
    inline fun <T : DbEntity> addObjects(                                   table: String,
                                                                          objects: List<T>,
                                                                            async: Boolean = false,
    ) = write("addObjects", async) {
        it.addObjects(table, objects)
    }
    
    
    suspend fun <T : DbEntity> addObjectsSusp(                                     table: String,
                                                                                 objects: List<T>,
    ): Int = readWriteSusp(0, "addObjectsSusp") {
        it.addObjects(table, objects)
    }
    
    
    
    fun <T : DbEntity> setObj(                                         table: String,
                                                                         obj: T,
                                                                       async: Boolean = false,
                                                                       where: WhereDsl.()->Unit,
    ) = write("setObj", async) {
        it.setObj(table, obj, where)
    }
    
    
    suspend fun <T : DbEntity> setObjSusp(                             table: String,
                                                                         obj: T,
                                                                       where: WhereDsl.()->Unit,
    ): Int = readWriteSusp(0, "setObjSusp") {
        it.setObj(table, obj, where)
    }
    
    
    
    
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun getInt(table: String, column: IntCol, where: WhereDsl.()->Unit): Int =
        read(0, "getInt") { it.getInt(table, column, where) }
    
    suspend fun getIntSusp(table: String, column: IntCol, where: WhereDsl.()->Unit): Int =
        readSusp(0, "getIntSusp") { it.getInt(table, column, where) }
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    fun getStr(table: String, column: StrCol, where: WhereDsl.()->Unit): String =
        read("", "getStr") { it.getStr(table, column, where) }
    
    suspend fun getStrSusp(table: String, column: StrCol, where: WhereDsl.()->Unit): String =
        readSusp("", "getStrSusp") { it.getStr(table, column, where) }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getBool(table: String, column: BoolCol, where: WhereDsl.()->Unit): Boolean =
        read(false, "getBool") { it.getBool(table, column, where) }
    
    suspend fun getBoolSusp(table: String, column: BoolCol, where: WhereDsl.()->Unit): Boolean =
        readSusp(false, "getBoolSusp") { it.getBool(table, column, where) }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getLong(table: String, column: LongCol, where: WhereDsl.()->Unit): Long =
        read(0L, "getLong") { it.getLong(table, column, where) }
    
    suspend fun getLongSusp(table: String, column: LongCol, where: WhereDsl.()->Unit): Long =
        readSusp(0L, "getLongSusp") { it.getLong(table, column, where) }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getFloat(table: String, column: FloatCol, where: WhereDsl.()->Unit): Float =
        read(0F, "getFloat") { it.getFloat(table, column, where) }
    
    suspend fun getFloatSusp(table: String, column: FloatCol, where: WhereDsl.()->Unit): Float =
        readSusp(0F, "getFloatSusp") { it.getFloat(table, column, where) }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getBlob(table: String, column: BlobCol, where: WhereDsl.()->Unit): ByteArray =
        read(ByteArray(0), "getBlob") { it.getBlob(table, column, where) }
    
    suspend fun getBlobSusp(table: String, column: BlobCol, where: WhereDsl.()->Unit): ByteArray =
        readSusp(ByteArray(0), "getBlobSusp") { it.getBlob(table, column, where) }
    
    
    
    
    
    
    
    
    // ==============================  G E T   O B J E C T S  ============================== \\
    
    inline fun <reified T : DbEntity> getObj(                     table: String,
                                                         noinline where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getObj") {
        it.getObj<T>(table, where)
    }
    
    
    suspend inline fun <reified T : DbEntity> getObjSusp(         table: String,
                                                         noinline where: WhereDsl.()->Unit = {},
    ): T? = readSusp(null, "getObjSusp") {
        it.getObj<T>(table, where)
    }
    
    
    fun <T : DbEntity> getObj(                                    table: String,
                                                                  clazz: KClass<T>,
                                                                  where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getObj") {
        it.getObj(table, clazz, where)
    }
    
    
    suspend fun <T : DbEntity> getObjSusp(                        table: String,
                                                                  clazz: KClass<T>,
                                                                  where: WhereDsl.()->Unit = {},
    ): T? = readSusp(null, "getObjSusp") {
        it.getObj(table, clazz, where)
    }
    
    // With defaults
    
    inline fun <reified T : DbEntity> getObj(                     table: String,
                                                                default: T,
                                                         noinline where: WhereDsl.()->Unit = {},
    ): T = read(default, "getObj") {
        it.getObj<T>(table, default, where)
    }
    
    suspend inline fun <reified T : DbEntity> getObjSusp(         table: String,
                                                                default: T,
                                                         noinline where: WhereDsl.()->Unit = {},
    ): T = readSusp(default, "getObjSusp") {
        it.getObj<T>(table, default, where)
    }
    
    fun <T : DbEntity> getObj(                                    table: String,
                                                                  clazz: KClass<T>,
                                                                default: T,
                                                                  where: WhereDsl.()->Unit = {},
    ): T = read(default, "getObj") {
        it.getObj(table, clazz, default, where)
    }
    
    suspend fun <T : DbEntity> getObjSusp(                        table: String,
                                                                  clazz: KClass<T>,
                                                                default: T,
                                                                  where: WhereDsl.()->Unit = {},
    ): T = readSusp(default, "getObjSusp") {
        it.getObj(table, clazz, default, where)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun <reified T : DbEntity> getObjPro(                  table: String,
                                                        noinline fullDsl: FullDsl.()->Unit = {},
    ): T? = read(null, "getObjPro") {
        it.getObjPro<T>(table, fullDsl)
    }
    
    
    suspend inline fun <reified T : DbEntity> getObjProSusp(         table: String,
                                                        noinline fullDsl: FullDsl.()->Unit = {},
    ): T? = readSusp(null, "getObjProSusp") {
        it.getObjPro<T>(table, fullDsl)
    }
    
    
    fun <T : DbEntity> getObjPro(                                         table: String,
                                                                          clazz: KClass<T>,
                                                                      fullDsl: FullDsl.()->Unit,
    ): T? = read(null, "getObjPro") {
        it.getObjPro(table, clazz, fullDsl)
    }
    
    
    suspend fun <T : DbEntity> getObjProSusp(                             table: String,
                                                                          clazz: KClass<T>,
                                                                      fullDsl: FullDsl.()->Unit,
    ): T? = readSusp(null, "getObjProSusp") {
        it.getObjPro(table, clazz, fullDsl)
    }
    
    
    // With defaults
    
    inline fun <reified T : DbEntity> getObjPro(                     table: String,
                                                                   default: T,
                                                        noinline fullDsl: FullDsl.()->Unit = {},
    ): T = read(default, "getObjPro") {
        it.getObjPro<T>(table, default, fullDsl)
    }
    
    
    suspend inline fun <reified T : DbEntity> getObjProSusp(         table: String,
                                                                   default: T,
                                                        noinline fullDsl: FullDsl.()->Unit = {},
    ): T = readSusp(default, "getObjProSusp") {
        it.getObjPro<T>(table, default, fullDsl)
    }
    
    
    fun <T : DbEntity> getObjPro(                                         table: String,
                                                                          clazz: KClass<T>,
                                                                        default: T,
                                                                      fullDsl: FullDsl.()->Unit,
    ): T = read(default, "getObjPro") {
        it.getObjPro(table, clazz, default, fullDsl)
    }
    
    
    suspend fun <T : DbEntity> getObjProSusp(                             table: String,
                                                                          clazz: KClass<T>,
                                                                        default: T,
                                                                      fullDsl: FullDsl.()->Unit,
    ): T = readSusp(default, "getObjProSusp") {
        it.getObjPro(table, clazz, default, fullDsl)
    }
    
    
    
    
    
    
    
    
    
    
    
    // =========================  M U L T I P L E   O B J E C T S  ========================= \\
    
    inline fun <reified T : DbEntity> getObjects(                 table: String,
                                                         noinline where: WhereDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjects(table, where)
    }
    
    suspend inline fun <reified T : DbEntity> getObjectsSusp(     table: String,
                                                         noinline where: WhereDsl.()->Unit = {},
    ): List<T> = readSusp(emptyList(), "getObjectsSusp") {
        it.getObjects(table, where)
    }
    
    
    fun <T : DbEntity> getObjects(                                
        clazz: KClass<T>,
                                                                  table: String,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjects(table, clazz, where)
    }
    
    suspend fun <T : DbEntity> getObjectsSusp(                    table: String,
                                                                  clazz: KClass<T>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<T> = readSusp(emptyList(), "getObjectsSusp") {
        it.getObjects(table, clazz, where)
    }
    
    
    
    inline fun <reified T : DbEntity> getObjMap(                  table: String,
                                                         noinline where: WhereDsl.()->Unit = {},
    ): Map<Int, T> = read(emptyMap(), "getObjMap") {
        it.getObjMap(table, where)
    }
    
    suspend inline fun <reified T : DbEntity> getObjMapSusp(      table: String,
                                                         noinline where: WhereDsl.()->Unit = {},
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapSusp") {
        it.getObjMap(table, where)
    }
    
    
    fun <T : DbEntity> getObjMap(                                 table: String,
                                                                  clazz: KClass<T>,
                                                                  where: WhereDsl.()->Unit = {},
    ): Map<Int, T> = read(emptyMap(), "getObjMap") {
        it.getObjMap(table, clazz, where)
    }
    
    suspend fun <T : DbEntity> getObjMapSusp(                     table: String,
                                                                  clazz: KClass<T>,
                                                                  where: WhereDsl.()->Unit = {},
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapSusp") {
        it.getObjMap(table, clazz, where)
    }
    
    
    
    
    
    
    // -------------------------------------------------------------------------------------- \\
    
    
    inline fun <reified T : DbEntity> getObjectsPro(                 table: String,
                                                        noinline fullDsl: FullDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjectsPro") {
        it.getObjectsPro(table, fullDsl)
    }
    
    suspend inline fun <reified T : DbEntity> getObjectsProSusp(     table: String,
                                                        noinline fullDsl: FullDsl.()->Unit = {},
    ): List<T> = readSusp(emptyList(), "getObjectsProSusp") {
        it.getObjectsPro(table, fullDsl)
    }
    
    
    fun <T : DbEntity> getObjectsPro(                                clazz: KClass<T>,
                                                                     table: String,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjectsPro") {
        it.getObjectsPro(clazz, table, fullDsl)
    }
    
    suspend fun <T : DbEntity> getObjectsProSusp(                    clazz: KClass<T>,
                                                                     table: String,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<T> = readSusp(emptyList(), "getObjectsProSusp") {
        it.getObjectsPro(clazz, table, fullDsl)
    }
    
    // -------------------------------------------------------------------------------------- \\
    
    
    inline fun <reified T : DbEntity> getObjMapPro(                  table: String,
                                                        noinline fullDsl: FullDsl.()->Unit = {},
    ): Map<Int, T> = read(emptyMap(), "getObjMapPro") {
        it.getObjMapPro(table, fullDsl)
    }
    
    suspend inline fun <reified T : DbEntity> getObjMapProSusp(      table: String,
                                                        noinline fullDsl: FullDsl.()->Unit = {},
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapProSusp") {
        it.getObjMapPro(table, fullDsl)
    }
    
    
    fun <T : DbEntity> getObjMapPro(                                 clazz: KClass<T>,
                                                                     table: String,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): Map<Int, T> = read(emptyMap(), "getObjMapPro") {
        it.getObjMapPro(clazz, table, fullDsl)
    }
    
    suspend fun <T : DbEntity> getObjMapProSusp(                     clazz: KClass<T>,
                                                                     table: String,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapProSusp") {
        it.getObjMapPro(clazz, table, fullDsl)
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // ==============================   C O L U M N   V A L s   ============================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun getColInts(                                               table: String,
                                                                 column: IntCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColInts") {
        it.getColInts(table, column, where)
    }
    
    suspend fun getColIntsSusp(                                   table: String,
                                                                 column: IntCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Int> = readSusp(emptyList(), "getColIntsSusp") {
        it.getColInts(table, column, where)
    }
    
    
    fun getColIntsPro(                                               table: String,
                                                                    column: IntCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColIntsPro") {
        it.getColIntsPro(table, column, fullDsl)
    }
    
    suspend fun getColIntsProSusp(                                   table: String,
                                                                    column: IntCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Int> = readSusp(emptyList(), "getColIntsProSusp") {
        it.getColIntsPro(table, column, fullDsl)
    }
    
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    
    fun getColStrings(                                            table: String,
                                                                 column: StrCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStrings") {
        it.getColStrings(table, column, where)
    }
    
    suspend fun getColStringsSusp(                                table: String,
                                                                 column: StrCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<String> = readSusp(emptyList(), "getColStringsSusp") {
        it.getColStrings(table, column, where)
    }
    
    
    fun getColStringsPro(                                            table: String,
                                                                    column: StrCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStringsPro") {
        it.getColStringsPro(table, column, fullDsl)
    }
    
    suspend fun getColStringsProSusp(                                table: String,
                                                                    column: StrCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<String> = readSusp(emptyList(), "getColStringsProSusp") {
        it.getColStringsPro(table, column, fullDsl)
    }
    
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getColBools(                                              table: String,
                                                                 column: BoolCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBools") {
        it.getColBools(table, column, where)
    }
    
    suspend fun getColBoolsSusp(                                  table: String,
                                                                 column: BoolCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Boolean> = readSusp(emptyList(), "getColBoolsSusp") {
        it.getColBools(table, column, where)
    }
    
    
    fun getColBoolsPro(                                              table: String,
                                                                    column: BoolCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBoolsPro") {
        it.getColBoolsPro(table, column, fullDsl)
    }
    
    suspend fun getColBoolsProSusp(                                  table: String,
                                                                    column: BoolCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Boolean> = readSusp(emptyList(), "getColBoolsProSusp") {
        it.getColBoolsPro(table, column, fullDsl)
    }
    
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getColLongs(                                              table: String,
                                                                 column: LongCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongs") {
        it.getColLongs(table, column, where)
    }
    
    suspend fun getColLongsSusp(                                  table: String,
                                                                 column: LongCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Long> = readSusp(emptyList(), "getColLongsSusp") {
        it.getColLongs(table, column, where)
    }
    
    
    fun getColLongsPro(                                              table: String,
                                                                    column: LongCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongsPro") {
        it.getColLongsPro(table, column, fullDsl)
    }
    
    suspend fun getColLongsProSusp(                                  table: String,
                                                                    column: LongCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Long> = readSusp(emptyList(), "getColLongsProSusp") {
        it.getColLongsPro(table, column, fullDsl)
    }
    
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getColFloats(                                             table: String,
                                                                 column: FloatCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloats") {
        it.getColFloats(table, column, where)
    }
    
    suspend fun getColFloatsSusp(                                 table: String,
                                                                 column: FloatCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Float> = readSusp(emptyList(), "getColFloatsSusp") {
        it.getColFloats(table, column, where)
    }
    
    
    
    fun getColFloatsPro(                                             table: String,
                                                                    column: FloatCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloatsPro") {
        it.getColFloatsPro(table, column, fullDsl)
    }
    
    suspend fun getColFloatsProSusp(                                 table: String,
                                                                    column: FloatCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Float> = readSusp(emptyList(), "getColFloatsProSusp") {
        it.getColFloatsPro(table, column, fullDsl)
    }
    
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getColBlobs(                                              table: String,
                                                                 column: BlobCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobs") {
        it.getColBlobs(table, column, where)
    }
    
    suspend fun getColBlobsSusp(                                  table: String,
                                                                 column: BlobCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = readSusp(emptyList(), "getColBlobsSusp") {
        it.getColBlobs(table, column, where)
    }
    
    
    fun getColBlobsPro(                                              table: String,
                                                                    column: BlobCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobsPro") {
        it.getColBlobsPro(table, column, fullDsl)
    }
    
    suspend fun getColBlobsProSusp(                                  table: String,
                                                                    column: BlobCol,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = readSusp(emptyList(), "getColBlobsProSusp") {
        it.getColBlobsPro(table, column, fullDsl)
    }
    
    
    
    
    
    // =================================   R O W   V A L s   ================================= \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun getRowInts(                                               table: String,
                                                                columns: Array<IntCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getRowInts") {
        it.getRowInts(table, columns, where)
    }
    
    suspend fun getRowIntsSusp(                                   table: String,
                                                                columns: Array<IntCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Int> = readSusp(emptyList(), "getRowIntsSusp") {
        it.getRowInts(table, columns, where)
    }
    
    fun getRowIntsPro(                                               table: String,
                                                                   columns: Array<IntCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Int> = read(emptyList(), "getRowIntsPro") {
        it.getRowIntsPro(table, columns, fullDsl)
    }
    
    suspend fun getRowIntsProSusp(                                   table: String,
                                                                   columns: Array<IntCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Int> = readSusp(emptyList(), "getRowIntsProSusp") {
        it.getRowIntsPro(table, columns, fullDsl)
    }
    
    
    //  -----------------------------------  S T R I N G  -----------------------------------  \\
    
    fun getRowStrings(                                            table: String,
                                                                columns: Array<StrCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStrings") {
        it.getRowStrings(table, columns, where)
    }
    
    suspend fun getRowStringsSusp(                                table: String,
                                                                columns: Array<StrCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<String> = readSusp(emptyList(), "getRowStringsSusp") {
        it.getRowStrings(table, columns, where)
    }
    
    fun getRowStringsPro(                                            table: String,
                                                                   columns: Array<StrCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<String> = read(emptyList(), "getRowStringsPro") {
        it.getRowStringsPro(table, columns, fullDsl)
    }
    
    suspend fun getRowStringsProSusp(                                table: String,
                                                                   columns: Array<StrCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<String> = readSusp(emptyList(), "getRowStringsProSusp") {
        it.getRowStringsPro(table, columns, fullDsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getRowBools(                                              table: String,
                                                                columns: Array<BoolCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBools") {
        it.getRowBools(table, columns, where)
    }
    
    suspend fun getRowBoolsSusp(                                  table: String,
                                                                columns: Array<BoolCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Boolean> = readSusp(emptyList(), "getRowBoolsSusp") {
        it.getRowBools(table, columns, where)
    }
    
    fun getRowBoolsPro(                                              table: String,
                                                                   columns: Array<BoolCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getRowBoolsPro") {
        it.getRowBoolsPro(table, columns, fullDsl)
    }
    
    suspend fun getRowBoolsProSusp(                                  table: String,
                                                                   columns: Array<BoolCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Boolean> = readSusp(emptyList(), "getRowBoolsProSusp") {
        it.getRowBoolsPro(table, columns, fullDsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getRowLongs(                                              table: String,
                                                                columns: Array<LongCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongs") {
        it.getRowLongs(table, columns, where)
    }
    
    suspend fun getRowLongsSusp(                                  table: String,
                                                                columns: Array<LongCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Long> = readSusp(emptyList(), "getRowLongsSusp") {
        it.getRowLongs(table, columns, where)
    }
    
    fun getRowLongsPro(                                              table: String,
                                                                   columns: Array<LongCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Long> = read(emptyList(), "getRowLongsPro") {
        it.getRowLongsPro(table, columns, fullDsl)
    }
    
    suspend fun getRowLongsProSusp(                                  table: String,
                                                                   columns: Array<LongCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Long> = readSusp(emptyList(), "getRowLongsProSusp") {
        it.getRowLongsPro(table, columns, fullDsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getRowFloats(                                             table: String,
                                                                columns: Array<FloatCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloats") {
        it.getRowFloats(table, columns, where)
    }
    
    suspend fun getRowFloatsSusp(                                 table: String,
                                                                columns: Array<FloatCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<Float> = readSusp(emptyList(), "getRowFloatsSusp") {
        it.getRowFloats(table, columns, where)
    }
    
    fun getRowFloatsPro(                                             table: String,
                                                                   columns: Array<FloatCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Float> = read(emptyList(), "getRowFloatsPro") {
        it.getRowFloatsPro(table, columns, fullDsl)
    }
    
    suspend fun getRowFloatsProSusp(                                 table: String,
                                                                   columns: Array<FloatCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<Float> = readSusp(emptyList(), "getRowFloatsProSusp") {
        it.getRowFloatsPro(table, columns, fullDsl)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getRowBlobs(                                              table: String,
                                                                columns: Array<BlobCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobs") {
        it.getRowBlobs(table, columns, where)
    }
    
    suspend fun getRowBlobsSusp(                                  table: String,
                                                                columns: Array<BlobCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<ByteArray> = readSusp(emptyList(), "getRowBlobsSusp") {
        it.getRowBlobs(table, columns, where)
    }
    
    fun getRowBlobsPro(                                              table: String,
                                                                   columns: Array<BlobCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getRowBlobsPro") {
        it.getRowBlobsPro(table, columns, fullDsl)
    }
    
    suspend fun getRowBlobsProSusp(                                  table: String,
                                                                   columns: Array<BlobCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<ByteArray> = readSusp(emptyList(), "getRowBlobsProSusp") {
        it.getRowBlobsPro(table, columns, fullDsl)
    }
    
    
    
    
    
    
    // ===============================   T A B L E   V A L s   =============================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun getTableInts(                                             table: String,
                                                                columns: Array<IntCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<Int>> = read(emptyList(), "getTableInts") {
        it.getTableInts(table, columns, where)
    }
    
    suspend fun getTableIntsSusp(                                 table: String,
                                                                columns: Array<IntCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<Int>> = readSusp(emptyList(), "getTableIntsSusp") {
        it.getTableInts(table, columns, where)
    }
    
    fun getTableIntsPro(                                             table: String,
                                                                   columns: Array<IntCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<Int>> = read(emptyList(), "getTableIntsPro") {
        it.getTableIntsPro(table, columns, fullDsl)
    }
    
    suspend fun getTableIntsProSusp(                                 table: String,
                                                                   columns: Array<IntCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<Int>> = readSusp(emptyList(), "getTableIntsProSusp") {
        it.getTableIntsPro(table, columns, fullDsl)
    }
    
    
    //  -----------------------------------  S T R I N G  -----------------------------------  \\
    
    fun getTableStrings(                                          table: String,
                                                                columns: Array<StrCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStrings") {
        it.getTableStrings(table, columns, where)
    }
    
    suspend fun getTableStringsSusp(                              table: String,
                                                                columns: Array<StrCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<String>> = readSusp(emptyList(), "getTableStringsSusp") {
        it.getTableStrings(table, columns, where)
    }
    
    fun getTableStringsPro(                                          table: String,
                                                                   columns: Array<StrCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<String>> = read(emptyList(), "getTableStringsPro") {
        it.getTableStringsPro(table, columns, fullDsl)
    }
    
    suspend fun getTableStringsProSusp(                              table: String,
                                                                   columns: Array<StrCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<String>> = readSusp(emptyList(), "getTableStringsProSusp") {
        it.getTableStringsPro(table, columns, fullDsl)
    }
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getTableBools(                                            table: String,
                                                                columns: Array<BoolCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBools") {
        it.getTableBools(table, columns, where)
    }
    
    suspend fun getTableBoolsSusp(                                table: String,
                                                                columns: Array<BoolCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<Boolean>> = readSusp(emptyList(), "getTableBoolsSusp") {
        it.getTableBools(table, columns, where)
    }
    
    fun getTableBoolsPro(                                            table: String,
                                                                   columns: Array<BoolCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<Boolean>> = read(emptyList(), "getTableBoolsPro") {
        it.getTableBoolsPro(table, columns, fullDsl)
    }
    
    suspend fun getTableBoolsProSusp(                                table: String,
                                                                   columns: Array<BoolCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<Boolean>> = readSusp(emptyList(), "getTableBoolsProSusp") {
        it.getTableBoolsPro(table, columns, fullDsl)
    }
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getTableLongs(                                            table: String,
                                                                columns: Array<LongCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongs") {
        it.getTableLongs(table, columns, where)
    }
    
    suspend fun getTableLongsSusp(                                table: String,
                                                                columns: Array<LongCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<Long>> = readSusp(emptyList(), "getTableLongsSusp") {
        it.getTableLongs(table, columns, where)
    }
    
    fun getTableLongsPro(                                            table: String,
                                                                   columns: Array<LongCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<Long>> = read(emptyList(), "getTableLongsPro") {
        it.getTableLongsPro(table, columns, fullDsl)
    }
    
    suspend fun getTableLongsProSusp(                                table: String,
                                                                   columns: Array<LongCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<Long>> = readSusp(emptyList(), "getTableLongsProSusp") {
        it.getTableLongsPro(table, columns, fullDsl)
    }
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getTableFloats(                                           table: String,
                                                                columns: Array<FloatCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloats") {
        it.getTableFloats(table, columns, where)
    }
    
    suspend fun getTableFloatsSusp(                               table: String,
                                                                columns: Array<FloatCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<Float>> = readSusp(emptyList(), "getTableFloatsSusp") {
        it.getTableFloats(table, columns, where)
    }
    
    fun getTableFloatsPro(                                           table: String,
                                                                   columns: Array<FloatCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<Float>> = read(emptyList(), "getTableFloatsPro") {
        it.getTableFloatsPro(table, columns, fullDsl)
    }
    
    suspend fun getTableFloatsProSusp(                               table: String,
                                                                   columns: Array<FloatCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<Float>> = readSusp(emptyList(), "getTableFloatsProSusp") {
        it.getTableFloatsPro(table, columns, fullDsl)
    }
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getTableBlobs(                                            table: String,
                                                                columns: Array<BlobCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobs") {
        it.getTableBlobs(table, columns, where)
    }
    
    suspend fun getTableBlobsSusp(                                table: String,
                                                                columns: Array<BlobCol>,
                                                                  where: WhereDsl.()->Unit = {},
    ): List<List<ByteArray>> = readSusp(emptyList(), "getTableBlobsSusp") {
        it.getTableBlobs(table, columns, where)
    }
    
    fun getTableBlobsPro(                                            table: String,
                                                                   columns: Array<BlobCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<ByteArray>> = read(emptyList(), "getTableBlobsPro") {
        it.getTableBlobsPro(table, columns, fullDsl)
    }
    
    suspend fun getTableBlobsProSusp(                                table: String,
                                                                   columns: Array<BlobCol>,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ): List<List<ByteArray>> = readSusp(emptyList(), "getTableBlobsProSusp") {
        it.getTableBlobsPro(table, columns, fullDsl)
    }
    
    
    
    
    
    
    // ===================================   M O D I F Y   =================================== \\
    
    fun addToInt(                                                addend: Number,
                                                                  table: String,
                                                                 column: IntCol,
                                                                  async: Boolean = false,
                                                                  where: WhereDsl.()->Unit = {},
    ) = write("addToInt", async) {
        it.addToInt(addend, table, column, where)
    }
    
    suspend fun addToIntSusp(                                    addend: Number,
                                                                  table: String,
                                                                 column: IntCol,
                                                                  where: WhereDsl.()->Unit = {},
    ) = writeSusp("addToIntSusp") {
        it.addToInt(addend, table, column, where)
    }
    
    
    fun addToLong(                                               addend: Number,
                                                                  table: String,
                                                                 column: LongCol,
                                                                  async: Boolean = false,
                                                                  where: WhereDsl.()->Unit = {},
    ) = write("addToLong", async) {
        it.addToLong(addend, table, column, where)
    }
    
    suspend fun addToLongSusp(                                   addend: Number,
                                                                  table: String,
                                                                 column: LongCol,
                                                                  where: WhereDsl.()->Unit = {},
    ) = writeSusp("addToLongSusp") {
        it.addToLong(addend, table, column, where)
    }
    
    
    fun addToFloat(                                              addend: Number,
                                                                  table: String,
                                                                 column: FloatCol,
                                                                  async: Boolean = false,
                                                                  where: WhereDsl.()->Unit = {},
    ) = write("addToFloat", async) {
        it.addToFloat(addend, table, column, where)
    }
    
    suspend fun addToFloatSusp(                                  addend: Number,
                                                                  table: String,
                                                                 column: FloatCol,
                                                                  where: WhereDsl.()->Unit = {},
    ) = writeSusp("addToFloatSusp") {
        it.addToFloat(addend, table, column, where)
    }
    
    
    
    fun flipBool(                                                 table: String,
                                                                 column: BoolCol,
                                                                  async: Boolean = false,
                                                                  where: WhereDsl.()->Unit = {},
    ) = write("flipBool", async) {
        it.flipBool(table, column, where)
    }
    
    suspend fun flipBoolSusp(                                     table: String,
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
    
    
    fun deleteRow(                                                     table: String,
                                                                       async: Boolean = false,
                                                                       where: WhereDsl.()->Unit,
    ) = write("deleteRow", async) {
        it.deleteRow(table, where)
    }
    
    
    suspend fun deleteRowSusp(                                         table: String,
                                                                       where: WhereDsl.()->Unit,
    ) = writeSusp("deleteRowSusp") {
        it.deleteRow(table, where)
    }
    
    
    
    
    
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
    
    
    inline fun clearTable(                                                  table: String,
                                                                            async: Boolean = false,
    ) = write("clearTable", async) { it.clearTable(table) }
    
    suspend fun clearTableSusp(table: String) = writeSusp("clearTableSusp") { it.clearTable(table) }
    
    
    inline fun deleteTable(                                                 table: String,
                                                                            async: Boolean = false,
    ) = write("deleteTable", async) { it.deleteTable(table) }
    
    suspend fun deleteTableSusp(table: String) = writeSusp("deleteTableSusp") { it.deleteTable(table) }
    
    
    
    
    
    
    // ====================================  C O U N T  ==================================== \\
    
    /** Returns the number of rows matching the query conditions. */
    fun getRowCount(                                              table: String,
                                                                  where: WhereDsl.()->Unit = {},
    ) = read(0, "getRowCount") {
        it.getRowCount(table, where)
    }
    
    /** Returns the number of rows matching the query conditions. */
    suspend fun getRowCountSusp(                                  table: String,
                                                                  where: WhereDsl.()->Unit = {},
    ) = readSusp(0, "getRowCountSusp") {
        it.getRowCount(table, where)
    }
    
    
    /** Returns the number of rows matching the query conditions. */
    fun getRowCountPro(                                              table: String,
                                                                  fullDsl: FullDsl.()->Unit = {},
    ) = read(0, "getRowCountPro") {
        it.getRowCountPro(table, fullDsl)
    }
    
    /** Returns the number of rows matching the query conditions. */
    suspend fun getRowCountProSusp(                                  table: String,
                                                                 fullDsl: FullDsl.()->Unit = {},
    ) = readSusp(0, "getRowCountProSusp") {
        it.getRowCountPro(table, fullDsl)
    }
    
    
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRows(                                                  table: String,
                                                                  where: WhereDsl.()->Unit = {},
    ) = read(false, "hasRows") {
        it.hasRows(table, where)
    }
    
    /** Returns true if at least one row matches the query conditions. */
    suspend fun hasRowsSusp(                                      table: String,
                                                                  where: WhereDsl.()->Unit = {},
    ) = readSusp(false, "hasRowsSusp") {
        it.hasRows(table, where)
    }
    
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRowsPro(                                                       table: String,
                                                                      fullDsl: FullDsl.()->Unit,
    ) = read(false, "hasRowsPro") {
        it.hasRowsPro(table, fullDsl)
    }
    
    /** Returns true if at least one row matches the query conditions. */
    suspend fun hasRowsProSusp(                                           table: String,
                                                                      fullDsl: FullDsl.()->Unit,
    ) = readSusp(false, "hasRowsProSusp") {
        it.hasRowsPro(table, fullDsl)
    }
    
    
    
    
    
    
    
    
    // ====================================  O T H E R  ==================================== \\
    
    inline fun getLastId(table: String) = read(0, "getLastId") { it.getLastId(table) }
    suspend fun getLastIdSusp(table: String) = readSusp(0, "getLastIdSusp") { it.getLastId(table) }
    
    
    inline fun getAllIDs(table: String) = read(emptyList(), "getAllIDs") { it.getAllIDs(table) }
    suspend fun getAllIDsSusp(table: String) = readSusp(emptyList(), "getAllIDsSusp") { it.getAllIDs(table) }
    
    
    inline fun tableExists(table: String) = read(false, "tableExists") { it.tableExists(table) }
    suspend fun tableExistsSusp(table: String) = readSusp(false, "tableExistsSusp") { it.tableExists(table) }
    
    
    inline fun isTableEmpty(table: String) = read(true, "isTableEmpty") { it.isTableEmpty(table) }
    suspend fun isTableEmptySusp(table: String) = readSusp(true, "isTableEmptySusp") { it.isTableEmpty(table) }
    
    
    inline fun getAppTableNames() = read(false, "getAppTableNames") { it.getAppTableNames() }
    suspend fun getAppTableNamesSusp() = readSusp(false, "getAppTableNamesSusp") { it.getAppTableNames() }
    
    inline fun getInternalTableNames() = read(false, "getInternalTableNames") { it.getInternalTableNames() }
    suspend fun getInternalTableNamesSusp() = readSusp(false, "getInternalTableNamesSusp") { it.getInternalTableNames() }
    
    
    inline fun getLastPosition(table: String) = read(0, "getLastPosition") { it.getLastPosition(table) }
    suspend fun getLastPositionSusp(table: String) = readSusp(0, "getLastPositionSusp") { it.getLastPosition(table) }
    
    
    
    
    
    
    fun getLargestInt(                                            table: String,
                                                                 column: IntCol,
                                                                  where: WhereDsl.()->Unit = {},
    ) = read(0, "getLargestInt") {
        it.getLargestInt(table, column, where)
    }
    
    suspend fun getLargestIntSusp(                                table: String,
                                                                 column: IntCol,
                                                                  where: WhereDsl.()->Unit = {},
    ) = readSusp(0, "getLargestIntSusp") {
        it.getLargestInt(table, column, where)
    }
    
    
    inline fun getDbFileName() = read("", "getDbFileName") { it.getDbFileName() }
    suspend fun getDbFileNameSusp() = readSusp("", "getDbFileNameSusp") { it.getDbFileName() }
    
    
    
    
    fun getRandomInt(                                             table: String,
                                                                 column: IntCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): Int = read(-1, "getRandomInt") { db ->
        db.getRandomInt(table, column, where)
    }
    
    suspend fun getRandomIntSusp(                                 table: String,
                                                                 column: IntCol,
                                                                  where: WhereDsl.()->Unit = {},
    ): Int = readSusp(-1, "getRandomIntSusp") { db ->
        db.getRandomInt(table, column, where)
    }
    
    
    fun getRandomId(                                              table: String,
                                                                  where: WhereDsl.()->Unit = {},
    ): Int = read(-1, "getRandomId") { db ->
        db.getRandomId(table, where)
    }
    
    suspend fun getRandomIdSusp(                                  table: String,
                                                                  where: WhereDsl.()->Unit = {},
    ): Int = readSusp(-1, "getRandomIdSusp") { db ->
        db.getRandomId(table, where)
    }
    
    
    inline fun <reified T : DbEntity> getRandomObj(               table: String,
                                                         noinline where: WhereDsl.()->Unit = {},
    ): T? = read(null, "getRandomObj") { db ->
        db.getRandomObj<T>(table, where)
    }
    
    suspend inline fun <reified T : DbEntity> getRandomObjSusp(
                                                                  table: String,
                                                         noinline where: WhereDsl.()->Unit = {},
    ): T? = readSusp(null, "getRandomObjSusp") { db ->
        db.getRandomObj<T>(table, where)
    }
    
    
    
    
    
    
    
}