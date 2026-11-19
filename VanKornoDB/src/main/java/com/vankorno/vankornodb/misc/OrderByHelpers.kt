/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.core.data.DbConstants.DESCENDING
import com.vankorno.vankornodb.core.data.DbConstants.RowID
import com.vankorno.vankornodb.core.data.DbConstants._ID
import com.vankorno.vankornodb.core.data.DbConstants._Position
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.*

fun orderBy(vararg columns: BaseColumn): OrderDsl<*, *>.()->Unit = {
    for (column in columns)
        raw(if (column is DescendingColumn) column.name + DESCENDING else column.name)
}

fun AscendingColumn<*>.by(): OrderDsl<*, *>.()->Unit = { raw(this@by.name) }

fun orderById(): OrderDsl<*, *>.()->Unit = { raw(_ID) }
fun orderByIdFlipped(): OrderDsl<*, *>.()->Unit = { raw(_ID + DESCENDING) }

fun orderByRowId(): OrderDsl<*, *>.()->Unit = { raw(RowID) }
fun orderByRowIdFlipped(): OrderDsl<*, *>.()->Unit = { raw(RowID + DESCENDING) }

fun orderByPosition(): OrderDsl<*, *>.()->Unit = { raw(_Position) }
fun orderByPositionFlipped(): OrderDsl<*, *>.()->Unit = { raw(_Position + DESCENDING) }

fun orderRandomly(): OrderDsl<*, *>.()->Unit = { random() }



fun AscendingColumn<*>.flip(): OrderDsl<*, *>.()->Unit = { raw(this@flip.name + DESCENDING) }

fun AscendingColumn<*>.flipIf(                                             condition: Boolean
): OrderDsl<*, *>.()->Unit = {
    raw(if (condition) this@flipIf.name + DESCENDING else this@flipIf.name)
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




