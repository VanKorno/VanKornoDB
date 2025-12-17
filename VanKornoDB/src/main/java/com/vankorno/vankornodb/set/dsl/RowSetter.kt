package com.vankorno.vankornodb.set.dsl
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.content.ContentValues
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.set.dsl.data.SetOp

class RowSetter : BaseSetter () {
    fun setCV(cv: ContentValues) { _ops += SetOp.SetCV(cv) }
    fun setContentValues(cv: ContentValues) = setCV(cv)
    
    infix fun <T> TypedColumn<T>.setTo(value: T) { _ops += SetOp.Set(this, value) }
    
    infix fun String.setNotyTo(value: Any) { _ops += SetOp.SetNoty(this, value) }
    
    fun BoolCol.flip() { _ops += SetOp.Flip(this) }
    fun OFF(col: BoolCol) { _ops += SetOp.Set(col, false) }
    fun ON(col: BoolCol) { _ops += SetOp.Set(col, true) }
    
    
    infix fun IntCol.add(value: Number) { _ops += SetOp.NumOp(this.name, value, "+") }
    infix fun LongCol.add(value: Number) { _ops += SetOp.NumOp(this.name, value, "+") }
    infix fun FloatCol.add(value: Number) { _ops += SetOp.NumOp(this.name, value, "+") }
    
    infix fun IntCol.sub(value: Number) { add(-value.toInt()) }
    infix fun LongCol.sub(value: Number) { add(-value.toLong()) }
    infix fun FloatCol.sub(value: Number) { add(-value.toFloat()) }
    
    infix fun IntCol.mult(value: Number) { _ops += SetOp.NumOp(this.name, value, "*") }
    infix fun LongCol.mult(value: Number) { _ops += SetOp.NumOp(this.name, value, "*") }
    infix fun FloatCol.mult(value: Number) { _ops += SetOp.NumOp(this.name, value, "*") }
    
    
    
    
    
    
}