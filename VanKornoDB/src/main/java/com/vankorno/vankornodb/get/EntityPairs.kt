/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.orderByRowId

/**
 * Retrieves a pair of entities from two tables sharing the specified [id].
 * Returns null if either entity is not found.
 */
fun <A : BaseEntity, B : BaseEntity> SQLiteDatabase.getEntityPair(         tableA: TableInfoBase<A>,
                                                                           tableB: TableInfoBase<B>,
                                                                               id: Long,
): Pair<A, B>? {
    val a = getEntity(tableA) { whereId(id) } ?: return null //\/\/\/\/\/\
    val b = getEntity(tableB) { whereId(id) } ?: return null //\/\/\/\/\/\
    return a to b
}

/**
 * Retrieves a pair of entities from two tables sharing the same ID,
 * where [where] conditions match an entity in [tableA].
 * Returns null if no matching entity is found in [tableA] or if the corresponding entity in [tableB] is missing.
 */
fun <A : BaseEntity, B : BaseEntity> SQLiteDatabase.getEntityPair(
                                                                     tableA: TableInfoBase<A>,
                                                                     tableB: TableInfoBase<B>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
): Pair<A, B>? {
    val a = getEntity(tableA, orderBy, extra, where) ?: return null //\/\/\/\/\/\
    val b = getEntity(tableB) { whereId(a.id) } ?: return null //\/\/\/\/\/\
    return a to b
}

/**
 * Retrieves a list of entity pairs from two tables sharing the same ID.
 * Matches rows in [tableA] according to [where], and uses an efficient IN subquery to retrieve
 * corresponding entities from [tableB].
 * Only pairs where both entities exist are included in the result.
 */
fun <A : BaseEntity, B : BaseEntity> SQLiteDatabase.getEntityPairs(
                                                         tableA: TableInfoBase<A>,
                                                         tableB: TableInfoBase<B>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
): List<Pair<A, B>> {
    val mapA = getEntityMap(tableA, orderBy, extra, where)
    if (mapA.isEmpty()) return emptyList() //\/\/\/\/\/\
    
    val mapB = getEntityMap(tableB) {
        cID.inSubquery(tableA.name, cID, orderBy, extra, where)
    }
    
    return mapA.mapNotNull { (id, a) ->
        val b = mapB[id] ?: return@mapNotNull null
        a to b
    }
}

/**
 * Retrieves a map of entity pairs from two tables sharing the same ID, keyed by row ID.
 * Matches rows in [tableA] according to [where], and uses an efficient IN subquery to retrieve
 * corresponding entities from [tableB].
 * Only pairs where both entities exist are included in the result.
 */
fun <A : BaseEntity, B : BaseEntity> SQLiteDatabase.getEntityPairMap(
                                                         tableA: TableInfoBase<A>,
                                                         tableB: TableInfoBase<B>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
): Map<Long, Pair<A, B>> {
    val mapA = getEntityMap(tableA, orderBy, extra, where)
    if (mapA.isEmpty()) return emptyMap() //\/\/\/\/\/\
    
    val mapB = getEntityMap(tableB) {
        cID.inSubquery(tableA.name, cID, orderBy, extra, where)
    }
    
    return buildMap {
        for ((id, a) in mapA) {
            val b = mapB[id] ?: continue //\/\/
            put(id, a to b)
        }
    }
}

/**
 * Retrieves a triple of entities from three tables sharing the specified [id].
 * Returns null if any entity is not found.
 */
fun <A : BaseEntity, B : BaseEntity, C : BaseEntity> SQLiteDatabase.getEntityTriple(
                                                                           tableA: TableInfoBase<A>,
                                                                           tableB: TableInfoBase<B>,
                                                                           tableC: TableInfoBase<C>,
                                                                               id: Long,
): Triple<A, B, C>? {
    val a = getEntity(tableA) { whereId(id) } ?: return null //\/\/\/\/\/\
    val b = getEntity(tableB) { whereId(id) } ?: return null //\/\/\/\/\/\
    val c = getEntity(tableC) { whereId(id) } ?: return null //\/\/\/\/\/\
    return Triple(a, b, c)
}

/**
 * Retrieves a triple of entities from three tables sharing the same ID,
 * where [where] conditions match an entity in [tableA].
 * Returns null if no matching entity is found in [tableA] or if corresponding entities in [tableB] or [tableC] are missing.
 */
fun <A : BaseEntity, B : BaseEntity, C : BaseEntity> SQLiteDatabase.getEntityTriple(
                                                                     tableA: TableInfoBase<A>,
                                                                     tableB: TableInfoBase<B>,
                                                                     tableC: TableInfoBase<C>,
                                                                    orderBy: OrderDsl.()->Unit = {},
                                                                      extra: ExtraDsl.()->Unit = {},
                                                                      where: WhereDsl.()->Unit,
): Triple<A, B, C>? {
    val a = getEntity(tableA, orderBy, extra, where) ?: return null //\/\/\/\/\/\
    val b = getEntity(tableB) { whereId(a.id) } ?: return null //\/\/\/\/\/\
    val c = getEntity(tableC) { whereId(a.id) } ?: return null //\/\/\/\/\/\
    return Triple(a, b, c)
}

/**
 * Retrieves a list of entity triples from three tables sharing the same ID.
 * Matches rows in [tableA] according to [where], and uses an efficient IN subquery to retrieve
 * corresponding entities from [tableB] and [tableC].
 * Only triples where all three entities exist are included in the result.
 */
fun <A : BaseEntity, B : BaseEntity, C : BaseEntity> SQLiteDatabase.getEntityTriples(
                                                         tableA: TableInfoBase<A>,
                                                         tableB: TableInfoBase<B>,
                                                         tableC: TableInfoBase<C>,
                                                        orderBy: OrderDsl.()->Unit = orderByRowId(),
                                                          extra: ExtraDsl.()->Unit = {},
                                                          where: WhereDsl.()->Unit = {},
): List<Triple<A, B, C>> {
    val mapA = getEntityMap(tableA, orderBy, extra, where)
    if (mapA.isEmpty()) return emptyList() //\/\/\/\/\/\
    
    val mapB = getEntityMap(tableB) {
        cID.inSubquery(tableA.name, cID, orderBy, extra, where)
    }
    val mapC = getEntityMap(tableC) {
        cID.inSubquery(tableA.name, cID, orderBy, extra, where)
    }
    
    return mapA.mapNotNull { (id, a) ->
        val b = mapB[id] ?: return@mapNotNull null
        val c = mapC[id] ?: return@mapNotNull null
        Triple(a, b, c)
    }
}
