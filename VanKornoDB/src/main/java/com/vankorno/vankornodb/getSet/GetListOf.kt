package com.vankorno.vankornodb.getSet
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.getBool

/**
 * Retrieves a list of values from a single column, cast to the specified type [V]. 
 * Supports filtering, sorting, grouping, and pagination.
 */
inline fun <reified V> SQLiteDatabase.getList(                    table: String,
                                                                 column: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = ""
): List<V> = getCursor(
    table, column, joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    buildList {
        while (cursor.moveToNext()) {
            @Suppress("UNCHECKED_CAST")
            add(
                when (V::class) {
                    Boolean::class -> cursor.getBool(0)
                    Int::class -> cursor.getInt(0)
                    Long::class -> cursor.getLong(0)
                    Float::class -> cursor.getFloat(0)
                    Double::class -> cursor.getDouble(0)
                    String::class -> cursor.getString(0)
                    ByteArray::class -> cursor.getBlob(0)
                    else -> error("Unsupported column type: ${V::class}")
                } as V
            )
        }
    }
}