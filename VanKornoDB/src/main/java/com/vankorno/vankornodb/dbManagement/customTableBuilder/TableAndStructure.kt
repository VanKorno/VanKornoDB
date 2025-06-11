package com.vankorno.vankornodb.dbManagement.customTableBuilder

data class TableAndStructure(
                              val tableName: String,
                              val structure: ArrayList<ColumnDef>
)