// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.WHERE
import com.vankorno.vankornodb.set.dsl.SetBuilder
import com.vankorno.vankornodb.set.dsl.data.SetOp
import com.vankorno.vankornodb.set.noty.getBoolSafeVal
import com.vankorno.vankornodb.set.noty.setRowValsNoty

fun SQLiteDatabase.setVals(                                            table: String,
                                                                       where: WhereBuilder.()->Unit,
                                                                     actions: SetBuilder.()->Unit,
) {
    val ops = SetBuilder().apply(actions).ops
    val builder = WhereBuilder().apply(where)
    
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
            if (builder.clauses.isNotEmpty()) {
                append(WHERE)
                append(builder.buildStr())
            }
        }
        execSQL(sql, (args + builder.args).toTypedArray())
        
        setParts.clear()
        args.clear()
        usedCols.clear()
    }
    
    
    fun playTogether(                                            setCol: String,
                                                                getCols: List<String> = emptyList(),
                                                                    run: (String)->Unit,
    ) {
        if (setCol in usedCols || getCols.any { it in usedCols })
            flush()
        run(setCol)
        usedCols += setCol
    }
    
    
    ops.forEach { op ->
        when (op) {
            is SetOp.Set<*> -> {
                playTogether(op.col.name) { col ->
                    setParts += "$col = ?"
                    args += getBoolSafeVal(op.value as Any)
                }
            }
            is SetOp.SetNoty -> {
                playTogether(op.col) { col ->
                    setParts += "$col = ?"
                    args += getBoolSafeVal(op.value)
                }
            }
            is SetOp.SetCV -> {
                flush()
                setRowValsNoty(table, op.cv, where)
            }
            
            is SetOp.Flip -> {
                playTogether(op.col.name) { col ->
                    setParts += "$col = NOT $col"
                }
            }
            
            is SetOp.NumOp -> {
                playTogether(op.colName) { col ->
                    setParts += "$col = $col ${op.sqlOp} ?"
                    args += op.value
                }
            }
            
            is SetOp.Abs -> {
                playTogether(op.colName) { col ->
                    setParts += "$col = ABS($col)"
                }
            }
            
            is SetOp.MinMax -> {
                playTogether(op.colName) { col ->
                    val func = if (op.isMax) "MAX" else "MIN"
                    setParts += "$col = $func($col, ?)"
                    args += op.value
                }
            }
            
            is SetOp.CoerceIn -> {
                playTogether(op.colName) { col ->
                    setParts += "$col = MIN(MAX($col, ?), ?)"
                    args += op.min
                    args += op.max
                }
            }
            
            is SetOp.SetAs -> {
                playTogether(op.setCol, listOf(op.getCol)) { setCol ->
                    val localArgs = mutableListOf<Any>()
                    var expr = op.getCol
                    
                    op.ops.forEach { innerOp ->
                        val next = buildExpr(expr, innerOp, localArgs)
                            ?: error("Invalid op inside FromCol: $innerOp")
                        expr = next
                    }
                    setParts += "$setCol = $expr"
                    args.addAll(localArgs)
                }
            }
        }
    }
    flush()
}


private fun buildExpr(                                                    rootCol: String,
                                                                               op: SetOp,
                                                                          outArgs: MutableList<Any>,
): String? =
    when (op) {
        is SetOp.NumOp ->
            "$rootCol ${op.sqlOp} ?".also {
                outArgs += op.value
            }
        
        is SetOp.Abs ->
            "ABS($rootCol)"
        
        is SetOp.MinMax -> {
            val func = if (op.isMax) "MAX" else "MIN"
            "$func($rootCol, ?)".also {
                outArgs += op.value
            }
        }
        
        is SetOp.CoerceIn ->
            "MIN(MAX($rootCol, ?), ?)".also {
                outArgs += op.min
                outArgs += op.max
            }
        
        else -> null
    }



