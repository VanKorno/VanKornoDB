package com.vankorno.vankornodb.api

import android.content.ContentValues
import android.database.Cursor
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import kotlin.reflect.KClass

/**
 * Marker interface for all VanKornoDB entities.
 * 
 * Entities must be data classes and implement this interface
 * to be mappable by VanKornoDB.
 */
interface DbEntity : BaseEntity


interface OldEntity : BaseEntity


/**
 * For classes that are used to get data from db, but do not create or migrate any actual db tables.
 */
interface LiteEntity : BaseEntity




interface EntitySpec<T : BaseEntity> {
    val clazz: KClass<out T>
    val columns: EntityColumns
    val getter: ((Cursor) -> T)?
    val setter: ((T, ContentValues) -> Unit)?
    val createTableSql: String?
}








