package com.vankorno.vankornodb.dbManagement
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.comma
import com.vankorno.vankornodb.core.DbConstants.dbCreateT
import com.vankorno.vankornodb.dbManagement.data.ColumnDef
import com.vankorno.vankornodb.dbManagement.data.TableAndStructure
import kotlin.collections.forEach

/**
 * Optional db table creator that gets structure not from a data class, but as ArrayList of ColumnDef
 */

fun SQLiteDatabase.createTablesCustom(vararg tables: TableAndStructure) = createTablesCustom(tables.toList())

fun SQLiteDatabase.createTablesCustom(                              tables: List<TableAndStructure>
) {
    tables.forEach { execSQL(newTableQueryCustom(it.tableName, it.structure))}
}


fun newTableQueryCustom(                                            tableName: String,
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

