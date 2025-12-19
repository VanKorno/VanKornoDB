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
import com.vankorno.vankornodb.set.dsl.data.FloatColOp
import com.vankorno.vankornodb.set.dsl.data.IntColOp
import com.vankorno.vankornodb.set.dsl.data.LongColOp
import com.vankorno.vankornodb.set.dsl.data.MathOp
import com.vankorno.vankornodb.set.dsl.data.SetOp

class SetBuilder : BaseSetBuilder () {
    fun setCV(cv: ContentValues) { _ops += SetOp.SetCV(cv) }
    fun setContentValues(cv: ContentValues) = setCV(cv)
    
    infix fun <T> TypedColumn<T>.setTo(value: T) { _ops += SetOp.Set(this, value) }
    
    infix fun String.setTo(value: Any) { _ops += SetOp.SetNoty(this, value) }
    
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
    
    
    infix fun IntCol.capAt(value: Number) { _ops += SetOp.MinMax(this.name, value, false) }
    infix fun IntCol.floorAt(value: Number) { _ops += SetOp.MinMax(this.name, value, true) }
    
    infix fun LongCol.capAt(value: Number) { _ops += SetOp.MinMax(this.name, value, false) }
    infix fun LongCol.floorAt(value: Number) { _ops += SetOp.MinMax(this.name, value, true) }
    
    infix fun FloatCol.capAt(value: Number) { _ops += SetOp.MinMax(this.name, value, false) }
    infix fun FloatCol.floorAt(value: Number) { _ops += SetOp.MinMax(this.name, value, true) }
    
    
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
    
    infix fun IntCol.setAs(col: IntCol) { _ops += SetOp.SetAs(this.name, col.name) }
    infix fun LongCol.setAs(col: LongCol) { _ops += SetOp.SetAs(this.name, col.name) }
    infix fun FloatCol.setAs(col: FloatCol) { _ops += SetOp.SetAs(this.name, col.name) }
    
    infix fun BoolCol.setAs(col: BoolCol) { _ops += SetOp.SetAs(this.name, col.name) }
    
    
    infix fun IntCol.setAs(colWithOp: IntColOp) {
        _ops += SetOp.SetAsModified(this.name, colWithOp)
    }
    infix fun LongCol.setAs(colWithOp: LongColOp) {
        _ops += SetOp.SetAsModified(this.name, colWithOp)
    }
    infix fun FloatCol.setAs(colWithOp: FloatColOp) {
        _ops += SetOp.SetAsModified(this.name, colWithOp)
    }
    
    
    
    // ----------------- Helpers for creating SetColOp instances -----------------
    
    infix fun IntCol.andAdd(value: Number) = IntColOp(this.name, value, MathOp.Add)
    infix fun IntCol.andSub(value: Number) = IntColOp(this.name, value, MathOp.Sub)
    infix fun IntCol.andMult(value: Number) = IntColOp(this.name, value, MathOp.Mult)
    infix fun IntCol.andDiv(value: Number) = IntColOp(this.name, value, MathOp.Div)
    infix fun IntCol.andCapAt(value: Number) = IntColOp(this.name, value, MathOp.CapAt(value))
    infix fun IntCol.andFloorAt(value: Number) = IntColOp(this.name, value, MathOp.FloorAt(value))
    infix fun IntCol.andCoerceIn(range: IntRange) = IntColOp(this.name, range.first, MathOp.CoerceIn(range.first, range.last))
    
    infix fun LongCol.andAdd(value: Number) = LongColOp(this.name, value, MathOp.Add)
    infix fun LongCol.andSub(value: Number) = LongColOp(this.name, value, MathOp.Sub)
    infix fun LongCol.andMult(value: Number) = LongColOp(this.name, value, MathOp.Mult)
    infix fun LongCol.andDiv(value: Number) = LongColOp(this.name, value, MathOp.Div)
    infix fun LongCol.andCapAt(value: Number) = LongColOp(this.name, value, MathOp.CapAt(value))
    infix fun LongCol.andFloorAt(value: Number) = LongColOp(this.name, value, MathOp.FloorAt(value))
    infix fun LongCol.andCoerceIn(range: LongRange) = LongColOp(this.name, range.first, MathOp.CoerceIn(range.first, range.last))
    
    infix fun FloatCol.andAdd(value: Number) = FloatColOp(this.name, value, MathOp.Add)
    infix fun FloatCol.andSub(value: Number) = FloatColOp(this.name, value, MathOp.Sub)
    infix fun FloatCol.andMult(value: Number) = FloatColOp(this.name, value, MathOp.Mult)
    infix fun FloatCol.andDiv(value: Number) = FloatColOp(this.name, value, MathOp.Div)
    infix fun FloatCol.andCapAt(value: Number) = FloatColOp(this.name, value, MathOp.CapAt(value))
    infix fun FloatCol.andFloorAt(value: Number) = FloatColOp(this.name, value, MathOp.FloorAt(value))
    infix fun FloatCol.andCoerceIn(range: ClosedFloatingPointRange<Float>) = FloatColOp(this.name, range.start, MathOp.CoerceIn(range.start, range.endInclusive))
    
    
    
    
    
    
    
    
    
    
    
}












