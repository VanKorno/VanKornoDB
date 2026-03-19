// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.get.noty.getCursorProNoty
import com.vankorno.vankornodb.get.noty.getTypedVal
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.suppressValGetterErrorLog

fun SQLiteDatabase.getRandomInt(                                      table: String,
                                                                     column: IntCol,
                                                                      where: WhereDsl.()->Unit = {},
): Int {
    val rand = getRandomValNoty<Int>(table, column.name, where) ?: run {
        // region LOG
        if (!suppressValGetterErrorLog)
            eLog("There are no available values to pick from. Returning -1")
        // endregion
        -1
    }
    return rand
}


fun SQLiteDatabase.getRandomString(                                  table: String,
                                                                     column: StrCol,
                                                                      where: WhereDsl.()->Unit = {},
): String {
    val rand = getRandomValNoty<String>(table, column.name, where) ?: run {
        // region LOG
        if (!suppressValGetterErrorLog)
            eLog("There are no available values to pick from. Returning empty string")
        // endregion
        ""
    }
    return rand
}


fun SQLiteDatabase.getRandomLong(                                    table: String,
                                                                     column: LongCol,
                                                                      where: WhereDsl.()->Unit = {},
): Long {
    val rand = getRandomValNoty<Long>(table, column.name, where) ?: run {
        // region LOG
        if (!suppressValGetterErrorLog)
            eLog("There are no available values to pick from. Returning -1")
        // endregion
        -1L
    }
    return rand
}


fun SQLiteDatabase.getRandomFloat(                                   table: String,
                                                                     column: FloatCol,
                                                                      where: WhereDsl.()->Unit = {},
): Float {
    val rand = getRandomValNoty<Float>(table, column.name, where) ?: run {
        // region LOG
        if (!suppressValGetterErrorLog)
            eLog("There are no available values to pick from. Returning -1")
        // endregion
        -1f
    }
    return rand
}




/**
 * Retrieves a single random ID from the specified table.
 *
 * @param table The name of the table to query.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random ID from the table, or -1 if no rows match.
 */
fun SQLiteDatabase.getRandomId(                                       table: String,
                                                                      where: WhereDsl.()->Unit = {},
): Int = getRandomInt(table, cID, where)









// Not type-safe

/**
 * Retrieves a single random value from the specified column in a table.
 *
 * @param T The type of the value to retrieve.
 * @param table The name of the table to query.
 * @param column The name of the column to retrieve the value from.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random value of type [T] from the column, or null if no rows match.
 */
inline fun <reified T> SQLiteDatabase.getRandomValNoty(               table: String,
                                                                     column: String,
                                                             noinline where: WhereDsl.()->Unit = {},
): T? = getCursorProNoty(table, arrayOf(column)) {
    this.where = where
    limit = 1
    orderRandomly()
}.use { cursor ->
    if (cursor.moveToFirst())
        cursor.getTypedVal<T>(0)
    else
        null
}





