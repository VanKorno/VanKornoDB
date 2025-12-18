// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set.dsl

import android.content.ContentValues
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.set.dsl.data.SetOp

class SetBuilder : BaseSetBuilder () {
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
    
    
    infix fun IntCol.mult(value: Number) = iMultDiv(this, value, "*")
    infix fun IntCol.div(value: Number) = iMultDiv(this, value, "/")
    
    infix fun LongCol.mult(value: Number) = lMultDiv(this, value, "*")
    infix fun LongCol.div(value: Number) = lMultDiv(this, value, "/")
    
    infix fun FloatCol.mult(value: Number) = fMultDiv(this, value, "*")
    infix fun FloatCol.div(value: Number) = fMultDiv(this, value, "/")
    
    
    fun IntCol.abs() { _ops += SetOp.Abs(this.name) }
    
    
    infix fun IntCol.max(value: Number) { _ops += SetOp.MinMax(this.name, value, true) }
    infix fun IntCol.min(value: Number) { _ops += SetOp.MinMax(this.name, value, false) }
    
    infix fun LongCol.max(value: Number) { _ops += SetOp.MinMax(this.name, value, true) }
    infix fun LongCol.min(value: Number) { _ops += SetOp.MinMax(this.name, value, false) }
    
    infix fun FloatCol.max(value: Number) { _ops += SetOp.MinMax(this.name, value, true) }
    infix fun FloatCol.min(value: Number) { _ops += SetOp.MinMax(this.name, value, false) }
    
    
    infix fun IntCol.coerceIn(                                                     range: IntRange
    ) {
        _ops += SetOp.CoerceIn(this.name, range.first, range.last)
    }
    infix fun LongCol.coerceIn(                                                    range: LongRange
    ) {
        _ops += SetOp.CoerceIn(this.name, range.first, range.last)
    }
    infix fun FloatCol.coerceIn(                              range: ClosedFloatingPointRange<Float>
    ) {
        _ops += SetOp.CoerceIn(this.name, range.start, range.endInclusive)
    }
    
    
    // ----------------- col-to-col DSL -----------------
    
    fun IntCol.setAs(                                                  col: IntCol,
                                                                     build: SetBuilder.()->Unit = {},
    ) {
        val innerSetter = SetBuilder().apply(build)
        _ops += SetOp.SetAs(this.name, col.name, innerSetter.ops)
    }
    
    fun LongCol.setAs(                                                 col: LongCol,
                                                                     build: SetBuilder.()->Unit = {},
    ) {
        val innerSetter = SetBuilder().apply(build)
        _ops += SetOp.SetAs(this.name, col.name, innerSetter.ops)
    }
    
    fun FloatCol.setAs(                                                col: FloatCol,
                                                                     build: SetBuilder.()->Unit = {},
    ) {
        val innerSetter = SetBuilder().apply(build)
        _ops += SetOp.SetAs(this.name, col.name, innerSetter.ops)
    }
    
    infix fun IntCol.setAs(col: IntCol) { _ops += SetOp.SetAs(this.name, col.name) }
    infix fun LongCol.setAs(col: LongCol) { _ops += SetOp.SetAs(this.name, col.name) }
    infix fun FloatCol.setAs(col: FloatCol) { _ops += SetOp.SetAs(this.name, col.name) }
    
    infix fun BoolCol.setAs(col: BoolCol) { _ops += SetOp.SetAs(this.name, col.name) }
    
    
    
    
    
    // TODO Move maybe
    
    private fun iMultDiv(                                                            col: IntCol,
                                                                                   value: Number,
                                                                                    oper: String,
    ) {
        val v = value.toInt()
        if (v == 1) return //\/\/\/\/\/\
        
        _ops += if (v == 0)
                    SetOp.Set(col, 0)
                else
                    SetOp.NumOp(col.name, v, oper)
    }
    private fun lMultDiv(                                                            col: LongCol,
                                                                                   value: Number,
                                                                                    oper: String,
    ) {
        val v = value.toLong()
        if (v == 1L) return //\/\/\/\/\/\
        
        _ops += if (v == 0L)
                    SetOp.Set(col, 0L)
                else
                    SetOp.NumOp(col.name, v, oper)
    }
    
    private fun fMultDiv(                                                            col: FloatCol,
                                                                                   value: Number,
                                                                                    oper: String,
    ) {
        val v = value.toFloat()
        if (v == 1f) return //\/\/\/\/\/\
        
        _ops += if (v == 0f)
                    SetOp.Set(col, 0f)
                else
                    SetOp.NumOp(col.name, v, oper)
    }
    
    
    
    
}












