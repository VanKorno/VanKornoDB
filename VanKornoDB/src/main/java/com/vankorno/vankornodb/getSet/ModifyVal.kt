package com.vankorno.vankornodb.getSet

import android.database.sqlite.SQLiteDatabase

fun SQLiteDatabase.addToInt(                                                      addend: Number,
                                                                                      id: Int,
                                                                               tableName: String,
                                                                                  column: String
) {
    val iAddend = addend.toInt()
    
    if (iAddend == 0) return //\/\/\/\/\/\
    
    execSQL(
        "UPDATE $tableName SET $column = $column + ? WHERE id = ?",
        arrayOf(iAddend, id)
    )
}


fun SQLiteDatabase.addToLong(                                                     addend: Number,
                                                                                      id: Int,
                                                                               tableName: String,
                                                                                  column: String
) {
    val lAddend = addend.toLong()
    
    if (lAddend == 0L) return //\/\/\/\/\/\
    
    execSQL(
        "UPDATE $tableName SET $column = $column + ? WHERE id = ?",
        arrayOf<Any>(lAddend, id)
    )
}


fun SQLiteDatabase.addToFloat(                                                    addend: Number,
                                                                                      id: Int,
                                                                               tableName: String,
                                                                                  column: String
) {
    val fAddend = addend.toFloat()
    
    if (fAddend == 0f) return //\/\/\/\/\/\
    
    execSQL(
        "UPDATE $tableName SET $column = $column + ? WHERE id = ?",
        arrayOf<Any>(fAddend, id)
    )
}


fun SQLiteDatabase.toggleBool(                                                        id: Int,
                                                                               tableName: String,
                                                                                  column: String
) {
    execSQL(
        "UPDATE $tableName SET $column = NOT $column WHERE id = ?",
        arrayOf(id)
    )
}

