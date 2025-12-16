package com.vankorno.vankornodb.set
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.set.dsl.RowSetter
import com.vankorno.vankornodb.set.dsl.data.SetOp
import com.vankorno.vankornodb.set.noty.setNoty
import com.vankorno.vankornodb.set.noty.setRowValsNoty

fun SQLiteDatabase.setRowVals(                                         table: String,
                                                                       where: WhereBuilder.()->Unit,
                                                                     actions: RowSetter.()->Unit,
) {
    val ops = RowSetter().apply(actions).ops
    
    ops.forEach { op ->
        when (op) {
            is SetOp.Set<*> -> {
                setNoty(op.value as Any, table, op.col.name, where)
            }
            is SetOp.SetNoty -> {
                setNoty(op.value, table, op.col, where)
            }
            is SetOp.SetCV -> {
                setRowValsNoty(table, op.cv, where)
            }
            
            is SetOp.AddToInt -> { addToInt(op.value, table, op.col, where) }
            is SetOp.AddToLong -> { addToLong(op.value, table, op.col, where) }
            is SetOp.AddToFloat -> { addToFloat(op.value, table, op.col, where) }
            
            is SetOp.Flip -> { flipBool(table, op.col, where) }
            //else -> {}
        }
    }
}




