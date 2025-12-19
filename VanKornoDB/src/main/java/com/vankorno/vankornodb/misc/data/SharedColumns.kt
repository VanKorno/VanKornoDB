// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.misc.data

import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.sCol

/**
 * Just some commonly used columns
 */
object SharedCol {
    val cID = iCol(_ID, -1)
    val cRowID by lazy { iCol(RowID, -1) }
    val cName = sCol(_Name)
    val cPosition = iCol(_Position)
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
