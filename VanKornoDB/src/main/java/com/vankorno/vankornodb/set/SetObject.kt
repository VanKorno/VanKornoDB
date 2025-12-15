package com.vankorno.vankornodb.set
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.add.internal.toContentValues
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.core.data.DbConstants._ID


/**
 * Updates the row with the specified [id] in the given table with the values from [obj].
 * Converts the entity into ContentValues and performs the SQLite update operation.
 *
 * @param table The name of the table to update.
 * @param id The primary key ID of the row to update.
 * @param obj The entity object with updated data.
 * @return The number of rows affected.
 */
fun <T : DbEntity> SQLiteDatabase.setObjById(                                         id: Int,
                                                                                   table: String,
                                                                                     obj: T,
): Int {
    val cv = toContentValues(obj)
    return update(table, cv, _ID+"=?", arrayOf(id.toString()))
}


inline fun <T : DbEntity> SQLiteDatabase.setObj(                       table: String,
                                                                         obj: T,
                                                                       where: WhereBuilder.()->Unit,
): Int {
    val cv = toContentValues(obj)
    val whereBuilder = WhereBuilder().apply(where)
    val whereClause = whereBuilder.clauses.joinToString(" ")
    val whereArgs = whereBuilder.args.toTypedArray()
    
    val affected = update(table, cv, whereClause, whereArgs)
    if (affected > 1) {
        // region LOG
        Log.w(DbTAG, "setObj: $affected rows updated in '$table'. You may want to set more specific conditions if you want to update a single row.")
        // endregion
    }
    return affected
}
