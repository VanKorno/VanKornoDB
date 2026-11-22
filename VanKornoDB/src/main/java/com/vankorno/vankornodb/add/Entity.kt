/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.add

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.api.toContentValues
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.MiniDslTable
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.get.id.getNextIDs
import com.vankorno.vankornodb.get.id.getNextId
import com.vankorno.vankornodb.misc.wLog


/** Inserts [entity], assigning a new ID when its ID is less than 1.
 * Returns the inserted row ID, or -1 on failure.
 */
fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.addEntity(                                         table: DslTable<TableInfo<T>, W, O, X>,
                                                            entity: T,
): Long = readWrite(-1L, "addEntity") { db ->
    db.addEntity(table, entity)
}

fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.addEntity(                              table: DslTable<TableInfo<T>, W, O, X>,
                                                            entity: T,
): Long {
    val modified = if (entity.id < 1L)
        table.info.schema.withId(entity, getNextId(table))
    else
        entity
    
    val rowId = addEntityWithoutIdCheck(table, modified)
    if (rowId == -1L) {
        // region LOG
            wLog("addEntity() FAILED to insert entity: $modified")
        // endregion
    }
    return rowId
}



/** Inserts [entity] using its existing ID. Returns the inserted row ID, or -1 on failure.
 */
fun <T : DbEntity, W> Dbh.addEntityWithoutIdCheck(             table: MiniDslTable<TableInfo<T>, W>,
                                                              entity: T,
): Long = readWrite(-1L, "addEntityWithoutIdCheck") { db ->
    db.addEntityWithoutIdCheck(table, entity)
}

fun <T : DbEntity, W> SQLiteDatabase.addEntityWithoutIdCheck(
                                                               table: MiniDslTable<TableInfo<T>, W>,
                                                              entity: T,
): Long {
    val cv = toContentValues(entity, table.info.schema)
    if (cv.size() == 0) return -1L //\/\/\/\/\/\
    
    return insert(table.name, null, cv)
}



/** Inserts [entities] using their existing IDs.
 * Returns the row IDs in input order, with -1 for each failed insert.
 */
fun <T : DbEntity, W> Dbh.addEntitiesWithoutIdCheck(           table: MiniDslTable<TableInfo<T>, W>,
                                                            entities: List<T>,
): List<Long> = readWrite(emptyList(), "addEntitiesWithoutIdCheck") { db ->
    db.addEntitiesWithoutIdCheck(table, entities)
}

fun <T : DbEntity, W> SQLiteDatabase.addEntitiesWithoutIdCheck(
                                                               table: MiniDslTable<TableInfo<T>, W>,
                                                            entities: List<T>,
): List<Long> = entities.map { addEntityWithoutIdCheck(table, it) }



/** Inserts [entities], assigning new IDs to entries whose IDs are less than 1.
 * Explicit positive IDs in the batch are reserved before allocating any new IDs.
 * Returns the row IDs in input order, with -1 for each failed insert.
 */
fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.addEntities(                                       table: DslTable<TableInfo<T>, W, O, X>,
                                                          entities: List<T>,
): List<Long> = readWrite(emptyList(), "addEntities") { db ->
    db.addEntities(table, entities)
}

fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.addEntities(                            table: DslTable<TableInfo<T>, W, O, X>,
                                                          entities: List<T>,
): List<Long> {
    val newIDs = getNextIDs(
        table = table,
        count = entities.count { it.id < 1L },
        reservedIDs = entities.filter { it.id > 0L }.map { it.id }.toSet(),
    ).iterator()
    
    return entities.map { entity ->
        val modified = if (entity.id < 1L)
            table.info.schema.withId(entity, newIDs.next())
        else
            entity
        
        val rowId = addEntityWithoutIdCheck(table, modified)
        if (rowId == -1L) {
            // region LOG
                wLog("addEntities() FAILED to insert entity: $modified")
            // endregion
        }
        rowId
    }
}



/** Vararg overload of [addEntities]. Returns row IDs in input order.
 */
fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.addEntities(                                       table: DslTable<TableInfo<T>, W, O, X>,
                                                   vararg entities: T,
): List<Long> = addEntities(table, entities.toList())

fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.addEntities(                            table: DslTable<TableInfo<T>, W, O, X>,
                                                   vararg entities: T,
): List<Long> = addEntities(table, entities.toList())
