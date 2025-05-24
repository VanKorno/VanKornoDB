package com.vankorno.vankornodb.getSet

import android.database.sqlite.SQLiteDatabase
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import com.vankorno.vankornodb.core.CondBuilder
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.getBool
import com.vankorno.vankornodb.getCursor
import kotlin.reflect.KClass


inline fun <reified V> SQLiteDatabase.getValueList(                table: String,
                                                                  column: String,
                                                          noinline joins: JoinBuilder.()->Unit = {},
                                                          noinline where: CondBuilder.()->Unit = {},
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




inline fun <reified T : Any> SQLiteDatabase.getList(        table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                   noinline joins: JoinBuilder.()->Unit = {},
                                                   noinline where: CondBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = null,
                                                           offset: Int? = null,
                                                        customEnd: String = "",
                                                noinline mapAfter: (T)->T = {it}
): List<T> = getCursor(
    table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(mapAfter(cursor.toEntity(T::class)))
            } while (cursor.moveToNext())
        }
    }
}



inline fun <reified T : Any> SQLiteDatabase.getList(               table: String,
                                                                  column: String,
                                                          noinline joins: JoinBuilder.()->Unit = {},
                                                          noinline where: CondBuilder.()->Unit = {},
                                                                 groupBy: String = "",
                                                                  having: String = "",
                                                                 orderBy: String = "",
                                                                   limit: Int? = null,
                                                                  offset: Int? = null,
                                                               customEnd: String = "",
                                                       noinline mapAfter: (T)->T = {it}
): List<T> = getList(
    table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter
)



fun <T : Any> SQLiteDatabase.getList(                       clazz: KClass<T>,
                                                            table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                            joins: JoinBuilder.()->Unit = {},
                                                            where: CondBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = null,
                                                           offset: Int? = null,
                                                        customEnd: String = "",
                                                         mapAfter: (T)->T = {it}
): List<T> = getCursor(
    table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    buildList {
        if (cursor.moveToFirst()) {
            do {
                add(mapAfter(cursor.toEntity(clazz)))
            } while (cursor.moveToNext())
        }
    }
}


fun <T : Any> SQLiteDatabase.getList(                              clazz: KClass<T>,
                                                                   table: String,
                                                                  column: String,
                                                                   joins: JoinBuilder.()->Unit = {},
                                                                   where: CondBuilder.()->Unit = {},
                                                                 groupBy: String = "",
                                                                  having: String = "",
                                                                 orderBy: String = "",
                                                                   limit: Int? = null,
                                                                  offset: Int? = null,
                                                               customEnd: String = "",
                                                                mapAfter: (T)->T = {it}
): List<T> = getList(
    clazz, table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset, customEnd, mapAfter
)

