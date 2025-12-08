package com.vankorno.vankornodb.core.data

import com.vankorno.vankornodb.core.data.DbConstants.*

/**
 * Just some commonly used columns, defined here for convenience and for some non-essential lib functions.
 */
object SharedCol {
    val shID = iCol(ID, -1)
    val shRowID = iCol(RowID, -1)
    val shName = sCol(Name)
    val shPositon = iCol(Position)
    val shType = sCol(Type)
    val shActive = bCol("active")
}
