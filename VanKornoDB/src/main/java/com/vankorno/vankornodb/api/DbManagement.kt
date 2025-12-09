package com.vankorno.vankornodb.api

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.dbManagement.DbHelperInternal
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta

open class DbHelper(             context: Context,
                                  dbName: String,
                               dbVersion: Int,
                              entityMeta: Collection<BaseEntityMeta>,
                                onCreate: (SQLiteDatabase)->Unit = {},
                               onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
) : DbHelperInternal(context, dbName, dbVersion, entityMeta, onCreate, onUpgrade)
















