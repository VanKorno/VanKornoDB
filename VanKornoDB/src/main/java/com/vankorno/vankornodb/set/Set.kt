/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.data.DbConstants.WHERE
import com.vankorno.vankornodb.core.data.QueryWithArgs
import com.vankorno.vankornodb.core.dsl.FullDslTable
import com.vankorno.vankornodb.set.dsl.SetDsl
import com.vankorno.vankornodb.set.dsl.data.FloatColOp
import com.vankorno.vankornodb.set.dsl.data.IntColOp
import com.vankorno.vankornodb.set.dsl.data.LongColOp
import com.vankorno.vankornodb.set.dsl.data.MathOp
import com.vankorno.vankornodb.set.dsl.data.SetColOp
import com.vankorno.vankornodb.set.dsl.data.SetOp
import com.vankorno.vankornodb.set.noty.getBoolSafeVal

/** Applies [actions] in declaration order to the rows matching [where].
 */
fun <W, S : SetDsl<W>> Dbh.set(                                   table: FullDslTable<*, W, *, *, S>,
                                                                  where: W.()->Unit = {},
                                                                  async: Boolean = false,
                                                                actions: S.()->Unit,
) = write("set", async) { db -> db.set(table, where, actions) }


fun <W, S : SetDsl<W>> SQLiteDatabase.set(                        table: FullDslTable<*, W, *, *, S>,
                                                                  where: W.()->Unit = {},
                                                                actions: S.()->Unit,
) {
    val ops = table.set(table.setFactory, actions)
    if (ops.isEmpty()) return //\/\/\/\/\/\
    
    val predicate = table.where(where)
    beginTransaction()
    try {
        applySetOps(table.name, ops, predicate)
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}




private fun SQLiteDatabase.applySetOps(                                       table: String,
                                                                                ops: List<SetOp>,
                                                                          predicate: QueryWithArgs,
) {
    val setParts = mutableListOf<String>()
    val args = mutableListOf<Any>()
    val usedCols = mutableSetOf<String>()
    
    fun flush() {
        if (setParts.isEmpty()) return //\/\/\/\/\/\
        
        val sql = buildString {
            append("UPDATE ")
            append(table)
            append(" SET ")
            append(setParts.joinToString(", "))
            if (predicate.query.isNotEmpty()) {
                append(WHERE)
                append(predicate.query)
            }
        }
        execSQL(sql, (args + predicate.args).toTypedArray())
        
        setParts.clear()
        args.clear()
        usedCols.clear()
    }
    
    
    
    fun playTogether(                                                       setCol: String,
                                                                            getCol: String? = null,
                                                                               run: (String)->Unit,
    ) {
        if (setCol in usedCols || (getCol != null && getCol in usedCols))
            flush()
        run(setCol)
        usedCols += setCol
    }
    
    ops.forEach { op ->
        when (op) {
            is SetOp.Set<*> -> playTogether(op.col.name) { col ->
                setParts += "$col = ?"
                args += getBoolSafeVal(op.value as Any)
            }
            is SetOp.SetNoty -> playTogether(op.col) { col ->
                setParts += "$col = ?"
                args += getBoolSafeVal(op.value)
            }
            is SetOp.SetCV -> {
                flush()
                update(table, op.cv, predicate.query.ifEmpty { null }, predicate.args)
            }
            
            is SetOp.Flip -> playTogether(op.col.name) { col ->
                setParts += "$col = NOT $col"
            }
            is SetOp.NumOp -> playTogether(op.colName) { col ->
                setParts += "$col = $col ${op.sqlOp} ?"
                args += op.value
            }
            is SetOp.Abs -> playTogether(op.colName) { col ->
                setParts += "$col = ABS($col)"
            }
            is SetOp.MinMax -> playTogether(op.colName) { col ->
                val func = if (op.isFloorOp) "MAX" else "MIN"
                setParts += "$col = $func($col, ?)"
                args += op.value
            }
            is SetOp.CoerceIn -> playTogether(op.colName) { col ->
                setParts += "$col = MIN(MAX($col, ?), ?)"
                args += op.min
                args += op.max
            }
            
            is SetOp.SetAs -> playTogether(op.setCol, op.getCol) { setCol ->
                setParts += "$setCol = ${op.getCol}"
            }
            is SetOp.SetAsModified -> playTogether(op.setCol, op.colWithModif.col) { setCol ->
                val mathPart = op.colWithModif.toSqlFragment(args)
                setParts += "$setCol = $mathPart"
            }
        }
    }
    flush()
}



private fun SetColOp.toSqlFragment(                                          args: MutableList<Any>
): String =
    when (this) {
        is IntColOp<*> -> buildMathPart(col, op, value, args)
        is LongColOp<*> -> buildMathPart(col, op, value, args)
        is FloatColOp<*> -> buildMathPart(col, op, value, args)
    }


private fun buildMathPart(                                                    col: String,
                                                                           mathOp: MathOp,
                                                                            value: Number,
                                                                             args: MutableList<Any>,
): String =
    when (mathOp) {
        is MathOp.Add -> "$col + ?".also { args += value }
        is MathOp.Sub -> "$col - ?".also { args += value }
        is MathOp.Mult -> "$col * ?".also { args += value }
        is MathOp.Div -> "$col / ?".also { args += value }
        is MathOp.CapAt -> "MIN($col, ?)".also { args += mathOp.cap }
        is MathOp.FloorAt -> "MAX($col, ?)".also { args += mathOp.floor }
        is MathOp.CoerceIn -> "MIN(MAX($col, ?), ?)".also { args += mathOp.min; args += mathOp.max }
    }
