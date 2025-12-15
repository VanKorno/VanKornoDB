package com.vankorno.vankornodb.get.noty
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.QueryOpts
import com.vankorno.vankornodb.get.getCursorPro
import com.vankorno.vankornodb.get.raw.getTypedValAt

/**
 * Retrieves a list of values from a single column, cast to the specified type [T].
 */
inline fun <reified T> SQLiteDatabase.getColValsNoty(                table: String,
                                                                columnName: String,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
): List<T> = getCursorPro(table, columnName) {
    applyOpts(queryOpts)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(cursor.getTypedValAt<T>(0))
            } while (cursor.moveToNext())
        }
    }
}




/**
 * Retrieves values of one type from multiple columns of a single row of the specified table.
 *
 * @param table Name of the table to query.
 * @param columns One or more column names to retrieve.
 * @return A list of column values (nullable) for the first matching row, or an empty list if no rows match.
 */
inline fun <reified T> SQLiteDatabase.getRowValsProNoty(             table: String,
                                                                   columns: Array<out String>,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
): List<T?> = getMultiRowValsProNoty<T>(table, columns) {
    applyOpts(queryOpts)
    limit = 1
}.firstOrNull() ?: emptyList()





/**
 * Retrieves values from multiple rows and multiple columns of the specified table, using the standard VanKornoDB DSL.
 *
 * @param table Name of the table to query.
 * @param columns Array of column names to retrieve.
 */
inline fun <reified T> SQLiteDatabase.getMultiRowValsProNoty(        table: String,
                                                                   columns: Array<out String>,
                                                        noinline queryOpts: QueryOpts.()->Unit = {},
): List<List<T?>> = getCursorPro(table, columns) {
    applyOpts(queryOpts)
}.use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(List(columns.size) { idx -> cursor.getTypedVal<T>(idx) })
            } while (cursor.moveToNext())
        }
    }
}





/**
 * Reads a typed value from the cursor at the given column index.
 *
 * @param idx Column index.
 * @return The value at the given index cast to type [T], or `null` if the value is SQL NULL.
 * @throws IllegalStateException if the type [T] is not supported.
 */
inline fun <reified T> Cursor.getTypedVal(                                           idx: Int
): T? {
    val value: Any? = when (getType(idx)) {
        Cursor.FIELD_TYPE_INTEGER -> getLong(idx)
        Cursor.FIELD_TYPE_FLOAT   -> getDouble(idx)
        Cursor.FIELD_TYPE_STRING  -> getString(idx)
        Cursor.FIELD_TYPE_BLOB    -> getBlob(idx)
        else                      -> null
    }
    
    @Suppress("UNCHECKED_CAST")
    return when (T::class) {
        Boolean::class   -> if (value == null) null else ((value as Long) != 0L) as T
        Int::class       -> (value as? Long)?.toInt() as T?
        Long::class      -> value as T?
        Float::class     -> (value as? Double)?.toFloat() as T?
        Double::class    -> value as? T
        String::class    -> value as T?
        ByteArray::class -> value as T?
        else             -> error("Unsupported type: ${T::class}")
    }
}

