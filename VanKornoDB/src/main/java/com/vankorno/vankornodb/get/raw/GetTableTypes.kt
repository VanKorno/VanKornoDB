/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.raw

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.core.data.DbConstants.DbTypeTable
import com.vankorno.vankornodb.core.data.DbConstants.TABLE_Master
import com.vankorno.vankornodb.misc.eLog
import com.vankorno.vankornodb.misc.suppressValGetterErrorLog

fun SQLiteDatabase.getTableTypesFromInitQuery(                                     table: String
): List<String> {
    val queryStr = rawQuery(
        "SELECT sql FROM $TABLE_Master WHERE type=? AND name=? LIMIT 1",
        arrayOf(DbTypeTable, table),
    ).use { cursor ->
        if (cursor.moveToFirst()) cursor.getString(0).orEmpty()
        else {
            // region LOG
            if (!suppressValGetterErrorLog)
                eLog("getTableTypesFromInitQuery(): No CREATE TABLE statement found for '$table'.")
            // endregion
            ""
        }
    }
    if (queryStr.isBlank()) return emptyList() //\/\/\/\/\/\
    
    val colsDef = "\\((.*)\\)".toRegex().find(queryStr)?.groups?.get(1)?.value ?: ""
    
    return colsDef.split(",").map { colDef ->
        val tokens = colDef.trim().split("\\s+".toRegex())
        if (tokens.size < 2) return@map "TEXT"
        
        /* type can be NUMERIC(10,2) or REAL, etc. */
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
