package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.DbConstants.*


fun SQLiteDatabase.getLastId(table: String) = getLargestInt(table, ID, null, null)


fun SQLiteDatabase.getAllIDs(                                                   table: String,
                                                                              orderBy: String = "",
) = getList<Int>(table, ID, orderBy = orderBy)



fun SQLiteDatabase.tableExists(                                                    table: String,
) = rawQuery(
    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
    arrayOf(table)
).use { it.moveToFirst() }


/**
 * Gets the names of all tables in the database that are not "internal"
 */
fun SQLiteDatabase.getAppTableNames(): List<String> = getList<String>(
    table = TABLE_Master,
    column = Name,
    where = {
        Type equal DbTypeTable
        and { Name notLike "sqlite_%" }
        and { Name.equalNone(TABLE_Master, TABLE_EntityVersions) }
    },
    orderBy = Name
)

/**
 * Gets the names of all internal, system tables in the database, including the entity version table,
 * that is managed by VanKornoDB
 */
fun SQLiteDatabase.getInternalTableNames(): List<String> = getList<String>(
    table = TABLE_Master,
    column = Name,
    where = {
        Type equal DbTypeTable
        andGroup {
            Name like "sqlite_%"
            or { Name.equalAny(TABLE_Master, TABLE_EntityVersions)}
        }
    },
    orderBy = Name
)

// TODO get tables with data, not just names



fun SQLiteDatabase.isTableEmpty(table: String) = !hasRows(table)


fun SQLiteDatabase.getLastOrder(table: String) = getLargestInt(table, Order, null, null)


fun <T> SQLiteDatabase.getLastOrderBy(                                             table: String,
                                                                             whereColumn: String,
                                                                                  equals: T,
) = getLargestInt(table, Order, whereColumn, equals)



fun <T> SQLiteDatabase.getLargestInt(                                        table: String,
                                                                      targetColumn: String,
                                                                       whereColumn: String? = null,
                                                                            equals: T? = null,
): Int {
    val hasConditions = whereColumn != null && equals != null
    
    val queryEnd = if (hasConditions) " WHERE $whereColumn=?" else ""
    val selectionArgs = if (hasConditions) arrayOf(equals.toString()) else null
    
    return rawQuery(
        "SELECT MAX($targetColumn) FROM $table" + queryEnd, selectionArgs
    ).use { cursor ->
        if (cursor.moveToFirst())
            cursor.getInt(0)
        else
            0
    }
}






