/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.DbConstants.RowID
import com.vankorno.vankornodb.core.data.DbConstants._ID
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.lCol

private val idDefinition = lCol(_ID, -1L)
private val rowIdDefinition = lCol(RowID)

interface DslWithId<W> {
    val Id: Field<W, LongCol> get() = Field(idDefinition)
}


interface DslWithIdAndRowId<W> : DslWithId<W> {
    val RowId: Field<W, LongCol> get() = Field(rowIdDefinition)
}


