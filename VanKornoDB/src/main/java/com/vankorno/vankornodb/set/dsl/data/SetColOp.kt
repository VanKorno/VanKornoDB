/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.dsl.data

sealed class SetColOp {
    abstract val col: String
}

data class IntColOp<W> internal constructor(override val col: String, val value: Number, val op: MathOp) : SetColOp()
data class LongColOp<W> internal constructor(override val col: String, val value: Number, val op: MathOp) : SetColOp()
data class FloatColOp<W> internal constructor(override val col: String, val value: Number, val op: MathOp) : SetColOp()

sealed class MathOp {
    object Add : MathOp()
    object Sub : MathOp()
    object Mult : MathOp()
    object Div : MathOp()
    data class CapAt(val cap: Number) : MathOp()
    data class FloorAt(val floor: Number) : MathOp()
    data class CoerceIn(val min: Number, val max: Number) : MathOp()
}
