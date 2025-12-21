// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.api

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.dbManagement.DbHelperInternal
import com.vankorno.vankornodb.dbManagement.createTablesInternal
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import kotlin.reflect.KClass


open class DbHelper(             context: Context,
                                  dbName: String,
                               dbVersion: Int,
                              entityMeta: Collection<BaseEntityMeta>,
                                onCreate: (SQLiteDatabase)->Unit = {},
                               onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
) : DbHelperInternal(context, dbName, dbVersion, entityMeta, onCreate, onUpgrade)






/**
 * Creates a single table in db.
 */
fun SQLiteDatabase.createTable(table: String, clazz: KClass<out BaseEntity>) = createTablesInternal(
    TableInfo(table, clazz)
)

/**
 * Creates multiple tables in the database given vararg TableInfo.
 */
fun SQLiteDatabase.createTables(vararg tables: TableInfo) = createTablesInternal(*tables)






