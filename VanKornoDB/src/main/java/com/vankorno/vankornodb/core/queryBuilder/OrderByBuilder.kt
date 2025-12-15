package com.vankorno.vankornodb.core.queryBuilder
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.*

open class OrderByBuilderInternal {
    val orderoids = mutableListOf<String>()
    val args = mutableListOf<String>()
    
    internal fun buildStr(): String = orderoids.joinToString(comma)
    
    
    operator fun AscendingColumn<*>.invoke() {
        orderoids += this.name
    }
    operator fun DescendingColumn.invoke() {
        orderoids += this.name + DESCENDING
    }
    
    fun col(vararg columns: BaseColumn) = columns.forEach {
        orderoids += it.name + if (it is DescendingColumn) DESCENDING else ""
    }
    
    fun flip(vararg columns: AscendingColumn<*>) = columns.forEach { orderoids += it.name + DESCENDING }
    
    fun IntCol.flip() = DescendingIntCol(this.name)()
    fun StrCol.flip() = DescendingStrCol(this.name)()
    fun BoolCol.flip() = DescendingBoolCol(this.name)()
    fun LongCol.flip() = DescendingLongCol(this.name)()
    fun FloatCol.flip() = DescendingFloatCol(this.name)()
    
    fun raw(vararg strings: String) = strings.forEach { orderoids += it }
    
    
    
    
    // Convenience fun
    
    fun random() { orderoids += RANDOM }
    
    fun byId() { orderoids += _ID }
    fun byPosition() { orderoids += _Position }
    
    /**
     * Flips the actual order of rows in a db table (row id)
     */
    fun flipRows() { orderoids += RowID + DESCENDING}
    
    
    
    
    
    fun case(encased: String) = "CASE$encased END" // TODO Find where to put
    
    
}





