package com.vankorno.vankornodb.get
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.misc.data.SharedCol.shID
import com.vankorno.vankornodb.misc.data.SharedCol.shName
import com.vankorno.vankornodb.misc.data.SharedCol.shType
import java.io.File


fun SQLiteDatabase.getLastId(table: String) = getLargestInt(table, ID, null, null)


fun SQLiteDatabase.getAllIDs(                                                   table: String,
                                                                              orderBy: String = "",
) = getColInts(table, shID) { orderBy(orderBy) }



fun SQLiteDatabase.tableExists(                                                    table: String,
) = rawQuery(
    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
    arrayOf(table)
).use { it.moveToFirst() }


/**
 * Gets the names of all tables in the database that are not "internal"
 */
fun SQLiteDatabase.getAppTableNames(): List<String> = getColStrings(TABLE_Master, shName) {
    where {
        shType equal DbTypeTable
        and { shName notLike "sqlite_%" }
        and { shName.notEqualAny(TABLE_AndroidMetadata, TABLE_EntityVersions) }
    }
    orderBy(Name)
}


/**
 * Gets the names of all internal, system tables in the database, including the entity version table,
 * that is managed by VanKornoDB
 */
fun SQLiteDatabase.getInternalTableNames(): List<String> = getColStrings(TABLE_Master, shName) {
    where {
        shType equal DbTypeTable
        andGroup {
            shName like "sqlite_%"
            or { shName.equalAny(TABLE_AndroidMetadata, TABLE_EntityVersions)}
        }
    }
    orderBy(Name)
}

// TODO get tables with data, not just names



fun SQLiteDatabase.isTableEmpty(table: String) = !hasRows(table)


fun SQLiteDatabase.getLastPosition(table: String) = getLargestInt(table, Position, null, null)


fun <T> SQLiteDatabase.getLastPositionBy(                                          table: String,
                                                                             whereColumn: String,
                                                                                  equals: T,
) = getLargestInt(table, Position, whereColumn, equals)



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


fun SQLiteDatabase.getDbFileName(): String = File(path).name




