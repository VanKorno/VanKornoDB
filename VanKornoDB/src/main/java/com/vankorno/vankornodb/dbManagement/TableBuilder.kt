package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.vankorno.vankornodb.core.DbConstants.dbCreateT
import com.vankorno.vankornodb.core.DbConstants.dbDefault
import com.vankorno.vankornodb.dbManagement.TableBuilderUtils.getColumnDefinition
import com.vankorno.vankornodb.dbManagement.TableBuilderUtils.getColumnType
import com.vankorno.vankornodb.dbManagement.data.AutoId
import com.vankorno.vankornodb.dbManagement.data.AutoIdNullable
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BlobColNullable
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.BoolColNullable
import com.vankorno.vankornodb.dbManagement.data.ColumnType
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.FloatColNullable
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.IntColNullable
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.LongColNullable
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.dbManagement.data.StrColNullable
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


/**
 * Creates a single table in db.
 */
fun SQLiteDatabase.createTable(tableName: String, entityClass: KClass<*>) = createTables(tableOf(tableName, entityClass))

/**
 * Creates multiple tables in the database given vararg TableInfo.
 */
fun SQLiteDatabase.createTables(vararg tables: TableInfo) = createTables(tables.toList())

/**
 * Creates multiple tables in the database from a list of TableInfo.
 */
fun SQLiteDatabase.createTables(tables: List<TableInfo>) = tables.forEach { execSQL(it.createQuery()) }



/** 
 * Creates a TableInfo instance for a data class and table name.
 * Used to define table schema for creation.
 */
fun tableOf(                                                                   tableName: String,
                                                                             entityClass: KClass<*>
): TableInfo = object : TableInfo {
    override val name = tableName
    override fun createQuery() = newTableQuery(tableName, entityClass)
}




/**
 * Generates a SQL `CREATE TABLE` statement for the specified [tableName], based on the structure of
 * the given data class [entityClass].
 *
 * Requirements:
 * - [entityClass] must have a primary constructor.
 * - All constructor parameters must have default values.
 *
 * Behavior:
 * - Each constructor parameter becomes a column, with its type inferred via reflection.
 * - Parameters ending in `"List"` and declared as `List<T>` with a non-empty default list are
 *   expanded into multiple columns. For example:
 *     `tagList: List<String> = listOf("a", "b")` → columns `tag1 TEXT DEFAULT 'a'`, `tag2 TEXT DEFAULT 'b'`.
 * - Only non-nullable, supported types are included. Empty lists, arrays, and unsupported types are skipped.
 * - When possible, default values are added to the SQL definition using the `DEFAULT` clause.
 *
 * @param tableName The name of the SQLite table to create.
 * @param entityClass The data class used to derive the table schema.
 * @return A complete `CREATE TABLE` SQL string based on [entityClass].
 */

fun newTableQuery(                                                             tableName: String,
                                                                             entityClass: KClass<*>
): String {
    val constructor = entityClass.primaryConstructor
        ?: error("Class ${entityClass.simpleName} must have a primary constructor")
    
    val defaultsInstance = constructor.callBy(emptyMap())
    val columns = mutableListOf<String>()
    
    constructor.parameters.forEach { param ->
        val name = param.name ?: return@forEach
        val classifier = param.type.classifier
        
        // Handle List<T> fields
        if (name.endsWith("List") && classifier == List::class) {
            val property = entityClass.memberProperties.firstOrNull { it.name == name } ?: return@forEach
            
            val value = property.getter.call(defaultsInstance)
            
            val list = value as? List<*> ?: return@forEach
            if (list.isEmpty()) return@forEach
            
            val elementType = param.type.arguments.firstOrNull()?.type?.classifier as? KClass<*>
                ?: error("Cannot determine element type for $name")
            
            val defaultValue = list.first()
            val columnType = getColumnType(name, elementType, isNullable = false)
                ?: error("Unsupported element type $elementType for list field $name")

            val defaultSqlValue = when (defaultValue) {
                is String -> "'$defaultValue'"
                is Boolean -> if (defaultValue) "1" else "0"
                is ByteArray -> null // skip default for blobs
                null -> null // treat null same as no default
                else -> defaultValue.toString()
            }
            
            val defaultClause = defaultSqlValue?.let { dbDefault + it } ?: ""
            
            list.indices.forEach { idx ->
                val colName = name.removeSuffix("List") + (idx + 1)
                val colDef = colName + columnType.sql + defaultClause
                columns += colDef
            }
        } else { // Normal field
            val col = getColumnDefinition(param, defaultsInstance)
            if (col != null) columns += col
        }
    }
    val queryStr = dbCreateT + tableName + " (" + columns.joinToString(", ") + ")"
    // region LOG
        println("newTableQuery<${T::class.simpleName}>(): $queryStr")
    // endregion
    return queryStr
}



object TableBuilderUtils {
    /**
    * Maps a Kotlin type to an internal SQL column type, considering nullability and field name.
    * Special case: if the field is named "id", it's treated as an auto-increment primary key.
    * Returns null for unsupported types.
    */
    fun getColumnType(                                                     paramName: String?,
                                                                          classifier: KClassifier?,
                                                                          isNullable: Boolean
    ): ColumnType? {
        if (paramName == null) return null
    
        return when (classifier) {
            Int::class ->       if (paramName == "id") {
                                    if (isNullable) AutoIdNullable else AutoId
                                } else {
                                    if (isNullable) IntColNullable else IntCol
                                }
            String::class ->    if (isNullable) StrColNullable else StrCol
            Boolean::class ->   if (isNullable) BoolColNullable else BoolCol
            Long::class ->      if (isNullable) LongColNullable else LongCol
            Float::class ->     if (isNullable) FloatColNullable else FloatCol
            ByteArray::class -> if (isNullable) BlobColNullable else BlobCol
            else -> null // unsupported types like List, Array, etc.
        }
    }
    
    inline fun <reified T : Any> getColumnDefinition(                            param: KParameter,
                                                                      defaultsInstance: T
    ): String? {
        val name = param.name ?: return null
        val classifier = param.type.classifier
        val isNullable = param.type.isMarkedNullable
        
        val colType = getColumnType(name, classifier, isNullable) ?: return null
        
        val defaultValue = defaultsInstance::class.memberProperties
            .firstOrNull { it.name == name }?.getter?.call(defaultsInstance)
        
        // SKIP default clause for AutoId/AutoIdNullable or null default
        val skipDefault = colType == AutoId || colType == AutoIdNullable || defaultValue == null
        
        val defaultClause = if (!isNullable  &&  !skipDefault) {
            val defaultSqlValue = when (defaultValue) {
                is String -> "'$defaultValue'"
                is Boolean -> if (defaultValue) "1" else "0"
                is ByteArray -> null // skip default for blobs
                else -> defaultValue.toString()
            }
            if (defaultSqlValue != null) dbDefault + defaultSqlValue else ""
        } else ""
        
        return name + colType.sql + defaultClause
    }
}






