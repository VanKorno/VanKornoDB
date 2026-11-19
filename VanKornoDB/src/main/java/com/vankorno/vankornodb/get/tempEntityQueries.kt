/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.ColumnSelector
import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.api.EntityTable
import com.vankorno.vankornodb.api.ExtraDsl
import com.vankorno.vankornodb.api.LiteEntity
import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.orderByRowId


fun <W> SQLiteDatabase.getColInts(                         table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, IntCol>,
                                                         orderBy: OrderDsl.()->Unit = {},
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<Int> = getColInts(
    table.name, table.select(column), orderBy, extra, table.where(where)
)





fun <W> SQLiteDatabase.getColStrings(                      table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, StrCol>,
                                                         orderBy: OrderDsl.()->Unit = {},
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<String> = getColStrings(
    table.name, table.select(column), orderBy, extra, table.where(where)
)




fun <W> SQLiteDatabase.getColBools(                        table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, BoolCol>,
                                                         orderBy: OrderDsl.()->Unit = {},
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<Boolean> = getColBools(
    table.name, table.select(column), orderBy, extra, table.where(where)
)




fun <W> SQLiteDatabase.getColLongs(                        table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, LongCol>,
                                                         orderBy: OrderDsl.()->Unit = {},
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<Long> = getColLongs(
    table.name, table.select(column), orderBy, extra, table.where(where)
)



fun <W> SQLiteDatabase.getColFloats(                       table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, FloatCol>,
                                                         orderBy: OrderDsl.()->Unit = {},
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<Float> = getColFloats(
    table.name, table.select(column), orderBy, extra, table.where(where)
)



fun <W> SQLiteDatabase.getColBlobs(                        table: EntityTable<*, W>,
                                                          column: ColumnSelector<W, BlobCol>,
                                                         orderBy: OrderDsl.()->Unit = {},
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): List<ByteArray> = getColBlobs(
    table.name, table.select(column), orderBy, extra, table.where(where)
)


fun <W> SQLiteDatabase.getId(                                              table: EntityTable<*, W>,
                                                                           where: W.()->Unit,
): Long = getId(table.name, table.where(where))



fun <W> SQLiteDatabase.getIDs(                             table: EntityTable<*, W>,
                                                         orderBy: OrderDsl.()->Unit = {},
                                                           where: W.()->Unit,
): List<Long> = getIDs(table.name, orderBy, table.where(where))



fun <W> SQLiteDatabase.getRowCount(                        table: EntityTable<*, W>,
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): Int = getRowCount(table.name, extra, table.where(where))



fun <W> SQLiteDatabase.hasRows(                            table: EntityTable<*, W>,
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit = {},
): Boolean = hasRows(table.name, extra, table.where(where))



fun <T : BaseEntity, W> SQLiteDatabase.getEntity(      tableInfo: EntityTable<TableInfoBase<T>, W>,
                                                         orderBy: OrderDsl.()->Unit = {},
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit,
): T? = getEntity(
    tableInfo.info, orderBy, extra, tableInfo.where(where)
)


fun <T : BaseEntity, W> SQLiteDatabase.getEntity(      tableInfo: EntityTable<TableInfoBase<T>, W>,
                                                         default: T,
                                                         orderBy: OrderDsl.()->Unit = {},
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit,
): T = getEntity(
    tableInfo.info, default, orderBy, extra, tableInfo.where(where)
)


fun <T : BaseEntity, W> SQLiteDatabase.getEntities(
                                           tableInfo: EntityTable<TableInfoBase<T>, W>,
                                             orderBy: OrderDsl.()->Unit = orderByRowId(),
                                               extra: ExtraDsl.()->Unit = {},
                                               where: W.()->Unit = {},
): List<T> = getEntities(
    tableInfo.info, orderBy, extra, tableInfo.where(where)
)


fun <T : BaseEntity, W> SQLiteDatabase.getEntityMap(
                                           tableInfo: EntityTable<TableInfoBase<T>, W>,
                                             orderBy: OrderDsl.()->Unit = orderByRowId(),
                                               extra: ExtraDsl.()->Unit = {},
                                               where: W.()->Unit = {},
): Map<Long, T> = getEntityMap(
    tableInfo.info, orderBy, extra, tableInfo.where(where)
)


fun <T : LiteEntity, W> SQLiteDatabase.getLiteEntity(
                                                       tableInfo: EntityTable<TableInfoLite<T>, W>,
                                                 originalColumns: EntityColumns,
                                                         default: T,
                                                         orderBy: OrderDsl.()->Unit = {},
                                                           extra: ExtraDsl.()->Unit = {},
                                                           where: W.()->Unit,
): T = getLiteEntity(
    tableInfo.info, originalColumns, default, orderBy, extra, tableInfo.where(where)
)


fun <T : LiteEntity, W> SQLiteDatabase.getLiteEntities(
                                           tableInfo: EntityTable<TableInfoLite<T>, W>,
                                     originalColumns: EntityColumns,
                                             orderBy: OrderDsl.()->Unit = orderByRowId(),
                                               extra: ExtraDsl.()->Unit = {},
                                               where: W.()->Unit = {},
): List<T> = getLiteEntities(
    tableInfo.info, originalColumns, orderBy, extra, tableInfo.where(where)
)

