package com.vankorno.vankornodb.set.dsl
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import com.vankorno.vankornodb.set.dsl.data.SetOp

open class BaseSetter {
    
    protected val _ops = mutableListOf<SetOp>()
    
    val ops: List<SetOp>
        get() = _ops
    
    
    
    
    
}