// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.lock

import com.vankorno.vankornodb.api.DbLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Allows running bundles of db operations or even business logic
 * as a single operation with the lock, to avoid conflicts with other ops running at the same time.
 */
abstract class LockedOpsRunnerInternal(                                         val lock: DbLock
) {
    
    inline fun <T> exec(                                                    defaultValue: T,
                                                                       crossinline block: ()->T,
    ): T {
        return try {
            lock.withLock { block() }
        } catch (_: Exception) {
            defaultValue
        }
    }
    
    inline fun exec(crossinline block: ()->Unit) { exec(Unit){block()} }
    
    
    
    fun async(                                                                    block: ()->Unit
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                lock.withLock {
                    block()
                }
            } catch (_: Exception) {
            }
        }
    }
    
    
    
    suspend inline fun <T> susp(                                            defaultValue: T,
                                                                       crossinline block: ()->T,
    ): T = withContext(Dispatchers.Default) {
        try {
            lock.withLock { block() }
        } catch (_: Exception) {
            defaultValue
        }
    }

    suspend inline fun susp(crossinline block: ()->Unit) { susp(Unit){block()} }
    
    

}

