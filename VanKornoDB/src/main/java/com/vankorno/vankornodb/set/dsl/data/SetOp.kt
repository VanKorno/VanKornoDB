package com.vankorno.vankornodb.set.dsl.data

import android.content.ContentValues
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

sealed class SetOp {
    data class Set<T>(val col: TypedColumn<T>, val value: T): SetOp()
    
    data class SetCV(val cv: ContentValues): SetOp()
    
    data class AddToInt(val col: IntCol, val value: Number): SetOp()
    data class AddToFloat(val col: FloatCol, val value: Number): SetOp()
    data class AddToLong(val col: LongCol, val value: Number): SetOp()
    
    data class Flip(val col: BoolCol): SetOp()
}
