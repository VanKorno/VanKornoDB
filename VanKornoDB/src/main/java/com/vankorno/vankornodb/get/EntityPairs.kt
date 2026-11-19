/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase

/**
 * Retrieves a pair of entities from two tables sharing the specified [id].
 * Returns null if either entity is not found.
 */
fun <A : BaseEntity, B : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>> Dbh.getEntityPair(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                      id: Long,
): Pair<A, B>? = read(null, "getEntityPair") { db ->
    db.getEntityPair(tableA, tableB, id)
}

fun <A : BaseEntity, B : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>> SQLiteDatabase.getEntityPair(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                      id: Long,
): Pair<A, B>? {
    val a = getEntity(tableA) { rawClause("id=?", id.toString()) } ?: return null //\/\/\/\/\/\
    val b = getEntity(tableB) { rawClause("id=?", id.toString()) } ?: return null //\/\/\/\/\/\
    return a to b
}

/**
 * Retrieves a pair of entities from two tables sharing the same ID,
 * where [where] conditions match an entity in [tableA].
 * Returns null if no matching entity is found in [tableA] or if the corresponding entity in [tableB] is missing.
 */
fun <A : BaseEntity, B : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>> Dbh.getEntityPair(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                 orderBy: OA.()->Unit = {},
                                                   extra: XA.()->Unit = {},
                                                   where: WA.()->Unit,
): Pair<A, B>? = read(null, "getEntityPair") { db ->
    db.getEntityPair(tableA, tableB, orderBy, extra, where)
}

fun <A : BaseEntity, B : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>> SQLiteDatabase.getEntityPair(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                 orderBy: OA.()->Unit = {},
                                                   extra: XA.()->Unit = {},
                                                   where: WA.()->Unit,
): Pair<A, B>? {
    val a = getEntity(tableA, orderBy, extra, where) ?: return null //\/\/\/\/\/\
    val b = getEntity(tableB) { rawClause("id=?", a.id.toString()) } ?: return null //\/\/\/\/\/\
    return a to b
}

/**
 * Retrieves a list of entity pairs from two tables sharing the same ID.
 * Matches rows in [tableA] according to [where], and uses an efficient IN subquery to retrieve
 * corresponding entities from [tableB].
 * Only pairs where both entities exist are included in the result.
 */
fun <A : BaseEntity, B : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>> Dbh.getEntityPairs(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                 orderBy: OA.()->Unit = { raw("ROWID") },
                                                   extra: XA.()->Unit = {},
                                                   where: WA.()->Unit = {},
): List<Pair<A, B>> = read(emptyList(), "getEntityPairs") { db ->
    db.getEntityPairs(tableA, tableB, orderBy, extra, where)
}

