/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.dsl

import android.content.ContentValues
import com.vankorno.vankornodb.core.dsl.DslInject
import com.vankorno.vankornodb.core.dsl.Field
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.NumberColumn
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.set.dsl.data.FloatColOp
import com.vankorno.vankornodb.set.dsl.data.IntColOp
import com.vankorno.vankornodb.set.dsl.data.LongColOp
import com.vankorno.vankornodb.set.dsl.data.SetOp

@DslInject
abstract class SetDsl<W> : SetDslBase<W>() {
    fun setCV(cv: ContentValues) { _ops += SetOp.SetCV(cv) }
    fun setContentValues(cv: ContentValues) = setCV(cv)
    
    infix fun <T> Field<W, TypedColumn<T>>.setTo(value: T) { _ops += SetOp.Set(definition, value) }
    
    
    fun Field<W, BoolCol>.flip() { _ops += SetOp.Flip(definition) }
    fun OFF(col: Field<W, BoolCol>) { _ops += SetOp.Set(col.definition, false) }
    fun ON(col: Field<W, BoolCol>) { _ops += SetOp.Set(col.definition, true) }
    
    
    infix fun <T : Number> Field<W, NumberColumn<T>>.add(value: Number) { _ops += SetOp.NumOp(definition.name, value, "+") }
    
    fun <T : Number> List<Field<W, NumberColumn<T>>>.addToEach(value: Number) { this.forEach { it.add(value) }}
    
    @JvmName("subInt")
    infix fun Field<W, IntCol>.sub(value: Number) { add(-value.toInt()) } // TODO Check later
    @JvmName("subLong")
    infix fun Field<W, LongCol>.sub(value: Number) { add(-value.toLong()) }
    @JvmName("subFloat")
    infix fun Field<W, FloatCol>.sub(value: Number) { add(-value.toFloat()) }
    
    
    @JvmName("multInt")
    infix fun Field<W, IntCol>.mult(value: Number) = iMultDiv(definition, value, "*")
    @JvmName("divInt")
    infix fun Field<W, IntCol>.div(value: Number) = iMultDiv(definition, value, "/")
    
    @JvmName("multLong")
    infix fun Field<W, LongCol>.mult(value: Number) = lMultDiv(definition, value, "*")
    @JvmName("divLong")
    infix fun Field<W, LongCol>.div(value: Number) = lMultDiv(definition, value, "/")
    
    @JvmName("multFloat")
    infix fun Field<W, FloatCol>.mult(value: Number) = fMultDiv(definition, value, "*")
    @JvmName("divFloat")
    infix fun Field<W, FloatCol>.div(value: Number) = fMultDiv(definition, value, "/")
    
    
    fun Field<W, IntCol>.abs() { _ops += SetOp.Abs(definition.name) }
    
    
    infix fun <T : Number> Field<W, NumberColumn<T>>.capAt(value: Number) { _ops += SetOp.MinMax(definition.name, value, false) }
    infix fun <T : Number> Field<W, NumberColumn<T>>.floorAt(value: Number) { _ops += SetOp.MinMax(definition.name, value, true) }
    
    
    @JvmName("coerceInInt")
    infix fun Field<W, IntCol>.coerceIn(                                                     range: IntRange
    ) {
        _ops += SetOp.CoerceIn(definition.name, range.first, range.last)
    }
    @JvmName("coerceInLong")
    infix fun Field<W, LongCol>.coerceIn(                                                    range: LongRange
    ) {
        _ops += SetOp.CoerceIn(definition.name, range.first, range.last)
    }
    @JvmName("coerceInFloat")
    infix fun Field<W, FloatCol>.coerceIn(                              range: ClosedFloatingPointRange<Float>
    ) {
        _ops += SetOp.CoerceIn(definition.name, range.start, range.endInclusive)
    }
    
    
    // ----------------- col-to-col DSL -----------------
    
    @JvmName("setAsInt")
    infix fun Field<W, IntCol>.setAs(col: Field<W, IntCol>) { _ops += SetOp.SetAs(definition.name, col.definition.name) }
    @JvmName("setAsLong")
    infix fun Field<W, LongCol>.setAs(col: Field<W, LongCol>) { _ops += SetOp.SetAs(definition.name, col.definition.name) }
    @JvmName("setAsFloat")
    infix fun Field<W, FloatCol>.setAs(col: Field<W, FloatCol>) { _ops += SetOp.SetAs(definition.name, col.definition.name) }
    
    @JvmName("setAsBool")
    infix fun Field<W, BoolCol>.setAs(col: Field<W, BoolCol>) { _ops += SetOp.SetAs(definition.name, col.definition.name) }
    
    
    @JvmName("setAsInt")
    infix fun Field<W, IntCol>.setAs(colWithOp: IntColOp<W>) {
        _ops += SetOp.SetAsModified(definition.name, colWithOp)
    }
    @JvmName("setAsLong")
    infix fun Field<W, LongCol>.setAs(colWithOp: LongColOp<W>) {
        _ops += SetOp.SetAsModified(definition.name, colWithOp)
    }
    @JvmName("setAsFloat")
    infix fun Field<W, FloatCol>.setAs(colWithOp: FloatColOp<W>) {
        _ops += SetOp.SetAsModified(definition.name, colWithOp)
    }
    
    
    
}
