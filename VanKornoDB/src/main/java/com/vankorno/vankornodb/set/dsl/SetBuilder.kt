/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.dsl

import android.content.ContentValues
import com.vankorno.vankornodb.core.dsl.DslInject
import com.vankorno.vankornodb.core.dsl.EntityColumn
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
    
    infix fun <T> EntityColumn<W, TypedColumn<T>>.setTo(value: T) { _ops += SetOp.Set(definition, value) }
    
    
    fun EntityColumn<W, BoolCol>.flip() { _ops += SetOp.Flip(definition) }
    fun OFF(col: EntityColumn<W, BoolCol>) { _ops += SetOp.Set(col.definition, false) }
    fun ON(col: EntityColumn<W, BoolCol>) { _ops += SetOp.Set(col.definition, true) }
    
    
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.add(value: Number) { _ops += SetOp.NumOp(definition.name, value, "+") }
    
    fun <T : Number> List<EntityColumn<W, NumberColumn<T>>>.addToEach(value: Number) { this.forEach { it.add(value) }}
    
    @JvmName("subInt")
    infix fun EntityColumn<W, IntCol>.sub(value: Number) { add(-value.toInt()) } // TODO Check later
    @JvmName("subLong")
    infix fun EntityColumn<W, LongCol>.sub(value: Number) { add(-value.toLong()) }
    @JvmName("subFloat")
    infix fun EntityColumn<W, FloatCol>.sub(value: Number) { add(-value.toFloat()) }
    
    
    @JvmName("multInt")
    infix fun EntityColumn<W, IntCol>.mult(value: Number) = iMultDiv(definition, value, "*")
    @JvmName("divInt")
    infix fun EntityColumn<W, IntCol>.div(value: Number) = iMultDiv(definition, value, "/")
    
    @JvmName("multLong")
    infix fun EntityColumn<W, LongCol>.mult(value: Number) = lMultDiv(definition, value, "*")
    @JvmName("divLong")
    infix fun EntityColumn<W, LongCol>.div(value: Number) = lMultDiv(definition, value, "/")
    
    @JvmName("multFloat")
    infix fun EntityColumn<W, FloatCol>.mult(value: Number) = fMultDiv(definition, value, "*")
    @JvmName("divFloat")
    infix fun EntityColumn<W, FloatCol>.div(value: Number) = fMultDiv(definition, value, "/")
    
    
    fun EntityColumn<W, IntCol>.abs() { _ops += SetOp.Abs(definition.name) }
    
    
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.capAt(value: Number) { _ops += SetOp.MinMax(definition.name, value, false) }
    infix fun <T : Number> EntityColumn<W, NumberColumn<T>>.floorAt(value: Number) { _ops += SetOp.MinMax(definition.name, value, true) }
    
    
    @JvmName("coerceInInt")
    infix fun EntityColumn<W, IntCol>.coerceIn(                                                     range: IntRange
    ) {
        _ops += SetOp.CoerceIn(definition.name, range.first, range.last)
    }
    @JvmName("coerceInLong")
    infix fun EntityColumn<W, LongCol>.coerceIn(                                                    range: LongRange
    ) {
        _ops += SetOp.CoerceIn(definition.name, range.first, range.last)
    }
    @JvmName("coerceInFloat")
    infix fun EntityColumn<W, FloatCol>.coerceIn(                              range: ClosedFloatingPointRange<Float>
    ) {
        _ops += SetOp.CoerceIn(definition.name, range.start, range.endInclusive)
    }
    
    
    // ----------------- col-to-col DSL -----------------
    
    @JvmName("setAsInt")
    infix fun EntityColumn<W, IntCol>.setAs(col: EntityColumn<W, IntCol>) { _ops += SetOp.SetAs(definition.name, col.definition.name) }
    @JvmName("setAsLong")
    infix fun EntityColumn<W, LongCol>.setAs(col: EntityColumn<W, LongCol>) { _ops += SetOp.SetAs(definition.name, col.definition.name) }
    @JvmName("setAsFloat")
    infix fun EntityColumn<W, FloatCol>.setAs(col: EntityColumn<W, FloatCol>) { _ops += SetOp.SetAs(definition.name, col.definition.name) }
    
    @JvmName("setAsBool")
    infix fun EntityColumn<W, BoolCol>.setAs(col: EntityColumn<W, BoolCol>) { _ops += SetOp.SetAs(definition.name, col.definition.name) }
    
    
    @JvmName("setAsInt")
    infix fun EntityColumn<W, IntCol>.setAs(colWithOp: IntColOp<W>) {
        _ops += SetOp.SetAsModified(definition.name, colWithOp)
    }
    @JvmName("setAsLong")
    infix fun EntityColumn<W, LongCol>.setAs(colWithOp: LongColOp<W>) {
        _ops += SetOp.SetAsModified(definition.name, colWithOp)
    }
    @JvmName("setAsFloat")
    infix fun EntityColumn<W, FloatCol>.setAs(colWithOp: FloatColOp<W>) {
        _ops += SetOp.SetAsModified(definition.name, colWithOp)
    }
    
    
    
}
