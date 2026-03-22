/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.noty

import android.database.sqlite.SQLiteDatabase

fun SQLiteDatabase.addToIntNoty(                                                  addend: Number,
                                                                                      id: Long,
                                                                                   table: String,
                                                                                  column: String,
) {
    val iAddend = addend.toInt()
    
    if (iAddend == 0) return //\/\/\/\/\/\
    
    execSQL(
        "UPDATE $table SET $column = $column + ? WHERE id = ?",
        arrayOf<Any>(iAddend, id)
    )
}


fun SQLiteDatabase.addToLongNoty(                                                 addend: Number,
                                                                                      id: Long,
                                                                                   table: String,
                                                                                  column: String,
) {
    val lAddend = addend.toLong()
    
    if (lAddend == 0L) return //\/\/\/\/\/\
    
    execSQL(
        "UPDATE $table SET $column = $column + ? WHERE id = ?",
        arrayOf(lAddend, id)
    )
}


fun SQLiteDatabase.addToFloatNoty(                                                addend: Number,
                                                                                      id: Long,
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


fun SQLiteDatabase.toggleBoolNoty(                                                    id: Long,
                                                                                   table: String,
                                                                                  column: String,
) {
    execSQL(
        "UPDATE $table SET $column = NOT $column WHERE id = ?",
        arrayOf(id)
    )
}








