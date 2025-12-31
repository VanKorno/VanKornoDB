// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.newTable

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.dbManagement.data.using

/**
 * Creates database tables.
 *
 * For each table:
 * - If an explicit column list is provided via `EntityColumns`, the table schema
 *   is generated from that list.
 * - Otherwise, the schema is derived from the entity class using reflection.
 *
 * This function acts as the central entry point for table creation and decides
 * whether to use the modern column-based system or the reflection-based fallback.
 *
 * @param tables One or more table descriptors containing table name and schema source.
 */

internal fun SQLiteDatabase.createTablesInternal(
                                                    vararg tables: TableInfoNormal<out NormalEntity>
) {
    for (table in tables) {
        val schemaBundle = table.schema
        val hasColList = schemaBundle.columns != null  &&  schemaBundle.columns!!.columns.isNotEmpty()
        
        val queryStr =  if (hasColList)
                            newTableQuery(table.name, schemaBundle.columns!!.columns)
                        else
                            newTableQuery(table.name, schemaBundle.clazz)
        execSQL(queryStr)
    }
}


/**
 * Creates tables for all single-table entities, that have EntityMeta.limitedToTable value set.
 */
internal fun SQLiteDatabase.createExclusiveTablesInternal(
                                                           allEntityMeta: Collection<BaseEntityMeta>
) {
    // region LOG
        Log.d(DbTAG, "createExclusiveTables() runs")
    // endregion
    for (entity in allEntityMeta) {
        val table = entity.limitedToTable
        if (table != null)
            createTablesInternal(table using entity.schemaBundle)
    }
}





