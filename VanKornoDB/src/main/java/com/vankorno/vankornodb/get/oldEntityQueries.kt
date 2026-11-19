/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.*
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.orderByRowId


fun <W> SQLiteDatabase.getInt(                             table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, IntCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): Int = getInt(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getColInts(                         table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, IntCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<Int> = getColInts(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getStr(                             table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, StrCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): String = getStr(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getColStrings(                      table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, StrCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<String> = getColStrings(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getBool(                            table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, BoolCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): Boolean = getBool(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getColBools(                        table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, BoolCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<Boolean> = getColBools(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getLong(                            table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, LongCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): Long = getLong(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getColLongs(                        table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, LongCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<Long> = getColLongs(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getFloat(                           table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, FloatCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): Float = getFloat(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getColFloats(                       table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, FloatCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<Float> = getColFloats(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getBlob(                            table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, BlobCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): ByteArray = getBlob(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getColBlobs(                        table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, BlobCol>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<ByteArray> = getColBlobs(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getId(                                              table: EntityTable<*, W>,
                                                                           where: W.()->Unit,
): Long = getId(table.name, table.where(where))



fun <W> SQLiteDatabase.getIDs(                             table: EntityTable<*, W>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           where: W.()->Unit,
): List<Long> = getIDs(table.name, orderBy, table.where(where))



fun <W> SQLiteDatabase.getRowCount(                        table: EntityTable<*, W>,
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): Int = getRowCount(table.name, extra, table.where(where))



fun <W> SQLiteDatabase.hasRows(                            table: EntityTable<*, W>,
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): Boolean = hasRows(table.name, extra, table.where(where))



fun <T : BaseEntity, W> SQLiteDatabase.getEntity(      tableInfo: EntityTable<TableInfoBase<T>, W>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit,
): T? = getEntity(
    tableInfo.info, orderBy, extra, tableInfo.where(where)
)


fun <T : BaseEntity, W> SQLiteDatabase.getEntity(      tableInfo: EntityTable<TableInfoBase<T>, W>,
                                                         default: T,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit,
): T = getEntity(
    tableInfo.info, default, orderBy, extra, tableInfo.where(where)
)


fun <T : BaseEntity, W> SQLiteDatabase.getEntities(
                                           tableInfo: EntityTable<TableInfoBase<T>, W>,
                                             orderBy: @DslInject OrderDsl.()->Unit = orderByRowId(),
                                               extra: @DslInject ExtraDsl.()->Unit = {},
                                               where: W.()->Unit = {},
): List<T> = getEntities(
    tableInfo.info, orderBy, extra, tableInfo.where(where)
)


fun <T : BaseEntity, W> SQLiteDatabase.getEntityMap(
                                           tableInfo: EntityTable<TableInfoBase<T>, W>,
                                             orderBy: @DslInject OrderDsl.()->Unit = orderByRowId(),
                                               extra: @DslInject ExtraDsl.()->Unit = {},
                                               where: W.()->Unit = {},
): Map<Long, T> = getEntityMap(
    tableInfo.info, orderBy, extra, tableInfo.where(where)
)


fun <T : LiteEntity, W> SQLiteDatabase.getLiteEntity(
                                                       tableInfo: EntityTable<TableInfoLite<T>, W>,
                                                 originalColumns: EntityColumns,
                                                         default: T,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit,
): T = getLiteEntity(
    tableInfo.info, originalColumns, default, orderBy, extra, tableInfo.where(where)
)


fun <T : LiteEntity, W> SQLiteDatabase.getLiteEntities(
                                           tableInfo: EntityTable<TableInfoLite<T>, W>,
                                     originalColumns: EntityColumns,
                                             orderBy: @DslInject OrderDsl.()->Unit = orderByRowId(),
                                               extra: @DslInject ExtraDsl.()->Unit = {},
                                               where: W.()->Unit = {},
): List<T> = getLiteEntities(
    tableInfo.info, originalColumns, orderBy, extra, tableInfo.where(where)
)


fun <W> SQLiteDatabase.getCursor(                          table: EntityTable<*, W>,
                                                  vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                         orderBy: @DslInject OrderDsl.()->Unit = {},
                                                           extra: @DslInject ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): Cursor = if (columns.isEmpty())
    getCursor(table.name, orderBy, extra, table.where(where))
else
    getCursor(table.name, table.select(columns), orderBy, extra, table.where(where))



inline fun <W, R> SQLiteDatabase.useCursor(                table: EntityTable<*, W>,
                                                  vararg columns: ColumnSelector<W, TypedColumn<*>>,
                                                noinline orderBy: @DslInject OrderDsl.()->Unit = {},
                                                  noinline extra: @DslInject ExtraDsl.()->Unit = {},
                                                  noinline where: W.()->Unit = {},
                                                           block: (Cursor)->R,
): R = getCursor(
    table, *columns, orderBy = orderBy, extra = extra, where = where
).use(block)
