// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get.raw

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.data.DbConstants.*
import com.vankorno.vankornodb.get.noty.getStrNoty

fun SQLiteDatabase.getTableTypesFromInitQuery(                                     table: String
): List<String> {
    val queryStr = getStrNoty(TABLE_Master, "sql",
        where = {
            _Type equal DbTypeTable
            and { _Name equal table }
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