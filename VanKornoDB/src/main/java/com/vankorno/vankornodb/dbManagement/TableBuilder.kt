// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.core.data.DbConstants.CREATE_TABLE
import com.vankorno.vankornodb.core.data.DbConstants.DEFAULT
import com.vankorno.vankornodb.dbManagement.TableBuilderUtils.getColumnDefinition
import com.vankorno.vankornodb.dbManagement.TableBuilderUtils.getColumnType
import com.vankorno.vankornodb.dbManagement.data.*
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


internal fun SQLiteDatabase.createTablesInternal(                          vararg tables: TableInfo
) {
    for (table in tables) {
        execSQL(newTableQuery(table.name, table.entityClass))
    }
}




/**
 * Generates a SQL `CREATE TABLE` statement for the specified [table], based on the structure of
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
 * @param table The name of the SQLite table to create.
 * @param entityClass The data class used to derive the table schema.
 * @return A complete `CREATE TABLE` SQL string based on [entityClass].
 */

internal fun newTableQuery(                                             table: String,
                                                                  entityClass: KClass<out DbEntity>,
): String {
    val constructor = entityClass.primaryConstructor
        ?: error("Class ${entityClass.simpleName} must have a primary constructor")
    
    val defaultsInstance = constructor.callBy(emptyMap())
    val columns = mutableListOf<String>()
    
    for (param in constructor.parameters) {
        val name = param.name ?: continue //\/\/\
        val classifier = param.type.classifier
        
        // Handle List<T> fields
        if (name.endsWith("List") && classifier == List::class) {
            val property = entityClass.memberProperties.firstOrNull { it.name == name } ?: continue //\/\/\
            
            val value = property.getter.call(defaultsInstance)
            
            val list = value as? List<*> ?: continue //\/\/\
            if (list.isEmpty()) continue //\/\/\
            
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
            
            val defaultClause = defaultSqlValue?.let { DEFAULT + it } ?: ""
            
            for (idx in list.indices) {
                val colName = name.removeSuffix("List") + (idx + 1)
                val colDef = colName + columnType.sql + defaultClause
                columns += colDef
            }
        } else { // Normal field
            val col = getColumnDefinition(param, defaultsInstance)
            if (col != null) columns += col
        }
    }
    val queryStr = CREATE_TABLE + table + " (" + columns.joinToString(", ") + ")"
    // region LOG
        println("newTableQuery(): $queryStr")
    // endregion
    return queryStr
}



private object TableBuilderUtils {
    /**
    * Maps a Kotlin type to an internal SQL column type, considering nullability and field name.
    * Special case: if the field is named "id", it's treated as non auto-incremented primary key.
    * Returns null for unsupported types.
    */
    fun getColumnType(                                                     paramName: String?,
                                                                          classifier: KClassifier?,
                                                                          isNullable: Boolean,
    ): ColumnType? {
        if (paramName == null) return null
    
        return when (classifier) {
            Int::class ->       if (paramName == "id") {
                                    ColumnType.ID
                                } else {
                                    if (isNullable) IntColNullable else ColumnType.INT
                                }
            String::class ->    if (isNullable) StrColNullable else ColumnType.STR
            Boolean::class ->   if (isNullable) BoolColNullable else ColumnType.BOOL
            Long::class ->      if (isNullable) LongColNullable else ColumnType.LONG
            Float::class ->     if (isNullable) FloatColNullable else ColumnType.FLOAT
            ByteArray::class -> if (isNullable) BlobColNullable else ColumnType.BLOB
            else -> null
        }
    }
    
    inline fun <reified T : Any> getColumnDefinition(                            param: KParameter,
                                                                      defaultsInstance: T,
    ): String? {
        val name = param.name ?: return null
        val classifier = param.type.classifier
        val isNullable = param.type.isMarkedNullable
        
        val colType = getColumnType(name, classifier, isNullable) ?: return null
        
        val defaultValue = defaultsInstance::class.memberProperties
            .firstOrNull { it.name == name }?.getter?.call(defaultsInstance)
        
        // SKIP default clause for AutoId/AutoIdNullable or null default
        val skipDefault = colType == ColumnType.ID || defaultValue == null
        
        val defaultClause = if (!isNullable  &&  !skipDefault) {
            val defaultSqlValue = when (defaultValue) {
                is String -> "'$defaultValue'"
                is Boolean -> if (defaultValue) "1" else "0"
                is ByteArray -> null // skip default for blobs
                else -> defaultValue.toString()
            }
            if (defaultSqlValue != null) DEFAULT + defaultSqlValue else ""
        } else ""
        
        return name + colType.sql + defaultClause
    }
}






