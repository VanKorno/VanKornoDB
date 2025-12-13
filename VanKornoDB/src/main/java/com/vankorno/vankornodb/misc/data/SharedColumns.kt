package com.vankorno.vankornodb.misc.data
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.sCol

/**
 * Just some commonly used columns
 */
object SharedCol {
    val shID = iCol(_ID, -1)
    val shRowID = iCol(RowID, -1)
    val shName = sCol(_Name)
    val shPosition = iCol(_Position)
    val shType = sCol(_Type)
    val shActive = bCol("active")
}
