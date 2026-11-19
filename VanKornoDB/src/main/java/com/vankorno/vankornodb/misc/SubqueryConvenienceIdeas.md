# Subquery Convenience Function Ideas for DbHelper (`dbh`)

Future ideas for high-level convenience functions at the `Dbh` (`dbh`) / `SQLiteDatabase` level that leverage subqueries under the hood.

---

## 1. Cross-Table Filtering (Sibling / Condition Tables)

For schemas where tables share IDs (e.g., items in Table A, visibility/settings in Table B):

```kotlin
// Fetch entities from targetTable where a condition in another table is met
fun <T : BaseEntity> getEntitiesWhereIn(
    targetTable: TableInfoBase<T>,
    fromTable: TableInfoBase<*>,
    matchingCol: TypedColumn<*> = SharedCol.cID,
    whereIn: WhereDsl.()->Unit = {},
): List<T>

// Fetch entities that do NOT match in another table (e.g. blacklist, exclusion filter)
fun <T : BaseEntity> getEntitiesWhereNotIn(
    targetTable: TableInfoBase<T>,
    fromTable: TableInfoBase<*>,
    matchingCol: TypedColumn<*> = SharedCol.cID,
    whereIn: WhereDsl.()->Unit = {},
): List<T>
```

---

## 2. Cross-Table Pairs / Multi-Entity Joins

Fetching related entities from 2 (or more) tables that share the same ID:

```kotlin
// Fetch single paired entities by ID or condition
fun <A : BaseEntity, B : BaseEntity> getEntityPair(
    tableA: TableInfoBase<A>,
    tableB: TableInfoBase<B>,
    id: Long,
): Pair<A, B>?

// Fetch multiple paired entities matching conditions
fun <A : BaseEntity, B : BaseEntity> getEntityPairs(
    tableA: TableInfoBase<A>,
    tableB: TableInfoBase<B>,
    where: WhereDsl.()->Unit = {},
): List<Pair<A, B>> // or Map<Long, Pair<A, B>>
```

*Implementation strategy:*
Can run two fast queries within a single `read` transaction:
1. Fetch `mapA = getEntityMap(tableA, where)`.
2. Fetch `mapB = getEntityMap(tableB) { Id.inSubquery(tableA, where = where) }`.
3. Pair them by ID in memory: `mapA.mapNotNull { (id, a) -> mapB[id]?.let { b -> a to b } }`.
This avoids column name collisions between tables that share property names (e.g., both having `name`, `int1`), while keeping mapping 100% type-safe.

---

## 3. Cascades & Cross-Table Cleanup (Without Triggers)

Cleanup orphaned rows or bulk-delete based on another table:

```kotlin
// Delete all rows in child/condition table whose parent rows no longer exist
fun deleteWhereNotIn(
    targetTable: String,
    fromTable: String,
    matchingCol: String = "id",
): Int
// SQL: DELETE FROM targetTable WHERE matchingCol NOT IN (SELECT matchingCol FROM fromTable)

// Bulk delete rows matching conditions in another table
fun deleteWhereIn(
    targetTable: String,
    fromTable: String,
    matchingCol: String = "id",
    whereIn: WhereDsl.()->Unit = {},
): Int

// Bulk update rows matching conditions in another table
fun setWhereIn(
    targetTable: TableInfoBase<*>,
    fromTable: TableInfoBase<*>,
    whereIn: WhereDsl.()->Unit = {},
    set: SetBuilder.()->Unit,
): Int
```

---

## 4. History & Cache Retention

Prune old rows while keeping the top N:

```kotlin
// Keep only the latest N rows, delete the rest
fun keepLatestNRows(
    table: String,
    count: Int,
    orderCol: String = "id",
): Int
// SQL: DELETE FROM table WHERE id NOT IN (SELECT id FROM table ORDER BY orderCol DESC LIMIT count)
```

---

## 5. Table Diffing & Integrity Checks

```kotlin
// Find IDs that exist in tableA but are missing in tableB
fun getMissingIDs(
    tableA: String,
    tableB: String,
    idCol: String = "id",
): List<Long>
// SQL: SELECT id FROM tableA WHERE id NOT IN (SELECT id FROM tableB)

// Quick boolean check across tables
fun hasMatchingRows(
    table: String,
    inTable: String,
    where: WhereDsl.()->Unit = {},
): Boolean
// SQL: SELECT EXISTS (SELECT 1 FROM table WHERE id IN (SELECT id FROM inTable WHERE ...))
```

---

## 6. Extrema with Ties

```kotlin
// Get all entities sharing the maximum value (unlike LIMIT 1 which only gets one)
fun <T : BaseEntity> getEntitiesWithMax(
    tableInfo: TableInfoBase<T>,
    column: TypedColumn<*>,
): List<T>
// SQL: SELECT * FROM table WHERE col = (SELECT MAX(col) FROM table)
```
