/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.data.DbConstants.DbTypeTable
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_AndroidMetadata
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_EntityVersions
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_Master
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cType
import java.io.File

fun SQLiteDatabase.tableExists(                                                    table: String,
) = rawQuery(
    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
    arrayOf(table)
).use { it.moveToFirst() }


/**
 * Gets the names of all tables in the database that are not "internal"
 */
fun SQLiteDatabase.getAppTableNames(): List<String> = getColStrings(
    table = TABLE_Master,
    column = cName,
    orderBy = { cName },
    where = {
        cType equal DbTypeTable
        and { cName notLike "sqlite_%" }
        and { cName.notEqualAnyOf(TABLE_AndroidMetadata, TABLE_EntityVersions) }
    },
)


/**
 * Gets the names of all internal, system tables in the database, including the entity version table,
 * that is managed by VanKornoDB
 */
fun SQLiteDatabase.getInternalTableNames(): List<String> = getColStrings(
    table = TABLE_Master,
    column = cName,
    orderBy = { cName },
    where = {
        cType equal DbTypeTable
        andGroup {
            cName like "sqlite_%"
            or { cName.equalAnyOf(TABLE_AndroidMetadata, TABLE_EntityVersions) }
        }
    }
)

// TODO get tables with data, not just names



fun SQLiteDatabase.isTableEmpty(table: String) = !hasRows(table)



fun SQLiteDatabase.getDbFileName(): String = File(path).name




