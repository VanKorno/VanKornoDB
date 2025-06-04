package com.vankorno.vankornodb.getSet

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.getBool
import kotlin.reflect.KClass


/** 
 * Retrieves a list of entities of type [T] mapped from the specified columns. 
 * Supports joins, filtering, grouping, sorting, pagination, and optional post-mapping. 
 */
inline fun <reified T : Any> SQLiteDatabase.getRows(              table: String,
                                                         noinline joins: JoinBuilder.()->Unit = {},
                                                         noinline where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                      noinline mapAfter: (T)->T = {it}
): List<T> = getCursor(
    table, arrayOf("*"), joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(mapAfter(cursor.toEntity(T::class)))
            } while (cursor.moveToNext())
        }
    }
}





/** 
 * Retrieves a list of entities of the specified [clazz] mapped from the given columns. 
 * Similar to the reified version but uses explicit KClass parameter.
 */
fun <T : Any> SQLiteDatabase.getRows(                             clazz: KClass<T>,
                                                                  table: String,
                                                                  joins: JoinBuilder.()->Unit = {},
                                                                  where: WhereBuilder.()->Unit = {},
                                                                groupBy: String = "",
                                                                 having: String = "",
                                                                orderBy: String = "",
                                                                  limit: Int? = null,
                                                                 offset: Int? = null,
                                                              customEnd: String = "",
                                                               mapAfter: (T)->T = {it}
): List<T> = getCursor(
    table, arrayOf("*"), joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(mapAfter(cursor.toEntity(clazz)))
            } while (cursor.moveToNext())
        }
    }
}




