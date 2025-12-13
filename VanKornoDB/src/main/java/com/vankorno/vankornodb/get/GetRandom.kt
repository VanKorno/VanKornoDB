package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants._ID
import com.vankorno.vankornodb.core.data.DbConstants.randomVal
import com.vankorno.vankornodb.mapper.toEntity

/**
 * Retrieves a single random value from the specified column in a table.
 *
 * @param T The type of the value to retrieve.
 * @param table The name of the table to query.
 * @param column The name of the column to retrieve the value from.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random value of type [T] from the column, or null if no rows match.
 */
inline fun <reified T> SQLiteDatabase.getRandomVal(               table: String,
                                                                 column: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
): T? = getCursorPro(table, column) {
    applyOpts(
        where = where,
        limit = 1
    )
    orderBy { +randomVal }
}.use { cursor ->
    if (cursor.moveToFirst())
        cursor.getTypedVal<T>(0)
    else
        null
}



/**
 * Retrieves a single random ID from the specified table.
 *
 * @param table The name of the table to query.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random ID from the table, or -1 if no rows match.
 */
fun SQLiteDatabase.getRandomId(                                   table: String,
                                                                  where: WhereBuilder.()->Unit = {},
): Int = getRandomVal<Int>(table, _ID, where) ?: -1




/**
 * Retrieves a single random object (row) from the specified table and maps it to a Kotlin class.
 *
 * @param T The type of the object to retrieve. Must be a Kotlin class representing the table structure.
 * @param table The name of the table to query.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random object of type [T] from the table, or null if no rows match.
 */
inline fun <reified T : DbEntity> SQLiteDatabase.getRandomObj(    table: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
): T? = getCursorPro(table) {
    applyOpts(
        where = where,
        limit = 1
    )
    orderBy { +randomVal }
}.use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(T::class)
}









