package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.JoinBuilder
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.ID
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.delete.clearTable
import com.vankorno.vankornodb.delete.deleteFirstRow
import com.vankorno.vankornodb.delete.deleteLastRow
import com.vankorno.vankornodb.delete.deleteRow
import com.vankorno.vankornodb.delete.deleteRowById
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
    
    fun <T> setInt(                                                    value: Int,
                                                                       table: String,
                                                                      column: IntCol,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("setInt", async) {
        it.setInt(value, table, column, where)
    }
    
    suspend fun <T> setIntSusp(                                        value: Int,
                                                                       table: String,
                                                                      column: IntCol,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setIntSusp") {
        it.setInt(value, table, column, where)
    }
    
    
    fun <T> setIntById(                                           value: Int,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: IntCol,
                                                                  async: Boolean = false,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = write("setIntById", async) {
        it.setIntById(value, id, table, column, andWhere)
    }
    
    suspend fun <T> setIntByIdSusp(                               value: Int,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: IntCol,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = writeSusp("setIntByIdSusp") {
        it.setIntById(value, id, table, column, andWhere)
    }
    
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    fun <T> setStr(                                                    value: String,
                                                                       table: String,
                                                                      column: StrCol,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("setStr", async) {
        it.setStr(value, table, column, where)
    }
    
    suspend fun <T> setStrSusp(                                        value: String,
                                                                       table: String,
                                                                      column: StrCol,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setStrSusp") {
        it.setStr(value, table, column, where)
    }
    
    
    fun <T> setStrById(                                           value: String,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: StrCol,
                                                                  async: Boolean = false,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = write("setStrById", async) {
        it.setStrById(value, id, table, column, andWhere)
    }
    
    suspend fun <T> setStrByIdSusp(                               value: String,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: StrCol,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = writeSusp("setStrByIdSusp") {
        it.setStrById(value, id, table, column, andWhere)
    }
    
    
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun <T> setBool(                                                   value: Boolean,
                                                                       table: String,
                                                                      column: BoolCol,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("setBool", async) {
        it.setBool(value, table, column, where)
    }
    
    suspend fun <T> setBoolSusp(                                       value: Boolean,
                                                                       table: String,
                                                                      column: BoolCol,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setBoolSusp") {
        it.setBool(value, table, column, where)
    }
    
    
    fun <T> setBoolById(                                          value: Boolean,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: BoolCol,
                                                                  async: Boolean = false,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = write("setBoolById", async) {
        it.setBoolById(value, id, table, column, andWhere)
    }
    
    suspend fun <T> setBoolByIdSusp(                              value: Boolean,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: BoolCol,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = writeSusp("setBoolByIdSusp") {
        it.setBoolById(value, id, table, column, andWhere)
    }
    
    
    
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun <T> setLong(                                                   value: Long,
                                                                       table: String,
                                                                      column: LongCol,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("setLong", async) {
        it.setLong(value, table, column, where)
    }
    
    suspend fun <T> setLongSusp(                                       value: Long,
                                                                       table: String,
                                                                      column: LongCol,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setLongSusp") {
        it.setLong(value, table, column, where)
    }
    
    
    fun <T> setLongById(                                          value: Long,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: LongCol,
                                                                  async: Boolean = false,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = write("setLongById", async) {
        it.setLongById(value, id, table, column, andWhere)
    }
    
    suspend fun <T> setLongByIdSusp(                              value: Long,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: LongCol,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = writeSusp("setLongByIdSusp") {
        it.setLongById(value, id, table, column, andWhere)
    }
    
    
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun <T> setFloat(                                                  value: Float,
                                                                       table: String,
                                                                      column: FloatCol,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("setFloat", async) {
        it.setFloat(value, table, column, where)
    }
    
    suspend fun <T> setFloatSusp(                                      value: Float,
                                                                       table: String,
                                                                      column: FloatCol,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setFloatSusp") {
        it.setFloat(value, table, column, where)
    }
    
    
    fun <T> setFloatById(                                         value: Float,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: FloatCol,
                                                                  async: Boolean = false,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = write("setFloatById", async) {
        it.setFloatById(value, id, table, column, andWhere)
    }
    
    suspend fun <T> setFloatByIdSusp(                             value: Float,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: FloatCol,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = writeSusp("setFloatByIdSusp") {
        it.setFloatById(value, id, table, column, andWhere)
    }
    
    
    
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun <T> setBlob(                                                   value: ByteArray,
                                                                       table: String,
                                                                      column: BlobCol,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("setBlob", async) {
        it.setBlob(value, table, column, where)
    }
    
    suspend fun <T> setBlobSusp(                                       value: ByteArray,
                                                                       table: String,
                                                                      column: BlobCol,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setBlobSusp") {
        it.setBlob(value, table, column, where)
    }
    
    
    fun <T> setBlobById(                                          value: ByteArray,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: BlobCol,
                                                                  async: Boolean = false,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = write("setBlobById", async) {
        it.setBlobById(value, id, table, column, andWhere)
    }
    
    suspend fun <T> setBlobByIdSusp(                              value: ByteArray,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: BlobCol,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = writeSusp("setBlobByIdSusp") {
        it.setBlobById(value, id, table, column, andWhere)
    }
    
    
    
    
    
    
    // -------------------- Not type-safe --------------------
    
    fun setByIdNoty(                                              value: Any,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: String,
                                                                  async: Boolean = false,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = write("setByIdNoty", async) {
        it.setByIdNoty(value, id, table, column, andWhere)
    }
    
    suspend fun setByIdNotySusp(                                  value: Any,
                                                                     id: Int,
                                                                  table: String,
                                                                 column: String,
                                                               andWhere: WhereBuilder.()->Unit = {},
    ) = writeSusp("setByIdNotySusp") {
        it.setByIdNoty(value, id, table, column, andWhere)
    }
    
    
    inline fun <T> setNoty(                                                 value: Any,
                                                                            table: String,
                                                                           column: String,
                                                                            where: String,
                                                                           equals: T,
                                                                            async: Boolean = false,
    ) = write("setNoty", async) {
        it.setNoty(value, table, column, where, equals)
    }
    
    fun <T> setNoty(                                                   value: Any,
                                                                       table: String,
                                                                      column: String,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("setNoty", async) {
        it.setNoty(value, table, column, where)
    }
    
    
    suspend fun <T> setNotySusp(                                                   value: Any,
                                                                                   table: String,
                                                                                  column: String,
                                                                                   where: String,
                                                                                  equals: T,
    ) = writeSusp("setNotySusp") {
        it.setNoty(value, table, column, where, equals)
    }
    
    suspend fun <T> setNotySusp(                                       value: Any,
                                                                       table: String,
                                                                      column: String,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setNotySusp") {
        it.setNoty(value, table, column, where)
    }
    
    
    
    // =============================  M U L T I - S E T T E R S  ============================= \\
    
    fun setRowVals(                                                    table: String,
                                                                          cv: ContentValues,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("setRowVals", async) {
        it.setRowVals(table, cv, where)
    }
    
    suspend fun setRowValsSusp(                                        table: String,
                                                                          cv: ContentValues,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("setRowValsSusp") {
        it.setRowVals(table, cv, where)
    }
    
    
    fun setRowVals(                                                    table: String,
                                                                       where: WhereBuilder.()->Unit,
                                                                       async: Boolean = false,
                                                               vararg values: Pair<String, Any?>,
    ) = write("setRowVals", async) {
        it.setRowVals(table, where, *values)
    }
    
    suspend fun setRowValsSusp(                                        table: String,
                                                                       where: WhereBuilder.()->Unit,
                                                                       async: Boolean = false,
                                                               vararg values: Pair<String, Any?>,
    ) = writeSusp("setRowValsSusp") { it.setRowVals(table, where, *values) }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun setRowValsById(                                                 id: Int,
                                                                            table: String,
                                                                               cv: ContentValues,
                                                                            async: Boolean = false,
    ) = write("setRowValsById", async) {
        it.setRowValsById(id, table, cv)
    }
    
    suspend fun setRowValsByIdSusp(                                              id: Int,
                                                                              table: String,
                                                                                 cv: ContentValues,
    ) = writeSusp("setRowValsByIdSusp") {
        it.setRowValsById(id, table, cv)
    }
    
    
    inline fun setRowValsById(                                               id: Int,
                                                                          table: String,
                                                                          async: Boolean = false,
                                                                  vararg values: Pair<String, Any?>,
    ) = write("setRowValsById", async) {
        it.setRowValsById(id, table, *values)
    }
    
    suspend fun setRowValsByIdSusp(                                          id: Int,
                                                                          table: String,
                                                                  vararg values: Pair<String, Any?>,
    ) = writeSusp("setRowValsByIdSusp") {
        it.setRowValsById(id, table, *values)
    }
    
    
    // -------------------------------------------------------------------------------------- \\
    
    inline fun setInAllRows(                                                value: Any,
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("setInAllRows", async) {
        it.setInAllRows(value, table, column)
    }
    suspend fun setInAllRowsSusp(                                                  value: Any,
                                                                                   table: String,
                                                                                  column: String,
    ) = writeSusp("setInAllRowsSusp") {
        it.setInAllRows(value, table, column)
    }
    
    
    inline fun setRowValsInAllRows(                                       table: String,
                                                                          async: Boolean = false,
                                                                  vararg values: Pair<String, Any?>,
    ) = write("setRowValsInAllRows", async) {
        it.setRowValsInAllRows(table, *values)
    }
    suspend fun setRowValsInAllRowsSusp(                                  table: String,
                                                                  vararg values: Pair<String, Any?>,
    ) = writeSusp("setRowValsInAllRowsSusp") {
        it.setRowValsInAllRows(table, *values)
    }
    
    
    // ---------------------------------  S E T   R O W S  --------------------------------- \\
    
    inline fun <T : DbEntity> insertObj(                                    table: String,
                                                                              obj: T,
                                                                            async: Boolean = false,
    ) = write("insertObj", async) {
        it.insertObj(table, obj)
    }
    
    
    suspend fun <T : DbEntity> insertObjSusp(                                      table: String,
                                                                                     obj: T,
    ): Long = readWriteSusp(-1L, "insertObjSusp") {
        it.insertObj(table, obj)
    }
    
    
    inline fun <T : DbEntity> insertObjects(                                table: String,
                                                                          objects: List<T>,
                                                                            async: Boolean = false,
    ) = write("insertObjects", async) {
        it.insertObjects(table, objects)
    }
    
    
    suspend fun <T : DbEntity> insertObjectsSusp(                                  table: String,
                                                                                 objects: List<T>,
    ): Int = readWriteSusp(0, "insertObjectsSusp") {
        it.insertObjects(table, objects)
    }
    
    
    
    fun <T : DbEntity> updateObj(                                      table: String,
                                                                         obj: T,
                                                                       async: Boolean = false,
                                                                       where: WhereBuilder.()->Unit,
    ) = write("updateObj", async) {
        it.updateObj(table, obj, where)
    }
    
    
    suspend fun <T : DbEntity> updateObjSusp(                          table: String,
                                                                         obj: T,
                                                                       where: WhereBuilder.()->Unit,
    ): Int = readWriteSusp(0, "updateObjSusp") {
        it.updateObj(table, obj, where)
    }
    
    
    inline fun <T : DbEntity> updateObjById(                                   id: Int,
                                                                            table: String,
                                                                              obj: T,
                                                                            async: Boolean = false,
    ) = write("updateObjById", async) {
        it.updateObjById(id, table, obj)
    }
    
    
    suspend fun <T : DbEntity> updateObjByIdSusp(                                     id: Int,
                                                                                   table: String,
                                                                                     obj: T,
    ): Int = readWriteSusp(0, "updateObjByIdSusp") {
        it.updateObjById(id, table, obj)
    }
    
    
    
    
    
    
    
    
    
    
    // ==================================   G E T T E R S  ================================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun getInt(table: String, column: IntCol, where: WhereBuilder.()->Unit): Int =
        read(0, "getInt") { it.getInt(table, column, where) }
    
    suspend fun getIntSusp(table: String, column: IntCol, where: WhereBuilder.()->Unit): Int =
        readSusp(0, "getIntSusp") { it.getInt(table, column, where) }
    
    fun getIntById(id: Int, table: String, column: IntCol, andWhere: WhereBuilder.()->Unit = {}): Int =
        read(0, "getIntById") { it.getIntById(id, table, column, andWhere) }
    
    suspend fun getIntByIdSusp(id: Int, table: String, column: IntCol, andWhere: WhereBuilder.()->Unit = {}): Int =
        readSusp(0, "getIntByIdSusp") { it.getIntById(id, table, column, andWhere) }
    
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    fun getStr(table: String, column: StrCol, where: WhereBuilder.()->Unit): String =
        read("", "getStr") { it.getStr(table, column, where) }
    
    suspend fun getStrSusp(table: String, column: StrCol, where: WhereBuilder.()->Unit): String =
        readSusp("", "getStrSusp") { it.getStr(table, column, where) }
    
    fun getIntById(id: Int, table: String, column: StrCol, andWhere: WhereBuilder.()->Unit = {}): String =
        read("", "getStrById") { it.getStrById(id, table, column, andWhere) }
    
    suspend fun getStrByIdSusp(id: Int, table: String, column: StrCol, andWhere: WhereBuilder.()->Unit = {}): String =
        readSusp("", "getStrByIdSusp") { it.getStrById(id, table, column, andWhere) }
    
    
    
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getBool(table: String, column: BoolCol, where: WhereBuilder.()->Unit): Boolean =
        read(false, "getBool") { it.getBool(table, column, where) }
    
    suspend fun getBoolSusp(table: String, column: BoolCol, where: WhereBuilder.()->Unit): Boolean =
        readSusp(false, "getBoolSusp") { it.getBool(table, column, where) }
    
    fun getBoolById(id: Int, table: String, column: BoolCol, andWhere: WhereBuilder.()->Unit = {}): Boolean =
        read(false, "getBoolById") { it.getBoolById(id, table, column, andWhere) }
    
    suspend fun getBoolByIdSusp(id: Int, table: String, column: BoolCol, andWhere: WhereBuilder.()->Unit = {}): Boolean =
        readSusp(false, "getBoolByIdSusp") { it.getBoolById(id, table, column, andWhere) }
    
    
    
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getLong(table: String, column: LongCol, where: WhereBuilder.()->Unit): Long =
        read(0L, "getLong") { it.getLong(table, column, where) }
    
    suspend fun getLongSusp(table: String, column: LongCol, where: WhereBuilder.()->Unit): Long =
        readSusp(0L, "getLongSusp") { it.getLong(table, column, where) }
    
    fun getLongById(id: Int, table: String, column: LongCol, andWhere: WhereBuilder.()->Unit = {}): Long =
        read(0L, "getLongById") { it.getLongById(id, table, column, andWhere) }
    
    suspend fun getLongByIdSusp(id: Int, table: String, column: LongCol, andWhere: WhereBuilder.()->Unit = {}): Long =
        readSusp(0L, "getLongByIdSusp") { it.getLongById(id, table, column, andWhere) }
    
    
    
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getFloat(table: String, column: FloatCol, where: WhereBuilder.()->Unit): Float =
        read(0F, "getFloat") { it.getFloat(table, column, where) }
    
    suspend fun getFloatSusp(table: String, column: FloatCol, where: WhereBuilder.()->Unit): Float =
        readSusp(0F, "getFloatSusp") { it.getFloat(table, column, where) }
    
    fun getFloatById(id: Int, table: String, column: FloatCol, andWhere: WhereBuilder.()->Unit = {}): Float =
        read(0F, "getFloatById") { it.getFloatById(id, table, column, andWhere) }
    
    suspend fun getFloatByIdSusp(id: Int, table: String, column: FloatCol, andWhere: WhereBuilder.()->Unit = {}): Float =
        readSusp(0F, "getFloatByIdSusp") { it.getFloatById(id, table, column, andWhere) }
    
    
    
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getBlob(table: String, column: BlobCol, where: WhereBuilder.()->Unit): ByteArray =
        read(ByteArray(0), "getBlob") { it.getBlob(table, column, where) }
    
    suspend fun getBlobSusp(table: String, column: BlobCol, where: WhereBuilder.()->Unit): ByteArray =
        readSusp(ByteArray(0), "getBlobSusp") { it.getBlob(table, column, where) }
    
    fun getBlobById(id: Int, table: String, column: BlobCol, andWhere: WhereBuilder.()->Unit = {}): ByteArray =
        read(ByteArray(0), "getBlobById") { it.getBlobById(id, table, column, andWhere) }
    
    suspend fun getBlobByIdSusp(id: Int, table: String, column: BlobCol, andWhere: WhereBuilder.()->Unit = {}): ByteArray =
        readSusp(ByteArray(0), "getBlobByIdSusp") { it.getBlobById(id, table, column, andWhere) }
    
    
    
    
    
    
    
    
    
    // ======================== NOT TYPE-SAFE
    
    // Simplified conditions
    
    inline fun <T> getIntNoty(table: String, column: String, whereClause: String, whereArg: T) =
        read(0, "getIntNoty") {
            it.getIntNoty(table, column, whereClause, whereArg)
        }
    suspend fun <T> getIntNotySusp(table: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0, "getIntNotySusp") {
            it.getIntNoty(table, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getStrNoty(table: String, column: String, whereClause: String, whereArg: T): String =
        read("", "getStrNoty") {
            it.getStrNoty(table, column, whereClause, whereArg)
        }
    suspend fun <T> getStrNotySusp(table: String, column: String, whereClause: String, whereArg: T): String =
        readSusp("", "getStrNotySusp") {
            it.getStrNoty(table, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getBoolNoty(table: String, column: String, whereClause: String, whereArg: T) =
        read(false, "getBoolNoty") { 
            it.getBoolNoty(table, column, whereClause, whereArg)
        }
    suspend fun <T> getBoolNotySusp(table: String, column: String, whereClause: String, whereArg: T) =
        readSusp(false, "getBoolNotySusp") {
            it.getBoolNoty(table, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getLongNoty(table: String, column: String, whereClause: String, whereArg: T) =
        read(0L, "getLongNoty") { 
            it.getLongNoty(table, column, whereClause, whereArg)
        }
    suspend fun <T> getLongNotySusp(table: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0L, "getLongNotySusp") {
            it.getLongNoty(table, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getFloatNoty(table: String, column: String, whereClause: String, whereArg: T) =
        read(0F, "getFloatNoty") { 
            it.getFloatNoty(table, column, whereClause, whereArg)
        }
    suspend fun <T> getFloatNotySusp(table: String, column: String, whereClause: String, whereArg: T) =
        readSusp(0F, "getFloatNotySusp") {
            it.getFloatNoty(table, column, whereClause, whereArg)
        }
    
    
    inline fun <T> getBlobNoty(table: String, column: String, whereClause: String, whereArg: T) =
        read(null, "getBlobNoty") {
            it.getBlobNoty(table, column, whereClause, whereArg)
        }
    suspend fun <T> getBlobNotySusp(table: String, column: String, whereClause: String, whereArg: T): ByteArray? =
        readSusp(null, "getBlobNotySusp") {
            it.getBlobNoty(table, column, whereClause, whereArg)
        }
    
    
    // DSL
    
    fun getIntNoty(table: String, column: String, where: WhereBuilder.()->Unit) =
        read(0, "getInt") {
            it.getIntNoty(table, column, where)
        }
    suspend fun getIntNotySusp(table: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(0, "getIntSusp") {
            it.getIntNoty(table, column, where)
        }
    
    
    fun getStrNoty(table: String, column: String, where: WhereBuilder.()->Unit): String =
        read("", "getStr") {
            it.getStrNoty(table, column, where)
        }
    suspend fun getStrNotySusp(table: String, column: String, where: WhereBuilder.()->Unit): String =
        readSusp("", "getStrSusp") {
            it.getStrNoty(table, column, where)
        }
    
    
    fun getBoolNoty(table: String, column: String, where: WhereBuilder.()->Unit) =
        read(false, "getBool") {
            it.getBoolNoty(table, column, where)
        }
    suspend fun getBoolNotySusp(table: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(false, "getBoolSusp") {
            it.getBoolNoty(table, column, where)
        }
    
    
    fun getLongNoty(table: String, column: String, where: WhereBuilder.()->Unit) =
        read(0L, "getLong") {
            it.getLongNoty(table, column, where)
        }
    suspend fun getLongNotySusp(table: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(0L, "getLongSusp") {
            it.getLongNoty(table, column, where)
        }
    
    
    fun getFloatNoty(table: String, column: String, where: WhereBuilder.()->Unit) =
        read(0F, "getFloat") {
            it.getFloatNoty(table, column, where)
        }
    suspend fun getFloatNotySusp(table: String, column: String, where: WhereBuilder.()->Unit) =
        readSusp(0F, "getFloatSusp") {
            it.getFloatNoty(table, column, where)
        }
    
    
    fun getBlobNoty(table: String, column: String, where: WhereBuilder.()->Unit) =
        read(null, "getBlob") {
            it.getBlobNoty(table, column, where)
        }
    suspend fun getBlobNotySusp(table: String, column: String, where: WhereBuilder.()->Unit): ByteArray? =
        readSusp(null, "getBlobSusp") {
            it.getBlobNoty(table, column, where)
        }
    
    
    
    // By ID
    
    inline fun getIntById(id: Int, table: String, column: String) = getIntNoty(table, column, ID, id)
    suspend fun getIntByIdSusp(id: Int, table: String, column: String) = getIntNotySusp(table, column, ID, id)
    
    inline fun getStrById(id: Int, table: String, column: String) = getStrNoty(table, column, ID, id)
    suspend fun getStrByIdSusp(id: Int, table: String, column: String) = getStrNotySusp(table, column, ID, id)
    
    inline fun getBoolById(id: Int, table: String, column: String) = getBoolNoty(table, column, ID, id)
    suspend fun getBoolByIdSusp(id: Int, table: String, column: String) = getBoolNotySusp(table, column, ID, id)
    
    inline fun getLongById(id: Int, table: String, column: String) = getLongNoty(table, column, ID, id)
    suspend fun getLongByIdSusp(id: Int, table: String, column: String) = getLongNotySusp(table, column, ID, id)
    
    inline fun getFloatById(id: Int, table: String, column: String) = getFloatNoty(table, column, ID, id)
    suspend fun getFloatByIdSusp(id: Int, table: String, column: String) = getFloatNotySusp(table, column, ID, id)
    
    inline fun getBlobById(id: Int, table: String, column: String): ByteArray? = getBlobNoty(table, column, ID, id)
    suspend fun getBlobByIdSusp(id: Int, table: String, column: String): ByteArray? = getBlobNotySusp(table, column, ID, id)
    
    
    // Multiple values
    
    inline fun <reified T : Any> getMultiRowVals(                    table: String,
                                                                   columns: Array<out String>,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
    ): List<List<T?>> = read(emptyList(), "getMultiRowVals") {
        it.getMultiRowVals(table, columns, queryOpts)
    }
    
    suspend inline fun <reified T : Any> getMultiRowValsSusp(        table: String,
                                                                   columns: Array<out String>,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
    ): List<List<T?>> = readSusp(emptyList(), "getMultiRowValsSusp") {
        it.getMultiRowVals(table, columns, queryOpts)
    }
    
    
    
    
    
    
    
    
    // -------------------------------  G E T   O B J E C T S  ------------------------------- \\
    
    inline fun <reified T : DbEntity> getObjOrNullById(                               id: Int,
                                                                                   table: String,
    ): T? = read(null, "getObjOrNullById") {
        it.getObjOrNullById<T>(id, table)
    }
    
    suspend inline fun <reified T : DbEntity> getObjOrNullByIdSusp(                   id: Int,
                                                                                   table: String,
    ): T? = readSusp(null, "getObjOrNullByIdSusp") {
        it.getObjOrNullById<T>(id, table)
    }
    
    
    inline fun <reified T : DbEntity> getObjOrNull(                  table: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
    ): T? = read(null, "getObjOrNull") {
        it.getObjOrNull<T>(table, queryOpts)
    }
    
    
    suspend inline fun <reified T : DbEntity> getObjOrNullSusp(      table: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
    ): T? = readSusp(null, "getObjOrNullSusp") {
        it.getObjOrNull<T>(table, queryOpts)
    }
    
    
    fun <T : DbEntity> getObjOrNull(                                 clazz: KClass<T>,
                                                                     table: String,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): T? = read(null, "getObjOrNull") {
        it.getObjOrNull(clazz, table, queryOpts)
    }
    
    
    suspend fun <T : DbEntity> getObjOrNullSusp(                     clazz: KClass<T>,
                                                                     table: String,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): T? = readSusp(null, "getObjOrNullSusp") {
        it.getObjOrNull(clazz, table, queryOpts)
    }
    
    
    
    
    inline fun <reified T : DbEntity> getObjects(                         table: String,
                                                             noinline queryOpts: QueryOpts.()->Unit,
                                                              noinline mapAfter: (T)->T,
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjects(table, queryOpts, mapAfter)
    }
    
    inline fun <reified T : DbEntity> getObjects(                    table: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjects(table, queryOpts)
    }
    
    
    suspend inline fun <reified T : DbEntity> getObjectsSusp(             table: String,
                                                             noinline queryOpts: QueryOpts.()->Unit,
                                                              noinline mapAfter: (T)->T,
    ): List<T> = readSusp(emptyList(), "getObjectsSusp") {
        it.getObjects(table, queryOpts, mapAfter)
    }
    suspend inline fun <reified T : DbEntity> getObjectsSusp(        table: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
    ): List<T> = readSusp(emptyList(), "getObjectsSusp") {
        it.getObjects(table, queryOpts)
    }
    
    
    
    fun <T : DbEntity> getObjects(                                        clazz: KClass<T>,
                                                                          table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
                                                                       mapAfter: (T)->T,
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjects(clazz, table, queryOpts, mapAfter)
    }
    
    fun <T : DbEntity> getObjects(                                   clazz: KClass<T>,
                                                                     table: String,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<T> = read(emptyList(), "getObjects") {
        it.getObjects(clazz, table, queryOpts)
    }
    
    
    suspend fun <T : DbEntity> getObjectsSusp(                            clazz: KClass<T>,
                                                                          table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
                                                                       mapAfter: (T)->T,
    ): List<T> = readSusp(emptyList(), "getObjectsSusp") {
        it.getObjects(clazz, table, queryOpts, mapAfter)
    }
    
    suspend fun <T : DbEntity> getObjectsSusp(                       clazz: KClass<T>,
                                                                     table: String,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<T> = readSusp(emptyList(), "getObjectsSusp") {
        it.getObjects(clazz, table, queryOpts)
    }
    
    
    
    inline fun <reified T : DbEntity> getObjMap(                          table: String,
                                                             noinline queryOpts: QueryOpts.()->Unit,
                                                              noinline mapAfter: (T)->T,
    ): Map<Int, T> = read(emptyMap(), "getObjMap") {
        it.getObjMap(table, queryOpts, mapAfter)
    }
    
    inline fun <reified T : DbEntity> getObjMap(                     table: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
    ): Map<Int, T> = read(emptyMap(), "getObjMap") {
        it.getObjMap(table, queryOpts)
    }
    
    
    suspend inline fun <reified T : DbEntity> getObjMapSusp(              table: String,
                                                             noinline queryOpts: QueryOpts.()->Unit,
                                                              noinline mapAfter: (T)->T,
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapSusp") {
        it.getObjMap(table, queryOpts, mapAfter)
    }
    
    suspend inline fun <reified T : DbEntity> getObjMapSusp(         table: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapSusp") {
        it.getObjMap(table, queryOpts)
    }
    
    
    
    fun <T : DbEntity> getObjMap(                                         clazz: KClass<T>,
                                                                          table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
                                                                       mapAfter: (T)->T,
    ): Map<Int, T> = read(emptyMap(), "getObjMap") {
        it.getObjMap(clazz, table, queryOpts, mapAfter)
    }
    
    fun <T : DbEntity> getObjMap(                                    clazz: KClass<T>,
                                                                     table: String,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): Map<Int, T> = read(emptyMap(), "getObjMap") {
        it.getObjMap(clazz, table, queryOpts)
    }
    
    
    suspend fun <T : DbEntity> getObjMapSusp(                             clazz: KClass<T>,
                                                                          table: String,
                                                                      queryOpts: QueryOpts.()->Unit,
                                                                       mapAfter: (T)->T,
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapSusp") {
        it.getObjMap(clazz, table, queryOpts, mapAfter)
    }
    
    suspend fun <T : DbEntity> getObjMapSusp(                        clazz: KClass<T>,
                                                                     table: String,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): Map<Int, T> = readSusp(emptyMap(), "getObjMapSusp") {
        it.getObjMap(clazz, table, queryOpts)
    }
    
    
    
    
    
    
    // =====================================   L I S T   ===================================== \\
    
    //  --------------------------------------  I N T  --------------------------------------  \\
    
    fun getColInts(                                               table: String,
                                                                 column: IntCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColInts") {
        it.getColInts(table, column, where)
    }
    
    suspend fun getColIntsSusp(                                   table: String,
                                                                 column: IntCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<Int> = readSusp(emptyList(), "getColIntsSusp") {
        it.getColInts(table, column, where)
    }
    
    
    fun getColIntsPro(                                               table: String,
                                                                    column: IntCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<Int> = read(emptyList(), "getColIntsPro") {
        it.getColIntsPro(table, column, queryOpts)
    }
    
    suspend fun getColIntsProSusp(                                   table: String,
                                                                    column: IntCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<Int> = readSusp(emptyList(), "getColIntsProSusp") {
        it.getColIntsPro(table, column, queryOpts)
    }
    
    
    
    //  ------------------------------------  S T R I N G  ------------------------------------  \\
    
    
    fun getColStrings(                                            table: String,
                                                                 column: StrCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStrings") {
        it.getColStrings(table, column, where)
    }
    
    suspend fun getColStringsSusp(                                table: String,
                                                                 column: StrCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<String> = readSusp(emptyList(), "getColStringsSusp") {
        it.getColStrings(table, column, where)
    }
    
    
    fun getColStringsPro(                                            table: String,
                                                                    column: StrCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<String> = read(emptyList(), "getColStringsPro") {
        it.getColStringsPro(table, column, queryOpts)
    }
    
    suspend fun getColStringsProSusp(                                table: String,
                                                                    column: StrCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<String> = readSusp(emptyList(), "getColStringsProSusp") {
        it.getColStringsPro(table, column, queryOpts)
    }
    
    
    
    //  ----------------------------------  B O O L E A N  ----------------------------------  \\
    
    fun getColBools(                                              table: String,
                                                                 column: BoolCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBools") {
        it.getColBools(table, column, where)
    }
    
    suspend fun getColBoolsSusp(                                  table: String,
                                                                 column: BoolCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<Boolean> = readSusp(emptyList(), "getColBoolsSusp") {
        it.getColBools(table, column, where)
    }
    
    
    fun getColBoolsPro(                                              table: String,
                                                                    column: BoolCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<Boolean> = read(emptyList(), "getColBoolsPro") {
        it.getColBoolsPro(table, column, queryOpts)
    }
    
    suspend fun getColBoolsProSusp(                                  table: String,
                                                                    column: BoolCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<Boolean> = readSusp(emptyList(), "getColBoolsProSusp") {
        it.getColBoolsPro(table, column, queryOpts)
    }
    
    
    
    //  -------------------------------------  L O N G  -------------------------------------  \\
    
    fun getColLongs(                                              table: String,
                                                                 column: LongCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongs") {
        it.getColLongs(table, column, where)
    }
    
    suspend fun getColLongsSusp(                                  table: String,
                                                                 column: LongCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<Long> = readSusp(emptyList(), "getColLongsSusp") {
        it.getColLongs(table, column, where)
    }
    
    
    fun getColLongsPro(                                              table: String,
                                                                    column: LongCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<Long> = read(emptyList(), "getColLongsPro") {
        it.getColLongsPro(table, column, queryOpts)
    }
    
    suspend fun getColLongsProSusp(                                  table: String,
                                                                    column: LongCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<Long> = readSusp(emptyList(), "getColLongsProSusp") {
        it.getColLongsPro(table, column, queryOpts)
    }
    
    
    
    //  ------------------------------------  F L O A T  ------------------------------------  \\
    
    fun getColFloats(                                             table: String,
                                                                 column: FloatCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloats") {
        it.getColFloats(table, column, where)
    }
    
    suspend fun getColFloatsSusp(                                 table: String,
                                                                 column: FloatCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<Float> = readSusp(emptyList(), "getColFloatsSusp") {
        it.getColFloats(table, column, where)
    }
    
    
    
    fun getColFloatsPro(                                             table: String,
                                                                    column: FloatCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<Float> = read(emptyList(), "getColFloatsPro") {
        it.getColFloatsPro(table, column, queryOpts)
    }
    
    suspend fun getColFloatsProSusp(                                 table: String,
                                                                    column: FloatCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<Float> = readSusp(emptyList(), "getColFloatsProSusp") {
        it.getColFloatsPro(table, column, queryOpts)
    }
    
    
    
    //  -------------------------------------  B L O B  -------------------------------------  \\
    
    fun getColBlobs(                                              table: String,
                                                                 column: BlobCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobs") {
        it.getColBlobs(table, column, where)
    }
    
    suspend fun getColBlobsSusp(                                  table: String,
                                                                 column: BlobCol,
                                                                  where: WhereBuilder.()->Unit = {},
    ): List<ByteArray> = readSusp(emptyList(), "getColBlobsSusp") {
        it.getColBlobs(table, column, where)
    }
    
    
    fun getColBlobsPro(                                              table: String,
                                                                    column: BlobCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<ByteArray> = read(emptyList(), "getColBlobsPro") {
        it.getColBlobsPro(table, column, queryOpts)
    }
    
    suspend fun getColBlobsProSusp(                                  table: String,
                                                                    column: BlobCol,
                                                                 queryOpts: QueryOpts.()->Unit = {},
    ): List<ByteArray> = readSusp(emptyList(), "getColBlobsProSusp") {
        it.getColBlobsPro(table, column, queryOpts)
    }
    
    
    
    
    // ------------- Not type-safe
    
    inline fun <reified T> getColValsNoty(                           table: String,
                                                                    column: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
    ): List<T> = read(emptyList(), "getColValsNoty") {
        it.getColValsNoty(table, column, queryOpts)
    }
    
    suspend inline fun <reified T> getColValsNotySusp(               table: String,
                                                                    column: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
    ): List<T> = readSusp(emptyList(), "getColValsNotySusp") {
        it.getColValsNoty(table, column, queryOpts)
    }
    
    
    
    
    
    
    
    // ===================================   M O D I F Y   =================================== \\
    
    inline fun addToInt(                                                   addend: Number,
                                                                               id: Int,
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("addToInt", async) {
        it.addToInt(addend, id, table, column)
    }
    
    suspend fun addToIntSusp(                                                     addend: Number,
                                                                                      id: Int,
                                                                                   table: String,
                                                                                  column: String,
    ) = writeSusp("addToIntSusp") {
        it.addToInt(addend, id, table, column)
    }
    
    
    inline fun addToLong(                                                  addend: Number,
                                                                               id: Int,
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("addToLong", async) {
        it.addToLong(addend, id, table, column)
    }
    
    suspend fun addToLongSusp(                                                    addend: Number,
                                                                                      id: Int,
                                                                                   table: String,
                                                                                  column: String,
    ) = writeSusp("addToLongSusp") {
        it.addToLong(addend, id, table, column)
    }
    
    
    inline fun addToFloat(                                                 addend: Number,
                                                                               id: Int,
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("addToFloat", async) {
        it.addToFloat(addend, id, table, column)
    }
    
    suspend fun addToFloatSusp(                                                   addend: Number,
                                                                                      id: Int,
                                                                                   table: String,
                                                                                  column: String,
    ) = writeSusp("addToFloatSusp") {
        it.addToFloat(addend, id, table, column)
    }
    
    
    
    inline fun toggleBool(                                                     id: Int,
                                                                            table: String,
                                                                           column: String,
                                                                            async: Boolean = false,
    ) = write("toggleBool", async) {
        it.toggleBool(id, table, column)
    }
    
    suspend fun toggleBoolSusp(                                                       id: Int,
                                                                                   table: String,
                                                                                  column: String,
    ) = readWriteSusp(Unit, "toggleBoolSusp") {
        it.toggleBool(id, table, column)
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
                                                                       where: WhereBuilder.()->Unit,
    ) = write("deleteRow", async) {
        it.deleteRow(table, where)
    }
    
    
    suspend fun deleteRowSusp(                                         table: String,
                                                                       where: WhereBuilder.()->Unit,
    ) = writeSusp("deleteRowSusp") {
        it.deleteRow(table, where)
    }
    
    
    inline fun deleteRowById(                                                  id: Int,
                                                                            table: String,
                                                                            async: Boolean = false,
    ) = write("deleteRowById", async) {
        it.deleteRowById(id, table)
    }
    
    suspend fun deleteRowByIdSusp(                                                    id: Int,
                                                                                   table: String,
    ) = writeSusp("deleteRowByIdSusp") {
        it.deleteRowById(id, table)
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
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = read(0, "getRowCount") {
        it.getRowCount(table, joins, where)
    }
    
    /** Returns the number of rows matching the query conditions. */
    suspend fun getRowCountSusp(                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = readSusp(0, "getRowCountSusp") {
        it.getRowCount(table, joins, where)
    }
    
    
    /** Returns true if at least one row matches the query conditions. */
    fun hasRows(                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = read(false, "hasRows") {
        it.hasRows(table, joins, where)
    }
    
    /** Returns true if at least one row matches the query conditions. */
    suspend fun hasRowsSusp(                                      table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
    ) = readSusp(false, "hasRowsSusp") {
        it.hasRows(table, joins, where)
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
    
    
    inline fun <T> getLastPositionBy(                                              table: String,
                                                                             whereColumn: String,
                                                                                  equals: T,
    ) = read(0, "getLastPositionBy") {
        it.getLastPositionBy(table, whereColumn, equals)
    }
    
    suspend fun <T> getLastPositionBySusp(                                         table: String,
                                                                             whereColumn: String,
                                                                                  equals: T,
    ) = readSusp(0, "getLastPositionBySusp") {
        it.getLastPositionBy(table, whereColumn, equals)
    }
    
    
    
    
    
    inline fun <T> getLargestInt(                                            table: String,
                                                                      targetColumn: String,
                                                                       whereColumn: String? = null,
                                                                            equals: T? = null,
    ) = read(0, "getLargestInt") {
        it.getLargestInt(table, targetColumn, whereColumn, equals)
    }
    
    suspend fun <T> getLargestIntSusp(                                       table: String,
                                                                      targetColumn: String,
                                                                       whereColumn: String? = null,
                                                                            equals: T? = null,
    ) = readSusp(0, "getLargestIntSusp") {
        it.getLargestInt(table, targetColumn, whereColumn, equals)
    }
    
    
    inline fun getDbFileName() = read("", "getDbFileName") { it.getDbFileName() }
    suspend fun getDbFileNameSusp() = readSusp("", "getDbFileNameSusp") { it.getDbFileName() }
    
    
    
    
    
    inline fun <reified T> getRandomVal(                          table: String,
                                                                 column: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
    ): T? = read(null, "getRandomVal") { db ->
        db.getRandomVal<T>(table, column, where)
    }
    
    suspend inline fun <reified T> getRandomValSusp(              table: String,
                                                                 column: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
    ): T? = readSusp(null, "getRandomValSusp") { db ->
        db.getRandomVal<T>(table, column, where)
    }
    
    
    fun getRandomId(                                              table: String,
                                                                  where: WhereBuilder.()->Unit = {},
    ): Int = read(-1, "getRandomId") { db ->
        db.getRandomId(table, where)
    }
    
    suspend fun getRandomIdSusp(                                  table: String,
                                                                  where: WhereBuilder.()->Unit = {},
    ): Int = readSusp(-1, "getRandomIdSusp") { db ->
        db.getRandomId(table, where)
    }
    
    
    inline fun <reified T : DbEntity> getRandomObj(               table: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
    ): T? = read(null, "getRandomObj") { db ->
        db.getRandomObj<T>(table, where)
    }
    
    suspend inline fun <reified T : DbEntity> getRandomObjSusp(
                                                                  table: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
    ): T? = readSusp(null, "getRandomObjSusp") { db ->
        db.getRandomObj<T>(table, where)
    }
    
    
    
    
    
    
    
}