fun <A : BaseEntity, B : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>> SQLiteDatabase.getEntityPairs(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                 orderBy: OA.()->Unit = { raw("ROWID") },
                                                   extra: XA.()->Unit = {},
                                                   where: WA.()->Unit = {},
): List<Pair<A, B>> {
    val mapA = getEntityMap(tableA, orderBy, extra, where)
    if (mapA.isEmpty()) return emptyList() //\/\/\/\/\/\
    
    val mapB = entitiesByIds(tableB, mapA.keys)
    
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
fun <A : BaseEntity, B : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>> Dbh.getEntityPairMap(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                 orderBy: OA.()->Unit = { raw("ROWID") },
                                                   extra: XA.()->Unit = {},
                                                   where: WA.()->Unit = {},
): Map<Long, Pair<A, B>> = read(emptyMap(), "getEntityPairMap") { db ->
    db.getEntityPairMap(tableA, tableB, orderBy, extra, where)
}

fun <A : BaseEntity, B : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>> SQLiteDatabase.getEntityPairMap(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                 orderBy: OA.()->Unit = { raw("ROWID") },
                                                   extra: XA.()->Unit = {},
                                                   where: WA.()->Unit = {},
): Map<Long, Pair<A, B>> {
    val mapA = getEntityMap(tableA, orderBy, extra, where)
    if (mapA.isEmpty()) return emptyMap() //\/\/\/\/\/\
    
    val mapB = entitiesByIds(tableB, mapA.keys)
    
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
fun <A : BaseEntity, B : BaseEntity, C : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>, WC : WhereDsl<WC>, OC : OrderDsl<WC, OC>, XC : ExtraDsl<WC>> Dbh.getEntityTriple(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                  tableC: QueryTable<TableInfoBase<C>, WC, OC, XC>,
                                                      id: Long,
): Triple<A, B, C>? = read(null, "getEntityTriple") { db ->
    db.getEntityTriple(tableA, tableB, tableC, id)
}

fun <A : BaseEntity, B : BaseEntity, C : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>, WC : WhereDsl<WC>, OC : OrderDsl<WC, OC>, XC : ExtraDsl<WC>> SQLiteDatabase.getEntityTriple(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                  tableC: QueryTable<TableInfoBase<C>, WC, OC, XC>,
                                                      id: Long,
): Triple<A, B, C>? {
    val a = getEntity(tableA) { rawClause("id=?", id.toString()) } ?: return null //\/\/\/\/\/\
    val b = getEntity(tableB) { rawClause("id=?", id.toString()) } ?: return null //\/\/\/\/\/\
    val c = getEntity(tableC) { rawClause("id=?", id.toString()) } ?: return null //\/\/\/\/\/\
    return Triple(a, b, c)
}

/**
 * Retrieves a triple of entities from three tables sharing the same ID,
 * where [where] conditions match an entity in [tableA].
 * Returns null if no matching entity is found in [tableA] or if corresponding entities in [tableB] or [tableC] are missing.
 */
fun <A : BaseEntity, B : BaseEntity, C : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>, WC : WhereDsl<WC>, OC : OrderDsl<WC, OC>, XC : ExtraDsl<WC>> Dbh.getEntityTriple(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                  tableC: QueryTable<TableInfoBase<C>, WC, OC, XC>,
                                                 orderBy: OA.()->Unit = {},
                                                   extra: XA.()->Unit = {},
                                                   where: WA.()->Unit,
): Triple<A, B, C>? = read(null, "getEntityTriple") { db ->
    db.getEntityTriple(tableA, tableB, tableC, orderBy, extra, where)
}

fun <A : BaseEntity, B : BaseEntity, C : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>, WC : WhereDsl<WC>, OC : OrderDsl<WC, OC>, XC : ExtraDsl<WC>> SQLiteDatabase.getEntityTriple(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                  tableC: QueryTable<TableInfoBase<C>, WC, OC, XC>,
                                                 orderBy: OA.()->Unit = {},
                                                   extra: XA.()->Unit = {},
                                                   where: WA.()->Unit,
): Triple<A, B, C>? {
    val a = getEntity(tableA, orderBy, extra, where) ?: return null //\/\/\/\/\/\
    val b = getEntity(tableB) { rawClause("id=?", a.id.toString()) } ?: return null //\/\/\/\/\/\
    val c = getEntity(tableC) { rawClause("id=?", a.id.toString()) } ?: return null //\/\/\/\/\/\
    return Triple(a, b, c)
}

/**
 * Retrieves a list of entity triples from three tables sharing the same ID.
 * Matches rows in [tableA] according to [where], and uses an efficient IN subquery to retrieve
 * corresponding entities from [tableB] and [tableC].
 * Only triples where all three entities exist are included in the result.
 */
fun <A : BaseEntity, B : BaseEntity, C : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>, WC : WhereDsl<WC>, OC : OrderDsl<WC, OC>, XC : ExtraDsl<WC>> Dbh.getEntityTriples(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                  tableC: QueryTable<TableInfoBase<C>, WC, OC, XC>,
                                                 orderBy: OA.()->Unit = { raw("ROWID") },
                                                   extra: XA.()->Unit = {},
                                                   where: WA.()->Unit = {},
): List<Triple<A, B, C>> = read(emptyList(), "getEntityTriples") { db ->
    db.getEntityTriples(tableA, tableB, tableC, orderBy, extra, where)
}

fun <A : BaseEntity, B : BaseEntity, C : BaseEntity, WA : WhereDsl<WA>, OA : OrderDsl<WA, OA>, XA : ExtraDsl<WA>, WB : WhereDsl<WB>, OB : OrderDsl<WB, OB>, XB : ExtraDsl<WB>, WC : WhereDsl<WC>, OC : OrderDsl<WC, OC>, XC : ExtraDsl<WC>> SQLiteDatabase.getEntityTriples(
                                                  tableA: QueryTable<TableInfoBase<A>, WA, OA, XA>,
                                                  tableB: QueryTable<TableInfoBase<B>, WB, OB, XB>,
                                                  tableC: QueryTable<TableInfoBase<C>, WC, OC, XC>,
                                                 orderBy: OA.()->Unit = { raw("ROWID") },
                                                   extra: XA.()->Unit = {},
                                                   where: WA.()->Unit = {},
): List<Triple<A, B, C>> {
    val mapA = getEntityMap(tableA, orderBy, extra, where)
    if (mapA.isEmpty()) return emptyList() //\/\/\/\/\/\
    
    val mapB = entitiesByIds(tableB, mapA.keys)
    val mapC = entitiesByIds(tableC, mapA.keys)
    
    return mapA.mapNotNull { (id, a) ->
        val b = mapB[id] ?: return@mapNotNull null
        val c = mapC[id] ?: return@mapNotNull null
        Triple(a, b, c)
    }
}

private fun <T : BaseEntity, W : WhereDsl<W>, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.entitiesByIds(
                                                    table: QueryTable<TableInfoBase<T>, W, O, X>,
                                                      ids: Set<Long>,
): Map<Long, T> = buildMap {
    for (batch in ids.chunked(900)) {
        putAll(getEntityMap(table) { idColumn<W>()(this) equalAny batch })
    }
}
