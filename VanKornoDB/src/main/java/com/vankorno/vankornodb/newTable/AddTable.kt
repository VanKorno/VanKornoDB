/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.newTable

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.misc.dLog


/** Creates the physical table described by [table].
 */
fun Dbh.createTable(                              table: DslTable<TableInfo<out DbEntity>, *, *, *>,
                                                  async: Boolean = false,
) = write("createTable", async) { db -> db.createTable(table) }


fun SQLiteDatabase.createTable(table: DslTable<TableInfo<out DbEntity>, *, *, *>) = createTable(table.info)


/** Creates each physical table in [tables].
 */
fun Dbh.createTables(                     vararg tables: DslTable<TableInfo<out DbEntity>, *, *, *>,
                                                  async: Boolean = false,
) = write("createTables", async) { db -> db.createTables(*tables) }


fun SQLiteDatabase.createTables(           vararg tables: DslTable<TableInfo<out DbEntity>, *, *, *>
) = tables.forEach { createTable(it) }


/** Creates each physical table in [tables].
 */
fun Dbh.createTables(                      tables: List<DslTable<TableInfo<out DbEntity>, *, *, *>>,
                                            async: Boolean = false,
) = write("createTables", async) { db ->
    db.createTables(tables)
}


fun SQLiteDatabase.createTables() = Unit


@JvmName("createQueryTables")
fun SQLiteDatabase.createTables(            tables: List<DslTable<TableInfo<out DbEntity>, *, *, *>>
) = tables.forEach { createTable(it) }


/** Creates a single table in db.
 */
fun SQLiteDatabase.createTable(tableInfo: TableInfo<out DbEntity>) = createTables(tableInfo)



/** Creates multiple tables in the database given a list of TableInfo (table name + schema bundle).
 */
fun SQLiteDatabase.createTables(                               tables: List<TableInfo<out DbEntity>>
) = createTables(*tables.toTypedArray())





/**
 * Creates multiple tables in the database.
 *
 * For each table:
 * - If an explicit column list is provided by the schema, the table schema
 *   is generated from that list.
 * - Otherwise, the schema is derived from the entity class using reflection.
 *
 * This function acts as the central entry point for table creation and decides
 * whether to use the modern column-based system or the reflection-based fallback.
 *
 * @param tables One or more table descriptors containing table name and schema source.
 */
fun SQLiteDatabase.createTables(                              vararg tables: TableInfo<out DbEntity>
) {
    for (table in tables) {
        val schemaBundle = table.schema
        val columns = schemaBundle.columns
        
        val queryStr =  if (!columns.isNullOrEmpty())
                            newTableQuery(table.name, columns)
                        else
                            newTableQuery(table.name, schemaBundle.clazz)
        execSQL(queryStr)
    }
}


/**
 * Creates tables for all single-table entities, that have EntityMeta.limitedToTable value set.
 * DbHelper/DbManager already runs this fun by default, if you don't disable it by setting the
 * 'createExclusiveTables' param to false.
 */
fun SQLiteDatabase.createExclusiveTables(                  allEntityMeta: Collection<BaseEntityMeta>
) {
    // region LOG
        dLog("createExclusiveTables() runs")
    // endregion
    for (entity in allEntityMeta) {
        val table = entity.limitedToTable
        if (table != null)
            createTables(table using entity.schemaBundle)
    }
}
