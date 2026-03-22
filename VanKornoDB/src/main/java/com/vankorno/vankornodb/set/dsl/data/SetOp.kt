/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.set.dsl.data

import android.content.ContentValues
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

sealed class SetOp {
    data class Set<T>(             val col: TypedColumn<T>,
                                 val value: T,
    ): SetOp()
    
    data class SetNoty(            val col: String,
                                 val value: Any,
    ): SetOp()
    
    
    data class SetCV(val cv: ContentValues): SetOp()
    
    
    data class Flip(val col: BoolCol): SetOp()
    
    
    data class NumOp(          val colName: String,
                                 val value: Number,
                                 val sqlOp: String,
    ) : SetOp()
    
    
    data class Abs(val colName: String) : SetOp()
    
    
    data class MinMax(         val colName: String,
                                 val value: Number,
                             val isFloorOp: Boolean,
    ) : SetOp()
    
    
    data class CoerceIn(       val colName: String,
                                   val min: Number,
                                   val max: Number,
    ) : SetOp()
    
    
    data class SetAs(           val setCol: String,
                                val getCol: String,
    ) : SetOp()
    
    data class SetAsModified(   val setCol: String,
                          val colWithModif: SetColOp,
    ) : SetOp()
    
    
}
