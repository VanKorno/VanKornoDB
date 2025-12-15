package com.vankorno.vankornodb.set.noty
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.sqlite.SQLiteDatabase

fun SQLiteDatabase.addToIntNoty(                                                  addend: Number,
                                                                                      id: Int,
                                                                                   table: String,
                                                                                  column: String,
) {
    val iAddend = addend.toInt()
    
    if (iAddend == 0) return //\/\/\/\/\/\
    
    execSQL(
        "UPDATE $table SET $column = $column + ? WHERE id = ?",
        arrayOf(iAddend, id)
    )
}


fun SQLiteDatabase.addToLongNoty(                                                 addend: Number,
                                                                                      id: Int,
                                                                                   table: String,
                                                                                  column: String,
) {
    val lAddend = addend.toLong()
    
    if (lAddend == 0L) return //\/\/\/\/\/\
    
    execSQL(
        "UPDATE $table SET $column = $column + ? WHERE id = ?",
        arrayOf<Any>(lAddend, id)
    )
}


fun SQLiteDatabase.addToFloatNoty(                                                addend: Number,
                                                                                      id: Int,
                                                                                   table: String,
                                                                                  column: String,
) {
    val fAddend = addend.toFloat()
    
    if (fAddend == 0f) return //\/\/\/\/\/\
    
    execSQL(
        "UPDATE $table SET $column = $column + ? WHERE id = ?",
        arrayOf<Any>(fAddend, id)
    )
}


fun SQLiteDatabase.toggleBoolNoty(                                                    id: Int,
                                                                                   table: String,
                                                                                  column: String,
) {
    execSQL(
        "UPDATE $table SET $column = NOT $column WHERE id = ?",
        arrayOf(id)
    )
}








