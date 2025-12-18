// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.set.dsl

import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.set.dsl.data.SetOp

open class BaseSetBuilder {
    protected val _ops = mutableListOf<SetOp>()
    
    val ops: List<SetOp>
        get() = _ops
    
    
    
    internal fun iMultDiv(                                                           col: IntCol,
                                                                                   value: Number,
                                                                                    oper: String,
    ) {
        val v = value.toInt()
        if (v == 1) return //\/\/\/\/\/\
        
        _ops += if (v == 0)
                    SetOp.Set(col, 0)
                else
                    SetOp.NumOp(col.name, v, oper)
    }
    internal fun lMultDiv(                                                           col: LongCol,
                                                                                   value: Number,
                                                                                    oper: String,
    ) {
        val v = value.toLong()
        if (v == 1L) return //\/\/\/\/\/\
        
        _ops += if (v == 0L)
                    SetOp.Set(col, 0L)
                else
                    SetOp.NumOp(col.name, v, oper)
    }
    
    internal fun fMultDiv(                                                           col: FloatCol,
                                                                                   value: Number,
                                                                                    oper: String,
    ) {
        val v = value.toFloat()
        if (v == 1f) return //\/\/\/\/\/\
        
        _ops += if (v == 0f)
                    SetOp.Set(col, 0f)
                else
                    SetOp.NumOp(col.name, v, oper)
    }
    
    
    
}