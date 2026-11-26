# VanKornoDB

Use the app's existing VanKornoDB setup. Check the dependency sources and nearby app call sites for the version in use. If sources are missing and a signature is unclear, ask the developer to load them.

## Database access and tables

- Most operations are Kotlin extension pairs on `Dbh` and `SQLiteDatabase`. Use the app's initialized `dbh` (often `api.DbRuntime.dbh`) from ordinary app code. Inside a database callback or transaction, use the supplied `db` for the same operations. Import extensions from `com.vankorno.vankornodb` subpackages such as `get`, `get.id`, `set`, `set.byId`, `add`, `delete`, and `newTable`.
- Prefer the high-level calls. Use `dbh.read(default) { db -> ... }` for custom reads, `dbh.write { db -> ... }` for grouped writes, and `dbh.readWrite(default) { db -> ... }` for a write transaction that returns a result. These manage database access and locking, and writes run in transactions. Keep dependent reads and writes on the same `db` in one write transaction. Suspend variants are named `readSusp`, `writeSusp`, and `readWriteSusp`. `async = true` on supported `Dbh` calls is fire-and-forget.
- The IDE generator typically provides `_Task`, a `SchemaBundle<Task>` with column definitions and row mapping, plus the entity-specific DSL receivers and a table alias such as `TaskT` for its `FullDslTable`. Reuse existing table vals. For dynamic names use `_Task.tableNamed(tableName)`. This constructs a typed handle. Use `createTable` if the physical table needs to be created. One entity can back multiple tables, and compatible entities can read different projections of the same table.
- Pass the typed handle to DSL calls and use `table.name` when a string table name is expected. DSL fields are scoped to their entity. `Id` is a built-in `Long` field in all DSLs. SQLite's `RowId` is available in selection, where, order and extra blocks. Use the original full table handle for `set` or `setById`, since table-level read modifiers return a `DslTable`.

## Select and read

```kotlin
val priority = dbh.getInt(Tasks.col { Priority }) { Id equal taskId }
val name = dbh.getStrById(taskId, Tasks.col { Name })
val ids = dbh.getColLongs(Tasks.col { Id }.orderBy { Position }.limit(10)) {
    Enabled equal true
}
val tasks = dbh.getEntities(Tasks.orderBy { Position }) { Enabled equal true }
```

Use `Table.col { Field }` for one column and `Table.columns { A then B then C }` or `Table.columns { listOf(A, B, C) }` for a nonempty projection. A reusable single-column block has type `ColumnSelector<TaskDsl, IntCol>`, where `TaskDsl` is the generated receiver and `IntCol` is the column type. For several columns the block type is `TaskDsl.() -> List<SelectExpr<TaskDsl, IntCol>>` for an all-Int projection. Use `core.dsl.raw("...")` as a selection expression for SQL expressions such as `COUNT(*)`.

Most reads take a table or selection followed by optional `orderBy`, `extra`, and a trailing `where`. Use named arguments when skipping blocks. An empty predicate matches all rows.

| Need | Call pattern | Result |
| --- | --- | --- |
| One cell | `getInt(Table.col { Priority }) { ... }` | First matching value |
| One column | `getColInts(Table.col { Priority }) { ... }` | One value per matching row |
| One row | `getRowInts(Table.columns { Priority then Score }) { ... }` | First matching row in projection order |
| Several rows and columns | `getInts(Table.columns { Priority then Score }) { ... }` | `List<List<Int>>`, outer list indexed by row |
| One cell or one row by ID | `getIntById(id, column)`, `getRowIntsById(id, columns)` | Same shapes as the corresponding filtered reads |

For single values, the other names are `getStr`, `getBool`, `getLong`, `getFloat`, and `getBlob` (`ByteArray`), with the same `ById` suffix. The plural families use `Strings`, `Bools`, `Longs`, `Floats`, and `Blobs` in place of `Ints`. Typed plural projections use one value type throughout. For mixed types, use a cursor or an entity. Missing scalar rows return `-1`, `""`, `false`, `-1L`, `-1F`, or an empty byte array, respectively. Collection reads return empty lists when no rows match.

