/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.DbConstants.DESCENDING
import com.vankorno.vankornodb.core.data.DbConstants.RANDOM
import com.vankorno.vankornodb.core.data.DbConstants.comma
import com.vankorno.vankornodb.dbManagement.data.AscendingColumn
import com.vankorno.vankornodb.dbManagement.data.BaseColumn
import com.vankorno.vankornodb.dbManagement.data.DescendingColumn
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cPosition
import com.vankorno.vankornodb.misc.data.SharedCol.cRowId

/**
 *  Internal base — use [com.vankorno.vankornodb.api.OrderDsl] from the api package instead
 */
open class OrderDslInternal {
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
    
    fun flipAll(vararg columns: AscendingColumn<*>) = columns.forEach { orderoids += it.name + DESCENDING }
    
    fun AscendingColumn<*>.flip() { orderoids += this@flip.name + DESCENDING }
    
    fun AscendingColumn<*>.flipIf(                                             condition: Boolean
    ) {
        if (condition)
            this.flip()
        else
            col(this)
    }
    
    fun raw(vararg strings: String) = strings.forEach { orderoids += it }
    
    fun byId() { cID() }
    fun byIdFlipped() { cID.flip() }
    
    fun byRowId() { cRowId() }
    fun byRowIdFlipped() { cRowId.flip() }
    
    fun byPosition() { cPosition() }
    fun byPositionFlipped() { cPosition.flip() }
    
    fun byName() { cName() }
    fun byNameFlipped() { cName.flip() }
    
    fun random() { orderoids += RANDOM }
    
    
    fun case(encased: String) = "CASE$encased END" // TODO Find where to put
}





