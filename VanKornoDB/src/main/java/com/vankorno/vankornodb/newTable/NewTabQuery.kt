// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.newTable

import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.*

/**
 * Generates a SQL `CREATE TABLE` statement for the specified [table], using a pre-defined
 * list of [TypedColumn] definitions.
 *
 * Behavior:
 * - Each entry in [columns] represents a single database column.
 * - Column names, types, and default values are taken directly from the provided
 *   [TypedColumn] instances.
 * - List-based fields are already expanded before reaching this function, so no
 *   special handling or reflection is required here.
 * - Default values are included in the SQL definition when applicable.
 * - Unsupported or invalid column definitions are skipped.
 *
 * This function performs no reflection and relies entirely on the structure produced
 * by the EntityColumns / ColumnsBuilder DSL.
 *
 * @param table The name of the SQLite table to create.
 * @param columns The list of fully-defined columns used to build the table schema.
 * @return A complete `CREATE TABLE` SQL string.
 */
internal fun newTableQuery(                                             table: String,
                                                                      columns: List<TypedColumn<*>>,
): String {
    val defs = columns.mapNotNull { col ->
        val name = col.name
        
        val (typeSql, defaultSql) = when (col) {
            is IntCol -> {
                if (name == _ID)
                    ColumnTypeSql.ID.sql to null
                else
                    ColumnTypeSql.INT.sql to col.defaultVal.toString()
            }
            is StrCol -> ColumnTypeSql.STR.sql to "'${col.defaultVal}'"
            
            is BoolCol -> ColumnTypeSql.BOOL.sql to (if (col.defaultVal) "1" else "0")
            
            is LongCol -> ColumnTypeSql.LONG.sql to col.defaultVal.toString()
            
            is FloatCol -> ColumnTypeSql.FLOAT.sql to col.defaultVal.toString()
            
            is BlobCol -> ColumnTypeSql.BLOB.sql to null
        }
        
        val defaultClause = if (defaultSql != null)
                                DEFAULT + defaultSql
                            else
                                ""
        
        name + typeSql + defaultClause
    }
    
    return CREATE_TABLE + table + " (" + defs.joinToString(", ") + ")"
}

