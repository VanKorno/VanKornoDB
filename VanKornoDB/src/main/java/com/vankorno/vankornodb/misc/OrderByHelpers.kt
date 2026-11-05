/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.api.OrderDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cRowId

fun orderBy(vararg columns: BaseColumn): OrderDsl.()->Unit = { col(*columns) }

fun AscendingColumn<*>.by(): OrderDsl.()->Unit = { col(this@by) }

fun orderById(): OrderDsl.()->Unit = { cID() }
fun orderByIdFlipped(): OrderDsl.()->Unit = { cID.flip() }

fun orderByRowId(): OrderDsl.()->Unit = { cRowId() }
fun orderByRowIdFlipped(): OrderDsl.()->Unit = { cRowId.flip() }


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




