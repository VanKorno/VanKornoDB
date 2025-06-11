package com.vankorno.vankornodb.dbManagement.customTableBuilder

import com.vankorno.vankornodb.dbManagement.data.ColumnType

data class ColumnDef(
                            val name: String,
                            val type: ColumnType
)