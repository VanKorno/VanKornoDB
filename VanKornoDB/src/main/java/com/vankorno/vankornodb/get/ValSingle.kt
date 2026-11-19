/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.ColumnSelector
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.QueryTable
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.getBoolean
import com.vankorno.vankornodb.misc.suppressValGetterErrorLog


fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getInt(           table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): Int = read(-1, "getInt") { db ->
    db.getInt(table, column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getInt(
                                                                   table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, IntCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): Int = getValue(table, column, -1, "getInt", orderBy, extra, where) {
    it.getInt(0)
}




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getStr(           table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, StrCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): String = read("", "getStr") { db ->
    db.getStr(table, column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getStr(
                                                                   table: QueryTable<*, W, O, X>,
                                                                  column: ColumnSelector<W, StrCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): String = getValue(table, column, "", "getStr", orderBy, extra, where) {
    it.getString(0)
}




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getBool(         table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): Boolean = read(false, "getBool") { db ->
    db.getBool(table, column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getBool(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, BoolCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): Boolean = getValue(table, column, false, "getBool", orderBy, extra, where) {
    it.getBoolean(0)
}




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLong(         table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): Long = read(-1L, "getLong") { db ->
    db.getLong(table, column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLong(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, LongCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): Long = getValue(table, column, -1L, "getLong", orderBy, extra, where) {
    it.getLong(0)
}




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getFloat(       table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): Float = read(-1F, "getFloat") { db ->
    db.getFloat(table, column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getFloat(
                                                                 table: QueryTable<*, W, O, X>,
                                                                column: ColumnSelector<W, FloatCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): Float = getValue(table, column, -1F, "getFloat", orderBy, extra, where) {
    it.getFloat(0)
}





fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getBlob(         table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, BlobCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): ByteArray = read(ByteArray(0), "getBlob") { db ->
    db.getBlob(table, column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getBlob(
                                                                  table: QueryTable<*, W, O, X>,
                                                                 column: ColumnSelector<W, BlobCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): ByteArray = getValue(table, column, ByteArray(0), "getBlob", orderBy, extra, where) {
    it.getBlob(0)
}








fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> SQLiteDatabase.getValue(
                                                           table: QueryTable<*, W, O, X>,
                                                          column: ColumnSelector<W, TypedColumn<R>>,
                                                         default: R,
                                                         funName: String,
                                                         orderBy: O.()->Unit = {},
                                                           extra: X.()->Unit = {},
                                                           where: W.()->Unit = {},
                                                  getCursorValue: (Cursor)->R,
): R {
    return getCursor(table, column, orderBy = orderBy, where = where,
        extra = {
            extra()
            limit = 1
        }
    ).use { cursor ->
        if (cursor.moveToFirst())
            getCursorValue(cursor)
        else {
            // region LOG
            if (!suppressValGetterErrorLog)
                eLog("$funName() Unable to get value from ${table.name} (column: ${cursor.getColumnName(0)}). Returning default.")
            // endregion
            default
        }
    }
}

