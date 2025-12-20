package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.WhereDsl
import kotlin.reflect.KClass

/** 
 * Retrieves a list of entities of type [T] mapped from the specified columns.
 */
inline fun <reified T : DbEntity> SQLiteDatabase.getObjects(          table: String,
                                                             noinline where: WhereDsl.()->Unit = {},
): List<T> = getObjectsPro(table) {
    this.where = where
}



/** 
 * Retrieves a list of objects of the specified [clazz] mapped from the given columns. 
 * Similar to the reified version but uses explicit KClass parameter.
 */
fun <T : DbEntity> SQLiteDatabase.getObjects(                         table: String,
                                                                      clazz: KClass<T>,
                                                                      where: WhereDsl.()->Unit = {},
): List<T> = getObjectsPro(table, clazz) {
    this.where = where
}





/** 
 * Retrieves a map of objects of type [T] from the specified table, 
 * using the `id` column (Int) as the key.
 */
inline fun <reified T : DbEntity> SQLiteDatabase.getObjMap(           table: String,
                                                             noinline where: WhereDsl.()->Unit = {},
): Map<Int, T> = getObjMapPro(table) {
    this.where = where
}



/** 
 * Retrieves a map of objects of the specified [clazz] from the given table, 
 * using the `id` column (Int) as the key. 
 * Similar to the reified version but uses explicit KClass parameter. 
 */
fun <T : DbEntity> SQLiteDatabase.getObjMap(                          table: String,
                                                                      clazz: KClass<T>,
                                                                      where: WhereDsl.()->Unit = {},
): Map<Int, T> = getObjMapPro(table, clazz) {
    this.where = where
}












