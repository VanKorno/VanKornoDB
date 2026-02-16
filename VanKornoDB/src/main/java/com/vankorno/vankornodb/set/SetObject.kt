// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.api.toContentValues
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.misc.wLog

/**
 * Updates the row with the specified WhereDsl conditions in the given table with the values from [obj].
 * Converts the entity into ContentValues and performs the SQLite update operation.
 * If [tableInfo]'s schema bundle doesn't have a setter - reflection-based mapping is used.
 *
 * @param tableInfo The combination of a table name and its SchemaBundle.
 * @param obj The entity object with updated data.
 * @param where DSL conditions.
 * @return The number of rows affected.
 */
inline fun <T : CurrEntity> SQLiteDatabase.setObj(                tableInfo: TableInfoNormal<out T>,
                                                                        obj: T,
                                                                      where: WhereDsl.()->Unit,
): Int {
    val cv = toContentValues(obj, tableInfo.schema)
    val whereDsl = WhereDsl().apply(where)
    val whereClause = whereDsl.clauses.joinToString(" ")
    val whereArgs = whereDsl.args.toTypedArray()
    val table = tableInfo.name
    
    val affected = update(table, cv, whereClause, whereArgs)
    if (affected > 1) {
        // region LOG
        wLog("setObj: $affected rows updated in '$table'. You may want to set more specific conditions if you want to update a single row.")
        // endregion
    }
    return affected
}






