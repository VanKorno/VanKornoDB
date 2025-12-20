// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set.dsl

import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.set.dsl.data.FloatColOp
import com.vankorno.vankornodb.set.dsl.data.IntColOp
import com.vankorno.vankornodb.set.dsl.data.LongColOp
import com.vankorno.vankornodb.set.dsl.data.MathOp
import com.vankorno.vankornodb.set.dsl.data.SetOp

/**
 *  Internal base — use `SetBuilder` from the api package instead
 */
open class BaseSetBuilder {
    protected val _ops = mutableListOf<SetOp>()
    
    val ops: List<SetOp>
        get() = _ops
    
    
    
    internal fun iMultDiv(                                                           col: IntCol,
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
    internal fun lMultDiv(                                                           col: LongCol,
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
    
    internal fun fMultDiv(                                                           col: FloatCol,
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
    
    
    // ----------------- Helpers for creating SetColOp instances -----------------
    
    infix fun IntCol.andAdd(value: Number) = IntColOp(this.name, value, MathOp.Add)
    infix fun IntCol.andSub(value: Number) = IntColOp(this.name, value, MathOp.Sub)
    infix fun IntCol.andMult(value: Number) = IntColOp(this.name, value, MathOp.Mult)
    infix fun IntCol.andDiv(value: Number) = IntColOp(this.name, value, MathOp.Div)
    infix fun IntCol.andCapAt(value: Number) = IntColOp(this.name, value, MathOp.CapAt(value))
    infix fun IntCol.andFloorAt(value: Number) = IntColOp(this.name, value, MathOp.FloorAt(value))
    infix fun IntCol.andCoerceIn(range: IntRange) =
        IntColOp(this.name, range.first, MathOp.CoerceIn(range.first, range.last))
    
    infix fun LongCol.andAdd(value: Number) = LongColOp(this.name, value, MathOp.Add)
    infix fun LongCol.andSub(value: Number) = LongColOp(this.name, value, MathOp.Sub)
    infix fun LongCol.andMult(value: Number) = LongColOp(this.name, value, MathOp.Mult)
    infix fun LongCol.andDiv(value: Number) = LongColOp(this.name, value, MathOp.Div)
    infix fun LongCol.andCapAt(value: Number) = LongColOp(this.name, value, MathOp.CapAt(value))
    infix fun LongCol.andFloorAt(value: Number) = LongColOp(this.name, value, MathOp.FloorAt(value))
    infix fun LongCol.andCoerceIn(range: LongRange) =
        LongColOp(this.name, range.first, MathOp.CoerceIn(range.first, range.last))
    
    infix fun FloatCol.andAdd(value: Number) = FloatColOp(this.name, value, MathOp.Add)
    infix fun FloatCol.andSub(value: Number) = FloatColOp(this.name, value, MathOp.Sub)
    infix fun FloatCol.andMult(value: Number) = FloatColOp(this.name, value, MathOp.Mult)
    infix fun FloatCol.andDiv(value: Number) = FloatColOp(this.name, value, MathOp.Div)
    infix fun FloatCol.andCapAt(value: Number) = FloatColOp(this.name, value, MathOp.CapAt(value))
    infix fun FloatCol.andFloorAt(value: Number) = FloatColOp(this.name, value, MathOp.FloorAt(value))
    infix fun FloatCol.andCoerceIn(range: ClosedFloatingPointRange<Float>) =
        FloatColOp(this.name, range.start, MathOp.CoerceIn(range.start, range.endInclusive))
    
    
    
    
    
    
}