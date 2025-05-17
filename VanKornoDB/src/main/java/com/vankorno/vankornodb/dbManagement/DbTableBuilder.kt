package com.vankorno.vankornodb.dbManagement

import com.vankorno.vankornodb.core.DbConstants.comma
import com.vankorno.vankornodb.core.DbConstants.dbCreateT
import com.vankorno.vankornodb.dbManagement.data.ColumnDef
import kotlin.collections.forEachIndexed
import kotlin.collections.lastIndex

class DbTableBuilder {
    
    fun newTableQuery(                                          tableName: String,
                                                                   entity: ArrayList<ColumnDef>
    ): String {
        val queryStr = buildString {
            append(dbCreateT)
            append(tableName)
            append(" (")
            entity.forEachIndexed { idx, column ->
                append(column.name)
                append(column.type.sql)
                if (idx < entity.lastIndex)
                    append(comma)
            }
            append(")")
        }
        // region LOG
            println("DbTableBuilder().newTableQuery() returns: $queryStr")
        // endregion
        return queryStr
    }
    
}