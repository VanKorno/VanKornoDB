/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.add

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.get.getNextIDs
import com.vankorno.vankornodb.get.getNextLongs
import com.vankorno.vankornodb.misc.data.DbIdAndPosition


/** Inserts [entities] with newly assigned IDs and positions.
 * [positionConditions] selects the rows whose positions are already occupied.
 * [setIdAndPosition] copies each assigned pair into the entity before insertion.
 * Returns the assigned pairs in input order.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.addWithNewIDsAndPositions(
                                                       table: QueryTable<TableInfo<T>, W, O, X>,
                                                    entities: List<T>,
                                                    position: ColumnSelector<W, LongCol>,
                                          positionConditions: W.()->Unit = {},
                                            setIdAndPosition: (T, Long, Long)->T,
): List<DbIdAndPosition> = readWrite(emptyList(), "addWithNewIDsAndPositions") { db ->
    db.addWithNewIDsAndPositions(table, entities, position, positionConditions, setIdAndPosition)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.addWithNewIDsAndPositions(
                                                       table: QueryTable<TableInfo<T>, W, O, X>,
                                                    entities: List<T>,
                                                    position: ColumnSelector<W, LongCol>,
                                          positionConditions: W.()->Unit = {},
                                            setIdAndPosition: (T, Long, Long)->T,
): List<DbIdAndPosition> {
    val ids = getNextIDs(table, entities.size)
    val positions = getNextLongs(table, position, entities.size, where = positionConditions)
    
    return entities.mapIndexed { idx, entity ->
        val id = ids[idx]
        val pos = positions[idx]
        addEntityWithoutIdCheck(table, setIdAndPosition(entity, id, pos))
        DbIdAndPosition(id, pos)
    }
}



/** Inserts [entity] with a newly assigned ID and position.
 * [positionConditions] selects the rows whose positions are already occupied.
 * Returns the assigned pair.
 */
fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.addWithNewIdAndPosition(
                                                           table: QueryTable<TableInfo<T>, W, O, X>,
                                                          entity: T,
                                                        position: ColumnSelector<W, LongCol>,
                                              positionConditions: W.()->Unit = {},
                                                setIdAndPosition: (T, Long, Long)->T,
): DbIdAndPosition = readWrite(DbIdAndPosition(), "addWithNewIdAndPosition") { db ->
    db.addWithNewIdAndPosition(table, entity, position, positionConditions, setIdAndPosition)
}

fun <T : DbEntity, W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.addWithNewIdAndPosition(
                                                           table: QueryTable<TableInfo<T>, W, O, X>,
                                                          entity: T,
                                                        position: ColumnSelector<W, LongCol>,
                                              positionConditions: W.()->Unit = {},
                                                setIdAndPosition: (T, Long, Long)->T,
): DbIdAndPosition = addWithNewIDsAndPositions(
    table = table,
    entities = listOf(entity),
    position = position,
    positionConditions = positionConditions,
    setIdAndPosition = setIdAndPosition,
).single()
