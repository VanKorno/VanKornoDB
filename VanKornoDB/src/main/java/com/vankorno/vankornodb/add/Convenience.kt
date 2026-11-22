/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.add

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DslTable
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.TableInfo
import com.vankorno.vankornodb.get.getNextPositions
import com.vankorno.vankornodb.get.id.getNextIDs
import com.vankorno.vankornodb.misc.data.DbIdAndPosition


/** Inserts [entities] with newly assigned IDs and positions.
 * [positionConditions] selects the rows whose positions are already occupied.
 * [setIdAndPosition] copies each assigned pair into the entity before insertion.
 * Returns the assigned pairs in input order.
 */
fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.addWithNewIDsAndPositions(
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                          entities: List<T>,
                                                positionConditions: W.()->Unit = {},
                                                  setIdAndPosition: (T, Long, Long)->T,
): List<DbIdAndPosition> = readWrite(
    default = emptyList(),
    funName = "addWithNewIDsAndPositions"
) { db ->
    db.addWithNewIDsAndPositions(table, entities, positionConditions, setIdAndPosition)
}

fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.addWithNewIDsAndPositions(
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                          entities: List<T>,
                                                positionConditions: W.()->Unit = {},
                                                  setIdAndPosition: (T, Long, Long)->T,
): List<DbIdAndPosition> {
    val positions = getNextPositions(table, entities.size, where = positionConditions)
    val ids = getNextIDs(table, entities.size)
    
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
fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > Dbh.addWithNewIdAndPosition(
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                            entity: T,
                                                positionConditions: W.()->Unit = {},
                                                  setIdAndPosition: (T, Long, Long)->T,
): DbIdAndPosition = readWrite(
    default = DbIdAndPosition(),
    funName = "addWithNewIdAndPosition"
) { db ->
    db.addWithNewIdAndPosition(table, entity, positionConditions, setIdAndPosition)
}

fun <
    T : DbEntity,
    W : WhereDsl<W>,
    O : OrderDsl<W, O>,
    X : ExtraDsl<W>
    > SQLiteDatabase.addWithNewIdAndPosition(
                                                             table: DslTable<TableInfo<T>, W, O, X>,
                                                            entity: T,
                                                positionConditions: W.()->Unit = {},
                                                  setIdAndPosition: (T, Long, Long)->T,
): DbIdAndPosition = addWithNewIDsAndPositions(
    table = table,
    entities = listOf(entity),
    positionConditions = positionConditions,
    setIdAndPosition = setIdAndPosition,
).single()
