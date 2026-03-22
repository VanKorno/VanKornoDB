/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.data.DbConstants.DbTypeTable
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_AndroidMetadata
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_EntityVersions
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_Master
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import com.vankorno.vankornodb.misc.data.SharedCol.cType
import java.io.File


fun SQLiteDatabase.getLastId(table: String) = getLargestLong(table, cID)


fun SQLiteDatabase.getAllIDs(                                         table: String,
                                                                    orderBy: OrderDsl.()->Unit = {},
) = getColLongsPro(table, cID) { orderBy(orderBy) }



fun SQLiteDatabase.tableExists(                                                    table: String,
) = rawQuery(
    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
    arrayOf(table)
).use { it.moveToFirst() }


/**
 * Gets the names of all tables in the database that are not "internal"
 */
fun SQLiteDatabase.getAppTableNames(): List<String> = getColStringsPro(TABLE_Master, cName) {
    where {
        cType equal DbTypeTable
        and { cName notLike "sqlite_%" }
        and { cName.notEqualAnyOf(TABLE_AndroidMetadata, TABLE_EntityVersions) }
    }
    orderByName()
}


/**
 * Gets the names of all internal, system tables in the database, including the entity version table,
 * that is managed by VanKornoDB
 */
fun SQLiteDatabase.getInternalTableNames(): List<String> = getColStringsPro(TABLE_Master, cName) {
    where {
        cType equal DbTypeTable
        andGroup {
            cName like "sqlite_%"
            or { cName.equalAnyOf(TABLE_AndroidMetadata, TABLE_EntityVersions)}
        }
    }
    orderByName()
}

// TODO get tables with data, not just names



fun SQLiteDatabase.isTableEmpty(table: String) = !hasRows(table)


fun SQLiteDatabase.getLastPosition(                                   table: String,
                                                                      where: WhereDsl.()->Unit = {},
) = getLargestLong(table, cPosition, where)



fun SQLiteDatabase.getLargestInt(                                     table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
): Int = getInt(
    table,
    iCol("IFNULL(MAX(${column.name}), 0)"),
    where
)


fun SQLiteDatabase.getLargestLong(                                    table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
): Long = getLong(
    table,
    lCol("IFNULL(MAX(${column.name}), 0)"),
    where
)



fun SQLiteDatabase.getDbFileName(): String = File(path).name




