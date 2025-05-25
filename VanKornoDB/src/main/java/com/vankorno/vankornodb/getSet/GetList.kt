package com.vankorno.vankornodb.getSet

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.WhereBuilder
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.getBool
import kotlin.reflect.KClass

/** 
 * Retrieves a list of values from a single column, cast to the specified type [V]. 
 * Supports filtering, sorting, grouping, and pagination.
 */
inline fun <reified V> SQLiteDatabase.getListOf(                   table: String,
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



/** 
 * Retrieves a list of entities of type [T] mapped from the specified columns. 
 * Supports joins, filtering, grouping, sorting, pagination, and optional post-mapping. 
 */
inline fun <reified T : Any> SQLiteDatabase.getList(        table: String,
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
fun <T : Any> SQLiteDatabase.getList(                       clazz: KClass<T>,
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




