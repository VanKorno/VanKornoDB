package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.comma
import com.vankorno.vankornodb.core.DbConstants.dbCreateT
import com.vankorno.vankornodb.core.DbConstants.dbDefault
import com.vankorno.vankornodb.dbManagement.TableBuilderUtils.getColumnDefinition
import com.vankorno.vankornodb.dbManagement.data.AutoId
import com.vankorno.vankornodb.dbManagement.data.AutoIdNullable
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BlobColNullable
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.BoolColNullable
import com.vankorno.vankornodb.dbManagement.data.ColumnDef
import com.vankorno.vankornodb.dbManagement.data.ColumnType
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.FloatColNullable
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.IntColNullable
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.LongColNullable
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.dbManagement.data.StrColNullable
import com.vankorno.vankornodb.dbManagement.data.TableAndEntt
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import kotlin.collections.forEachIndexed
import kotlin.collections.lastIndex
import kotlin.reflect.KClassifier
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


// ===================  S I M P L E,   S E M I - A U T O M A T I C  ===================

fun SQLiteDatabase.createAllTablesSimple(tables: List<TableAndEntt>) {
    tables.forEach { execSQL(newTableQuerySimple(it.tableName, it.entity))}
}

fun newTableQuerySimple(                                            tableName: String,
                                                                      columns: ArrayList<ColumnDef>
): String {
    val queryStr = buildString {
        append(dbCreateT)
        append(tableName)
        append(" (")
        columns.forEachIndexed { idx, column ->
            append(column.name)
            append(column.type.sql)
            if (idx < columns.lastIndex)
                append(comma)
        }
        append(")")
    }
    // region LOG
        println("DbTableBuilder().newTableQuery() returns: $queryStr")
    // endregion
    return queryStr
}



// ===================  A D V A N C E D,   A U T O M A T I C  ===================

/** For db helper's onCreate 
 * Usage example:
 *      val tables = listOf(
 *          tableOf<MyUser>("users"),
 *          tableOf<Settings>("settings")
 *      )
 *      db.createAllTables(tables)
 **/

inline fun <reified T : Any> tableOf(name: String): TableInfo = object : TableInfo {
    override val name = name
    override fun createQuery() = newTableQuery<T>(name)
}

fun SQLiteDatabase.createAllTables(tables: List<TableInfo>) {
    tables.forEach { execSQL(it.createQuery()) }
}




/** More advanced. Used in createAllTables().
 * Gets type and default values from the data class itself
**/

inline fun <reified T : Any> newTableQuery(                                    tableName: String
): String {
    val constructor = T::class.primaryConstructor
        ?: error("Class ${T::class.simpleName} must have a primary constructor")

    val defaultsInstance = constructor.callBy(emptyMap())

    val columns = constructor.parameters.mapNotNull { param ->
        getColumnDefinition(param, defaultsInstance)
    }

    val queryStr = dbCreateT + tableName + " (" + columns.joinToString(", ") + ")"
    // region LOG
        println("newTableQuery<${T::class.simpleName}>(): $queryStr")
    // endregion
    return queryStr
}



object TableBuilderUtils {
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
        
        val defaultValue = T::class.memberProperties
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











