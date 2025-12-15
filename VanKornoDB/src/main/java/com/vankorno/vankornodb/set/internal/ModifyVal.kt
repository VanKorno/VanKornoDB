package com.vankorno.vankornodb.set.internal

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants.WHERE
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

internal fun SQLiteDatabase.baseAddTo(                           addend: Number,
                                                                  table: String,
                                                                 column: TypedColumn<*>,
                                                                  where: WhereBuilder.()->Unit = {},
) {
    val sAddend = addend.toString()
    
    if (sAddend == "0" || sAddend == "0.0") return //\/\/\/\/\/\
    
    val colName = column.name
    val builder = WhereBuilder().apply(where)
    val args = mutableListOf(sAddend)
    
    val hasWhere = builder.clauses.isNotEmpty()
    var wherePart = ""
    
    if (hasWhere) {
        wherePart = WHERE + builder.buildStr()
        args.addAll(builder.args)
    }
    execSQL("UPDATE $table SET $colName = $colName + ?" + wherePart,  args.toTypedArray())
}






