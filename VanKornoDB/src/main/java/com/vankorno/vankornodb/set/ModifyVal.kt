// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.WhereDsl
import com.vankorno.vankornodb.core.data.DbConstants.WHERE
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.set.internal.baseAddTo


fun SQLiteDatabase.addToInt(                                     addend: Number,
                                                                  table: String,
                                                                 column: IntCol,
                                                                  where: WhereDsl.()->Unit = {},
) = baseAddTo(addend, table, column, where)


fun SQLiteDatabase.addToLong(                                    addend: Number,
                                                                  table: String,
                                                                 column: LongCol,
                                                                  where: WhereDsl.()->Unit = {},
) = baseAddTo(addend, table, column, where)


fun SQLiteDatabase.addToFloat(                                   addend: Number,
                                                                  table: String,
                                                                 column: FloatCol,
                                                                  where: WhereDsl.()->Unit = {},
) = baseAddTo(addend, table, column, where)



fun SQLiteDatabase.flipBool(                                      table: String,
                                                                 column: BoolCol,
                                                                  where: WhereDsl.()->Unit = {},
) {
    val colName = column.name
    val builder = WhereDsl().apply(where)
    
    val queryStr1 = "UPDATE $table SET $colName = NOT $colName"
    
    val hasWhere = builder.clauses.isNotEmpty()
    
    if (hasWhere)
        execSQL(queryStr1 + WHERE + builder.buildStr(), builder.args.toTypedArray())
    else
        execSQL(queryStr1)
}