package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.EntitySpec
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BaseEntity

/** 
 * Retrieves a map of objects from the given table.
 */
inline fun <reified T : BaseEntity> SQLiteDatabase.getObjects(        table: String,
                                                             noinline where: WhereDsl.()->Unit = {},
): List<T> = getObjectsPro(table) {
    this.where = where
}



/** 
 * Retrieves a map of objects from the given table.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjects(                       table: String,
                                                                      spec: EntitySpec<T>,
                                                                      where: WhereDsl.()->Unit = {},
): List<T> = getObjectsPro(table, spec) {
    this.where = where
}





/** 
 * Retrieves a map of objects of type [T] from the given table.
 */
inline fun <reified T : BaseEntity> SQLiteDatabase.getObjMap(         table: String,
                                                             noinline where: WhereDsl.()->Unit = {},
): Map<Int, T> = getObjMapPro(table) {
    this.where = where
}



/** 
 * Retrieves a map of objects from the given table.
 */
fun <T : BaseEntity> SQLiteDatabase.getObjMap(                        table: String,
                                                                       spec: EntitySpec<T>,
                                                                      where: WhereDsl.()->Unit = {},
): Map<Int, T> = getObjMapPro(table, spec) {
    this.where = where
}












