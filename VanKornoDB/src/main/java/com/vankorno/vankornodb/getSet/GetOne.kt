package com.vankorno.vankornodb.getSet

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.CondBuilder
import com.vankorno.vankornodb.core.DbConstants.ID
import com.vankorno.vankornodb.core.JoinBuilder
import com.vankorno.vankornodb.getCursor
import kotlin.reflect.KClass

inline fun <reified T : Any> SQLiteDatabase.getOneById(                            table: String,
                                                                                      id: Int
): T = getOne(table, where = {ID equal id}, limit = 1)


inline fun <reified T : Any> SQLiteDatabase.getOneOrNullById(                      table: String,
                                                                                      id: Int
): T? = getOneOrNull(table, where = {ID equal id}, limit = 1)




inline fun <reified T : Any> SQLiteDatabase.getOne(         table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                   noinline joins: JoinBuilder.()->Unit = {},
                                                   noinline where: CondBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = 1,
                                                           offset: Int? = null,
                                                        customEnd: String = ""
): T = getCursor(
    table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    if (!cursor.moveToFirst()) error("No result for getOne<>()")
    cursor.toEntity(T::class)
}



inline fun <reified T : Any> SQLiteDatabase.getOne(                table: String,
                                                                  column: String,
                                                          noinline joins: JoinBuilder.()->Unit = {},
                                                          noinline where: CondBuilder.()->Unit = {},
                                                                 groupBy: String = "",
                                                                  having: String = "",
                                                                 orderBy: String = "",
                                                                   limit: Int? = 1,
                                                                  offset: Int? = null,
                                                               customEnd: String = ""
): T = getOne(
    table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset, customEnd
)



fun <T : Any> SQLiteDatabase.getOne(                        clazz: KClass<T>,
                                                            table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                            joins: JoinBuilder.()->Unit = {},
                                                            where: CondBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = 1,
                                                           offset: Int? = null,
                                                        customEnd: String = ""
): T = getCursor(
    table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    if (!cursor.moveToFirst()) error("No result for getOne($clazz)")
    cursor.toEntity(clazz)
}



fun <T : Any> SQLiteDatabase.getOne(                               clazz: KClass<T>,
                                                                   table: String,
                                                                  column: String,
                                                                   joins: JoinBuilder.()->Unit = {},
                                                                   where: CondBuilder.()->Unit = {},
                                                                 groupBy: String = "",
                                                                  having: String = "",
                                                                 orderBy: String = "",
                                                                   limit: Int? = 1,
                                                                  offset: Int? = null,
                                                               customEnd: String = ""
): T = getOne(
    clazz, table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset, customEnd
)











inline fun <reified T : Any> SQLiteDatabase.getOneOrNull(   table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                   noinline joins: JoinBuilder.()->Unit = {},
                                                   noinline where: CondBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = 1,
                                                           offset: Int? = null,
                                                        customEnd: String = ""
): T? = getCursor(
    table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(T::class)
}



inline fun <reified T : Any> SQLiteDatabase.getOneOrNull(          table: String,
                                                                  column: String,
                                                          noinline joins: JoinBuilder.()->Unit = {},
                                                          noinline where: CondBuilder.()->Unit = {},
                                                                 groupBy: String = "",
                                                                  having: String = "",
                                                                 orderBy: String = "",
                                                                   limit: Int? = 1,
                                                                  offset: Int? = null,
                                                               customEnd: String = ""
): T? = getOneOrNull(
    table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset, customEnd
)



fun <T : Any> SQLiteDatabase.getOneOrNull(                  clazz: KClass<T>,
                                                            table: String,
                                                          columns: Array<out String> = arrayOf("*"),
                                                            joins: JoinBuilder.()->Unit = {},
                                                            where: CondBuilder.()->Unit = {},
                                                          groupBy: String = "",
                                                           having: String = "",
                                                          orderBy: String = "",
                                                            limit: Int? = 1,
                                                           offset: Int? = null,
                                                        customEnd: String = ""
): T? = getCursor(
    table, columns, joins, where, groupBy, having, orderBy, limit, offset, customEnd
).use { cursor ->
    if (!cursor.moveToFirst()) return null
    cursor.toEntity(clazz)
}



fun <T : Any> SQLiteDatabase.getOneOrNull(                         clazz: KClass<T>,
                                                                   table: String,
                                                                  column: String,
                                                                   joins: JoinBuilder.()->Unit = {},
                                                                   where: CondBuilder.()->Unit = {},
                                                                 groupBy: String = "",
                                                                  having: String = "",
                                                                 orderBy: String = "",
                                                                   limit: Int? = 1,
                                                                  offset: Int? = null,
                                                               customEnd: String = ""
): T? = getOneOrNull(
    clazz, table, arrayOf(column), joins, where, groupBy, having, orderBy, limit, offset, customEnd
)




