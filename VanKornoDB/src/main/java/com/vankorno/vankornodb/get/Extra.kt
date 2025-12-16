package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.OrderByBuilder
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import com.vankorno.vankornodb.misc.data.SharedCol.cType
import java.io.File


fun SQLiteDatabase.getLastId(table: String) = getLargestInt(table, cID)


fun SQLiteDatabase.getAllIDs(                                   table: String,
                                                              orderBy: OrderByBuilder.()->Unit = {},
) = getColIntsPro(table, cID) { orderBy(orderBy) }



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
        and { cName.notEqualAny(TABLE_AndroidMetadata, TABLE_EntityVersions) }
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
            or { cName.equalAny(TABLE_AndroidMetadata, TABLE_EntityVersions)}
        }
    }
    orderByName()
}

// TODO get tables with data, not just names



fun SQLiteDatabase.isTableEmpty(table: String) = !hasRows(table)


fun SQLiteDatabase.getLastPosition(                               table: String,
                                                                  where: WhereBuilder.()->Unit = {},
) = getLargestInt(table, cPosition, where)



fun SQLiteDatabase.getLargestInt(                                 table: String,
                                                                 column: IntCol,
                                                                  where: WhereBuilder.()->Unit = {},
): Int = getInt(
    table,
    iCol("MAX(${column.name})"),
    where
)




fun SQLiteDatabase.getDbFileName(): String = File(path).name




