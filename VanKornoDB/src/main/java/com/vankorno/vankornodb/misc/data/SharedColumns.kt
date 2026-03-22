/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc.data

import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol

/**
 * Just some commonly used columns
 */
object SharedCol {
    val cID = lCol(_ID, -1L)
    val cRowID by lazy { lCol(RowID, -1L) }
    val cName = sCol(_Name)
    val cPosition = lCol(_Position)
    val cType by lazy { sCol(_Type) }
    
    val cActive by lazy { bCol("active") }
    val cEnabled by lazy { bCol("enabled") }
    
    val cText by lazy { sCol("text") }
    val cInfo by lazy { sCol("info") }
    val cDescr by lazy { sCol("description") }
    
    val cInt by lazy { iCol("cInt") }
    val cStr by lazy { iCol("cStr") }
    val cBool by lazy { iCol("cBool") }
    val cLong by lazy { iCol("cLong") }
    val cFloat by lazy { iCol("cFloat") }
    
}
