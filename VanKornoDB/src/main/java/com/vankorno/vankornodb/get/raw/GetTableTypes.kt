/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.raw

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.data.DbConstants.DbTypeTable
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_Master
import com.vankorno.vankornodb.get.noty.getStrNoty
import com.vankorno.vankornodb.misc.data.SharedCol.cName
import com.vankorno.vankornodb.misc.data.SharedCol.cType

fun SQLiteDatabase.getTableTypesFromInitQuery(                                     table: String
): List<String> {
    val queryStr = getStrNoty(TABLE_Master, "sql",
        where = {
            cType equal DbTypeTable
            and { cName equal table }
        }
    )
    if (queryStr.isBlank()) {
        return emptyList() //\/\/\/\/\/\
    }
    
    val colsDef = "\\((.*)\\)".toRegex().find(queryStr)?.groups?.get(1)?.value ?: ""

    return colsDef.split(",").map { colDef ->
        val tokens = colDef.trim().split("\\s+".toRegex())
        if (tokens.size < 2) return@map "TEXT"

        // type can be NUMERIC(10,2) or REAL, etc.
        val typeTokens = mutableListOf<String>()
        var parenCount = 0
        
        for (token in tokens.drop(1)) {
            typeTokens += token
            parenCount += token.count { it == '(' } - token.count { it == ')' }
            if (parenCount <= 0) break
        }
        typeTokens.joinToString(" ")
    }
}
