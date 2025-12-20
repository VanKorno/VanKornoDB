// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.SetBuilder
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.WHERE
import com.vankorno.vankornodb.set.dsl.data.FloatColOp
import com.vankorno.vankornodb.set.dsl.data.IntColOp
import com.vankorno.vankornodb.set.dsl.data.LongColOp
import com.vankorno.vankornodb.set.dsl.data.MathOp
import com.vankorno.vankornodb.set.dsl.data.SetColOp
import com.vankorno.vankornodb.set.dsl.data.SetOp
import com.vankorno.vankornodb.set.noty.getBoolSafeVal
import com.vankorno.vankornodb.set.noty.setRowValsNoty

fun SQLiteDatabase.set(                                           table: String,
                                                                  where: WhereBuilder.()->Unit = {},
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
    
    
    fun playTogether(                                      setCol: String,
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
                setRowValsNoty(table, op.cv, where)
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
        is IntColOp -> buildMathPart(col, op, value, args)
        is LongColOp -> buildMathPart(col, op, value, args)
        is FloatColOp -> buildMathPart(col, op, value, args)
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











