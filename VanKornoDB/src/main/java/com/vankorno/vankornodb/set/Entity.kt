/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.api.toContentValues
import com.vankorno.vankornodb.core.dsl.EntityTable
import com.vankorno.vankornodb.dbManagement.data.TableInfoNormal
import com.vankorno.vankornodb.misc.wLog

/** Updates matching rows with [entity] and returns the number updated.
 */
fun <T : CurrEntity, W> Dbh.setEntity(                    table: EntityTable<TableInfoNormal<T>, W>,
                                                         entity: T,
                                                          where: W.()->Unit,
): Int = readWrite(0, "setEntity") { db ->
    db.setEntity(table = table, entity = entity, where = where)
}


fun <T : CurrEntity, W> SQLiteDatabase.setEntity(         table: EntityTable<TableInfoNormal<T>, W>,
                                                         entity: T,
                                                          where: W.()->Unit,
): Int {
    val cv = toContentValues(entity, table.info.schema)
    val predicate = table.where(where)
    val updatedRowCount = update(table.name, cv, predicate.query, predicate.args)
    if (updatedRowCount > 1) {
        // region LOG
        wLog("setEntity: $updatedRowCount rows updated in '${table.name}'. You may want to set more specific conditions if you want to update a single row.")
        // endregion
    }
    return updatedRowCount
}






