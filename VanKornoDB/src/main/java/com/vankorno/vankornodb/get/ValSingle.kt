/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.DbColumn
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.getBoolean
import com.vankorno.vankornodb.misc.suppressValGetterErrorLog


fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getInt(          column: DbColumn<W, O, X, IntCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): Int = read(-1, "getInt") { db ->
    db.getInt(column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getInt(
                                                                  column: DbColumn<W, O, X, IntCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): Int = getValue(column, -1, "getInt", orderBy, extra, where) {
    it.getInt(0)
}




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getStr(          column: DbColumn<W, O, X, StrCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): String = read("", "getStr") { db ->
    db.getStr(column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getStr(
                                                                  column: DbColumn<W, O, X, StrCol>,
                                                                 orderBy: O.()->Unit = {},
                                                                   extra: X.()->Unit = {},
                                                                   where: W.()->Unit = {},
): String = getValue(column, "", "getStr", orderBy, extra, where) {
    it.getString(0)
}




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getBool(        column: DbColumn<W, O, X, BoolCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): Boolean = read(false, "getBool") { db ->
    db.getBool(column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getBool(
                                                                 column: DbColumn<W, O, X, BoolCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): Boolean = getValue(column, false, "getBool", orderBy, extra, where) {
    it.getBoolean(0)
}




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getLong(        column: DbColumn<W, O, X, LongCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): Long = read(-1L, "getLong") { db ->
    db.getLong(column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getLong(
                                                                 column: DbColumn<W, O, X, LongCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): Long = getValue(column, -1L, "getLong", orderBy, extra, where) {
    it.getLong(0)
}




fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getFloat(      column: DbColumn<W, O, X, FloatCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): Float = read(-1F, "getFloat") { db ->
    db.getFloat(column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getFloat(
                                                                column: DbColumn<W, O, X, FloatCol>,
                                                               orderBy: O.()->Unit = {},
                                                                 extra: X.()->Unit = {},
                                                                 where: W.()->Unit = {},
): Float = getValue(column, -1F, "getFloat", orderBy, extra, where) {
    it.getFloat(0)
}





fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> Dbh.getBlob(        column: DbColumn<W, O, X, BlobCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): ByteArray = read(ByteArray(0), "getBlob") { db ->
    db.getBlob(column, orderBy, extra, where)
}
fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> SQLiteDatabase.getBlob(
                                                                 column: DbColumn<W, O, X, BlobCol>,
                                                                orderBy: O.()->Unit = {},
                                                                  extra: X.()->Unit = {},
                                                                  where: W.()->Unit = {},
): ByteArray = getValue(column, ByteArray(0), "getBlob", orderBy, extra, where) {
    it.getBlob(0)
}








fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>, R> SQLiteDatabase.getValue(
                                                      column: DbColumn<W, O, X, out TypedColumn<R>>,
                                                     default: R,
                                                     funName: String,
                                                     orderBy: O.()->Unit = {},
                                                       extra: X.()->Unit = {},
                                                       where: W.()->Unit = {},
                                              getCursorValue: (Cursor)->R,
): R {
    return getCursor(column, orderBy = orderBy, where = where,
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
                eLog("$funName() Unable to get value from ${column.table.name} (column: ${cursor.getColumnName(0)}). Returning default.")
            // endregion
            default
        }
    }
}
