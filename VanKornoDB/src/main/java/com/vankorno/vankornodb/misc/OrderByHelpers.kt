/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.*

fun orderBy(vararg columns: BaseColumn): OrderDsl.()->Unit = { col(*columns) }

fun AscendingColumn<*>.by(): OrderDsl.()->Unit = { col(this@by) }

fun orderById(): OrderDsl.()->Unit = { orderById() }
fun orderByIdFlipped(): OrderDsl.()->Unit = { orderByIdFlipped() }

fun orderByRowId(): OrderDsl.()->Unit = { orderByRowId() }
fun orderByRowIdFlipped(): OrderDsl.()->Unit = { orderByRowIdFlipped() }

fun orderByPosition(): OrderDsl.()->Unit = { orderByPosition() }
fun orderByPositionFlipped(): OrderDsl.()->Unit = { orderByPositionFlipped() }

fun orderByName(): OrderDsl.()->Unit = { orderByName() }
fun orderByNameFlipped(): OrderDsl.()->Unit = { orderByNameFlipped() }

fun orderRandomly(): OrderDsl.()->Unit = { orderRandomly() }



fun AscendingColumn<*>.flip(): OrderDsl.()->Unit = { this@flip.flip() }

fun AscendingColumn<*>.flipIf(                                             condition: Boolean
): OrderDsl.()->Unit = {
    if (condition)
        this@flipIf.flip()
    else
        col(this@flipIf)
}

fun IntCol.flipped() = DescendingIntCol(this@flipped.name)
fun StrCol.flipped() = DescendingStrCol(this@flipped.name)
fun BoolCol.flipped() = DescendingBoolCol(this@flipped.name)
fun LongCol.flipped() = DescendingLongCol(this@flipped.name)
fun FloatCol.flipped() = DescendingFloatCol(this@flipped.name)

fun IntCol.flippedIf(condition: Boolean) = if (condition) this.flipped() else this
fun StrCol.flippedIf(condition: Boolean) = if (condition) this.flipped() else this
fun BoolCol.flippedIf(condition: Boolean) = if (condition) this.flipped() else this
fun LongCol.flippedIf(condition: Boolean) = if (condition) this.flipped() else this
fun FloatCol.flippedIf(condition: Boolean) = if (condition) this.flipped() else this




