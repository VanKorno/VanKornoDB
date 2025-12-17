package com.vankorno.vankornodb.set
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.WHERE
import com.vankorno.vankornodb.set.dsl.RowSetter
import com.vankorno.vankornodb.set.dsl.data.SetOp
import com.vankorno.vankornodb.set.noty.getBoolSafeVal
import com.vankorno.vankornodb.set.noty.setRowValsNoty

fun SQLiteDatabase.setRowVals(                                         table: String,
                                                                       where: WhereBuilder.()->Unit,
                                                                     actions: RowSetter.()->Unit,
) {
    val ops = RowSetter().apply(actions).ops
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
    
    fun playTogether(                            colName: String,
                                                     run: (String)->Unit,
    ) {
        if (colName in usedCols) flush()
        run(colName)
        usedCols += colName
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
            
            is SetOp.Clamp -> {
                playTogether(op.colName) { col ->
                    val func = if (op.isMax) "MAX" else "MIN"
                    setParts += "$col = $func($col, ?)"
                    args += op.value
                }
            }
        }
    }
    flush()
}




