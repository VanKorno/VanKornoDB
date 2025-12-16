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
    
    
    
    infix fun IntCol.add(value: Number) { _ops += SetOp.AddToInt(this, value) }
    infix fun LongCol.add(value: Number) { _ops += SetOp.AddToLong(this, value) }
    infix fun FloatCol.add(value: Number) { _ops += SetOp.AddToFloat(this, value) }
    
    fun BoolCol.flip() { _ops += SetOp.Flip(this) }
    
    
}