- `getEntity(table) { ... }` and `getEntityById(id, table)` return an entity or null. Both offer an overload with a `default` entity. Use `getEntities(table) { ... }` for a list or `getEntityMap(table) { ... }` for a map keyed by ID. `getFirstEntity` and `getLastEntity` use SQLite row order. `getRandomEntity` picks a random match. `getEntityPair` / `getEntityPairs` and triple variants read related entities by a shared ID across tables. `steal(ownerTable, thiefSchema, default) { ... }` and `stealALot(ownerTable, thiefSchema) { ... }` read a compatible smaller schema from another table.
- `getId(table) { ... }`, `getIDs(table) { ... }`, `getAllIDs(table)`, `hasId(id, table)`, `hasRows(table) { ... }`, and `getRowCount(table) { ... }` cover lookups and counts. `getLastId(table)` reads the maximum ID. Use `getNextId` / `getNextIDs` or `getNextInt` / `getNextInts` / `getNextLong` / `getNextLongs` for candidate numbers. Pass an unbounded table or column to these helpers. A candidate is available at read time and is never reserved by the read. Position helpers include `getLastPosition`, `getNextPosition`, and `getNextPositions`. See also `getLargestInt` / `getLargestLong` and `getRandomInt` / `getRandomString` / `getRandomLong` / `getRandomFloat` / `getRandomId`.
- Use `dbh.useCursor(tableOrColumns, where = { ... }) { cursor -> ... }` for scoped access and `dbh.getFromCursor(tableOrColumns, default = ...) { cursor -> ... }` to return a value. On `db`, `useCursor` returns the callback result, and `getCursor` returns a cursor you must close with `.use { ... }`. Move the cursor before reading, and consume it inside the callback.

## WhereDsl

Write predicates with DSL operations. Successive predicates are joined by AND. Use `and { ... }` and `or { ... }` to attach parenthesized groups, and use Kotlin `if` to emit predicates conditionally. To express `A AND (B OR C)`, write `A` then `and { B; or { C } }`, using actual predicate expressions for A, B, and C. For reusable filters use a receiver lambda such as `val enabled: TaskDsl.() -> Unit = { Enabled equal true }`, then pass `where = enabled` or call `and(enabled)`.

- Use `equal`, `notEqual`, and numeric `greater`, `greaterEqual`, `less`, `lessEqual`. For comparisons to another field in the same scope, use the `Col` suffix, for example `Score greaterCol Priority`.
- `Id equalAny ids` and `Id notEqualAny ids` take a list or array and emit IN or NOT IN. Use `Id.equalAnyOf(1L, 2L)` and `notEqualAnyOf` for varargs. String helpers include `like`, `notLike`, `likeCol`, `notLikeCol`, `likeAny`, `notLikeAny`, and their multi-column forms `likeAnyCol` / `notLikeAnyCol`. Include SQL wildcards in the pattern where needed.
- Use `rawClause("name = ?", name)` for predicates outside the typed DSL. Bind values via `?` arguments.

```kotlin
val visible = dbh.getEntities(Tasks) {
    Id.inSubquery(Availability.col { TaskId }) { Enabled equal true }
    notExists(matchColumn = Hiders.col { TargetId }, equalTo = Id) {
        TargetTable equal Tasks.name
    }
    subquery(Id, Attempts.col { TaskId }, selectColumn = { raw("COUNT(*)") }) greater 2
}
```

`inSubquery` / `notInSubquery` accept a single selected column or a built `Subquery`. `exists` / `notExists` accept a built subquery, a table plus an inner filter, or a correlated `matchColumn` (inner selection) and `equalTo` (outer field). For a correlated scalar expression, `subquery(outerColumn, innerColumn, selectColumn = { ... })` takes the outer field **first** and the projection is in the inner scope. A standalone `core.dsl.subquery(columns, orderBy, extra, where)` builds an uncorrelated read.

## OrderDsl and ExtraDsl

Chain `.orderBy { Position }`, `.orderFlip { Position }`, `.orderIf(flag) { Position }`, `.orderById()`, `.orderByIdFlipped()`, or `.limit(n)` on a table or projection. `orderIf(false)` leaves the existing order in place. A selection order takes precedence over its table's order, and each chained ordering call replaces the earlier order on that receiver.

```kotlin
val names = dbh.getColStrings(
    Tasks.col { Name }.limit(20),
    orderBy = { Position.flipIf(descending); Id() },
    extra = { offset = pageOffset },
)
```

