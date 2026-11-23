/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.dsl

import com.vankorno.vankornodb.core.dsl.Field
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.set.dsl.data.FloatColOp
import com.vankorno.vankornodb.set.dsl.data.IntColOp
import com.vankorno.vankornodb.set.dsl.data.LongColOp
import com.vankorno.vankornodb.set.dsl.data.MathOp
import com.vankorno.vankornodb.set.dsl.data.SetOp

abstract class SetDslBase<W> {
    protected val _ops = mutableListOf<SetOp>()
    
    val ops: List<SetOp>
        get() = _ops
    
    
    
    internal fun iMultDiv(                                                         col: IntCol,
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
    internal fun lMultDiv(                                                         col: LongCol,
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
    
    internal fun fMultDiv(                                                         col: FloatCol,
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
    
    @JvmName("andAddInt")
    infix fun Field<W, IntCol>.andAdd(value: Number) = IntColOp<W>(definition.name, value, MathOp.Add)
    @JvmName("andSubInt")
    infix fun Field<W, IntCol>.andSub(value: Number) = IntColOp<W>(definition.name, value, MathOp.Sub)
    @JvmName("andMultInt")
    infix fun Field<W, IntCol>.andMult(value: Number) = IntColOp<W>(definition.name, value, MathOp.Mult)
    @JvmName("andDivInt")
    infix fun Field<W, IntCol>.andDiv(value: Number) = IntColOp<W>(definition.name, value, MathOp.Div)
    @JvmName("andCapAtInt")
    infix fun Field<W, IntCol>.andCapAt(value: Number) = IntColOp<W>(definition.name, value, MathOp.CapAt(value))
    @JvmName("andFloorAtInt")
    infix fun Field<W, IntCol>.andFloorAt(value: Number) = IntColOp<W>(definition.name, value, MathOp.FloorAt(value))
    @JvmName("andCoerceInInt")
    infix fun Field<W, IntCol>.andCoerceIn(                                      range: IntRange
    ) = IntColOp<W>(definition.name, range.first, MathOp.CoerceIn(range.first, range.last))
    
    @JvmName("andAddLong")
    infix fun Field<W, LongCol>.andAdd(value: Number) = LongColOp<W>(definition.name, value, MathOp.Add)
    @JvmName("andSubLong")
    infix fun Field<W, LongCol>.andSub(value: Number) = LongColOp<W>(definition.name, value, MathOp.Sub)
    @JvmName("andMultLong")
    infix fun Field<W, LongCol>.andMult(value: Number) = LongColOp<W>(definition.name, value, MathOp.Mult)
    @JvmName("andDivLong")
    infix fun Field<W, LongCol>.andDiv(value: Number) = LongColOp<W>(definition.name, value, MathOp.Div)
    @JvmName("andCapAtLong")
    infix fun Field<W, LongCol>.andCapAt(value: Number) = LongColOp<W>(definition.name, value, MathOp.CapAt(value))
    @JvmName("andFloorAtLong")
    infix fun Field<W, LongCol>.andFloorAt(value: Number) = LongColOp<W>(definition.name, value, MathOp.FloorAt(value))
    @JvmName("andCoerceInLong")
    infix fun Field<W, LongCol>.andCoerceIn(                                     range: LongRange
    ) = LongColOp<W>(definition.name, range.first, MathOp.CoerceIn(range.first, range.last))
    
    @JvmName("andAddFloat")
    infix fun Field<W, FloatCol>.andAdd(value: Number) = FloatColOp<W>(definition.name, value, MathOp.Add)
    @JvmName("andSubFloat")
    infix fun Field<W, FloatCol>.andSub(value: Number) = FloatColOp<W>(definition.name, value, MathOp.Sub)
    @JvmName("andMultFloat")
    infix fun Field<W, FloatCol>.andMult(value: Number) = FloatColOp<W>(definition.name, value, MathOp.Mult)
    @JvmName("andDivFloat")
    infix fun Field<W, FloatCol>.andDiv(value: Number) = FloatColOp<W>(definition.name, value, MathOp.Div)
    @JvmName("andCapAtFloat")
    infix fun Field<W, FloatCol>.andCapAt(value: Number) = FloatColOp<W>(definition.name, value, MathOp.CapAt(value))
    @JvmName("andFloorAtFloat")
    infix fun Field<W, FloatCol>.andFloorAt(value: Number) = FloatColOp<W>(definition.name, value, MathOp.FloorAt(value))
    @JvmName("andCoerceInFloat")
    infix fun Field<W, FloatCol>.andCoerceIn(               range: ClosedFloatingPointRange<Float>
    ) = FloatColOp<W>(definition.name, range.start, MathOp.CoerceIn(range.start, range.endInclusive))
    
    
    
    
    
    
}
