package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.get.noty.getCursorProNoty
import com.vankorno.vankornodb.get.noty.getTypedVal
import com.vankorno.vankornodb.mapper.toEntity
import com.vankorno.vankornodb.misc.data.SharedCol.shID

fun SQLiteDatabase.getRandomInt(                                  table: String,
                                                                 column: IntCol,
                                                                  where: WhereBuilder.()->Unit = {},
): Int {
    val rand = getRandomValNoty<Int>(table, column.name, where) ?: run {
        // region LOG
            Log.e(DbTAG, "There are no available values to pick from. Returning -1")
        // endregion
        -1
    }
    return rand
}

// TODO random String, Long, Float



/**
 * Retrieves a single random ID from the specified table.
 *
 * @param table The name of the table to query.
 * @param where Optional lambda to specify additional WHERE conditions.
 * @return A random ID from the table, or -1 if no rows match.
 */
fun SQLiteDatabase.getRandomId(                                   table: String,
                                                                  where: WhereBuilder.()->Unit = {},
): Int = getRandomInt(table, shID, where)




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
    orderRandomly()
}.use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(T::class)
}




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
inline fun <reified T> SQLiteDatabase.getRandomValNoty(           table: String,
                                                                 column: String,
                                                         noinline where: WhereBuilder.()->Unit = {},
): T? = getCursorProNoty(table, arrayOf(column)) {
    applyOpts(
        where = where,
        limit = 1
    )
    orderRandomly()
}.use { cursor ->
    if (cursor.moveToFirst())
        cursor.getTypedVal<T>(0)
    else
        null
}





