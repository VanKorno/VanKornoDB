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
internal object SharedCol {
    val shID = iCol(ID, -1)
    val shRowID = iCol(RowID, -1)
    val shName = sCol(Name)
    val shPosition = iCol(Position)
    val shType = sCol(Type)
    val shActive = bCol("active")
}