In an explicit `orderBy` block, use `Position()` or `Position.by()` for ascending, `Position.flip()` for descending, and `Position.flipIf(flag)` for descending if true and ascending otherwise. A bare `Position` emits no order term. Use `col(Position, Id)` or `flipAll(Position, Id)` for several fields, `random()` for random order, or `raw("...")` for a SQL order expression. `and { ... }` appends a reusable order block. `When(orderWhen({ Position() }) { Enabled equal true }, Else = { Id() })` builds a CASE order expression. Use one scalar order expression per CASE result. An explicit block that emits terms overrides the chained default. An empty block falls back to the chained order if present.

- `extra = { limit = 10; offset = 20 }` controls pagination. `extra = limit(10)` uses the standalone `misc.limit` helper. An explicit extra limit overrides a chained limit. Offset alone preserves the chained limit. Supply a limit when using an offset because SQLite requires one. Single-cell and first-row getters impose their own one-row limit.
- Use `extra = { groupBy(Status); having { Score greater 0 } }` for grouping and HAVING, or `groupByRaw` / `havingRaw` for SQL expressions. `customEnd(sql)` appends a SQL suffix.
- Use `extra = { joins { inner(Owners, { OwnerId }, { Id }) } }`. `inner` and `left` take the joined table, base-table selector, and joined-table selector. They also accept a SQL ON clause with bindings. `cross(Owners)` adds a cross join. Qualify ambiguous column names using explicit SQL expressions where needed.

## Write and delete

```kotlin
dbh.addEntity(Tasks, task)
dbh.setInt(3, Tasks.col { Priority }) { Name equal someName }
dbh.setIntById(taskId, 3, Tasks.col { Priority })
dbh.set(Tasks, where = { SecId equal secId }) {
    Enabled setTo true
    Score add 5
}
dbh.setById(taskId, Tasks) { Enabled.flip() }
dbh.setEntityById(table = Tasks, entity = task)
dbh.deleteRowById(taskId, Tasks)
```

- `addEntity(table, entity)` and `addEntities(table, entities)` (list or vararg) allocate IDs for entities with `id < 1L` and preserve positive IDs. Returns are SQLite insert row IDs in input order for bulk calls, with `-1L` for a failed insert. `addEntityWithoutIdCheck` / `addEntitiesWithoutIdCheck` use existing IDs directly. `addWithNewIdAndPosition` / `addWithNewIDsAndPositions` take a callback to copy the allocated ID and position into the entity.
- `setInt(value, column) { ... }` also has `setStr`, `setBool`, `setLong`, `setFloat`, and `setBlob` equivalents. Their `ById` variants take `(id, value, column)`. Use `addToInt`, `addToLong`, `addToFloat`, or `flipBool` for direct mutations. `setEntity(table, entity) { ... }` writes the entity's mapped values and returns the row count. `setEntityById(id, table, entity)` defaults the lookup ID to `entity.id` when omitted with named arguments.
- In `SetDsl`, use `Field setTo value`, Boolean `Field.flip()` / `ON(Field)` / `OFF(Field)`, numeric `add`, `sub`, `mult`, `div`, `capAt`, `floorAt`, `coerceIn`, and Int `abs()`. Use `Score setAs Priority` for supported same-type fields (Int, Long, Float, Boolean), or `Score setAs (Priority andAdd 3)` for numeric expressions. Other expression helpers are `andSub`, `andMult`, `andDiv`, `andCapAt`, `andFloorAt`, and `andCoerceIn`. Use `setContentValues(cv)` for existing `ContentValues`. Actions run in declaration order.
- An empty write predicate can affect every row. Use `deleteRows(table) { ... }` for filtered deletes and `deleteRowsFromTables` when applying the same predicate to several tables with the same DSL. `deleteFirstRow` / `deleteLastRow` use SQLite ROWID. `clearTable` / `clearTables` removes rows and keeps the schema. `deleteTable` / `deleteTables` drops tables. Use `createTable`, `createTables`, `tableExists`, and `isTableEmpty` for table management.

## Schema changes

Follow the app's entity metadata and upgrade path when changing a persisted schema. `defineMigrations` records historical versions using archived `DbEntity` data classes directly (`version(n, OldTask::class)`), with rename and transform rules as needed. Historical classes need no generated schema or DSL.
